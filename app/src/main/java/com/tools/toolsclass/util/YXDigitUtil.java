package com.tools.toolsclass.util;


import java.text.DecimalFormat;

/**
 * 主要是对数字相关问题进行处理，比如四舍五入、数字类型转换，百分百转换
 * Created by 杨小明 on 2018/8/14.
 */

public class YXDigitUtil {
    private final static String FLOAT_PERCENT_PATTERN = "#%";
    private final static String FLOAT_KEEP_1DECIMAL = "#.#";
    private final static String FLOAT_KEEP_2DECIMAL = "#.##";
    private final static String FLOAT_PERCENT_KEEP_2_DECIMAL_PATTERN = "#.##%";


    /**
     * 显示百分比，不保留小数，且小数部分四舍五入
     * 如：0.957 = 96%
     * 0.954 = 95%
     *
     * @param digit
     * @return
     */
    public static String floatToPercent(String digit) {
        double d = 0;
        try {
            d = Double.valueOf(digit);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new DecimalFormat(FLOAT_PERCENT_PATTERN).format(d);
    }

    /**
     * 显示数字，如果存在小数最多保留1位小数，四舍五入
     * 1.678->1.7
     * @param digit
     * @return
     */
    public static String floatKeep1Decimal(String digit) {
        double d = 0d;
        //防止格式不正确或者为null
        try {
            d = Double.valueOf(digit);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new DecimalFormat(FLOAT_KEEP_1DECIMAL).format(d);
    }

    /**
     * 显示数字，如果存在小数最多保留1位小数，四舍五入
     * 1.678->1.7
     * @param digit
     * @return
     */
    public static String floatKeep1Decimal(float digit) {
        double d = 0d;
        //防止格式不正确或者为null
        try {
            d = Double.valueOf(digit);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new DecimalFormat(FLOAT_KEEP_1DECIMAL).format(d);
    }

    /**
     * 显示数字，如果存在小数最多保留2位小数，四舍五入
     * 1.678->1.68
     * @param digit
     * @return
     */
    public static String floatKeep2Decimal(String digit) {
        double d = 0d;
        //防止格式不正确或者为null
        try {
            d = Double.valueOf(digit);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new DecimalFormat(FLOAT_KEEP_2DECIMAL).format(d);
    }



    /**
     * 显示百分比，如果存在小数最多取两位小数, 最后1位四舍五入
     * 如果最后一位为0，则去掉，如果小数点后两位都为0，则不显示小数部分
     *  0.09156->9.16%
     * @param digit
     * @return
     */
    public static String floatToPercentKeep2Decimal(String digit) {
        double d = 0;
        try {
            d = Double.valueOf(digit);
        } catch (Exception e) {

        }
        return new DecimalFormat(FLOAT_PERCENT_KEEP_2_DECIMAL_PATTERN).format(d);
    }

    /**
     * 把byte转化为KB、MB、GB的方法
     * @param size
     * @return
     */
    public static String getNetFileSizeDescription(long size) {
        StringBuffer bytes = new StringBuffer();
//        DecimalFormat format = new DecimalFormat("###.0");
        DecimalFormat format = new DecimalFormat(FLOAT_KEEP_2DECIMAL);
        if (size >= 1024 * 1024 * 1024) {
            double i = (size / (1024.0 * 1024.0 * 1024.0));
            bytes.append(format.format(i)).append("GB");
        }
        else if (size >= 1024 * 1024) {
            double i = (size / (1024.0 * 1024.0));
            bytes.append(format.format(i)).append("MB");
        }
        else if (size >= 1024) {
            double i = (size / (1024.0));
            bytes.append(format.format(i)).append("KB");
        }
        else if (size < 1024) {
            if (size <= 0) {
                bytes.append("0B");
            }
            else {
                bytes.append((int) size).append("B");
            }
        }
        return bytes.toString();
    }
}
