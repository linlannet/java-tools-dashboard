package net.linlan.tools.data.provider;

import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONObject;
import com.google.common.base.Charsets;
import com.google.common.hash.Hashing;
import com.googlecode.aviator.AviatorEvaluator;
import net.linlan.commons.core.StringComparator;
import net.linlan.datas.core.abs.Aggregatable;
import net.linlan.datas.core.provider.config.AggConfig;
import net.linlan.datas.core.provider.config.CompositeConfig;
import net.linlan.datas.core.provider.config.ConfigComponent;
import net.linlan.datas.core.provider.config.DimensionConfig;
import net.linlan.datas.core.provider.expression.NowFunction;
import net.linlan.datas.core.provider.result.AggregateResult;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 *
 * Filename:DataProvider.java
 * Desc:
 *
 * @author Linlan
 * CreateTime:2017/12/20 22:13
 *
 * @version 1.0
 * @since 1.0
 *
 */
public abstract class DataProvider {

    private InnerAggregator innerAggregator;
    protected Map<String, String> dataSource;
    protected Map<String, String> query;
    private int resultLimit;
    private boolean isUsedForTest = false;
    private long interval = 12 * 60 * 60; // second

    public static final String NULL_STRING = "#NULL";
    private static final Logger logger = LoggerFactory.getLogger(DataProvider.class);

    static {
        AviatorEvaluator.addFunction(new NowFunction());
    }

    public abstract boolean doAggregationInDataSource();

    public boolean isDataSourceAggInstance() {
        if (this instanceof Aggregatable && doAggregationInDataSource()) {
            return true;
        } else {
            return false;
        }
    }
    /**
     * get the aggregated data by user's widget designer
     *
     * @return
     */
    public final AggregateResult getAggData(AggConfig ac, boolean reload) throws Exception {
        evalValueExpression(ac);
        if (isDataSourceAggInstance()) {
            return ((Aggregatable) this).queryAggData(ac);
        } else {
            checkAndLoad(reload);
            return innerAggregator.queryAggData(ac);
        }
    }

    public final String getViewAggDataQuery(AggConfig config) throws Exception {
        evalValueExpression(config);
        if (isDataSourceAggInstance()) {
            return ((Aggregatable) this).viewAggDataQuery(config);
        } else {
            return "Not Support";
        }
    }

    /**
     * Get the options values of a dimension column
     *
     * @param columnName
     * @return
     */
    public final String[] getDimVals(String columnName, AggConfig config, boolean reload) throws Exception {
        String[] dimVals = null;
        evalValueExpression(config);
        if (isDataSourceAggInstance()) {
            dimVals = ((Aggregatable) this).queryDimVals(columnName, config);
        } else {
            checkAndLoad(reload);
            dimVals = innerAggregator.queryDimVals(columnName, config);
        }
        return Arrays.stream(dimVals)
                .map(member -> {
                    return Objects.isNull(member) ? NULL_STRING : member;
                })
                .sorted(new StringComparator()).limit(1000).toArray(String[]::new);
    }

    public String[] invokeGetColumn(boolean reload) throws Exception {
        String[] columns = null;
        if (isDataSourceAggInstance()) {
            columns = ((Aggregatable) this).getColumn(reload);
        } else {
            checkAndLoad(reload);
            columns = innerAggregator.getColumn(reload);
        }
        Arrays.sort(columns);
        return columns;
    }

    private void checkAndLoad(boolean reload) throws Exception {
        String key = getLockKey();
        synchronized (key.intern()) {
            if (reload || !innerAggregator.checkExist()) {  //
                String[][] data = getData();
                if (data != null) {
                    innerAggregator.loadData(data, interval);
                }
                logger.info("loadData {}", key);
            }
        }
    }

    private void evalValueExpression(AggConfig ac) {
        if (ac == null) {
            return;
        }
        ac.getFilters().forEach(e -> evaluator(e));
        ac.getColumns().forEach(e -> evaluator(e));
        ac.getRows().forEach(e -> evaluator(e));
    }

    private void evaluator(ConfigComponent e) {
        if (e instanceof DimensionConfig) {
            DimensionConfig dc = (DimensionConfig) e;
            dc.setValues(dc.getValues().stream().flatMap(v -> getFilterValue(v)).collect(Collectors.toList()));
        }
        if (e instanceof CompositeConfig) {
            CompositeConfig cc = (CompositeConfig) e;
            cc.getConfigComponents().forEach(_e -> evaluator(_e));
        }
    }

    private Stream<String> getFilterValue(String value) {
        List<String> list = new ArrayList<>();
        if (value == null || !(value.startsWith("{") && value.endsWith("}"))) {
            list.add(value);
//        } else if ("{username}".equals(value)) {
//            list.add(authService.getCurrentUser().getUsername());
//        } else if ("{realName}".equals(value)) {
//            list.add(authService.getCurrentUser().getName());
//        } else if ("{userRoles}".equals(value)) {
//            String userId = authService.getCurrentUser().getUserId();
//            Map<String, Object> params = new HashMap<>();
//            params.put("userId", userId);
//            List<DashAdminRole> roles = dashAdminRoleService.getList(params);
//            roles.forEach(role -> list.add(role.getRoleName()));
        } else {
            list.add(AviatorEvaluator.compile(value.substring(1, value.length() - 1), true).execute().toString());
        }
        return list.stream();
    }

    public String getLockKey() {
        String dataSourceStr = JSON.toJSON(dataSource).toString();
        String queryStr = JSON.toJSON(query).toString();
        return Hashing.md5().newHasher().putString(dataSourceStr + queryStr, Charsets.UTF_8).hash().toString();
    }

    public List<DimensionConfig> filterCCList2DCList(List<ConfigComponent> filters) {
        List<DimensionConfig> result = new LinkedList<>();
        filters.stream().forEach(cc -> {
            result.addAll(configComp2DimConfigList(cc));
        });
        return result;
    }

    public List<DimensionConfig> configComp2DimConfigList(ConfigComponent cc) {
        List<DimensionConfig> result = new LinkedList<>();
        if (cc instanceof DimensionConfig) {
            result.add((DimensionConfig) cc);
        } else {
            Iterator<ConfigComponent> iterator = cc.getIterator();
            while (iterator.hasNext()) {
                ConfigComponent next = iterator.next();
                result.addAll(configComp2DimConfigList(next));
            }
        }
        return result;
    }

    abstract public String[][] getData() throws Exception;

    public void test() throws Exception {
        getData();
    }

    public void setDataSource(Map<String, String> dataSource) {
        this.dataSource = dataSource;
    }

    public void setQuery(Map<String, String> query) {
        this.query = query;
    }

    public void setResultLimit(int resultLimit) {
        this.resultLimit = resultLimit;
    }

    public int getResultLimit() {
        return resultLimit;
    }

    public void setInterval(long interval) {
        this.interval = interval;
    }

    public InnerAggregator getInnerAggregator() {
        return innerAggregator;
    }

    public void setInnerAggregator(InnerAggregator innerAggregator) {
        this.innerAggregator = innerAggregator;
    }

    public boolean isUsedForTest() {
        return isUsedForTest;
    }

    public void setUsedForTest(boolean usedForTest) {
        isUsedForTest = usedForTest;
    }

}
