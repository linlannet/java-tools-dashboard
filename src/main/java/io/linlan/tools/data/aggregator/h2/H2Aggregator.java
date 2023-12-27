package io.linlan.tools.data.aggregator.h2;

import com.google.common.base.Stopwatch;
import io.linlan.tools.data.provider.InnerAggregator;
import org.apache.commons.collections.map.HashedMap;
import org.apache.commons.dbcp2.BasicDataSource;
import io.linlan.commons.cache.CacheManager;
import io.linlan.datas.core.provider.config.AggConfig;
import io.linlan.datas.core.provider.result.AggregateResult;
import io.linlan.datas.core.utils.DataProviderUtils;
import io.linlan.datas.core.utils.H2SyntaxHelper;
import io.linlan.datas.core.utils.SqlHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import java.sql.*;
import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.stream.IntStream;

/**
 * Filename:H2Aggregator.java
 * Desc:
 *
 * @author hcday
 * @author <a href="mailto:hcday@qq.com">hcday soo</a>
 * CreateTime:2017/12/20 22:10
 * @version 1.0
 * @since 1.0
 */
@Service
@Scope("prototype")
public class H2Aggregator extends InnerAggregator {

    private Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    @Qualifier("h2DataSource")
    private BasicDataSource jdbcDataSource;

    @Autowired
    @Qualifier("rawDataCache")
    protected CacheManager<String[][]> rawDataCache;

    private static final String TBL_PREFIX = "TMP_";
    protected static Map<String, Long> h2AggMetaCacher = new HashMap<>();

    @Override
    public void beforeLoad(String[] header) {
        String tableName = getTmpTblName();
        //添加一个排序字段位置
        StringJoiner ddl = new StringJoiner(", ", "CREATE TABLE " + tableName + "(", ", `TMP_TABLE_INDEX` int);");
        Arrays.stream(header).map(col -> SqlHelper.surround(col, "`") + " VARCHAR(255)").forEach(ddl::add);
        // Recreate table
        try (Connection conn = jdbcDataSource.getConnection();
             Statement statmt = conn.createStatement()) {
            String dropTableStr = "DROP TABLE IF EXISTS " + tableName + ";";
            logger.info("Execute: {}", dropTableStr);
            statmt.execute(dropTableStr);
            logger.info("Execute: {}", ddl.toString());
            statmt.execute(ddl.toString());
        } catch (SQLException e) {
            logger.error("", e);
        }
    }

    @Override
    public void loadBatch(String[] header, String[][] data) {
        Stopwatch stopwatch = Stopwatch.createStarted();
        final int batchSize = 20000;
        int count = 0;

        if (data != null && data.length > 0) {
            // Load data
            try (Connection conn = jdbcDataSource.getConnection();
                 PreparedStatement ps = conn.prepareStatement(buildPreparedStatment(header))) {
                for (int i = 0; i < data.length; i++) {
                    for (int j = 1; j <= header.length; j++) {
                        ps.setString(j, data[i][j - 1]);
                    }
                    ps.setString(header.length + 1, i + 1 + "");//添加一个排序字段
                    ps.addBatch();
                    if (++count % batchSize == 0) {
                        ps.executeBatch();
                        logger.info("Thread id: {}, H2 load batch {}", Thread.currentThread().getName(), count);
                    }
                }
                ps.executeBatch();
            } catch (SQLException e) {
                logger.error("", e);
            }
        }
        stopwatch.stop();
        logger.info("H2 Database loadBatch using time: {} ms", stopwatch.elapsed(TimeUnit.MILLISECONDS));
    }

    @Override
    public void afterLoad() {
        h2AggMetaCacher.put(getTmpTblName(), System.currentTimeMillis());
    }

    private String buildPreparedStatment(String[] header) {
        String tableName = getTmpTblName();
        StringJoiner insertJoiner = new StringJoiner(", ", "INSERT INTO " + tableName + " VALUES (", ", ?);");//添加一个排序字段位置
        IntStream.range(0, header.length).forEach(i -> insertJoiner.add("?"));
        return insertJoiner.toString();
    }

