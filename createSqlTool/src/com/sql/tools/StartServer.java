package com.sql.tools;

import java.io.File;
import java.util.Scanner;
/**
 * 
 * @author guanyifeng
 * @description exportFromExcel2.0
 *   执行此类将完成以下功能：有
 *      1、对数据库数据做备份BakSQL-startBak；
 *      2、执行ExportUntils-zipAnddelFiles；
 *      3、处理完成之后选择是否自动导入数据AntExecSql-execSqlFile
 *      
 *   操作说明：
 *   	1、请检查数据库连接相关参数设置及Excel文件路径（Constants.java）；
 *   	2、请检查要操作的表，修改tabNames
 *   
 *   备注：
 *   	1、扫描未入库错误码，执行CheckMsg.java；
 *   	2、数据字典表导出数据，执行GetDict.java
 *   	3、账务、会计错误码表以及数据字典表需同步到UI库。
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
		System.out.println("汇总表："+tabNames);
		String tabArray[] = tabNames.split("\\|") ;
		
		//ExportUntils.zipAnddelFiles(Constants.BAKFILEPATH) ;//处理之前备份之前产生的数据文件
		
		for (int i = 0; i < tabArray.length; i++) {
			String tabName = tabArray[i] ;
			
			//getBefore(tabName) ;//逐一备份数据库原数据
			
			String methodName = "get" +tabName.replaceFirst(tabName.substring(0, 1), tabName.substring(0, 1).toUpperCase()) ;
			
			doMethod(methodName,fileName) ;//动态调用业务处理方法
		}	
		//getEnd(tabArray) ;//所有处理完成之后，选择是否自动导入初始化数据
	}
	
	//备份原数据
	public static void getBefore(String tableName) throws Exception {
		BakSQL.startBak(tableName) ;
	}
	
	//动态调用方法
	public static void doMethod(String methodName,String fileName) throws Exception {
		System.out.println("动态调用方法,方法名:"+methodName);
		getAccEntry.getClass().getMethod(methodName, 
				new Class[]{String.class}).invoke(getAccEntry, new Object[]{fileName}) ;
	}
	
	//导出数据之后，是否自动导入
	public static void getEnd(String[] tabArray) throws Exception {
		Scanner in = new Scanner(System.in,"UTF-8"); 
		System.out.println("请选择是否自动导入初始化数据? y/n"); 
		String name = in.nextLine();//读取输入的下一行内容 
		if("y".equals(name)){
			System.out.println("自动导入初始化数据开始...");
			for (int i = 0; i < tabArray.length; i++) {
				String tabName = tabArray[i] ;				
				AntExecSql.execSqlFile(tabName, Constants.BASEDIR+File.separator+tabName.toUpperCase()+".sql") ;	
				System.out.println(tabName+"初始化数据导入成功！");
			}	
			System.out.println("全部完成！");
		}else if("n".equals(name)){
			System.out.println("请依据实际需求，手动导入初始化数据！");
		}else{
			System.out.println("输入错误！请手动导入初始化数据！");
		}
	}

}
