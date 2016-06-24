package com.feng;

import java.io.IOException;

import de.greenrobot.daogenerator.DaoGenerator;
import de.greenrobot.daogenerator.Entity;
import de.greenrobot.daogenerator.Property;
import de.greenrobot.daogenerator.Schema;

/**
 * 生成实体和对应DAO类
 * 
 */
public class MyDaoGenerator {

	public static void main(String[] args) throws IOException, Exception {
		// Schema schema = new Schema(1, "com.yuexunit.zjll.db");
		// addBean(schema);
		Schema schema = new Schema(1, "com.wuwind.db");
		tallyUrl(schema);
		// 生成的entity和dao文件夹路径
		new DaoGenerator().generateAll(schema, "\\ViewApplication\\greendaogenerator\\src-gen");
	}
	
	private static void tallyUrl(Schema schema) {
		Entity bean = schema.addEntity("RequestEntity");
		bean.addStringProperty("url").primaryKey();
		bean.addStringProperty("content");//内容
		Entity bean2 = schema.addEntity("MediaEntity");
		bean2.addStringProperty("fileUuid").primaryKey();
		bean2.addStringProperty("fileName");//内容
		bean2.addStringProperty("fileSize");//内容
		bean2.addStringProperty("filePath");//内容
		bean2.addIntProperty("type");//内容
	}

	private static void addBeans(Schema schema) {
		// 位置
		Entity bean = schema.addEntity("PlaceEntity");
		bean.addIdProperty().primaryKey().autoincrement();
		bean.addStringProperty("uid");
		bean.addStringProperty("key");// 地点名称
		bean.addStringProperty("addr");// 详细地址
		bean.addIntProperty("type"); // 0.终点；1起点；2未知
		bean.addDoubleProperty("latitude");// 经度
		bean.addDoubleProperty("longtitude");// 纬度
		// 联系人
		Entity contactEntity = schema.addEntity("ContactEntity");
		contactEntity.addIdProperty().primaryKey().autoincrement();
		contactEntity.addStringProperty("name");
		contactEntity.addStringProperty("phoneNumber");
	}

	private static void addBean(Schema schema) {
		// 消息
		Entity messageBean = schema.addEntity("MessageBean");
		messageBean.addIdProperty().primaryKey().autoincrement();
		messageBean.addStringProperty("time");
		messageBean.addStringProperty("title");
		messageBean.addStringProperty("content");
		// 多媒体文件
		Entity mediaBean = schema.addEntity("MediaBean");
		mediaBean.addIdProperty().primaryKey().autoincrement();
		mediaBean.addIntProperty("type");
		mediaBean.addStringProperty("filePath");
		// 箱子
		Entity boxBean = schema.addEntity("BoxBean");
		boxBean.addIdProperty().primaryKey().autoincrement();
		boxBean.addStringProperty("boxNumber");
		boxBean.addDoubleProperty("loadNumber");
		boxBean.addStringProperty("boxMaster");
		boxBean.addStringProperty("boxSize");
		// 我的任务 小任务
		Entity myTaskBean = schema.addEntity("MyTaskBean");
		myTaskBean.addIdProperty().primaryKey().autoincrement();
		myTaskBean.addBooleanProperty("exception");
		myTaskBean.addIntProperty("methodNo");
		myTaskBean.addStringProperty("time");
		myTaskBean.addStringProperty("temperature");
		myTaskBean.addStringProperty("operator");
		myTaskBean.addStringProperty("exceptionNo");
		Property myTaskId = mediaBean.addLongProperty("myTaskId").getProperty();
		myTaskBean.addToMany(mediaBean, myTaskId);
		// 大任务
		Entity taskBean = schema.addEntity("TaskBean");
		taskBean.addIdProperty().primaryKey().autoincrement();
		taskBean.addBooleanProperty("exception");
		taskBean.addStringProperty("sealNo");
		taskBean.addStringProperty("taskNo");
		taskBean.addStringProperty("billNo");
		taskBean.addStringProperty("task");
		taskBean.addStringProperty("preTemperatureNo");
		taskBean.addStringProperty("preTemperature");
		taskBean.addStringProperty("latelyTemperature");
		taskBean.addStringProperty("requireTemperature");
		taskBean.addStringProperty("intervalTime");
		taskBean.addStringProperty("requireVentilation");
		taskBean.addIntProperty("statue");
		Property boxId = taskBean.addLongProperty("boxId").getProperty();
		Property taskId = myTaskBean.addLongProperty("taskId").getProperty();
		taskBean.addToOne(boxBean, boxId);
		taskBean.addToMany(myTaskBean, taskId);
		// 任务PTI
		Entity myTaskPTI = schema.addEntity("MyTaskPTI");
		myTaskPTI.addIdProperty().primaryKey().autoincrement();
		myTaskPTI.addStringProperty("checkDate");
		myTaskPTI.addStringProperty("beginDate");
		myTaskPTI.addStringProperty("endDate");
		myTaskPTI.addStringProperty("billNo");
		myTaskPTI.addStringProperty("sealNo");
		myTaskPTI.addStringProperty("shipName");
		myTaskPTI.addStringProperty("shipNo");
		myTaskPTI.addStringProperty("boxControlCompany");
		myTaskPTI.addStringProperty("boxColdCompany");
		myTaskPTI.addStringProperty("delegatePerson");
		myTaskPTI.addStringProperty("checkedPerson");
		myTaskPTI.addStringProperty("planPerson");
		myTaskPTI.addIntProperty("statue");
		Property boxIdPTI = myTaskPTI.addLongProperty("boxId").getProperty();
		myTaskPTI.addToOne(boxBean, boxIdPTI);

	}
}
