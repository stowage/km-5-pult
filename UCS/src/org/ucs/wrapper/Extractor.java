package org.ucs.wrapper;

import java.text.*;
import java.util.*;

public class Extractor {

    public static Double getDouble(String value){
        Double ret_value = null;
        try{
            ret_value = new Double(value);
        } catch (Exception e){}
        return ret_value;

    }
    public static Integer getInteger(String value){
        Integer ret_value = null;
        try{
            ret_value = new Integer(value);
        } catch (Exception e){}
        return ret_value;

    }
    public static double getDbl(String value) throws NumberFormatException{
        double result = Double.parseDouble(value);
        return result;
    }
    public static double getDblDef(String value, double default_value){
        double result = default_value;
        try{
            result = Double.parseDouble(value);
        } catch(NumberFormatException e){}
        return result;
    }
    public static int getInt(String value) throws NumberFormatException{
        int result = Integer.parseInt(value);
        return result;
    }
    public static int getIntDef(String value, int default_value){
        int result = default_value;
        try{
            result = Integer.parseInt(value);
        } catch(NumberFormatException e){}
        return result;
    }
    public static String getStringDef(String value, String default_value){
        if(value != null)
            return value;
        else
            return default_value;
    }
    public static java.util.Date getDate(String date_str){
        return getDate(date_str, null);
    }
    public static java.util.Date getDate(String date_str, String format_pattern){
        java.util.Date date = null;
        if(date_str != null){
            SimpleDateFormat dateFormat;
            if(format_pattern != null && !format_pattern.equals("")){
                dateFormat = new SimpleDateFormat(format_pattern, Locale.US);
            } else {
                dateFormat = new SimpleDateFormat("MM/dd/yyyy", Locale.US);
            }
            try{
                date = dateFormat.parse(date_str);
            } catch (ParseException e){}
        }
        return date;
    }


}