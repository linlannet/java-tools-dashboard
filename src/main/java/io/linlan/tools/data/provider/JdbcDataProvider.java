package io.linlan.tools.data.provider;

import com.alibaba.druid.pool.DruidDataSource;
import com.alibaba.druid.pool.DruidDataSourceFactory;
import com.alibaba.fastjson.JSONObject;
import com.google.common.base.Charsets;
import com.google.common.base.Stopwatch;
import com.google.common.hash.Hashing;
import org.apache.commons.collections.map.HashedMap;
import org.apache.commons.lang.StringUtils;
import io.linlan.commons.cache.CacheManager;
import io.linlan.commons.cache.HeapCacheManager;
import io.linlan.commons.core.CoreException;
import io.linlan.commons.db.annotation.DataProviderName;
import io.linlan.commons.db.annotation.DataQueryParameter;
import io.linlan.commons.db.annotation.DataSourceParameter;
import io.linlan.datas.core.abs.Aggregatable;
import io.linlan.datas.core.abs.Initializing;
import io.linlan.datas.core.provider.config.AggConfig;
import io.linlan.datas.core.provider.result.AggregateResult;
import io.linlan.datas.core.utils.DataProviderUtils;
import io.linlan.datas.core.utils.SqlHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;

import javax.sql.DataSource;
import java.sql.*;
import java.util.*;
import java.util.concurrent.*;

/**
 *
 * Filename:JdbcDataProvider.java
 * Desc:
 *
 * @author hcday
 * @author <a href="mailto:hcday@qq.com">hcday soo</a>
 * CreateTime:2017/12/20 22:15
 *
 * @version 1.0
 * @since 1.0
 *
 */
@DataProviderName(name = "jdbc")
public class JdbcDataProvider extends DataProvider implements Aggregatable, Initializing {

    private static final Logger logger = LoggerFactory.getLogger(JdbcDataProvider.class);

    @Value("${dataprovider.resultLimit:200000}")
    private int resultLimit;

    @DataSourceParameter(label = "{{'DATAPROVIDER.JDBC.DRIVER'|translate}} *",
            type = DataSourceParameter.Type.Input,
            required = true,
            order = 1)
    private String DRIVER = "driver";

    @DataSourceParameter(label = "{{'DATAPROVIDER.JDBC.JDBCURL'|translate}} *",
            type = DataSourceParameter.Type.Input,
            required = true,
            order = 2)
    private String JDBC_URL = "jdbcurl";

    @DataSourceParameter(label = "{{'DATAPROVIDER.JDBC.USERNAME'|translate}} *",
            type = DataSourceParameter.Type.Input,
            required = true,
            order = 3)
    private String USERNAME = "username";

    @DataSourceParameter(label = "{{'DATAPROVIDER.JDBC.PASSWORD'|translate}}",
            type = DataSourceParameter.Type.Password,
            order = 4)
    private String PASSWORD = "password";

    @DataSourceParameter(label = "{{'DATAPROVIDER.POOLEDCONNECTION'|translate}}", checked = true, type = DataSourceParameter.Type.Checkbox, order = 99)
    private String POOLED = "pooled";

    @DataSourceParameter(label = "{{'DATAPROVIDER.AGGREGATABLE_PROVIDER'|translate}}", type = DataSourceParameter.Type.Checkbox, order = 100)
    private String aggregateProvider = "aggregateProvider";

    @DataQueryParameter(label = "{{'DATAPROVIDER.JDBC.SQLTEXT'|translate}}",
            type = DataQueryParameter.Type.TextArea,
            required = true,
            order = 1)
    private String SQL = "sql";

    private static final CacheManager<Map<String, Integer>> typeCahce = new HeapCacheManager<>();

    private static final ConcurrentMap<String, DataSource> datasourceMap = new ConcurrentHashMap<>();

    private SqlHelper sqlHelper;

    @Override
    public boolean doAggregationInDataSource() {
        String v = dataSource.get(aggregateProvider);
        return v != null && "true".equals(v);
    }

