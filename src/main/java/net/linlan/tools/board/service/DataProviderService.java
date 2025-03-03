package net.linlan.tools.board.service;

import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.JSONPath;
import com.google.common.base.Functions;
import com.google.common.collect.Maps;
import net.linlan.tools.data.provider.DataProvider;
import net.linlan.tools.board.dto.DataProviderResult;
import net.linlan.tools.board.entity.DashDataset;
import net.linlan.tools.board.entity.DashDatasource;
import org.apache.commons.lang3.StringUtils;
import net.linlan.commons.core.CoreException;
import net.linlan.commons.core.Rcode;
import net.linlan.commons.script.json.JsonUtils;
import net.linlan.datas.core.provider.config.AggConfig;
import net.linlan.datas.core.provider.config.DimensionConfig;
import net.linlan.datas.core.provider.result.AggregateResult;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.*;
import java.util.function.Consumer;

/**
 * Filename:DataProviderService.java
 * Desc: 数据源业务处理服务
 *
 * @author Linlan
 * CreateTime:2018/1/3 12:00
 * @version 1.0
 * @since 1.0
 */
@Repository
public class DataProviderService {
    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private DashDatasourceService dashDatasourceService;

    @Autowired
    private DashDatasetService dashDatasetService;

    private DataProvider getDataProvider(String datasourceId, Map<String, String> query, Dataset dataset) throws Exception {
        if (dataset != null) {
            datasourceId = dataset.getDatasourceId();
            query = dataset.getQuery();
        }
        DashDatasource datasource = dashDatasourceService.findById(datasourceId);
        JSONObject datasourceConfig = JsonUtils.parseJO(datasource.getContent());
        Map<String, String> dataSource = Maps.transformValues(datasourceConfig, Functions.toStringFunction());
        DataProvider dataProvider = DataProviderManager.getDataProvider(datasource.getType(), dataSource, query);
        if (dataset != null && dataset.getInterval() != null && dataset.getInterval() > 0) {
            dataProvider.setInterval(dataset.getInterval());
        }
        return dataProvider;
    }

    public Map<String, String> getDataSource(String datasourceId) {
        DashDatasource datasource = dashDatasourceService.findById(datasourceId);
        JSONObject datasourceConfig = JsonUtils.parseJO(datasource.getContent());
        return Maps.transformValues(datasourceConfig, Functions.toStringFunction());
    }

    public AggregateResult queryAggData(String datasourceId, Map<String, String> query, String datasetId, AggConfig config, boolean reload) {
        try {
            Dataset dataset = getDataset(datasetId);
            attachCustom(dataset, config);
            DataProvider dataProvider = getDataProvider(datasourceId, query, dataset);
            return dataProvider.getAggData(config, reload);
        } catch (Exception e) {
            logger.error("", e);
            throw new CoreException(e.getMessage());
        }
    }

    public DataProviderResult getColumns(String datasourceId, Map<String, String> query, String datasetId, boolean reload) {
        DataProviderResult dps = new DataProviderResult();
        try {
            Dataset dataset = getDataset(datasetId);
            DataProvider dataProvider = getDataProvider(datasourceId, query, dataset);
            String[] result = dataProvider.invokeGetColumn(reload);
            result = doChinFilters(result, "TMP_TABLE_INDEX");//去除添加的排序字段，add on 20210124
            dps.setColumns(result);
            dps.setMsg("1");
        } catch (Exception e) {
            logger.error("", e);
            dps.setMsg(e.getMessage());
        }
        return dps;
    }

    /**
     * 删除数组中的指定值  或者数组中的元素包含指定值，add on 20210124
     *
     * @param filters 数组
     * @param target  指定值
     * @return
     */
    public String[] doChinFilters(String[] filters, String target) {
        String[] res = null;
        if (filters.length > 0) {
            List<String> tempList = Arrays.asList(filters);
            //Arrays.asList(filters) 迭代器实现类 不支持remove() 删除，多一步转化
            List<String> arrList = new ArrayList<String>(tempList);
            Iterator<String> it = arrList.iterator();
            while (it.hasNext()) {
                String x = it.next();
                if (x.indexOf(target) != -1) {
                    it.remove();
                }
            }
            res = new String[arrList.size()];
            arrList.toArray(res);
        }
        return res;
    }

