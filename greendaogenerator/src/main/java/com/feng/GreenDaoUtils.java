package com.feng;

import de.greenrobot.daogenerator.Entity;
import de.greenrobot.daogenerator.Schema;

/**
 * Created by Wuhf on 2016/6/23.
 * Description ：
 */
public class GreenDaoUtils {

    private static Schema schema;
    private static Entity note;

    public static void createPackage(int version, String packageName, String tableName) {
        schema = new Schema(version, packageName);
        note = schema.addEntity(tableName);
    }

    /**
     * 一个实体（类）就关联到数据库中的一张表，此处表名为「Note」（既类名）
     * Entity note = schema.addEntity("Note");
     * greenDAO 会自动根据实体类的属性值来创建表字段，并赋予默认值
     * 设置表中的字段：
     * note.addIdProperty().primaryKey().autoincrement();
     * note.addStringProperty("time");
     * note.addStringProperty("text").notNull();
     * note.addDateProperty("date");
     * 与在 Java 中使用驼峰命名法不同，默认数据库中的命名是使用大写和下划线来分割单词的。
     * For example, a property called “creationDate” will become a database column “CREATION_DATE”.
     */
    private static void addIdPrimaryKey(boolean isAutoincrement) {
        if (isAutoincrement)
            note.addIdProperty().primaryKey().autoincrement();
        else
            note.addIdProperty().primaryKey();
    }

    private static void addString(String string) {
        note.addStringProperty(string);
    }

    private static void addInt(String string) {
//        note.add(string);
    }
}