    @Override
    public String[][] getData() throws Exception {
        final int batchSize = 100000;
        Stopwatch stopwatch = Stopwatch.createStarted();
        logger.debug("Execute JdbcDataProvider.getData() Start!");
        String sql = getAsSubQuery(query.get(SQL));
        List<String[]> list = null;
        logger.info("SQL String: " + sql);

        try (Connection con = getConnection();
             Statement ps = con.createStatement();
             ResultSet rs = ps.executeQuery(sql)) {

            ResultSetMetaData metaData = rs.getMetaData();
            int columnCount = metaData.getColumnCount();
            list = new LinkedList<>();
            String[] row = new String[columnCount];
            for (int i = 0; i < columnCount; i++) {
                row[i] = metaData.getColumnLabel(i + 1);
            }

            String[] header = row;
            getInnerAggregator().beforeLoad(header);

            int resultCount = 0;
            int threadId = 0;
            ExecutorService executor = Executors.newFixedThreadPool(5);
            while (rs.next()) {
                resultCount++;
                row = new String[columnCount];
                for (int j = 0; j < columnCount; j++) {
                    row[j] = rs.getString(j + 1);
                }
                list.add(row);

                if (resultCount % batchSize == 0) {
                    logger.info("JDBC load batch {}", resultCount);
                    final String[][] batchData = list.toArray(new String[][]{});
                    Thread loadThread = new Thread(() -> {
                        getInnerAggregator().loadBatch(header, batchData);
                    }, threadId++ + "");
                    executor.execute(loadThread);
                    list.clear();
                }
                if (resultCount > resultLimit) {
                    throw new CoreException("Cube result count " + resultCount + ", is greater than limit " + resultLimit);
                }
            }
            executor.shutdown();
            while (!executor.awaitTermination(10, TimeUnit.SECONDS));
            final String[][] batchData = list.toArray(new String[][]{});
            getInnerAggregator().loadBatch(header, batchData);
        } catch (Exception e) {
            logger.error("ERROR:" + e.getMessage());
            throw new Exception("ERROR:" + e.getMessage(), e);
        }
        getInnerAggregator().afterLoad();
        stopwatch.stop();
        logger.info("getData() using time: {} ms", stopwatch.elapsed(TimeUnit.MILLISECONDS));
        return null;
    }

    @Override
    public void test() throws Exception {
        String queryStr = query.get(SQL);
        logger.info("Execute query: {}", queryStr);
        try (Connection con = getConnection();
             Statement ps = con.createStatement()) {
            ps.executeQuery(queryStr);
        } catch (Exception e) {
            logger.error("Error when execute: {}",  queryStr);
            throw new Exception("ERROR:" + e.getMessage(), e);
        }
    }


    /**
     * Convert the sql text to subquery string:
     * remove blank line
     * remove end semicolon ;
     *
     * @param rawQueryText
     * @return
     */
    private String getAsSubQuery(String rawQueryText) {
        String deletedBlankLine = rawQueryText.replaceAll("(?m)^[\\s\t]*\r?\n", "").trim();
        return deletedBlankLine.endsWith(";") ? deletedBlankLine.substring(0, deletedBlankLine.length() - 1) : deletedBlankLine;
    }

    private Connection getConnection() throws Exception {
        String usePool = dataSource.get(POOLED);
        String username = dataSource.get(USERNAME);
        String password = dataSource.get(PASSWORD);
        Connection conn = null;
        if (usePool != null && "true".equals(usePool)) {
            String key = Hashing.md5().newHasher().putString(JSONObject.toJSON(dataSource).toString(), Charsets.UTF_8).hash().toString();
            DataSource ds = datasourceMap.get(key);
            if (ds == null) {
                synchronized (key.intern()) {
                    ds = datasourceMap.get(key);
                    if (ds == null) {
                        Map<String, String> conf = new HashedMap();
                        conf.put(DruidDataSourceFactory.PROP_DRIVERCLASSNAME, dataSource.get(DRIVER));
                        conf.put(DruidDataSourceFactory.PROP_URL, dataSource.get(JDBC_URL));
                        conf.put(DruidDataSourceFactory.PROP_USERNAME, dataSource.get(USERNAME));
                        if (StringUtils.isNotBlank(password)) {
                            conf.put(DruidDataSourceFactory.PROP_PASSWORD, dataSource.get(PASSWORD));
                        }
                        conf.put(DruidDataSourceFactory.PROP_INITIALSIZE, "3");
                        DruidDataSource druidDS = (DruidDataSource) DruidDataSourceFactory.createDataSource(conf);
                        druidDS.setBreakAfterAcquireFailure(true);
                        druidDS.setConnectionErrorRetryAttempts(5);
                        datasourceMap.put(key, druidDS);
                        ds = datasourceMap.get(key);
                    }
                }
            }
            try {
                conn = ds.getConnection();
            } catch (SQLException e) {
                datasourceMap.remove(key);
                throw e;
            }
            return conn;
        } else {
            String driver = dataSource.get(DRIVER);
            String jdbcurl = dataSource.get(JDBC_URL);

            Class.forName(driver);
            Properties props = new Properties();
            props.setProperty("user", username);
            if (StringUtils.isNotBlank(password)) {
                props.setProperty("password", password);
            }
            return DriverManager.getConnection(jdbcurl, props);
        }
    }

