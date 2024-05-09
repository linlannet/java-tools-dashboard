package io.linlan.tools.data.provider;

import com.alibaba.fastjson.JSONObject;
import com.google.common.base.Charsets;
import com.google.common.hash.Hashing;
import io.linlan.config.SolrServerPoolFactory;
import org.apache.commons.pool2.impl.GenericObjectPoolConfig;
import org.apache.solr.client.solrj.SolrClient;
import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.impl.HttpSolrClient;
import org.apache.solr.client.solrj.response.QueryResponse;
import org.apache.solr.common.SolrDocumentList;
import org.apache.solr.common.util.SimpleOrderedMap;
import io.linlan.commons.core.CoreException;
import io.linlan.commons.core.StringUtils;
import io.linlan.commons.db.annotation.DataProviderName;
import io.linlan.commons.db.annotation.DataQueryParameter;
import io.linlan.commons.db.annotation.DataSourceParameter;
import io.linlan.datas.core.abs.Aggregatable;
import io.linlan.datas.core.abs.Initializing;
import io.linlan.datas.core.provider.config.*;
import io.linlan.datas.core.provider.result.AggregateResult;
import io.linlan.datas.core.provider.result.ColumnIndex;
import io.linlan.datas.core.utils.DataProviderUtils;
import io.linlan.datas.core.utils.SqlHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;

import java.net.URLDecoder;
import java.util.*;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

/**
 *
 * Filename:SolrDataProvider.java
 * Desc:
 *
 * @author Linlan
 * CreateTime:2017/12/20 22:15
 *
 * @version 1.0
 * @since 1.0
 *
 */
@DataProviderName(name = "Solr")
public class SolrDataProvider extends DataProvider implements Aggregatable, Initializing {

    private static final Logger logger = LoggerFactory.getLogger(SolrDataProvider.class);

    @Value("${dataprovider.resultLimit:300000}")
    private int resultLimit;

    @DataSourceParameter(label = "{{'DATAPROVIDER.SOLR.SOLR_SERVERS'|translate}}", required = true, placeholder = "<ip>:<port>,[<ip>:<port>]...", type = DataSourceParameter.Type.Input, order = 1)
    private String SOLR_SERVERS = "solrServers";

    @DataSourceParameter(label = "{{'DATAPROVIDER.POOLEDCONNECTION'|translate}}", type = DataSourceParameter.Type.Checkbox, order = 2)
    private String POOLED = "pooled";

    @DataSourceParameter(label = "{{'DATAPROVIDER.AGGREGATABLE_PROVIDER_SOLR'|translate}}", type = DataSourceParameter.Type.Checkbox, order = 3)
    private String AGGREGATE_PROVIDER = "aggregateProvider";

    @DataQueryParameter(label = "{{'DATAPROVIDER.SOLR.COLLECTION'|translate}}", required = true, pageType = "test,dataset,widget", type = DataQueryParameter.Type.Input, order = 1)
    private String COLLECTION = "collection";

    @DataQueryParameter(label = "{{'DATAPROVIDER.SOLR.Q'|translate}}", required = true, pageType = "dataset,widget", value = "*:*", placeholder = "*:*|<fieldName>:<fieldValue>[ <AND|OR> <fieldName>:<fieldValue>]...", type = DataQueryParameter.Type.TextArea2, order = 2)
    private String Q = "q";

    @DataQueryParameter(label = "{{'DATAPROVIDER.SOLR.FQ'|translate}}", pageType = "dataset,widget", placeholder = "<fieldName>:<fieldValue>[,<fieldName>:<fieldValue>]...", type = DataQueryParameter.Type.Input, order = 3)
    private String FQ = "fq";

    @DataQueryParameter(label = "{{'DATAPROVIDER.SOLR.SORT'|translate}}", pageType = "dataset,widget", placeholder = "<fieldName> <ASC|DESC>[,<fieldName> <ASC|DESC>]...", type = DataQueryParameter.Type.Input, order = 4)
    private String SORT = "sort";

    @DataQueryParameter(label = "{{'DATAPROVIDER.SOLR.START'|translate}}", required = true, pageType = "dataset,widget", value = "0", placeholder = "default value is 0", type = DataQueryParameter.Type.Number, order = 5)
    private String START = "start";

    @DataQueryParameter(label = "{{'DATAPROVIDER.SOLR.ROWS'|translate}}", required = true, pageType = "dataset,widget", value = "10", placeholder = "default value is 10", type = DataQueryParameter.Type.Number, order = 6)
    private String ROWS = "rows";

