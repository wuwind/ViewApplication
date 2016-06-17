package com.wuwind.corelibrary.utils;

import android.util.Log;

/**
 * Created by Administrator on 2016/3/11.
 */
public class LogUtil {

    private static boolean isDbug = true;
    private static int[] debugIDs = new int[]{0};//

    /**
     * 是否开启日志
     * @param isdbug
     */
    public static void setDebug(boolean isdbug) {
        isDbug = isdbug;
    }

    /**
     * 设置需要输出的debugId，没有设置则只输出debugId等0的日志
     * @param debugIds
     */
    public static void setDebugIDs(int[] debugIds ) {
        debugIDs = debugIds;
    }

    /**
     * see {@link #e(int, String, String)}
     */
    public static void e(double content) {
        e(0, content+"");
    }
    /**
     * see {@link #e(int, String, String)}
     */
    public static void e(String content) {
        e(0, content);
    }
    /**
     * see {@link #e(int, String, String)}
     */
    public static void e(int id, double content) {
        e(id,"", content+"");
    }
    /**
     * see {@link #e(int, String, String)}
     */
    public static void e(int id,  String Tag, double content) {
        e(id, Tag, content+"");
    }
    /**
     * see {@link #e(int, String, String)}
     */
    public static void e(int id, String content) {
        e(id, "LogUtil", content);
    }

    /**
     * 输出日志
     * @param id 日志id
     * @param Tag
     * @param content 日志内容
     */
    public static void e(int id, String Tag, String content) {
        if(isDbug) {
            for (int debugID : debugIDs) {
                if(debugID == id) {
                    Log.e(Tag + " debugId:" + id, content);
                    break;
                }
            }
        }
    }
}