    @Override
    public String[] queryDimVals(String columnName, AggConfig config) throws Exception {
        String fsql = null;
        String exec = null;
        String sql = getAsSubQuery(query.get(SQL));
        List<String> filtered = new ArrayList<>();
        String whereStr = "";
        if (config != null) {
            whereStr = sqlHelper.assembleFilterSql(config);
        }
        fsql = "SELECT tmp_view.%s FROM (\n%s\n) tmp_view %s GROUP BY tmp_view.%s";
        exec = String.format(fsql, columnName, sql, whereStr, columnName);
        logger.info(exec);
        try (Connection connection = getConnection();
             Statement stat = connection.createStatement();
             ResultSet rs = stat.executeQuery(exec)) {
            while (rs.next()) {
                filtered.add(rs.getString(1));
            }
        } catch (Exception e) {
            logger.error("ERROR:" + e.getMessage());
            throw new Exception("ERROR:" + e.getMessage(), e);
        }
        return filtered.toArray(new String[]{});
    }


    private ResultSetMetaData getMetaData(String subQuerySql, Statement stat) throws Exception {
        ResultSetMetaData metaData;
        try {
            stat.setMaxRows(100);
            String fsql = "\nSELECT * FROM (\n%s\n) tmp_view WHERE 1=0";
            String sql = String.format(fsql, subQuerySql);
            logger.info(sql);
            ResultSet rs = stat.executeQuery(sql);
            metaData = rs.getMetaData();
        } catch (Exception e) {
            logger.error("ERROR:" + e.getMessage());
            throw new Exception("ERROR:" + e.getMessage(), e);
        }
        return metaData;
    }

    private Map<String, Integer> getColumnType() throws Exception {
        Map<String, Integer> result = null;
        String key = getLockKey();
        result = typeCahce.get(key);
        if (result != null) {
            return result;
        } else {
            try (Connection connection = getConnection();
                 Statement stat = connection.createStatement()) {
                String subQuerySql = getAsSubQuery(query.get(SQL));
                ResultSetMetaData metaData = getMetaData(subQuerySql, stat);
                int columnCount = metaData.getColumnCount();
                result = new HashedMap();
                for (int i = 0; i < columnCount; i++) {
                    result.put(metaData.getColumnLabel(i + 1).toUpperCase(), metaData.getColumnType(i + 1));
                }
                typeCahce.put(key, result, 12 * 60 * 60 * 1000);
                return result;
            }
        }
    }

    @Override
    public String[] getColumn(boolean reload) throws Exception {
        String subQuerySql = getAsSubQuery(query.get(SQL));
        try (Connection connection = getConnection();
                Statement stat = connection.createStatement()) {
            ResultSetMetaData metaData = getMetaData(subQuerySql, stat);
            int columnCount = metaData.getColumnCount();
            String[] row = new String[columnCount];
            for (int i = 0; i < columnCount; i++) {
                row[i] = metaData.getColumnLabel(i + 1);
            }
            return row;
        }
    }

    @Override
    public AggregateResult queryAggData(AggConfig config) throws Exception {
        String exec = sqlHelper.assembleAggDataSql(config);
        List<String[]> list = new LinkedList<>();
        logger.info(exec);
        try (Connection connection = getConnection();
                Statement stat = connection.createStatement();
                ResultSet rs = stat.executeQuery(exec)) {
            ResultSetMetaData metaData = rs.getMetaData();
            int columnCount = metaData.getColumnCount();
            while (rs.next()) {
                String[] row = new String[columnCount];
                for (int j = 0; j < columnCount; j++) {
                    row[j] = rs.getString(j + 1);
                }
                list.add(row);
            }
        } catch (Exception e) {
            logger.error("ERROR:" + e.getMessage());
            throw new Exception("ERROR:" + e.getMessage(), e);
        }
        return DataProviderUtils.transform2AggResult(config, list);
    }


    @Override
    public String viewAggDataQuery(AggConfig config) throws Exception {
        return sqlHelper.assembleAggDataSql(config);
    }


    @Override
    public void afterPropertiesSet() throws Exception {
        String subQuery = null;
        if (query != null) {
            subQuery = getAsSubQuery(query.get(SQL));
        }
        SqlHelper sqlHelper = new SqlHelper(subQuery, true);
        if (!isUsedForTest()) {
            Map<String, Integer> columnTypes = null;
            try {
                columnTypes = getColumnType();
            } catch (Exception e) {
                //  getColumns() and test() methods do not need columnTypes properties,
                // it doesn't matter for these methods even getMeta failed
                logger.warn("getColumnType failed: {}", e.getMessage());
            }
            sqlHelper.getSqlSyntaxHelper().setColumnTypes(columnTypes);
        }
        this.sqlHelper = sqlHelper;
    }

}
