package com.sql.tools;

import java.io.File;
import java.util.Scanner;
/**
 * 
 * @author guanyifeng
 * @description exportFromExcel2.0
 *   ִ�д��ཫ������¹��ܣ���
 *      1�������ݿ�����������BakSQL-startBak��
 *      2��ִ��ExportUntils-zipAnddelFiles��
 *      3���������֮��ѡ���Ƿ��Զ���������AntExecSql-execSqlFile
 *      
 *   ����˵����
 *   	1���������ݿ�������ز������ü�Excel�ļ�·����Constants.java����
 *   	2������Ҫ�����ı��޸�tabNames
 *   
 *   ��ע��
 *   	1��ɨ��δ�������룬ִ��CheckMsg.java��
 *   	2�������ֵ�������ݣ�ִ��GetDict.java
 *   	3�����񡢻�ƴ�������Լ������ֵ����ͬ����UI�⡣
 *   
 *   
 */


public class StartServer {

	static String dateStr = Constants.DATESTR;
	
	static GetAccEntry getAccEntry = new GetAccEntry() ;

	public static void main(String[] args) throws Exception {
		
		String fileName = Constants.FILENAME;
		String tabNames = ""
				+Constants.TAB_ACTTITEM
				+"|"
					+Constants.TAB_ACTTITMR
					+"|"
					+Constants.TAB_ACTTACCF
					+"|"
					+Constants.TAB_ACTTINIF
					/*+"|"
					+Constants.TAB_ACMTINADIF
				+"|"
					+Constants.TAB_ACMTINIF*/;
		
		init(fileName, tabNames);		
		/*
		getBefore(Constants.TAB_ACTTITEM);	GetAccEntry.getActtitem(fileName);		
		getBefore(Constants.TAB_ACTTITMR);	GetAccEntry.getActtitmr(fileName);		
		getBefore(Constants.TAB_ACTTACCF);	GetAccEntry.getActtaccf(fileName);		
		getBefore(Constants.TAB_ACMTINADIF);GetAccEntry.getAcmtinadif(fileName);	
		getBefore(Constants.TAB_ACTTINIF);	GetAccEntry.getActtinif(fileName);	
		getBefore(Constants.TAB_ACMTINIF);	GetAccEntry.getAcmtinif(fileName);
		*/		
	}
	
	public static void init(String fileName,String tabNames) throws Exception {
		System.out.println("���ܱ�"+tabNames);
		String tabArray[] = tabNames.split("\\|") ;
		
		//ExportUntils.zipAnddelFiles(Constants.BAKFILEPATH) ;//����֮ǰ����֮ǰ�����������ļ�
		
		for (int i = 0; i < tabArray.length; i++) {
			String tabName = tabArray[i] ;
			
			//getBefore(tabName) ;//��һ�������ݿ�ԭ����
			
			String methodName = "get" +tabName.replaceFirst(tabName.substring(0, 1), tabName.substring(0, 1).toUpperCase()) ;
			
			doMethod(methodName,fileName) ;//��̬����ҵ������
		}	
		//getEnd(tabArray) ;//���д������֮��ѡ���Ƿ��Զ������ʼ������
	}
	
	//����ԭ����
	public static void getBefore(String tableName) throws Exception {
		BakSQL.startBak(tableName) ;
	}
	
	//��̬���÷���
	public static void doMethod(String methodName,String fileName) throws Exception {
		System.out.println("��̬���÷���,������:"+methodName);
		getAccEntry.getClass().getMethod(methodName, 
				new Class[]{String.class}).invoke(getAccEntry, new Object[]{fileName}) ;
	}
	
	//��������֮���Ƿ��Զ�����
	public static void getEnd(String[] tabArray) throws Exception {
		Scanner in = new Scanner(System.in,"UTF-8"); 
		System.out.println("��ѡ���Ƿ��Զ������ʼ������? y/n"); 
		String name = in.nextLine();//��ȡ�������һ������ 
		if("y".equals(name)){
			System.out.println("�Զ������ʼ�����ݿ�ʼ...");
			for (int i = 0; i < tabArray.length; i++) {
				String tabName = tabArray[i] ;				
				AntExecSql.execSqlFile(tabName, Constants.BASEDIR+File.separator+tabName.toUpperCase()+".sql") ;	
				System.out.println(tabName+"��ʼ�����ݵ���ɹ���");
			}	
			System.out.println("ȫ����ɣ�");
		}else if("n".equals(name)){
			System.out.println("������ʵ�������ֶ������ʼ�����ݣ�");
		}else{
			System.out.println("����������ֶ������ʼ�����ݣ�");
		}
	}

}
