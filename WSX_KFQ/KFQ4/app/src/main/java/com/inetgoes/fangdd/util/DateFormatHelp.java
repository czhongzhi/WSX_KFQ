package com.inetgoes.fangdd.util;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class DateFormatHelp {


    public static final String CONFIG_PATH = System.getProperty("user.dir").charAt(0) + ":\\FlexWebConfig\\";

    public static final DateFormat DATE_FORMAT = new SimpleDateFormat("yyyy/MM/dd");

    public static final DateFormat SIMPLE_DATE_FORMAT = new SimpleDateFormat("yyyyMMdd");

    public static final DateFormat TIME_FORMAT = new SimpleDateFormat("HH:mm:ss");

    public static final DateFormat SIMPLE_TIME_FORMAT = new SimpleDateFormat("HHmmss");

    public static final DateFormat DATETIME_FORMAT = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");

    public static final DateFormat SIMPLE_DATETIME_FORMAT = new SimpleDateFormat("yyyyMMdd HH:mm:ss");

    public static final DateFormat AndroidApp_Format = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");  //SSS为毫秒

    public static final DateFormat CommentTime_Format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    public static final DateFormat TIME_24_FORMAT = new SimpleDateFormat("HH");  //获取24小时制的时

    public static final DateFormat AppPayTime_Format = new SimpleDateFormat("yyyy-MM-dd HH:mm");


    /**
     * 时间监听器，用于在8：00 - 23：00开启服务检索
     */
    public static boolean timeListen() {
        int hTime = Integer.valueOf(DateFormatHelp.TIME_24_FORMAT.format(new Date()));
        if (hTime >= 8 && hTime <= 23)
            return true;
        return false;
    }
}
