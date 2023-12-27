package io.linlan.tools.data.aggregator.utils;

import java.math.BigDecimal;

public class ResultFunctions {
    public ResultFunctions() {
    }

    /**
     * 字符串转double，会出现科学计数法
     *
     * @param str
     * @return 返回double类型
     */
    public static double parseStr2Double(String str) {
        if (null == str) {
            return 0.0D;
        } else {
            try {
                return Double.parseDouble(str);
            } catch (Exception var) {
                return 0.0D;
            }
        }
    }

    /**
     * 字符串转BigDecimal
     *
     * @param str
     * @return 返回BigDecimal类型
     */
    public static BigDecimal parseStrBigDecimal(String str) {
        if (null == str) {
            return new BigDecimal("0");
        } else {
            try {
                return new BigDecimal(str);
            } catch (Exception var) {
                return new BigDecimal("0");
            }
        }
    }
}
