package com.sql.tools;

import java.text.SimpleDateFormat;
import java.util.Date;

public class Constants {
	
	/*	--????????? start-- */	
	public static final String SITURL = "jdbc:oracle:thin:@192.168.0.78:1521:t0pay";//??????????????????
	
	public static final String DRIVER = "oracle.jdbc.driver.OracleDriver" ;
	
	public static final String ACMUSER = "payadm" ;
	
	public static final String ACMPASS = "payadm" ;

	public static final String ACTUSER = "payadm" ;
	
	public static final String ACTPASS = "payadm" ;
	/*
	public static final String SITURL = "jdbc:oracle:thin:@192.168.9.72:1521:ZFACC";//UAT-ACM????
	public static final String SITURL = "jdbc:oracle:thin:@192.168.9.2:1521:ntdata92";//UAT-ACT????
	
	public static final String ACMUSER = "acmadm" ;
	
	public static final String ACMPASS = "acmadm_0903" ;

	public static final String ACTUSER = "actadm" ;
	
	public static final String ACTPASS = "actadm_0902" ;
	*/
	/*	--????????? end--	*/
	
	/*	--?????? start-- */
	//public static final String FILENAME = "C:/My Data/0A-?????????/?????/????????????????V0.0.4.xls";
	
	public static final String FILENAME_HLP = "E:/My Data/0A-?????????/?????/???????(????-???-???????)-20160702.xls";
	
	
	//public static final String FILENAME = "/Users/wulouhua/Downloads/九鼎支付-科目属性表(例行-合规-拆分统一版本) - 20170821.xls";
	public static final String FILENAME = "/Users/jc/Downloads/科目属性表.xls";

	//public static final String FILENAME = "C:/My Data/0A-?????????/?????/??????-???????????????-v0.0.4.xls";
	
	public static final String BAKFILEPATH = "/Users/jc/Documents/createSqlTool/release";

	public static final String BASEDIR = "/Users/jc/Documents/release";
    
	/*
	
	public static final String FILENAME = "C:/My documents/0A-P2P???/Itm/p2p??????????????????V0.0.7.xls";

	public static final String BAKFILEPATH = "C:\\My documents\\0A-P2P???\\???sql\\";
	
	public static final String BASEDIR = "C:\\My documents\\0A-P2P???\\???sql\\";
	*/
	/*	--?????? end-- */
	
	public static String TM_SMP = new SimpleDateFormat("yyyyMMdd").format(new Date())+"080000";
	//public static String TM_SMP = "20160322080000";
	public static String DATESTR = new SimpleDateFormat("yyyyMMdd").format(new Date());
	//public static String DATESTR = "20160322";
	public static final String ACCORG = "100001";
	
	public static final String OPERID = "T00001";

	public static final String TAB_ACTTITEM = "acttitem";//?????
	
	public static final String TAB_ACTTITMR = "acttitmr";//?????????
	
	public static final String TAB_ACTTACCF = "acttaccf";//??????????
	
	public static final String TAB_ACTTINIF = "acttinif";//?????????
	
	public static final String TAB_ACMTINADIF = "acmtinadif";//????????????
	
	public static final String TAB_ACMTINIF = "acmtinif";//????????????????


}
 