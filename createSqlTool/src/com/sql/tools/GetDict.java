package com.sql.tools;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang.StringUtils;

public class GetDict {
	static String tm_smp = Constants.TM_SMP;
	static String dateStr = Constants.DATESTR;

	static String baseDir = Constants.BASEDIR;
	static String accOrg = Constants.ACCORG;
	static String operId = Constants.OPERID;

	public static void main(String[] args) throws IOException {
		
		String fileName=Constants.FILENAME_HLP ;
		
		getTxtyp(fileName);
		getBustyp(fileName);
	//	getSyscnl(fileName);
	//	getBuscnl(fileName);
	//	getOrdtyp(fileName);

	}
	
	public static void getBustyp(String fileName)throws IOException {

		List<ArrayList<String>> dataLst = new ReadExcelPOI(2)
				.read(fileName);

		//ExportUntils.exportToFile("DELETE FROM actadm.PUBTHLP WHERE FLD_NM='BUS_TYP';"+"\r\n","PUBTHLP");
		//ExportUntils.exportToFile("DELETE FROM payadm.PUBTHLP WHERE FLD_NM='BUS_TYP';"+"\r\n","PUBTHLP");
		for (int index = 2; index < dataLst.size(); index++) {
			ArrayList<String> innerLst = dataLst.get(index);
			StringBuffer rowData = new StringBuffer();
			boolean zeroDataRow = false;
			for (int i = 0; i < innerLst.size(); i++) {
				String dataStr = innerLst.get(i);
				if(i==0&&StringUtils.equals(dataStr, ""))
				{
						zeroDataRow=true;
						break;
				}
				if(i==1)
				{
					dataStr=dataStr.trim();
				}
				else if (i >= 2 ) {
//					System.out.println(""+i+":"+dataStr);
					continue;
				}
				rowData.append(",'").append(dataStr).append("'");
			}
			if ((!zeroDataRow)&&rowData.length() > 0) {
				StringBuffer sqlData = new StringBuffer();
				sqlData.append("INSERT INTO PUBTHLP  (")
						.append("FLD_ORDER, FLD_NM, FLD_VAL, FLD_EXP, FLD_TYP, FLD_EXP_DESC, TM_SMP)")
						.append("values (")
						.append("'99','BUS_TYP',")
						.append(rowData.deleteCharAt(0).toString())
						.append(",'x','业务类型','"+ tm_smp + "'").append(");");
				System.out.println(sqlData.toString());
				ExportUntils.exportToFile(sqlData.toString()+"\r\n","actadm.PUBTHLP");
				ExportUntils.exportToFile(sqlData.toString()+"\r\n","payadm.PUBTHLP");
			}
		}

	}
	
	public static void getTxtyp(String fileName)throws IOException {

		List<ArrayList<String>> dataLst = new ReadExcelPOI(1)
				.read(fileName);

		ExportUntils.exportToFile("DELETE FROM PUBTHLP WHERE FLD_NM='TX_TYP';"+"\r\n","PUBTHLP");
		for (int index = 2; index < dataLst.size(); index++) {
			ArrayList<String> innerLst = dataLst.get(index);
			StringBuffer rowData = new StringBuffer();
			boolean zeroDataRow = false;
			for (int i = 0; i < innerLst.size(); i++) {
				String dataStr = innerLst.get(i);
				if(i==0&&StringUtils.equals(dataStr, ""))
				{
						zeroDataRow=true;
						break;
				}
				if(i==1)
				{
					dataStr=dataStr.trim();
				}
				else if (i >= 2 ) {
//					System.out.println(""+i+":"+dataStr);
					continue;
				}
				rowData.append(",'").append(dataStr).append("'");
			}
			if (rowData.length() > 0) {
				StringBuffer sqlData = new StringBuffer();
				sqlData.append("INSERT INTO PUBTHLP  (")
						.append("FLD_ORDER, FLD_NM, FLD_VAL, FLD_EXP, FLD_TYP, FLD_EXP_DESC, TM_SMP)")
						.append("values (")
						.append("'99','TX_TYP',")
						.append(rowData.deleteCharAt(0).toString())
						.append(",'x','交易类型','"+ tm_smp + "'").append(");");
				System.out.println(sqlData.toString());
				ExportUntils.exportToFile(sqlData.toString()+"\r\n","PUBTHLP");
			 
			}
		}

	}
	
