package com.sql.tools;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang.StringUtils;

import com.hisun.atc.common.HiAmt;
import com.hisun.exception.HiException;
import com.hiunicom.acm.tools.BalTagUtils;

public class CreateAccountBalanceBatch {
	static String tm_smp = Constants.TM_SMP;
	static String dateStr = Constants.DATESTR;
	static String baseDir = Constants.BASEDIR;
	static String accOrg = Constants.ACCORG;
	static String operId = Constants.OPERID;

	public static void main(String[] args) throws IOException, HiException {
		
		String fileName="F:/9D/111acin/acinfo.txt";
		String str ="";
		BufferedReader reader = new BufferedReader(new FileReader(fileName));
		while(true)
		{
			str=reader.readLine();
			if(str.equals("")||str==null) break;
			int index = str.indexOf("|");
			String ac_no=str.substring(0, index);
			String usr_no = str.substring(index+1);
			System.out.println("AC_NO:"+ac_no);
			System.out.println("USR_NO:"+usr_no);
			String tag=BalTagUtils.createBalanceTag(ac_no, "1",new HiAmt("0.00"));
			String acblSql="Insert into ACMADM.ACMTACBL"
					+ " (AC_NO,CAP_TYP,CCY,AC_TYP,USR_NO,USR_PROV,USR_CITY,AC_PROV,AC_CITY,AC_ORG,"
					+ "CAP_TYP_STS,CRE_DT,LAST_AC_BAL,CUR_AC_BAL,UAVA_BAL,OD_LMT,TOT_OD_AMT,MAX_BAL_LMT,MIN_BAL_LMT,MIN_TRG_VAL,"
					+ "BAL_TAG,UPD_DT,UPD_JRN,TM_SMP,LAST_UAVA_BAL,NOT_TX_AVA_BAL,ORD_SEQ,CAL_INT_BAL,CAL_INT_DT,NOD_ID,REQ_ID) values ("
					+ "'"+ac_no+"','1','CNY','111',"+usr_no+",' ',' ',' ',' ','100001',"
					+ "'0','"+dateStr+"',0,0,0,0,0,0,0,0,"
					+ "'"+tag+"','"+dateStr+"',0,'"+tm_smp+"',0,0,0,0,' ',' ',' ');";
			System.out.println(acblSql);
			ExportUntils.exportToFile(acblSql+ "\r\n", "00_acmadm_acbl");
		}
		reader.close();

	}
	

}
