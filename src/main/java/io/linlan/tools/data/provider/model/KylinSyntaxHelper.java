package io.linlan.tools.data.provider.model;

import org.apache.commons.lang.StringUtils;
import io.linlan.datas.core.provider.config.DimensionConfig;
import io.linlan.datas.core.provider.config.ValueConfig;
import io.linlan.datas.core.utils.SqlSyntaxHelper;

/**
 *
 * Filename:KylinSyntaxHelper.java
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
public class KylinSyntaxHelper extends SqlSyntaxHelper {

    private KylinBaseModel kylinModel;

    public KylinSyntaxHelper(KylinBaseModel kylinModel) {
        this.kylinModel = kylinModel;
    }

    @Override
    public String getProjectStr(DimensionConfig config) {
        return kylinModel.getColumnWithAliasPrefix(config.getColumnName());
    }

    @Override
    public String getDimMemberStr(DimensionConfig config, int index) {
        if (kylinModel.getColumnType(config.getColumnName()).startsWith("varchar")) {
            return "'" + config.getValues().get(index) + "'";
        } else {
            return config.getValues().get(index);
        }
    }

    @Override
    public String getAggStr(ValueConfig vConfig) {
        String columnStr = vConfig.getColumn();
        if (columnStr.indexOf(".") != -1) {
            columnStr = StringUtils.substringAfter(vConfig.getColumn(), ".");
        }
        switch (vConfig.getAggType()) {
            case "sum":
                return "SUM(" + kylinModel.getColumnWithAliasPrefix(vConfig.getColumn()) + ") AS sum_" + columnStr;
            case "avg":
                return "AVG(" + kylinModel.getColumnWithAliasPrefix(vConfig.getColumn()) + ") AS avg_" + columnStr;
            case "max":
                return "MAX(" + kylinModel.getColumnWithAliasPrefix(vConfig.getColumn()) + ") AS max_" + columnStr;
            case "min":
                return "MIN(" + kylinModel.getColumnWithAliasPrefix(vConfig.getColumn()) + ") AS min_" + columnStr;
            case "distinct":
                return "COUNT(DISTINCT " + kylinModel.getColumnWithAliasPrefix(vConfig.getColumn()) + ") AS count_d_" + columnStr;
            default:
                return "COUNT(" + kylinModel.getColumnWithAliasPrefix(vConfig.getColumn()) + ") AS count_" + columnStr;
        }
    }


}