    @DataQueryParameter(label = "{{'DATAPROVIDER.SOLR.FL'|translate}}", pageType = "dataset,widget", placeholder = "*|<fieldName>[,<fieldName>]...", type = DataQueryParameter.Type.Input, order = 7)
    private String FL = "fl";

    private static Map<String, SolrServerPoolFactory> poolMap;

    private synchronized SolrServerPoolFactory getSolrServerPoolFactory(String solrServers, String collectionName) {
        String poolKey = getPoolKey(solrServers, collectionName);
        if (poolMap == null) {
            poolMap = new HashMap();
        }
        SolrServerPoolFactory factory = poolMap.get(poolKey);
        if (factory == null) {
            GenericObjectPoolConfig config = new GenericObjectPoolConfig();
            config.setMaxTotal(10);
            config.setLifo(true);
            config.setMinIdle(1);
            config.setMaxTotal(10);
            config.setMaxWaitMillis(3000);
            config.setTimeBetweenEvictionRunsMillis(30000);
            config.setTestWhileIdle(true);
            config.setTestOnBorrow(false);
            config.setTestOnReturn(false);
            factory = new SolrServerPoolFactory(config, solrServers, collectionName);
        }
        poolMap.put(poolKey, factory);
        return factory;
    }

    private SolrClient getConnection(String solrServers, String collectionName) {
        String usePool = dataSource.get(POOLED);
        SolrClient solrServer = null;
        if (usePool != null && "true".equals(usePool)) {
            solrServer = getSolrServerPoolFactory(solrServers, collectionName).getConnection();
        } else {
            solrServer = getSolrServer(solrServers, collectionName);
        }
        return solrServer;
    }

    private void releaseConnection(String solrServers, String collectionName, SolrClient solrClient) {
        getSolrServerPoolFactory(solrServers, collectionName).releaseConnection(solrClient);
    }

    private SolrQuery getSolrQuery() {
        SolrQuery solrQuery = new SolrQuery();

        String q = StringUtils.isBlank(query.get(Q)) ? "*:*" : query.get(Q);
        String fqs = query.getOrDefault(FQ, "");
        String fl = query.getOrDefault(FL, "");
        String sort = query.getOrDefault(SORT, "");
        int start = StringUtils.isBlank(query.get(START)) ? 0 : Integer.parseInt(query.get(START));
        int rows = StringUtils.isBlank(query.get(ROWS)) ? 10 : Integer.parseInt(query.get(ROWS));

        solrQuery.set("q", q);
        String[] fqArr = fqs.split(",");
        for (String fq : fqArr)
            solrQuery.set("fq", fq.trim());
        solrQuery.set("fl", fl);
        solrQuery.set("sort", sort);
        solrQuery.setStart(start);
        solrQuery.setRows(rows);

        logger.debug("solrQuery=" + solrQuery.toString());
        return solrQuery;
    }

    private QueryResponse getQueryResponse(String solrServers, String collectionName, SolrQuery solrQuery) {
        SolrClient solrServer = null;
        QueryResponse res = null;
        try {
            solrServer = getConnection(solrServers, collectionName);
            res = solrServer.query(solrQuery);
        } catch (Exception e) {
            logger.error("", e);
        } finally {
            if (solrServer != null) {
                releaseConnection(solrServers, collectionName, solrServer);
            }
        }
        return res;
    }

    private String getPoolKey(String solrServers, String collectionName) {
        return Hashing.md5().newHasher().putString(solrServers + "_" + collectionName, Charsets.UTF_8).hash().toString();
    }

    private String getCacheKey() {
        return Hashing.md5().newHasher().putString(JSONObject.toJSON(dataSource).toString() + JSONObject.toJSON(query).toString(), Charsets.UTF_8).hash().toString();
    }

    private SolrClient getSolrServer(String solrServers, String collectionName) {
        String[] tempServers = solrServers.split(",");
        String[] servers = new String[tempServers.length];
        for (int i = 0; i < tempServers.length; i++) {
            servers[i] = "http://" + tempServers[i] + "/solr/" + collectionName;
        }
        //modify 2017-12-20
        SolrClient solrServer = new HttpSolrClient.Builder(servers[0]).withConnectionTimeout(10000)
                .withSocketTimeout(60000)
                .build();
        return solrServer;
    }

    @Override
    public String[][] getData() throws Exception {
        return getSolrData();
    }

