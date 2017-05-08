package org.ucs;

/**
 * Created by IntelliJ IDEA.
 * User: @
 * Date: 11.11.2005
 * Time: 0:57:39
 * To change this template use File | Settings | File Templates.
 */
import java.util.*;
import java.lang.*;
import java.text.*;

public class StringUtils {


    public static boolean isNumeric(String value) {
        NumberFormat format = NumberFormat.getInstance();
        try {
            Number number = format.parse(value);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public static String formatNumber(double value, String pattern) {
        NumberFormat format = NumberFormat.getInstance();
        if (format instanceof DecimalFormat) {
            ((DecimalFormat) format).applyPattern(pattern);
            ((DecimalFormat) format).setDecimalSeparatorAlwaysShown(false);
        }
        return format.format(value);
    }


    public static String formatFloat(float value, int precision) {
        return StringUtils.formatDouble((double) value, precision);
    }

    public static String formatDouble(double value, int precision) {
        String pattern;
        switch (precision) {
            case 2:
                pattern = "###,##0.00";
                break;
            case 4:
                pattern = "###,##0.0000";
                break;
            default:
                StringBuffer tmp = new StringBuffer("###,##0.");
                for (int i = 0; i < precision; i++) {
                    tmp.append('0');
                }
                pattern = tmp.toString();
        }
        return StringUtils.formatNumber(value, pattern);
    }

    public static String formatISODouble(double value, int precision) {
        String pattern;
        switch (precision) {
            case 2:
                pattern = "#####0.00";
                break;
            case 4:
                pattern = "#####0.0000";
                break;
            case 6:
                pattern = "#####0.000000";
                break;
            default:
                StringBuffer tmp = new StringBuffer("#####0.");
                for (int i = 0; i < precision; i++) {
                    tmp.append('0');
                }
                pattern = tmp.toString();
        }
        return StringUtils.formatNumber(value, pattern);
    }

    public static String getNAorNumber(float value) {
        if (value == 0F) {
            return "N/A";
        } else {
            return StringUtils.formatFloat(value, 2);
        }
    }

    public static String getNAorPercent(float value) {
        return getNAorNumber(value * 100);
    }

    public static String formatDate(java.util.Date value, String pattern) {
        SimpleDateFormat format = new SimpleDateFormat(pattern);
        return format.format(value);
    }

    public static String getFracString(float value, int base) {
        return formatDouble((double) value, 2);
    }

}