	public static void getSyscnl(String fileName)throws IOException {

		List<ArrayList<String>> dataLst = new ReadExcelPOI(3)
				.read(fileName);

		ExportUntils.exportToFile("DELETE FROM PUBTHLP WHERE FLD_NM='SYS_CNL';"+"\r\n","PUBTHLP");
		for (int index = 2; index < dataLst.size(); index++) {
			ArrayList<String> innerLst = dataLst.get(index);
			StringBuffer rowData = new StringBuffer();
			for (int i = 0; i < innerLst.size(); i++) {
				String dataStr = innerLst.get(i);
				if(i==1)
				{
					dataStr=dataStr.trim();
				}
				else if (i >= 2 ) {
//					System.out.println(""+i+":"+dataStr);
					continue;
				}
				rowData.append(",'").append(dataStr).append("'");
			}
			if (rowData.length() > 0) {
				StringBuffer sqlData = new StringBuffer();
				sqlData.append("INSERT INTO PUBTHLP  (")
						.append("FLD_ORDER, FLD_NM, FLD_VAL, FLD_EXP, FLD_TYP, FLD_EXP_DESC, TM_SMP)")
						.append("values (")
						.append("'99','SYS_CNL',")
						.append(rowData.deleteCharAt(0).toString())
						.append(",'x','系统渠道','"+ tm_smp + "'").append(");");
				System.out.println(sqlData.toString());
				ExportUntils.exportToFile(sqlData.toString()+"\r\n","PUBTHLP");
			}
		}

	}
	
	public static void getBuscnl(String fileName)throws IOException {

		List<ArrayList<String>> dataLst = new ReadExcelPOI(4)
				.read(fileName);

		ExportUntils.exportToFile("DELETE FROM PUBTHLP WHERE FLD_NM='BUS_CNL';"+"\r\n","PUBTHLP");
		for (int index = 2; index < dataLst.size(); index++) {
			ArrayList<String> innerLst = dataLst.get(index);
			StringBuffer rowData = new StringBuffer();
			for (int i = 0; i < innerLst.size(); i++) {
				String dataStr = innerLst.get(i);
				if(i==1)
				{
					dataStr=dataStr.trim();
				}
				else if (i >= 2 ) {
//					System.out.println(""+i+":"+dataStr);
					continue;
				}
				rowData.append(",'").append(dataStr).append("'");
			}
			if (rowData.length() > 0) {
				StringBuffer sqlData = new StringBuffer();
				sqlData.append("INSERT INTO PUBTHLP  (")
						.append("FLD_ORDER, FLD_NM, FLD_VAL, FLD_EXP, FLD_TYP, FLD_EXP_DESC, TM_SMP)")
						.append("values (")
						.append("'99','BUS_CNL',")
						.append(rowData.deleteCharAt(0).toString())
						.append(",'x','业务渠道','"+ tm_smp + "'").append(");");
				System.out.println(sqlData.toString());
				ExportUntils.exportToFile(sqlData.toString()+"\r\n","PUBTHLP");
			}
		}

	}
	
	public static void getOrdtyp(String fileName)throws IOException {

		List<ArrayList<String>> dataLst = new ReadExcelPOI(5)
				.read(fileName);

		ExportUntils.exportToFile("DELETE FROM PUBTHLP WHERE FLD_NM='ORD_TYP';"+"\r\n","PUBTHLP");
		for (int index = 2; index < dataLst.size(); index++) {
			ArrayList<String> innerLst = dataLst.get(index);
			StringBuffer rowData = new StringBuffer();
			for (int i = 0; i < innerLst.size(); i++) {
				String dataStr = innerLst.get(i);
				if(i==1)
				{
					dataStr=dataStr.trim();
				}
				else if (i >= 2 ) {
//					System.out.println(""+i+":"+dataStr);
					continue;
				}
				rowData.append(",'").append(dataStr).append("'");
			}
			if (rowData.length() > 0) {
				StringBuffer sqlData = new StringBuffer();
				sqlData.append("INSERT INTO PUBTHLP  (")
						.append("FLD_ORDER, FLD_NM, FLD_VAL, FLD_EXP, FLD_TYP, FLD_EXP_DESC, TM_SMP)")
						.append("values (")
						.append("'99','ORD_TYP',")
						.append(rowData.deleteCharAt(0).toString())
						.append(",'x','订单类型','"+ tm_smp + "'").append(");");
				System.out.println(sqlData.toString());
				ExportUntils.exportToFile(sqlData.toString()+"\r\n","PUBTHLP");
			}
		}

	}
	
/*	// 将字符串写入文件中
			public static void exportToFile(String sqlData, String tableName) throws IOException {
				String base = baseDir + File.separator;
				createFolder(base);
				String fileName = base + tableName+".sql";
				File f = new File(fileName);
				OutputStream out = new FileOutputStream(f, true);
				byte[] b = sqlData.getBytes();
				out.write(b);
				out.close();
			}

			// 创建文件夹
			public static void createFolder(String folderPath) {
				try {
					File folder = new File(folderPath);
					if (!folder.exists()) {
						folder.mkdirs();
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
			}

*/

}