    @Override
    public void test() throws Exception {
        getSolrData();
    }

    public String[][] getSolrData() throws Exception {
        String solrServers = dataSource.get("solrServers");
        if (StringUtils.isBlank(solrServers))
            throw new CoreException("Datasource config Solr Servers can not be empty.");
        String collectionName = query.get("collection");
        if (StringUtils.isBlank(collectionName))
            throw new CoreException("Collection can not be empty.");

        QueryResponse qs = getQueryResponse(solrServers, collectionName, getSolrQuery());

        if (qs == null || qs.getResults().size() == 0) {
            return new String[0][0];
        }

        SolrDocumentList results = qs.getResults();
        Set<Map.Entry<String, Object>> entrySet = results.get(0).entrySet();
        String[][] strings = new String[results.size() + 1][entrySet.size()];

        // 字段行
        int col = 0;
        for (Map.Entry<String, Object> entry : entrySet) {
            strings[0][col] = entry.getKey();
            col++;
        }

        if (results.size() > resultLimit) {
            throw new CoreException("Cube result count is greater than limit " + resultLimit);
        }

        // 数据集
        for (int i = 1; i <= results.size(); i++) {
            int j = 0;
            for (Map.Entry<String, Object> e : entrySet) {
                strings[i][j] = String.valueOf(results.get(i - 1).get(e.getKey()));
                j++;
            }
        }

        return strings;
    }

    public String[][] getSolrData(String dimColsStr, SolrDocumentList results) throws Exception {
        if (StringUtils.isEmpty(dimColsStr) || results == null || results.size() <= 0) {
            throw new CoreException("Cube result is null");
        }
        String[] fields = dimColsStr.split(",");
        String[][] strings = new String[results.size()][fields.length];
        // 数据集
        for (int i = 0; i < results.size(); i++) {
            for (int j = 0; j < fields.length; j++) {
                strings[i][j] = String.valueOf(results.get(i).get(fields[j]));
            }
        }
        return strings;
    }

    /**
     * Solr5.x才开始支持group by a,b
     *
     * @return
     */
    @Override
    public boolean doAggregationInDataSource() {
        String v = dataSource.get(AGGREGATE_PROVIDER);
        return v != null && "true".equals(v);
    }

    @Override
    public void afterPropertiesSet() throws Exception {

    }

    @Override
    public String[] queryDimVals(String columnName, AggConfig config) throws Exception {
        String[][] data = getSolrData();
        Map<String, Integer> columnIndex = getColumnIndex(data);
        final int fi = columnIndex.get(columnName);
        String[] result = Arrays.stream(data).parallel().skip(1)
                .filter(e -> e != null)
                .map(e -> e[fi])
                .distinct()
                .toArray(String[]::new);
        return result;
    }

    private Map<String, Integer> getColumnIndex(String[][] data) {
        Map<String, Integer> map = new HashMap<>();
        for (int i = 0; i < data[0].length; i++) {
            map.put(data[0][i], i);
        }
        return map;
    }

    @Override
    public String[] getColumn(boolean reload) throws Exception {
        String solrServers = dataSource.get("solrServers");
        if (StringUtils.isBlank(solrServers))
            throw new CoreException("Datasource config Solr Servers can not be empty.");
        String collectionName = query.get("collection");
        if (StringUtils.isBlank(collectionName))
            throw new CoreException("Collection can not be empty.");

        QueryResponse qs = getQueryResponse(solrServers, collectionName, getSolrQuery());

        if (qs == null || qs.getResults().size() == 0) {
            return new String[0];
        }

        SolrDocumentList results = qs.getResults();
        Set<Map.Entry<String, Object>> entrySet = results.get(0).entrySet();
        String[] fields = new String[entrySet.size()];

        // 字段行
        int col = 0;
        for (Map.Entry<String, Object> entry : entrySet) {
            fields[col] = entry.getKey();
            col++;
        }
        return fields;
    }


