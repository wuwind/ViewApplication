package com.wuwind.corelibrary.utils;

import java.util.Date;

public class TimeCompareUtil {

    //是否相差1小时 yyyy-MM-dd HH:mm:ss
    public static boolean isLowOneHour(long timestamp) {
        //设定时间的模板
        Date d1 = new Date();
        //比较
        if ((d1.getTime() - timestamp) >= 3600) {
            return false;
        } else {
            return true;
        }
    }
}
