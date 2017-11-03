package com.sql.tools;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import org.apache.commons.lang.StringUtils;

/***
 * 
 * @author  
 *
 *
 */
public class GetAccEntry{
	static String tm_smp = Constants.TM_SMP;
	static String dateStr = Constants.DATESTR;
	static String baseDir = Constants.BASEDIR;
	static String accOrg = Constants.ACCORG;
	static String operId = Constants.OPERID;
	static String dealDate = "*";
	public static void main(String[] args) throws Exception{
		String fileName = Constants.FILENAME;
		getAcmtaccf(fileName);
	 	
		getActtaccf(fileName);

		/**getActtinbl(fileName);
		
	 	getActtinif(fileName);

	 	getActtitem(fileName);
	 	/*
	    getActtitmr(fileName);
	    */
	  
	 	
	 
	 	/*
		 * getAcmtinadif(fileName);
		 */
	}
	public static void getActtinbl(String fileName) throws Exception{
		ExportUntils.clearContents("05_actadm_inbl");
		String sqlData="insert into actadm.ACTTINBL(AC_ORG,AC_NO, AC_PROV, AC_CITY,BAL_TAG, CAP_TYP, IAC_BAL_DRT,CCY,CRE_DT,IN_AC_TYP,UPD_DT,UPD_JRN,AC_DT)"
                +"\r\n"                 
				+" select AC_ORG, AC_NO, AC_PROV, AC_CITY,' ','1', IAC_BAL_DRT, 'CNY', '"+dateStr+"',  IN_AC_TYP,  '"+dateStr+"', '0', '"+dateStr+"'"
                  +"\r\n"                   
				+" from actadm.ACTTINIF where AC_ORG='100001' and AC_NO not in (select ac_no from actadm.ACTTINBL where AC_ORG='100001');";
		ExportUntils.exportToFile(sqlData + "\r\n", "05_actadm_inbl");
	}
	public static void getAcmtaccf(String fileName) throws Exception{
		List<ArrayList<String>> dataLst = new ReadExcelPOI(3).read(fileName);
		System.out.println("##create sql of ACMTACCF##");
		int expNum =dataLst.size();
		Boolean first = true;
		for (int index = 1; index < expNum; index++) {
			ArrayList<String> innerLst = dataLst.get(index);
			StringBuffer rowData = new StringBuffer();
			boolean zeroDataRow = false;
			String deleteStr = ",' ','" + dealDate + "'";
			deleteStr = deleteStr.replace(",'*'", "");
			for (int i = 0; i < innerLst.size()&&i<16; i++) {
				String dataStr = innerLst.get(i);
				if(i==0&&StringUtils.equals(dataStr, ""))
				{
						zeroDataRow=true;
						break;
				}
				if (i == 1 || i == 3 || i == 5 || i == 8 || i == 9|| i == 13 ) {
					continue;
				}

				if (i == 7 && StringUtils.equals(dataStr, "借")) {
					dataStr = "D";
				} else if (i == 7 && StringUtils.equals(dataStr, "贷"))
					dataStr = "C";
				if (i == 12) {
					if (StringUtils.isBlank(dataStr.trim())) {
						dataStr += " ";
					}
				}
				if (StringUtils.equals(dataStr, ".000000")) {
					dataStr = "0";
				}
				if(StringUtils.equals(dataStr, "")|| i == 14) {
					dataStr=" ";
				}
				if(i == 15) {
					
					if("*".equals(dealDate)) {
						continue;
					} else if(!dealDate.equals(dataStr)) {
						zeroDataRow = true;
						System.out.println("dealDate:" + dealDate);
						System.out.println("dataStr:" + dataStr);
						System.out.println("?????" );
						break;
					}
				}
				rowData.append(",'").append(dataStr).append("'");
			}
			
			if ((!zeroDataRow)&&rowData.length() > 0) {
				System.out.println(rowData.toString());
				System.out.println(deleteStr.toString());
				
				int begIndex = rowData.indexOf(deleteStr);
				if(begIndex == -1) {
					deleteStr = ",' '";
					begIndex = rowData.length() - 5;
				}
				rowData = rowData.delete(begIndex, begIndex+deleteStr.length());
				System.out.println("==rowData:" + rowData.toString());
				StringBuffer sqlData = new StringBuffer();
				sqlData.append("insert into acmadm.ACMTACCF  (")
						.append("AC_ORG, PRD_NO, TX_TYP, BUS_TYP, AE_NO,DC_FLG, AC_TYP, AC_FLG,DEF_SEP_CD, AC_TX_STS, AC_OPR, CHK_OPR, AC_TM, CHK_TM, AC_DT, CHK_AC_DT, TM_SMP,  CHK_IDEA, NOD_ID, REQ_ID)")
						.append("values (").append("'" + accOrg + "',").append(rowData.deleteCharAt(0).toString())
						.append(",'0','" + operId + "',' ',' ',' ',' ',' '," + tm_smp + ",' ',' ',' '").append(");");
				System.out.println(sqlData.toString());
				ExportUntils.exportToFile(sqlData.toString() + "\r\n", "acmadm.ACMTACCF","00_acmadm_accf", first, expNum + 1);
				first = false;
			}
		}
		System.out.println("##create sql of ACMTACCF success##");
	}
	public static void getActtaccf(String fileName) throws Exception{
		List<ArrayList<String>> dataLst = new ReadExcelPOI(3).read(fileName);
		System.out.println("##create sql of ACTTACCF##");
		int expNum =dataLst.size();
		Boolean first = true;
		for (int index = 1; index < expNum; index++) {
			ArrayList<String> innerLst = dataLst.get(index);
			StringBuffer rowData = new StringBuffer();
			boolean zeroDataRow = false;
			for (int i = 0; i < innerLst.size()&&i<14; i++) {
				String dataStr = innerLst.get(i);
				if(i==0&&StringUtils.equals(dataStr, ""))
				{
						zeroDataRow=true;
						break;
				}
				if (i == 1 || i == 3 || i == 5 || i == 8 || i == 9|| i == 13) {
					continue;
				}
				if (i == 7 && StringUtils.equals(dataStr, "借")) {
					dataStr = "D";
				} else if (i == 7 && StringUtils.equals(dataStr, "贷"))
					dataStr = "C";
				if (i == 12) {
					if (StringUtils.isBlank(dataStr.trim())) {
						dataStr += " ";
					}
				}
				if (StringUtils.equals(dataStr, ".000000")) {
					dataStr = "0";
				}
				if(StringUtils.equals(dataStr, "")|| i == 14)
					dataStr=" ";
				rowData.append(",'").append(dataStr).append("'");
			}
		
			if ((!zeroDataRow)&&rowData.length() > 0) {
				StringBuffer sqlData = new StringBuffer();
				sqlData.append("insert into actadm.ACTTACCF  (")
						.append("AC_ORG, PRD_NO, TX_TYP, BUS_TYP, AE_NO,DC_FLG, AC_TYP, AC_FLG,DEF_SEP_CD, AC_TX_STS, AC_OPR, CHK_OPR, AC_TM, CHK_TM, AC_DT, CHK_AC_DT, TM_SMP,  CHK_IDEA, NOD_ID, REQ_ID)")
						.append("values (").append("'" + accOrg + "',").append(rowData.deleteCharAt(0).toString())
						.append(",'0','" + operId + "',' ',' ',' ',' ',' '," + tm_smp + ",' ',' ',' '").append(");");
				//System.out.println(sqlData.toString());
				ExportUntils.exportToFile(sqlData.toString() + "\r\n", "actadm.ACTTACCF","01_actadm_accf", first, expNum + 1);
				first = false;
			}
		}
		System.out.println("##create sql of ACTTACCF success##");
	}
	
