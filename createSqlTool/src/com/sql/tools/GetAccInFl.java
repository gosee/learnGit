package com.sql.tools;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringUtils;
/**
 * 
 * @author guanyifeng
 * @description 供张伟使用
 */
public class GetAccInFl {

	static String tm_smp = Constants.TM_SMP;
	static String dateStr = Constants.DATESTR;

	static String baseDir = Constants.BASEDIR;
	static String accOrg = Constants.ACCORG;
	static String operId = Constants.OPERID;

	public static void main(String[] args) throws Exception {
		String fileName = "D:/ACTLINFL.xls";
		getActlinfl(fileName);

	}

	public static void getActlinfl(String fileName) throws IOException {

		List<ArrayList<String>> dataLst = new ReadExcelPOI(0).read(fileName);

		// System.out.println("truncate table ACTLINFL;");
		for (int index = 2; index < dataLst.size(); index++) {
			ArrayList<String> innerLst = dataLst.get(index);
			StringBuffer rowData = new StringBuffer();
			boolean zeroLvlItem = false;
			for (int i = 0; i < innerLst.size(); i++) {
				String dataStr = innerLst.get(i);

				if (i == 1) {
					dataStr = dataStr.trim();
				} else if (i == 8 || i == 9 || i == 10) {
					if (StringUtils.isBlank(dataStr.trim())) {
						dataStr += " ";
					}
				} else if (i == 11) {
					continue;
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
				sqlData.append("INSERT INTO ACTLINFL  (")
						.append("AC_ORG, AC_NO, AC_NM, IN_AC_TYP, IAC_BAL_DRT, BAL_RED_DRT, UPD_BAL_FLG, MNU_AC_FLG, RVS_CLS, SEP_CD, CMM_ORG_NO, LIN_FLG,"
								+ "RVS_TYP,IN_AC_STS, AC_STSW, AC_PROV, AC_CITY, OPN_MOD, OPN_DT, OPN_TM, OPN_SYS_CNL, OPN_OPR, CLO_DT, UPD_DT, UPD_OPR, UPD_JRN, TM_SMP)")
						.append("values (")
						.append("'" + accOrg + "',")
						.append(rowData.deleteCharAt(0).toString())
						.append(",'0','0','0',' ',' ','1','" + dateStr
								+ "','000000','SYS','" + operId + "',' ','"
								+ dateStr + "','" + operId + "','" + tm_smp
								+ "','" + tm_smp + "'").append(");");
				// System.out.println(sqlData.toString());
				//ExportUntils.exportToFile(sqlData.toString() + "\r\n","ACTLINFL", index, dataLst.size());
			}
		}

	}
}
