package com.zero.util.number;

import java.math.BigDecimal;

/**
 * @author yezhaoxing
 * @date 2017/08/10
 */
public class NumberUtil {

    public static double add(double v1, double v2) {// 加法
        return BigDecimal.valueOf(v1).add(BigDecimal.valueOf(v2)).doubleValue();
    }

    public static double sub(double v1, double v2) {// 减法
        return BigDecimal.valueOf(v1).subtract(BigDecimal.valueOf(v2)).doubleValue();
    }

    public static boolean judge(double v1, double v2) {// 比较两个数的大小 v1>=v2返回true
        return BigDecimal.valueOf(v1).subtract(BigDecimal.valueOf(v2)).doubleValue() >= 0;
    }

    public static double mul(double v1, double v2) {// 乘法
        return BigDecimal.valueOf(v1).multiply(BigDecimal.valueOf(v2)).doubleValue();
    }

    public static double div(double v1, double v2) {// 除法
        return BigDecimal.valueOf(v1).divide(BigDecimal.valueOf(v2), 3, BigDecimal.ROUND_HALF_UP).doubleValue();
    }

    public static double div(double v1, double v2, int bit) {// 除法
        return BigDecimal.valueOf(v1).divide(BigDecimal.valueOf(v2), bit, BigDecimal.ROUND_HALF_UP).doubleValue();
    }

    public static double round(double v, String div, int bit) {// 截取几位,bit
        BigDecimal one = new BigDecimal(div);
        return BigDecimal.valueOf(v).divide(one, bit, BigDecimal.ROUND_HALF_UP).doubleValue();
    }

    public static Integer roundUp(double v) {// 四舍五入
        BigDecimal one = new BigDecimal("1");
        return BigDecimal.valueOf(v).divide(one, 0, BigDecimal.ROUND_UP).intValue();
    }

    public static double round(BigDecimal v, String div, int bit) {
        BigDecimal one = new BigDecimal(div);
        return v.divide(one, bit, BigDecimal.ROUND_HALF_UP).doubleValue();
    }

    public static Long moveByte(long oldHistory, long moveAmonut) {
        long moveResult = oldHistory << moveAmonut;
        return Long.parseLong(toFullBinaryString(moveResult), 2) + 1;
    }

    /**
     * 读取
     */
    private static String toFullBinaryString(long num) {
        final int size = 42;
        char[] chs = new char[size];
        for (int i = 0; i < size; i++) {
            chs[size - 1 - i] = (char) (((num >> i) & 1) + '0');
        }
        return new String(chs);
    }
}