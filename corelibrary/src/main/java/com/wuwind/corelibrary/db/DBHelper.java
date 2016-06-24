package com.wuwind.corelibrary.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import java.lang.reflect.Constructor;
import java.lang.reflect.Method;

import de.greenrobot.dao.AbstractDaoMaster;
import de.greenrobot.dao.AbstractDaoSession;
import de.greenrobot.dao.query.QueryBuilder;

/**
 * 数据库帮助类
 */
public class DBHelper {

    private static AbstractDaoMaster daoMaster;
    private static AbstractDaoSession daoSession;

    private DBHelper() {
    }

    private static class InstanceHolder {
        private static DBHelper instance = new DBHelper();
    }

    public static DBHelper getInstance() {
        return InstanceHolder.instance;
    }

    public void init(Context context, String DB_NAME, String daoMasterName) {
        try {
            Class<?> aClass = Class.forName(daoMasterName);

            Class<?> openHelperClass = Class.forName(daoMasterName + "$DevOpenHelper");

            Class[] argType = new Class[]{Context.class, String.class, SQLiteDatabase.CursorFactory.class};
            Object[] argParam = new Object[]{context, DB_NAME, null};
            Constructor constructor = openHelperClass.getConstructor(argType);

            Object oOpenHelper = constructor.newInstance(argParam);

            Method getWritableDatabase = openHelperClass.getMethod("getWritableDatabase");

            Object invoke = getWritableDatabase.invoke(oOpenHelper);

            Class[] argDMType = new Class[]{invoke.getClass()};
            Constructor constructor1 = aClass.getConstructor(argDMType);
            Object o = constructor1.newInstance(invoke);
            daoMaster = (AbstractDaoMaster) o;
            daoSession = daoMaster.newSession();
        } catch (Exception e) {
            e.printStackTrace();
        }
//        OpenHelper helper = new DaoMaster.DevOpenHelper(context, DB_NAME, null);
//		daoMaster = new DaoMaster(helper.getWritableDatabase());
//        daoSession = daoMaster.newSession();
    }

    /**
     * 设置是否为调试模式，调试模式会打印查询语句和结果日志
     *
     * @param debug
     */
    public void setDebugMode(boolean debug) {
        QueryBuilder.LOG_SQL = debug;
        QueryBuilder.LOG_VALUES = debug;
    }

    /**
     * 取得DaoMaster
     *
     * @return
     */
    public static AbstractDaoMaster getDaoMaster() {
        return daoMaster;
    }

    /**
     * 取得DaoSession
     *
     * @return
     */
    public static AbstractDaoSession getDaoSession() {
        if (daoSession == null) {
            if (daoMaster == null) {
                daoMaster = getDaoMaster();
            }
            daoSession = daoMaster.newSession();
        }
        return daoSession;
    }
}
