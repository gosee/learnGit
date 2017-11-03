package com.sql.tools;

import java.io.File;

import org.apache.tools.ant.*;

import org.apache.tools.ant.taskdefs.*;

//import org.apache.tools.ant.types.*;
/**
 * 
 * @author guanyifeng
 * @description 自动导入初始化数据
 */

public class AntExecSql {

	public static void main(String[] args) {
		String tableName="acmtinif";
		String sqlFile="D:\\createSql\\ACMTINIF.sql";
		execSqlFile(tableName,sqlFile);
	}

	public static void execSqlFile(String tableName, String sqlFile) {
		System.out.println("文件全路径："+sqlFile);
		String siturl = Constants.SITURL;
		String driver = Constants.DRIVER;
		String user = "";
		String password = "";
		if (tableName.substring(0, 3).equals("act")) {
			user = Constants.ACTUSER;
			password = Constants.ACTPASS;
		} else {
			user = Constants.ACMUSER;
			password = Constants.ACMPASS;
		}

		SQLExec sqlExec = new SQLExec();

		// 设置数据库参数
		sqlExec.setDriver(driver);
		sqlExec.setUrl(siturl);
		sqlExec.setUserid(user);
		sqlExec.setPassword(password);
		sqlExec.setSrc(new File(sqlFile));
		// sqlExec.setOnerror((SQLExec.OnError)(EnumeratedAttribute.getInstance(SQLExec.OnError.class, "abort")));

		sqlExec.setPrint(true); // 设置是否输出

		// 输出到文件 sql.out 中；不设置该属性，默认输出到控制台
		// sqlExec.setOutput(new File("d:/script/sql.out"));

		sqlExec.setProject(new Project()); // 要指定这个属性，不然会报错

		sqlExec.execute();

	}

}