    @Override
    public void loadData(String[][] data, long interval) {
        Stopwatch stopwatch = Stopwatch.createStarted();

        final int batchSize = 20000;
        int count = 0;

        if (data != null && data.length > 1) {
            String[] header = data[0];
            String tableName = getTmpTblName();
            beforeLoad(header);
            // Load data
            synchronized (tableName.intern()) {
                try (Connection conn = jdbcDataSource.getConnection();
                     PreparedStatement ps = conn.prepareStatement(buildPreparedStatment(header))) {
                    for (int i = 1; i < data.length; i++) {
                        for (int j = 1; j <= data[i].length; j++) {
                            ps.setString(j, data[i][j - 1]);
                        }
                        ps.setString(data[i].length + 1, i + "");//添加一个排序字段
                        ps.addBatch();
                        if (++count % batchSize == 0) {
                            ps.executeBatch();
                            logger.info("H2 load batch {}", count);
                        }
                    }
                    ps.executeBatch();
                } catch (SQLException e) {
                    logger.error("", e);
                }
            }
        }

        afterLoad();
        stopwatch.stop();
        logger.info("H2 Database loading using time: {} ms", stopwatch.elapsed(TimeUnit.MILLISECONDS));
    }

    @Override
    public String[] queryDimVals(String columnName, AggConfig config) throws Exception {
        String fsql = "SELECT %s FROM %s %s GROUP BY %s";
        String exec = null;
        List<String> result = new ArrayList<>();
        String whereStr = "";
        if (config != null) {
            SqlHelper sqlHelper = new SqlHelper(getTmpTblName(), false);
            sqlHelper.setSqlSyntaxHelper(new H2SyntaxHelper());
            sqlHelper.getSqlSyntaxHelper().setColumnTypes(getColumnType());
            whereStr = sqlHelper.assembleFilterSql(config);
        }
        exec = String.format(fsql, SqlHelper.surround(columnName, "`"), getTmpTblName(), whereStr, SqlHelper.surround(columnName, "`"));
        logger.info(exec);
        try (Connection conn = jdbcDataSource.getConnection();
             Statement stat = conn.createStatement();
             ResultSet rs = stat.executeQuery(exec)) {
            while (rs.next()) {
                result.add(rs.getString(1));
            }
        } catch (Exception e) {
            logger.error("ERROR:" + e.getMessage());
            throw new Exception("ERROR:" + e.getMessage(), e);
        }
        return result.toArray(new String[]{});
    }

    @Override
    public String[] getColumn(boolean reload) throws Exception {
        String template = "SELECT column_name FROM INFORMATION_SCHEMA.columns WHERE table_name = upper('%s')";
        String colsQuery = String.format(template, getTmpTblName());
        List<String> columns = new ArrayList<>();
        try (Connection conn = jdbcDataSource.getConnection();
             Statement stat = conn.createStatement();
             ResultSet rs = stat.executeQuery(colsQuery)) {
            while (rs.next()) {
                String column = rs.getString("column_name");
                columns.add(column);
            }
        }
        return columns.toArray(new String[]{});
    }

