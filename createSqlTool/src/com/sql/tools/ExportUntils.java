package com.sql.tools;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;



import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.tools.ant.Project;
import org.apache.tools.ant.taskdefs.Zip;
import org.apache.tools.ant.types.FileSet;
/**
 * 
 * @author guanyifeng
 *
 */

public class ExportUntils {

	// ���ַ���д���ļ��� ��δʹ��
	public static void exportToFile(String sqlData, String tableName)
			throws IOException {
		String base = Constants.BASEDIR + File.separator;
		createFolder(base);
		String fileName = base + tableName + ".sql";
		File f = new File(fileName);
		OutputStream out = new FileOutputStream(f, true);
		byte[] b = sqlData.getBytes();
		out.write(b);
		out.close();
	}
	
	public static void clearContents(String tableName) throws IOException
	{
		String base = Constants.BASEDIR + File.separator;
		createFolder(base);
		String fileName = base + tableName + ".sql";
		File f = new File(fileName);		
		FileWriter fw = new FileWriter(f);
		fw.write("");
		fw.close();
	}

	public static void exportToFile(String sqlData, String tableName,String nameDef,
			Boolean first, int end) throws IOException {
		String base = Constants.BASEDIR + File.separator;
		createFolder(base);
		String fileName = base + nameDef + ".sql";
		File f = new File(fileName);		
		if (first) {
			// ���ԭ�ļ�����
			FileWriter fw = new FileWriter(f);
			fw.write("");
			fw.close();
			//sqlData = "DELETE FROM " + tableName + " WHERE AC_ORG='100001';" + "\r\n\r\n" + sqlData;
		}
		

		OutputStream out = new FileOutputStream(f, true);
		byte[] b = sqlData.getBytes();
		out.write(b);
		out.close();
	}

	// �����ļ���
	public static void createFolder(String folderPath) {
		try {
			File folder = new File(folderPath);
			if (!folder.exists()) {
				folder.mkdirs();
				System.err.println("Ŀ¼�����ڣ������ɹ���");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	//ɾ��excel�հ���/ȡ����Ч����
	public static int getExpNum(String fileName,int index) throws Exception{
		FileInputStream is = new FileInputStream(fileName) ;
		HSSFWorkbook wb = new HSSFWorkbook(is) ;
		HSSFSheet sheet = wb.getSheetAt(index) ;
		int lastNum = sheet.getLastRowNum() ;
		System.out.println("������="+lastNum+1) ;
		int expNum = 1 ;
		for (expNum=1; expNum < lastNum; expNum++) {
			HSSFRow row = sheet.getRow(expNum) ;
			HSSFCell celdata = row.getCell((short)0) ;
			//System.out.println(celdata);			
			if(null==celdata||"".equals(celdata.toString().trim())){
				break ;
			}
		}
		/*
		while(lastNum > 0){
			lastNum--;
			HSSFRow row = sheet.getRow(lastNum) ;
			HSSFCell celdata = row.getCell(0) ;	
			if(null==celdata||"".equals(celdata.toString().trim())){
				expNum = lastNum ;
				break ;
			}
		}
		//ɾ���հ���
		int i = sheet.getLastRowNum();
		HSSFRow tempRow;
		while (i > 0) {
			i--;
			tempRow = sheet.getRow(i);
			if (tempRow == null) {
				sheet.shiftRows(i + 1, sheet.getLastRowNum(), -1);
			}
		}*/
		System.out.println("��Ч����="+expNum);
		return expNum ;		
		
	}
	
	//�����ļ�zip��ɾ���ļ�
	public static boolean zipAnddelFiles(String filePath){
		if(!filePath.endsWith(File.separator)){
			filePath = filePath +File.separator ;
		}
		System.out.println("111");
		File dirFile = new File(filePath) ;
		if (!dirFile.exists()) {
			dirFile.mkdirs();
			System.err.println("Ŀ¼�����ڣ������ɹ���");
		}
		File files[] = dirFile.listFiles() ;
		int sqlFileNum=0;
		System.out.println("222;"+files.length);
		for (int j = 0; j < files.length; j++) {
			if(files[j].getName().contains(".sql")){
				sqlFileNum++ ;
			}
		}
		System.out.println("sql�ļ�������"+sqlFileNum);
		if(sqlFileNum>10){
			if(zipFile(filePath)){
				for (int i = 0; i < files.length; i++) {
					if(files[i].exists()&&files[i].isFile()&&files[i].getName().contains(".sql")&&sqlFileNum>10){
						files[i].delete();
					}
				}
			} 
		}	
		return true ;
	} 
	
	//�����ļ� ���
	public static boolean zipFile(String filePath){
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
		String tm_smp = sdf.format(new Date());
		String dateStr = tm_smp.substring(0, 12);
		
		if(!filePath.endsWith(File.separator)){
			filePath = filePath +File.separator ;
		}
		File srcdir = new File(filePath); 
		File destFile = new File(filePath+dateStr+".zip ");
        if (!srcdir.exists())  
            throw new RuntimeException(filePath + "�����ڣ�");  
          
        Project prj = new Project();  
        Zip zip = new Zip();  
        zip.setProject(prj);  
        zip.setDestFile(destFile) ; 
        FileSet fileSet = new FileSet();  
        fileSet.setProject(prj);  
        fileSet.setDir(srcdir);  
        fileSet.setIncludes("**/*.sql"); //������Щ�ļ����ļ��� eg:zip.setIncludes("*.java");  
        fileSet.setExcludes("**/*.zip"); //�ų���Щ�ļ����ļ���  
        zip.addFileset(fileSet);  
          
        zip.execute();  
		return true ;
	}  
	

	public static void main(String[] args) throws IOException {
		String filePath = Constants.BAKFILEPATH ;
			if(zipAnddelFiles(filePath)) {
				System.out.println("ZIP SUCCESS!");
			}
		}

		/*List<ArrayList<String>> dataLst = new ReadExcelPOI(5)
				.read("C:/Users/Admin/Desktop/¼��/�ϲ���Ʒ�¼_v0.3_1.xls");
		for (ArrayList<String> innerLst : dataLst) {
			StringBuffer rowData = new StringBuffer();
			for (String dataStr : innerLst) {
				rowData.append(",").append("'").append(dataStr).append("'");
			}
			if (rowData.length() > 0) {
				StringBuffer sqlData = new StringBuffer();
				sqlData.append("insert into ACTTITMR (")
						.append("AC_ORG, AC_TYP, CAP_TYP, CCY, ITM_NO, ITM_CCY, IN_AC_FLG, IAC_BAL_DRT, BAL_RED_DRT, UPD_BAL_FLG, MNU_AC_FLG, RVS_CLS, RVS_TYP, UPD_JRN, TM_SMP, UPD_DT, UPD_OPR, ID_NO)")
						.append("values (")
						.append(rowData.deleteCharAt(0).toString())
						.append(");");
				System.out.println(sqlData.toString());
			}
		}*/

	}