    @Override
    public AggregateResult queryAggData(AggConfig config) throws Exception {
        //获取统计集合
        QueryResponse response = getQueryResponse(dataSource.get("solrServers"), query.get("collection"), getAggQuery(config));
        //获取行列字段
        Supplier<Stream<DimensionConfig>> dimStream = () -> Stream.concat(config.getColumns().stream(), config.getRows().stream());
        String dimColsStr = assembleDimColumns(dimStream.get()).replaceAll(" ", "");
        List<ColumnIndex> dimensionList = dimStream.get().map(ColumnIndex::fromDimensionConfig).collect(Collectors.toList());
        //没有聚合参数直接返回
        if (config.getValues().size() <= 0) {
            String[][] strings = getSolrData(dimColsStr, response.getResults());
            IntStream.range(0, dimensionList.size()).forEach(j -> dimensionList.get(j).setIndex(j));
            return new AggregateResult(dimensionList, strings);
        }
        //解析统计集合
        SimpleOrderedMap obj = (SimpleOrderedMap) ((SimpleOrderedMap) response.getResponse().get("facet_counts")).get("facet_pivot");
        List statList = (List) obj.get(dimColsStr);
        List<String[]> list = dealStatList(statList, config);
        return DataProviderUtils.transform2AggResult(config, list);

//        int dimSize = dimensionList.size();
//        dimensionList.addAll(config.getValues().stream().map(ColumnIndex::fromValueConfig).collect(Collectors.toList()));
//        IntStream.range(0, dimensionList.size()).forEach(j -> dimensionList.get(j).setIndex(j));
//        list.forEach(row -> {
//            IntStream.range(0, dimSize).forEach(a -> {
//                if (row[a] == null) row[a] = NULL_STRING;
//            });
//        });
//        String[][] result = list.toArray(new String[][]{});
//        return new AggregateResult(dimensionList, result);
    }

    private List<String[]> dealStatList(List statList, AggConfig config) {
        Stream<DimensionConfig> dimStream = Stream.concat(config.getColumns().stream(), config.getRows().stream());
        String dimColsStr = assembleDimColumns(dimStream).replaceAll(" ", "");
        List<String[]> list = new LinkedList<>();
        for (ValueConfig e : config.getValues()) {
            dimColsStr += "," + e.getAggType() + "@" + e.getColumn();
        }
        String[] fields = dimColsStr.split(",");
        for (Object e : statList) {
            SimpleOrderedMap orderedMap = (SimpleOrderedMap) e;
            String[] row = new String[fields.length];
            for (int x = 0; x < fields.length; x++) {
                if (x <= fields.length - config.getValues().size() - 1) {
                    if (orderedMap.get("value") != null && fields[x].equals(orderedMap.get("field"))) {
                        row[x] = orderedMap.get("value").toString();
                    }
                    if (x == fields.length - config.getValues().size() - 1) {
                        orderedMap = (SimpleOrderedMap) ((SimpleOrderedMap) orderedMap.get("stats")).get("stats_fields");
                    } else {
                        List orderedList = (List) orderedMap.get("pivot");
                        if (orderedList != null) {
                            orderedMap = (SimpleOrderedMap) orderedList.get(0);
                        } else {
                            continue;
                        }
                    }
                } else {
                    String aggType = fields[x].split("@")[0];
                    String field = fields[x].split("@")[1];
                    SimpleOrderedMap statMap = (SimpleOrderedMap) orderedMap.get(field);
                    if (orderedMap != null) {
                        switch (aggType) {
                            case "sum":
                                row[x] = statMap.get("sum") != null ? statMap.get("sum").toString() : "";
                                break;
                            case "avg":
                                row[x] = statMap.get("mean") != null ? statMap.get("mean").toString() : "";
                                break;
                            case "max":
                                row[x] = statMap.get("max") != null ? statMap.get("max").toString() : "";
                                break;
                            case "min":
                                row[x] = statMap.get("min") != null ? statMap.get("min").toString() : "";
                                break;
                            case "distinct":
                                row[x] = statMap.get("count") != null ? statMap.get("count").toString() : "";
                                break;
                            default:
                                row[x] = statMap.get("count") != null ? statMap.get("count").toString() : "";
                        }
                    }
                }
            }
            list.add(row);
        }
        return list;
    }

    private String configComponentToSql(ConfigComponent cc) {
        if (cc instanceof DimensionConfig) {
            return filter2SolrCondition.apply((DimensionConfig) cc);
        } else if (cc instanceof CompositeConfig) {
            CompositeConfig compositeConfig = (CompositeConfig) cc;
            String sql = compositeConfig.getConfigComponents().stream().map(e -> SqlHelper.separateNull(e)).map(e -> configComponentToSql(e)).collect(Collectors.joining(" " + compositeConfig.getType() + " "));
            return "(" + sql + ")";
        }
        return null;
    }

