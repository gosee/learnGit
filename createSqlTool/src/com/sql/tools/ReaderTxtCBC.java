package com.sql.tools;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Date;

public class ReaderTxtCBC {
	

	private static FileWriter fw ;
	private static BufferedWriter bw ;
	
	private static String sqlPath="C://My Data//0A-账务系统//备付金核对//Game of Thrones//建行2016//sql//";
	
	 private static ArrayList<String> filelist = new ArrayList<String>();
	 
	 public static void main(String[] args) throws Exception {
	    
	    String filePath = "E://Struts2";
	    filePath = "C://My Data//0A-账务系统//备付金核对//Game of Thrones//建行2016//ccb.chk_a";
	    
	   // getFilesS(filePath);
	    renameFile(sqlPath);
	    
	 } 
	 /*
	  * 得到某一路径下所有的文件
	  */
	 public static void getFilesS(String filePath) throws IOException{
	  File root = new File(filePath);
	    File[] files = root.listFiles();
	    int i =0;
	    for(File file:files){
	    	i++;
	    	if(!file.isDirectory()) {
	    	    System.out.println("处理文件"+file.getAbsolutePath());
	    		createSql(i,file.getAbsolutePath(),sqlPath+i+"_"+file.getName().replace(".txt", "")+".sql");
	    	}
	    }
	 }
	 
	 
	 public static void renameFile(String filePath) throws IOException{
		  File root = new File(filePath);
		    File[] files = root.listFiles();
		    int i =0;
		    for(File file:files){
		    	i++;
		    	if(!file.isDirectory()) {
		    		
		    		File newFile = new File(sqlPath+i+".sql");
                    file.renameTo(newFile);
		    		
		    	}
		    }
		 }
		 
	 
	public static void createSql(int m,String filePath,String sqlPath) throws IOException {
		//fw = new FileWriter(sqlPath+(new Date()).toString().replace(" ", "").replace(":", "")+".sql");	    	
		//fw = new FileWriter(sqlPath);	    	
		bw = new BufferedWriter((new OutputStreamWriter(new FileOutputStream(sqlPath),"UTF-8")));

    	
    	File file=new File(filePath);
    	try {
			InputStreamReader read = new InputStreamReader(new FileInputStream(file),"UTF-8");//考虑到编码格式
			BufferedReader bufferedReader = new BufferedReader(read);
			String lineTxt = null;
			
			int i=0;
			while((lineTxt = bufferedReader.readLine()) != null){
				//System.out.println(lineTxt);
				i++;
				String txDt = lineTxt.substring(0, 8);
				String txTm = lineTxt.substring(8, 14);
				String idsTyp = lineTxt.substring(14, 52);
				idsTyp=idsTyp.replace(" ", "");
				String idNo = lineTxt.substring(52, 117);
				idNo=idNo.replace(" ", "");
				String rmk = " ";
				//rmk = rmk.replace(" ", "");
				String amt = lineTxt.substring(117,130);
				amt =amt.replace(" ", "");
				amt =amt.replace(",", "");
				String bal = lineTxt.substring(130, 143);
				bal =bal.replace(" ", "");
				bal =bal.replace(",", "");
				String dcFlg = lineTxt.substring(143, 148);
				dcFlg=dcFlg.replace(" ", "");
				String drAmt = "0";
				String crAmt = "0";
				if(dcFlg.equals("0")){
					drAmt = amt;
				}else{
					crAmt = amt;
				}
				String opAcNo = lineTxt.substring(148, 167);
				opAcNo = opAcNo.replace(" ", "");
				String cusNm = lineTxt.substring(167, 230);
				cusNm = cusNm.replace(" ", "");
				String txTp = lineTxt.substring(230, 232);
				txTp = txTp.replace(" ", "");
				String bJrn = lineTxt.substring(232,280);
				bJrn = bJrn.replace(" ", "");
				String busTyp = lineTxt.substring(280, 322); //企业支付流水
				busTyp =busTyp.replace(" ", "");
				String acNo =  " ";
				String txDesc = lineTxt.substring(322);
				txDesc = txDesc.replace(" ", "");
				String sql = "insert into B_CBC_DATAS " +
						"(JRN_NO,TX_DT,TX_TM,IDSTYP,IDS,RMK,DR_AMT,CR_AMT,BAL,DC_FLG,OPACNO,CUSNM,TX_TYP,"
						+ "B_JRN,BUS_TYP,ACNO,TX_DESC) VALUES (" +
						"'"+m+txDt+i +"','"+txDt+"','"+txTm+"','"+idsTyp+"','"+idNo+"','"+rmk+"',"
						+drAmt+","+crAmt+","+bal+","+dcFlg+",'"+opAcNo+"','"+cusNm+"','"+txTp+"','"
						+bJrn+"','"+busTyp+"','"+acNo+"','"+txDesc+"'"
						+ ");" ;
				//System.out.println(sql);
				bw.write(sql+"\n");
			}
			read.close();
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    	bw.close();


	}
	 
	 
	 
	 
	 
	 
	 
	 
	 
	 /*
	  * 通过递归得到某一路径下所有的目录及其文件
	  */
	 static void getFiles(String filePath){
	  File root = new File(filePath);
	    File[] files = root.listFiles();
	    for(File file:files){     
	     if(file.isDirectory()){
	      /*
	       * 递归调用
	       */
	    	 
	      getFiles(file.getAbsolutePath());
	      filelist.add(file.getAbsolutePath());
	      System.out.println("显示"+filePath+"下所有子目录及其文件"+file.getAbsolutePath());
	     }else{
	      System.out.println("显示"+filePath+"下所有子目录"+file.getAbsolutePath());
	     }     
	    }
	 }

}