    public String[] getDimensionValues(String datasourceId, Map<String, String> query, String datasetId, String columnName, AggConfig config, boolean reload) {
        try {
            Dataset dataset = getDataset(datasetId);
            attachCustom(dataset, config);
            DataProvider dataProvider = getDataProvider(datasourceId, query, dataset);
            String[] result = dataProvider.getDimVals(columnName, config, reload);
            return result;
        } catch (Exception e) {
            logger.error("", e);
        }
        return null;
    }

    public String viewAggDataQuery(String datasourceId, Map<String, String> query, String datasetId, AggConfig config) {
        try {
            Dataset dataset = getDataset(datasetId);
            attachCustom(dataset, config);
            DataProvider dataProvider = getDataProvider(datasourceId, query, dataset);
            return dataProvider.getViewAggDataQuery(config);
        } catch (Exception e) {
            logger.error("", e);
            throw new CoreException(e.getMessage());
        }
    }

    public Rcode test(JSONObject dataSource, Map<String, String> query) {
        try {
            DataProvider dataProvider = DataProviderManager.getDataProvider(
                    dataSource.getString("type"),
                    Maps.transformValues(dataSource.getJSONObject("content"), Functions.toStringFunction()),
                    query, true);
            dataProvider.test();
            return Rcode.ok();
        } catch (Exception e) {
            logger.error("", e);
            return Rcode.error(e.getMessage());
        }
    }

    public boolean isDataSourceAggInstance(String datasourceId, Map<String, String> query, String datasetId) {
        try {
            Dataset dataset = getDataset(datasetId);
            DataProvider dataProvider = getDataProvider(datasourceId, query, dataset);
            return dataProvider.isDataSourceAggInstance();
        } catch (Exception e) {
            logger.error("", e);
            throw new CoreException(e.getMessage());
        }
    }

    private void attachCustom(Dataset dataset, AggConfig aggConfig) {
        if (dataset == null || aggConfig == null) {
            return;
        }
        Consumer<DimensionConfig> predicate = (config) -> {
            if (StringUtils.isNotEmpty(config.getId())) {
                String custom = (String) JSONPath.eval(dataset.getSchema(), "$.dimension[id='" + config.getId() + "'][0].custom");
                if (custom == null) {
                    custom = (String) JSONPath.eval(dataset.getSchema(), "$.dimension[type='level'].columns[id='" + config.getId() + "'][0].custom");
                }
                config.setCustom(custom);
            }
        };
        aggConfig.getRows().forEach(predicate);
        aggConfig.getColumns().forEach(predicate);
    }

    protected Dataset getDataset(String datasetId) {
        if (datasetId == null) {
            return null;
        }
        return new Dataset(dashDatasetService.findById(datasetId));
    }

    protected class Dataset {
        private String datasourceId;
        private Map<String, String> query;
        private Long interval;
        private JSONObject schema;

        public Dataset(DashDataset dataset) {
            JSONObject data = JsonUtils.parseJO(dataset.getContent());
            this.query = Maps.transformValues(data.getJSONObject("query"), Functions.toStringFunction());
            this.datasourceId = data.getString("datasource");
            this.interval = data.getLong("interval");
            this.schema = data.getJSONObject("schema");
        }

        public JSONObject getSchema() {
            return schema;
        }

        public void setSchema(JSONObject schema) {
            this.schema = schema;
        }

        public String getDatasourceId() {
            return datasourceId;
        }

        public void setDatasourceId(String datasourceId) {
            this.datasourceId = datasourceId;
        }

        public Map<String, String> getQuery() {
            return query;
        }

        public void setQuery(Map<String, String> query) {
            this.query = query;
        }

        public Long getInterval() {
            return interval;
        }

        public void setInterval(Long interval) {
            this.interval = interval;
        }
    }
}
