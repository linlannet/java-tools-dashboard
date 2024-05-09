package io.linlan.tools.board.dto;

import io.linlan.datas.core.provider.config.AggConfig;
import io.linlan.datas.core.provider.config.ConfigComponent;
import io.linlan.datas.core.provider.config.DimensionConfig;
import io.linlan.datas.core.provider.config.ValueConfig;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 
 * Filename:ViewAggConfig.java
 * Desc:
 *
 * @author Linlan
 * CreateTime:2018/1/3 11:58
 *
 * @version 1.0
 * @since 1.0
 *
 */
public class ViewAggConfig {

    private List<DimensionConfig> rows;
    private List<DimensionConfig> columns;
    private List<DimensionConfig> filters;
    private List<ValueConfig> values;

    public static AggConfig getAggConfig(ViewAggConfig viewAggConfig) {
        if (viewAggConfig != null) {
            AggConfig aggConfig = new AggConfig();
            aggConfig.setRows(viewAggConfig.getRows());
            aggConfig.setColumns(viewAggConfig.getColumns());
            if (viewAggConfig.getFilters() != null) {
                aggConfig.setFilters(viewAggConfig.getFilters().stream().map(e -> (ConfigComponent) e).collect(Collectors.toList()));
            } else {
                aggConfig.setFilters(new ArrayList<>());
            }
            aggConfig.setValues(viewAggConfig.getValues());
            return aggConfig;
        }
        return null;
    }

    public List<DimensionConfig> getRows() {
        return rows;
    }

    public void setRows(List<DimensionConfig> rows) {
        this.rows = rows;
    }

    public List<DimensionConfig> getColumns() {
        return columns;
    }

    public void setColumns(List<DimensionConfig> columns) {
        this.columns = columns;
    }

    public List<DimensionConfig> getFilters() {
        return filters;
    }

    public void setFilters(List<DimensionConfig> filters) {
        this.filters = filters;
    }

    public List<ValueConfig> getValues() {
        return values;
    }

    public void setValues(List<ValueConfig> values) {
        this.values = values;
    }
}
