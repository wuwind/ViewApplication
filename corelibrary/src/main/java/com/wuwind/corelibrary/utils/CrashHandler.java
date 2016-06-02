package com.wuwind.corelibrary.utils;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.Date;

/**
 * Created by Wuhf on 2016/4/11.
 * Description ：
 */
public class CrashHandler implements Thread.UncaughtExceptionHandler {


    public void init() {
        Thread.setDefaultUncaughtExceptionHandler(this);
    }

    @Override
    public void uncaughtException(Thread thread, final Throwable ex) {
        ex.printStackTrace();
        LogUtil.e(0,"uncaughtException");
        new Thread() {
            @Override
            public void run() {
                StringWriter sw = new StringWriter();
                PrintWriter pw = new PrintWriter(sw);
                ex.printStackTrace(pw);
                String s = sw.toString();
                sw.getBuffer().toString();
                LogUtil.e(2, s);
                try {
                    String fileName = DateUtil.convertDate2Str(new Date(), DateUtil.PATTERN_4) + ".txt";
                    FileUtils.writeFile(FilePathUtil.getMainPath() + fileName + ".txt", s);
                    LogUtil.e(0,FilePathUtil.getMainPath() + fileName + ".txt");
                    sw.close();
                } catch (Exception e) {
                    LogUtil.e(0,"error save");
                    e.printStackTrace();
                }
                pw.close();
            }
        }.start();
        System.exit(0);
    }
}