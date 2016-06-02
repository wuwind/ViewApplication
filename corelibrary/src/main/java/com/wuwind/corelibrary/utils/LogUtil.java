package com.wuwind.corelibrary.utils;

import android.util.Log;

/**
 * Created by Administrator on 2016/3/11.
 */
public class LogUtil {

    private static boolean isDbug = true;
    private static int[] debugIDs = new int[]{0};//

    public static void setDebug(boolean isdbug) {
        isDbug = isdbug;
    }

    public static void setDebugIDs(int[] debugIds ) {
        debugIDs = debugIds;
    }

    public static void e(double content) {
        e(0, content+"");
    }

    public static void e(String content) {
        e(0, content);
    }

    public static void e(int id, double content) {
        e(id,"", content+"");
    }

    public static void e(int id,  String Tag, double content) {
        e(id, Tag, content+"");
    }

    public static void e(int id, String content) {
        e(id, "LogUtil", content);
    }

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