	public static void getActtitem(String fileName) throws IOException{
		System.out.println("##create sql of ACTTITEM##");
		List<ArrayList<String>> dataLst = new ReadExcelPOI(0).read(fileName);
		Boolean first = true;
		for (int index = 2; index < dataLst.size(); index++) {
			ArrayList<String> innerLst = dataLst.get(index);
			StringBuffer rowData = new StringBuffer();
			boolean zeroLvlItem = false;
			boolean zeroDataRow = false;
			for (int i = 0; i < innerLst.size(); i++) {
				String dataStr = innerLst.get(i);
				//System.out.println(i+dataStr);
				if(i==0&&StringUtils.equals(dataStr, ""))
				{
					//System.out.println("i==0&&StringUtils.equals(dataStr");
					zeroDataRow=true;
					break;
				}
				if(i==2&&(StringUtils.equals(dataStr, "0")||StringUtils.equals(dataStr, ".000000")))
				{
				//	System.out.println("i==2&&StringUtils.equals(dataStr");
					zeroLvlItem = true;
				}
				if(StringUtils.equals(dataStr, ""))
					//dataStr=" ";
					continue;
				if (StringUtils.equals(dataStr, ".000000")) {
					dataStr = "0";
				}
				rowData.append(",'").append(dataStr).append("'");
			}
			if (zeroLvlItem||zeroDataRow)
				continue;
			//System.out.println(rowData);
			if (rowData.length() > 0) {
				StringBuffer sqlData = new StringBuffer();
				
				sqlData.append("insert into actadm.ACTTITEM  (")
						.append("AC_ORG, ITM_NO, ITM_CNM, ITM_LVL, UP_ITM_NO, BTM_ITM_FLG, ITM_TYP, ITM_CLS, ITM_ZBAL_FLG, LP_BF_FLG, BAL_DRT, BAL_OD_FLG,BAL_CHK_FLG,ITM_ENM, ITM_STS, EFF_DT, EXP_DT, UPD_DT, UPD_OPR, TM_SMP, NOD_ID, REQ_ID)")
						.append("values (").append("'" + accOrg + "',").append(rowData.deleteCharAt(0).toString())
						.append(",' ','A','" + dateStr + "','29990101','" + dateStr + "','" + operId + "','" + tm_smp + "',' ',' '").append(");");
				System.out.println(sqlData.toString());
				ExportUntils.exportToFile(sqlData.toString() + "\r\n", "actadm.ACTTITEM","03_actadm_item", first, dataLst.size() - 1);
				first = false;
			}
		}
		System.out.println("##create sql of ACTTITEM success##");
	}
	public static void getActtitmr(String fileName) throws IOException{
		List<ArrayList<String>> dataLst = new ReadExcelPOI(1).read(fileName);
		System.out.println("##create sql of ACTTITMR##");
		Boolean first = true;
		for (int index = 2; index < dataLst.size(); index++) {
			ArrayList<String> innerLst = dataLst.get(index);
			StringBuffer rowData = new StringBuffer();
			boolean zeroLvlItem = false;
			boolean zeroDataRow = false;
			for (int i = 0; i < innerLst.size(); i++) {
				String dataStr = innerLst.get(i);
				if(i==0&&StringUtils.equals(dataStr, ""))
				{
						zeroDataRow=true;
						break;
				}
				if (i == 1 || i == 5) {
					continue;
				}
				if (StringUtils.equals(dataStr, ".000000")) {
					dataStr = "0";
				}
				if (StringUtils.equals(dataStr, "")) {
					dataStr = " ";
				}
				rowData.append(",'").append(dataStr).append("'");
			}
			if (zeroLvlItem||zeroDataRow)
				continue;
			if (rowData.length() > 0) {
				StringBuffer sqlData = new StringBuffer();
				sqlData.append("insert into actadm.ACTTITMR  (")
						.append("AC_ORG, AC_TYP, CAP_TYP, CCY, ITM_NO, ITM_CCY, IN_AC_FLG, IAC_BAL_DRT, BAL_RED_DRT, UPD_BAL_FLG, MNU_AC_FLG, RVS_CLS, RVS_TYP, UPD_JRN, TM_SMP, UPD_DT, UPD_OPR, ID_NO, NOD_ID, REQ_ID)")
						.append("values (").append("'" + accOrg + "',").append(rowData.deleteCharAt(0).toString())
						.append(",'0','" + tm_smp + "','" + dateStr + "','" + operId + "',' ',' ',' '").append(");");
				//System.out.println(sqlData.toString());
				ExportUntils.exportToFile(sqlData.toString() + "\r\n", "actadm.ACTTITMR","04_actadm_itmr", first, dataLst.size());
				first = false;
			}
		}
		System.out.println("##create sql of ACTTITMR success##");
	}
	
