package com.thlh.baselib.utils;


import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;



public class TimeUtils {

    public static String longToDateString(long unixDate, String dateformat) {
        if(unixDate>0){
            String date = new SimpleDateFormat(dateformat).format(new Date(unixDate*1000));
            return date;
        }else{
            return "";
        }
    }

    public static String stringToDateString(String unixDate) {
        if(unixDate != null && !unixDate.equals("")){
            long time = Long.parseLong(unixDate)*1000;
            String date = new SimpleDateFormat("yyyy-M-d HH:mm").format(new Date(time));
            return date;
        }else{
            return "";
        }
    }

    public static String stringToDateString(String unixDate, String dateformat) {
        if(unixDate != null && !unixDate.equals("")){
            long time = Long.parseLong(unixDate)*1000;
            String date = new SimpleDateFormat(dateformat).format(new Date(time));
            return date;
        }else{
            return "";
        }
    }



    public static long dateStringToLong(String date) {
        long time =0l;
        try{
            if(date!=null&&!date.equals("")){
                SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd");
                time = sf.parse(date).getTime()/1000;
            }
        }catch (ParseException e){
            e.printStackTrace();
        }
        return time;
    }

}