    /**
     * Parser a single filter configuration to sql syntax
     */
    private Function<DimensionConfig, String> filter2SolrCondition = (config) -> {
        if (config.getValues().size() == 0) {
            return null;
        }
        if (NULL_STRING.equals(config.getValues().get(0))) {
            switch (config.getFilterType()) {
                case "=":
                    return "-" + config.getColumnName() + ":*";
                case "≠":
                    return config.getColumnName() + ":*";
            }
        }

        switch (config.getFilterType()) {
            case "=":
            case "eq":
                return config.getColumnName() + ":(" + IntStream.range(0, config.getValues().size()).boxed().map(i -> config.getValues().get(i)).collect(Collectors.joining("OR", " ", " ")) + ")";
            case "≠":
            case "ne":
                return "-" + config.getColumnName() + ":(" + IntStream.range(0, config.getValues().size()).boxed().map(i -> config.getValues().get(i)).collect(Collectors.joining("OR", " ", " ")) + ")";
            case ">":
                return config.getColumnName() + ":{" + config.getValues().get(0) + " TO *]";
            case "<":
                return config.getColumnName() + ":{* TO " + config.getValues().get(0) + "}";
            case "≥":
                return config.getColumnName() + ":[" + config.getValues().get(0) + " TO *]";
            case "≤":
                return config.getColumnName() + ":{* TO " + config.getValues().get(0) + "]";
            case "(a,b]":
                if (config.getValues().size() >= 2) {
                    return config.getColumnName() + ":{" + config.getValues().get(0) + " TO " + config.getValues().get(1) + "]";
                } else {
                    return null;
                }
            case "[a,b)":
                if (config.getValues().size() >= 2) {
                    return config.getColumnName() + ":[" + config.getValues().get(0) + " TO " + config.getValues().get(1) + "}";
                } else {
                    return null;
                }
            case "(a,b)":
                if (config.getValues().size() >= 2) {
                    return config.getColumnName() + ":{" + config.getValues().get(0) + " TO " + config.getValues().get(1) + "}";
                } else {
                    return null;
                }
            case "[a,b]":
                if (config.getValues().size() >= 2) {
                    return config.getColumnName() + ":[" + config.getValues().get(0) + " TO " + config.getValues().get(1) + "]";
                } else {
                    return null;
                }
        }
        return null;
    };

    private String assembleDimColumns(Stream<DimensionConfig> columnsStream) {
        StringJoiner columns = new StringJoiner(", ", "", " ");
        columns.setEmptyValue("");
        columnsStream.map(g -> g.getColumnName()).distinct().filter(e -> e != null).forEach(columns::add);
        return columns.toString();
    }

    private SolrQuery getAggQuery(AggConfig config) throws Exception {
        Stream<DimensionConfig> dimStream = Stream.concat(config.getColumns().stream(), config.getRows().stream());
        String dimColsStr = assembleDimColumns(dimStream).replaceAll(" ", "");
        SolrQuery sQuery = getSolrQuery();
        //行纬 列维 过滤 过滤条件
        Stream<DimensionConfig> c = config.getColumns().stream();
        Stream<DimensionConfig> r = config.getRows().stream();
        Stream<ConfigComponent> f = config.getFilters().stream();
        Stream<ConfigComponent> filters = Stream.concat(Stream.concat(c, r), f);
        StringJoiner where = new StringJoiner(",", "", "");
        where.setEmptyValue("");
        filters.map(e -> SqlHelper.separateNull(e)).map(e -> configComponentToSql(e)).filter(e -> e != null).forEach(where::add);
        if (org.apache.commons.lang.StringUtils.isNotEmpty(where.toString())) {
            sQuery.set("fq", where.toString().split(","));
        }
        //存在聚合参数
        if (config.getValues() != null && config.getValues().size() > 0) {
            sQuery.set("stats", true);
            String[] stats = new String[config.getValues().size()];
            int i = 0;
            for (ValueConfig e : config.getValues()) {
                stats[i] = "{!tag=piv}" + e.getColumn();
                i++;
            }
            sQuery.set("stats.field", stats);
            sQuery.setFacet(true);
            sQuery.add("facet.pivot", "{!stats=piv}" + dimColsStr);
        }
        sQuery.set("wt", "json");
        return sQuery;
    }

    @Override
    public String viewAggDataQuery(AggConfig ac) throws Exception {
        //返回查询sql
        return "http://" + dataSource.get("solrServers") + "/solr/" + query.get("collection") + "/select?" +
                URLDecoder.decode(getAggQuery(ac).toString(), "UTF-8");
    }
}