    @Override
    public AggregateResult queryAggData(AggConfig config) throws Exception {
        Stopwatch stopwatch = Stopwatch.createStarted();
        SqlHelper sqlHelper = new SqlHelper(getTmpTblName(), false);
        sqlHelper.setSqlSyntaxHelper(new H2SyntaxHelper().setColumnTypes(getColumnType()));
        String exec = sqlHelper.assembleAggDataSql(config);

        //查询添加一个排序字段TMP_TABLE_INDEX
        exec = exec.replace("FROM " + TBL_PREFIX, ", min(TMP_TABLE_INDEX) FROM " + TBL_PREFIX);

        List<String[]> list = new LinkedList<>();
        logger.info(exec);
        ResultSet rs = null;
        try (Connection conn = jdbcDataSource.getConnection();
             Statement stat = conn.createStatement()) {
//            stat.execute("CREATE ALIAS IF NOT EXISTS f_todouble FOR \"io.linlan.datas.core.utils.ResultFunctions.parseStr2Double\" ");
//            stat.execute("DROP ALIAS IF EXISTS f_todouble;");//改f_todouble，需先DROP再CREATE一次可生效
            stat.execute("CREATE ALIAS IF NOT EXISTS f_todouble FOR \"io.linlan.tools.data.aggregator.utils.ResultFunctions.parseStrBigDecimal\" ");
            rs = stat.executeQuery(exec);
            ResultSetMetaData metaData = rs.getMetaData();
            int columnCount = metaData.getColumnCount();
            while (rs.next()) {
                String[] row = new String[columnCount];
                for (int j = 0; j < columnCount; j++) {
                    row[j] = rs.getString(j + 1);
                }
                list.add(row);
            }

            //list结果排序
            Collections.sort(list, new Comparator<Object>() {
                @Override
                public int compare(Object o1, Object o2) {
                    int num1 = Integer.parseInt(((String[]) o1)[columnCount - 1]);
                    int num2 = Integer.parseInt(((String[]) o2)[columnCount - 1]);
                    if (num1 > num2) {
                        return 1;
                    } else {
                        return -1;
                    }
                }
            });

        } catch (Exception e) {
            logger.error("ERROR:" + e.getMessage());
            throw new Exception("ERROR:" + e.getMessage(), e);
        } finally {
            rs.close();
        }
        AggregateResult result = DataProviderUtils.transform2AggResult(config, list);
        logger.info("H2 Database queryAggData using time: {} ms", stopwatch.elapsed(TimeUnit.MILLISECONDS));
        return result;
    }

    public boolean checkExist() {
        if (isTimeout() || !isTableExists()) {
            return false;
        } else {
            return true;
        }
    }

    private boolean isTimeout() {
        Long createTimeStamp = h2AggMetaCacher.get(getTmpTblName());
        if (createTimeStamp == null || System.currentTimeMillis() - createTimeStamp > (12 * 60 * 60 * 1000)) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * Check if the temporary table exists in h2 db
     *
     * @return
     */
    private boolean isTableExists() {
        boolean exists = false;
        String template = "SELECT count(*) FROM INFORMATION_SCHEMA.tables WHERE table_name = upper('%s')";
        String colsQuery = String.format(template, getTmpTblName());
        try (Connection conn = jdbcDataSource.getConnection();
             Statement stat = conn.createStatement();
             ResultSet rs = stat.executeQuery(colsQuery)) {
            int count = 0;
            while (rs.next()) {
                count = rs.getInt(1);
            }
            if (count > 0) {
                exists = true;
            }
        } catch (Exception e) {
            logger.error("ERROR:" + e.getMessage());
        }
        return exists;
    }

    private String getTmpTblName() {
        return TBL_PREFIX + getCacheKey();
    }

    /**
     * 需要考虑ORACLE等各个不同数据库等差异化处理
     *
     * @return
     * @throws Exception
     */
    private Map<String, Integer> getColumnType() throws Exception {
        Map<String, Integer> result = new HashedMap();
        String template = "SELECT column_name, type_name FROM INFORMATION_SCHEMA.columns WHERE table_name = upper('%s')";
        String colsQuery = String.format(template, getTmpTblName());
        try (Connection conn = jdbcDataSource.getConnection();
             Statement stat = conn.createStatement();
             ResultSet rs = stat.executeQuery(colsQuery)) {
            List<String> columns = new ArrayList<>();
            while (rs.next()) {
                String column = rs.getString("column_name");
                //String dataType = rs.getString("type_name");
                result.put(column, Types.VARCHAR);
            }
        }
        return result;
    }


//    public boolean checkExist() {
//        return rawDataCache.get(getCacheKey()) != null;
//    }

    public void cleanExist() {
        rawDataCache.remove(getCacheKey());
    }

}
