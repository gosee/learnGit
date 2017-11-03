package com.sql.tools;

import java.io.File;

import org.apache.tools.ant.*;

import org.apache.tools.ant.taskdefs.*;

//import org.apache.tools.ant.types.*;
/**
 * 
 * @author guanyifeng
 * @description �Զ������ʼ������
 */

public class AntExecSql {

	public static void main(String[] args) {
		String tableName="acmtinif";
		String sqlFile="D:\\createSql\\ACMTINIF.sql";
		execSqlFile(tableName,sqlFile);
	}

	public static void execSqlFile(String tableName, String sqlFile) {
		System.out.println("�ļ�ȫ·����"+sqlFile);
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

		// �������ݿ����
		sqlExec.setDriver(driver);
		sqlExec.setUrl(siturl);
		sqlExec.setUserid(user);
		sqlExec.setPassword(password);
		sqlExec.setSrc(new File(sqlFile));
		// sqlExec.setOnerror((SQLExec.OnError)(EnumeratedAttribute.getInstance(SQLExec.OnError.class, "abort")));

		sqlExec.setPrint(true); // �����Ƿ����

		// ������ļ� sql.out �У������ø����ԣ�Ĭ�����������̨
		// sqlExec.setOutput(new File("d:/script/sql.out"));

		sqlExec.setProject(new Project()); // Ҫָ��������ԣ���Ȼ�ᱨ��

		sqlExec.execute();

	}

}