	public static void getActtinif(String fileName) throws IOException{
		List<ArrayList<String>> dataLst = new ReadExcelPOI(2).read(fileName);
		System.out.println("##create sql of ACTTINIF##");
		Boolean first = true;
		for (int index = 2; index < dataLst.size(); index++) {
			ArrayList<String> innerLst = dataLst.get(index);
			StringBuffer rowData = new StringBuffer();
			boolean zeroLvlItem = false;
			boolean zeroDataRow = false;
			for (int i = 0; i < innerLst.size(); i++) {
				String dataStr = innerLst.get(i);				
				if(i==0&&StringUtils.equals(dataStr, ""))
				{
						zeroDataRow=true;
						break;
				}
				if (i == 1) {
					dataStr = dataStr.trim();
				} else if (i == 8||i==10||i==11||i==12 ||i==13) {
					if (StringUtils.isBlank(dataStr.trim())) {
						dataStr += " ";
					}
				} else if (i == 9||i == 14) {
					continue;
				}
				if (StringUtils.equals(dataStr, ".000000")) {
					dataStr = "0";
				}
				if (StringUtils.equals(dataStr, "")) {
					dataStr = " ";
				}
				rowData.append(",'").append(dataStr).append("'");
			}
			if (zeroLvlItem||zeroDataRow)
				continue;
			if (rowData.length() > 0) {
				StringBuffer sqlData = new StringBuffer();
				sqlData.append("insert into actadm.ACTTINIF  (")
						.append("AC_ORG,"
								+ " AC_NO, AC_NM, IN_AC_TYP, IAC_BAL_DRT, BAL_RED_DRT, UPD_BAL_FLG, MNU_AC_FLG, RVS_CLS, "
								+ "SEP_CD, "
								+ "RVS_TYP,IN_AC_STS, SUB_TIM,AC_STSW,"
								+ " AC_PROV, AC_CITY, OPN_MOD, OPN_DT, OPN_TM, "
								+ "OPN_SYS_CNL, "
								+ "OPN_OPR, CLO_DT, UPD_DT, UPD_OPR, UPD_JRN, TM_SMP, NOD_ID, REQ_ID)")
						.append("values (")
						.append("'" + accOrg + "',")
						.append(rowData.deleteCharAt(0).toString())
						.append(",' ',' ','1','" + dateStr + "','000000','SYS','" +
						operId + "',' ','" + dateStr + "','" + operId + "','" + tm_smp
								+ "','" + tm_smp + "',' ',' '").append(");");
				// System.out.println(sqlData.toString());
				ExportUntils.exportToFile(sqlData.toString() + "\r\n", "actadm.ACTTINIF","02_actadm_inif", first, dataLst.size());
				first = false;
			}
		}
		System.out.println("##create sql of ACTTINIF success##");
	}
	public static void getAcmtinif(String fileName) throws IOException{
		List<ArrayList<String>> dataLst = new ReadExcelPOI(1).read(fileName);
		// System.out.println("truncate table ACMTINIF;");

		Boolean first = true;
		for (int index = 2; index < dataLst.size(); index++) {
			ArrayList<String> innerLst = dataLst.get(index);
			StringBuffer rowData = new StringBuffer();
			boolean zeroLvlItem = false;
			for (int i = 0; i < innerLst.size(); i++) {
				String dataStr = innerLst.get(i);
				if (i == 0) {
					dataStr = dataStr.trim();
					if (!StringUtils.equals(dataStr.substring(0, 1), "1")) {
						zeroLvlItem = true;
						// continue;
					}
				} else if (i == 1) {
					dataStr = dataStr.trim();
				} else if (i == 8 || i == 9) {
					if (StringUtils.isBlank(dataStr.trim())) {
						dataStr += " ";
					}
				} else if (i == 10) {
					continue;
				}
				if (StringUtils.equals(dataStr, ".000000")) {
					dataStr = "0";
				}
				rowData.append(",'").append(dataStr).append("'");
			}
			/*
			 * if (index == dataLst.size() - 1) {// ????????????????????????
			 * StringBuffer sqlData = new StringBuffer();
			 * sqlData.append("");
			 * ExportUntils.exportToFile(sqlData.toString() + "\r\n",
			 * "ACMTINIF", index, dataLst.size());
			 * }
			 */
			if (zeroLvlItem) {
				StringBuffer sqlData = new StringBuffer();
				sqlData.append("");
				//ExportUntils.exportToFile(sqlData.toString() + "\r\n", "ACMTINIF", index, index + 1);
				break;
				// continue;
			}
			// System.out.println(rowData.deleteCharAt(0).toString());
			if (rowData.length() > 0) {
				StringBuffer sqlData = new StringBuffer();
				sqlData.append("INSERT INTO ACMTINIF  (")
						.append("AC_ORG, AC_NO, AC_NM, IN_AC_TYP, IAC_BAL_DRT, BAL_RED_DRT, UPD_BAL_FLG, MNU_AC_FLG, RVS_CLS, SEP_CD, CMM_ORG_NO, "
								+ "RVS_TYP,IN_AC_STS, AC_STSW, AC_PROV, AC_CITY, OPN_MOD, OPN_DT, OPN_TM, OPN_SYS_CNL, OPN_OPR, CLO_DT, UPD_DT, UPD_OPR, UPD_JRN, TM_SMP)")
						.append("values (")
						.append("'" + accOrg + "',")
						.append(rowData.deleteCharAt(0).toString())
						.append(",'0','0','0',' ',' ','1','" + dateStr + "','000000','SYS','" + operId + "',' ','" + dateStr + "','" + operId + "','" + tm_smp
								+ "','" + tm_smp + "'").append(");");
				System.out.println(sqlData.toString());
				//ExportUntils.exportToFile(sqlData.toString() + "\r\n", "ACMTINIF", index, dataLst.size());
			}
		}
	}
	public static void getAcmtinadif(String fileName) throws IOException{
		List<ArrayList<String>> dataLst = new ReadExcelPOI(4).read(fileName);
		// System.out.println("truncate table ACMTINADIF;");
		for (int index = 2; index < dataLst.size(); index++) {
			ArrayList<String> innerLst = dataLst.get(index);
			StringBuffer rowData = new StringBuffer();
			boolean zeroLvlItem = false;
			for (int i = 0; i < innerLst.size(); i++) {
				String dataStr = innerLst.get(i);
				if (i == 0 || i == 1) {
					continue;
				}
				if (i == 3 || i == 4 || i == 10 || i == 11) {
					if (StringUtils.isBlank(dataStr.trim())) {
						dataStr += " ";
					}
				}
				if (StringUtils.equals(dataStr, ".000000")) {
					dataStr = "0";
				}
				rowData.append(",'").append(dataStr).append("'");
			}
			if (zeroLvlItem)
				continue;
			// System.out.println(rowData.deleteCharAt(0).toString());
			if (rowData.length() > 0) {
				StringBuffer sqlData = new StringBuffer();
				sqlData.append("INSERT INTO ACMTINADIF  (")
						.append("AC_ORG, AC_NO, LK_NO,BKAC_NO, BKAC_NM, BK_NM, BKAC_PROP, BK_BCH, PRE_LPNM, PRE_USE, PRE_TYP, TM_SMP)").append("values (")
						.append("'" + accOrg + "',").append(rowData.deleteCharAt(0).toString()).append(",'" + tm_smp + "'").append(");");
				// System.out.println(sqlData.toString());
				//ExportUntils.exportToFile(sqlData.toString() + "\r\n", "ACMTINADIF", index, dataLst.size());
			}
		}
	}
}
