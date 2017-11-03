package com.sql.tools;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.math.BigDecimal;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import jxl.Cell;
import jxl.Sheet;
import jxl.Workbook;
import jxl.format.CellFormat;
import jxl.write.Label;
import jxl.write.WritableCell;
import jxl.write.WritableSheet;
import jxl.write.WritableWorkbook;

public class ReaderCSVCEB {
	private int monthDay = 20;         //五月，31天
	private String [] dataDebit = new String[monthDay]; 
	private String [] dataCredit = new String[monthDay]; 
	private String [] dataBalance = new String[monthDay]; 
	
	private FileWriter fw ;
	private BufferedWriter bw ;
	
    public static void main(String[] args) {
    	
    	ReaderCSVCEB readerCSV = new ReaderCSVCEB();
 //   	readerCSV.executeICBC();  //工行
 //   	readerCSV.executeCMB();   //招行
 //   	readerCSV.executeCCB();
//    	String tmpPath = "D:/tmp/工行 May/工行0523.csv";
//    	readerCSV.reader(tmpPath);   	
//    	String tmpPath = "D:/tmp/bank/CCB/中信0502.xls";
//    	
//      readerCSV.readerCCB(tmpPath);
    	readerCSV.executeSql("ICBC");
	}
    
    public int executeSql(String bankName) {
    	
    	for (int i = 1 ; i <= monthDay ; ++i ) {
			String date = "";
			if ( i < 10 ) {
				date = "0" + i ;
			} else {
				date = i + "";
			}
    		
			String path = "D:/tmp/bank/" + bankName +"/";   //路径
			path = "C:/My Data/0A-账务系统/备付金核对/Game of Thrones/光大/光大2016 Apr/";
    	    String filePath = path +"光大04" + date + ".xls";   //xls文件路径
    	    String outSqlPath = path +"光大04" + date + ".sql";
    	    File fileSql = new File(outSqlPath);
    	    
    	    try {
				fileSql.createNewFile();
			} catch (IOException e) {
				e.printStackTrace();
			}   
    	    bankName="A_CEB";
    	    genSql(bankName, filePath, outSqlPath);      	    
    	}
    	
    	return 0;
    }
    
	public void genSql(String bankName,String filePath, String outSqlPath) {

		InputStream is = null;
		Workbook rwb = null;
		try {
			is = new FileInputStream(filePath);
			rwb = Workbook.getWorkbook(is);

			Sheet rs = rwb.getSheet(0);

			int col = rs.getColumns();       //列数
			int row = rs.getRows();          //行数
			System.out.println("row： " + row);
			System.out.println("col： " + row);
			String[] data = new String[col];
			
		    fw = new FileWriter(outSqlPath);	    	
	    	bw = new BufferedWriter(fw);

			for (int i = 15; i < row; ++i) {
				boolean zeroRow = false;
				for (int j = 0 ; j < col; ++j ) {
				    Cell cDebit = rs.getCell(j, i);
			        String cellContent = cDebit.getContents();
			        data[j] = cellContent.trim();
			        if(j == 2||j==3||j==4) {
			        	if(data[j].equals(""))
			        		data[j]="0";
			        }
			        if(j==0&&data[j].equals("")) {
			        	zeroRow = true;
			        	break;
			        }
			        /*
			        if (j == 3) {
			        	String [] tmp = data[j].split(" ");
			        	String [] tmpDate = tmp[0].split("/");
			        	String [] tmpTime = tmp[1].split(":");
			        	if ( tmpDate[0].length() == 1) {
			        		tmpDate[0] = "0" + tmpDate[0];
			        	}
			        	
			        	if ( tmpDate[1].length() == 1) {
			        		tmpDate[1] = "0" + tmpDate[1];
			        	}
			        	
			        	if ( tmpTime[0].length() == 1) {
			        		tmpTime[0] = "0" + tmpTime[0];
			        	}
			        	
			        	data[j] = "20" + tmpDate[2] + "-" + tmpDate[0] + "-" + tmpDate[1] + " " + 
			        	           tmpTime[0] + ":" + tmpTime[1];
			        }
			        */
			        data[j] = data[j].replaceAll(",", "");
			        data[j] = data[j].replaceAll("-", "");
			        data[j] = data[j].replaceAll(":", "");
			        
				}
				if(!zeroRow){
				   /*String sql = "insert into " + bankName + "_DATAS " +
				   		"(JRN_NO,TX_DT,TX_TM,DR_AMT,CR_AMT,BAL,OPACNO,CUSNM,IDS,B_JRN,RMK) VALUES (" +
				   		"'" + data[0]+i +"','" + data[0] +"',"+ "'" + data[1] +"'," + "" + data[2] +"," +
				   		"" + data[3] +","+ "" + data[4] +"," + "'" + data[5] +"'," +
				   		"'" + data[6] +"',"+ "'" + data[7] +"',"+ "'" + data[8] +"',"+ "'" + data[9] +"'"+
				   		");" ;
				   */
					String sql = "insert into " + bankName + "_DATAS " +
					   		"(JRN_NO,TX_DT,TX_TM,DR_AMT,CR_AMT,BAL,OPACNO,CUSNM,IDS,RMK) VALUES (" +
					   		"'" + data[0]+i +"','" + data[0] +"',"+ "'" + data[1] +"'," + "" + data[2] +"," +
					   		"" + data[3] +","+ "" + data[4] +"," + "'" + data[5] +"'," +
					   		"'" + data[6] +"',"+ "'" + data[7] +"',"+ "'" + data[8] +"'"+
					   		");" ;
					   
				   bw.write(sql + "\n");
				   System.out.println(sql);
				}
			}

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (rwb !=null) {
				rwb.close();
			}
			if ( is !=null ) {
				try {
					is.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			
		    try {
				bw.flush();
			    bw.close();
			    fw.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
    
    
    
    public int executeXls(String bankName, String month) {
    	for (int i = 1 ; i <= monthDay ; ++i ) {
			String date = "";
			if ( i < 10 ) {
				date = "0" + i ;
			} else {
				date = i + "";
			}
    		
    	    String path = "D:/tmp/bank/CCB/中信05" + date + ".xls";
    	    String[] data = readerCCB( path );
    	    
    	    dataDebit[i-1] =  data[0];
			dataCredit[i-1] =  data[1];
			dataBalance[i-1] =  data[2];
    	}
    	
    	int col = 12;      //写入第13列
		String writePath = "D:/tmp/bank/out/中信-支付公司银行余额调节表.xls";
		writeInXls(writePath,dataDebit,dataCredit,dataBalance, col,monthDay );		
    	return 0;
    }
    
    public int executeICBC() {    	   	
		for ( int i = 1 ; i <= monthDay ; ++i ) {			
			if ( i == 23) {
				String[] data = new String[3];
				dataDebit[22] =   "0";
				dataCredit[22] =  "0";
				dataBalance[22] = "0";
				continue;
			}
			
			String date = "";
			if ( i < 10 ) {
				date = "0" + i ;
			} else {
				date = i + "";
			}
			
			String path = "D:/tmp/bank/ICBC/工行05" + date + ".csv";
			System.out.println(path);
			String[] data = readerICBC( path );
			dataDebit[i-1] =  data[0];
			dataCredit[i-1] =  data[1];
			dataBalance[i-1] =  data[2];
		}
		
		int col = 12;      //写入第13列
		String writePath = "D:/tmp/bank/out/工行-支付公司银行余额调节表.xls";
		writeInXls(writePath,dataDebit,dataCredit,dataBalance, col,monthDay );		
		return 0;
    }
    
    public int executeCMB() {    	    	
    	for (int i = 1 ; i <= monthDay ; ++i ) {
    	    String path = "D:/tmp/bank/CMB/" + i + ".xls";
    	    String[] data = readerCMB( path );
    	    
    	    dataDebit[i-1] =  data[0];
			dataCredit[i-1] =  data[1];
			dataBalance[i-1] =  data[2];
    	}
    	
    	int col = 12;      //写入第13列
		String writePath = "D:/tmp/bank/out/招行-支付公司银行余额调节表.xls";
		writeInXls(writePath,dataDebit,dataCredit,dataBalance, col,monthDay );		
		return 0;
    }
    
    public int executeCCB() {    	    	
    	for (int i = 1 ; i <= monthDay ; ++i ) {
			String date = "";
			if ( i < 10 ) {
				date = "0" + i ;
			} else {
				date = i + "";
			}
    		
    	    String path = "D:/tmp/bank/CCB/中信05" + date + ".xls";
    	    String[] data = readerCCB( path );
    	    
    	    dataDebit[i-1] =  data[0];
			dataCredit[i-1] =  data[1];
			dataBalance[i-1] =  data[2];
    	}
    	
    	int col = 12;      //写入第13列
		String writePath = "D:/tmp/bank/out/中信-支付公司银行余额调节表.xls";
		writeInXls(writePath,dataDebit,dataCredit,dataBalance, col,monthDay );		
		return 0;
    }
    
    public int executeCEB() {    	    	
    	for (int i = 1 ; i <= monthDay ; ++i ) {
			String date = "";
			if ( i < 10 ) {
				date = "0" + i ;
			} else {
				date = i + "";
			}
    		
    	    String path = "D:/tmp/bank/CCB/光大05" + date + ".xls";
    	    String[] data = readerCEB( path );
    	    
    	    dataDebit[i-1] =  data[0];
			dataCredit[i-1] =  data[1];
			dataBalance[i-1] =  data[2];
    	}
    	
    	int col = 12;      //写入第13列
		String writePath = "D:/tmp/bank/out/中信-支付公司银行余额调节表.xls";
		writeInXls(writePath,dataDebit,dataCredit,dataBalance, col,monthDay );		
		return 0;
    }
    
    
    public String[] readerICBC(String path) {
    	String[] data = new String[3];
    	 try {  
             BufferedReader reader = new BufferedReader(new FileReader(path));  //换成你的文件名 
            // reader.readLine();//第一行信息，为标题信息，不用,如果需要，注释掉 
             String line = null;  
             int count = 0;
             double sumDebit = 0;
             double sumCredit = 0;
             
             //由于采用double会有精度损失，因此最终结果进行2位小数的四舍五入，如有必要，采用BigDecimal类型
             //BigDecimal testD = new BigDecimal(0);
             //BigDecimal testC = new BigDecimal(0);
             
             while((line=reader.readLine())!=null){  
            	 count++;
                 String item[] = line.split("\",\"");//CSV格式文件为逗号分隔符文件，这里根据逗号切分                 
                 String lastData = item[item.length-1];                                 
                 
                 //从第九行开始有数据
                 if (count == 3) {
                     System.out.println("余额： "+ delComma(lastData).replaceAll("\"","") ); 
                     data[2] = String.format("%.2f",Double.parseDouble(delComma(lastData).replaceAll("\"","")));        //余额
                 }
                 
                 if (count >= 3) {	                 	                 
	                 sumDebit += Double.parseDouble( delComma(item[1]) );     //借方发生额
	                 sumCredit += Double.parseDouble( delComma(item[2]) );    //贷方发生额
                 }                
             }  
             
             data[0] = String.format("%.2f",sumDebit);
             data[1] = String.format("%.2f",sumCredit);
             System.out.println("借方发生额:" + data[0]);
             System.out.println("贷方发生额:" + data[1]);
             System.out.println("余额： "+ data[2] ); 
             
         } catch (Exception e) {  
             e.printStackTrace();  
         }  
    	return data;
    }
    
    public String[] readerCMB(String filePath) {
    	String[] data = new String[3];
    	
		double sumDebit = 0;
        double sumCredit = 0;
    	
    	InputStream is = null;
    	Workbook rwb = null;
		try {
			is = new FileInputStream(filePath);
			rwb = Workbook.getWorkbook(is);
			
			Sheet rs = rwb.getSheet(0);
			
			int col = 4;
			int row = rs.getRows();
			System.out.println("row： " + row);
					
			for (int i = 13 ; i < row; ++i ) {
			    Cell cDebit = rs.getCell(col, i);
			    String cellContent = cDebit.getContents().replaceAll(",","");
			    if ( cellContent == null || cellContent.equals("") ) {
			    	cellContent = "0";
			    }
			    sumDebit += Double.parseDouble(cellContent);
			    
			    Cell cCredit = rs.getCell(col + 1, i);
			    cellContent = cCredit.getContents().replaceAll(",","");
			    if ( cellContent == null || cellContent.equals("") ) {
			    	cellContent = "0";
			    }
			    sumCredit += Double.parseDouble(cellContent);
			    
			    			    
			}
			
			Cell c = rs.getCell(col + 2, row-1);
			data[2] = c.getContents().replaceAll(",","");
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
		
		data[0] = String.format("%.2f",sumDebit);
		data[1] = String.format("%.2f",sumCredit);
		
		System.out.println("sumDebit:" + data[0] + " sumCredit: " + data[1] + " balance: " + data[2]);
 	
    	return data;
    }
    
    public String[] readerCCB(String filePath) {
    	String[] data = new String[3];
    	
		double sumDebit = 0;
        double sumCredit = 0;
    	
    	InputStream is = null;
    	Workbook rwb = null;
		try {
			is = new FileInputStream(filePath);
			rwb = Workbook.getWorkbook(is);
			
			Sheet rs = rwb.getSheet(0);
			
			int col = 6;
			int row = rs.getRows();
			System.out.println("row： " + row);
					
			for (int i = 2 ; i < row; ++i ) {
			    Cell cDebit = rs.getCell(col, i);
			    String cellContent = cDebit.getContents().replaceAll(",","");
			    if ( cellContent == null || cellContent.equals("") ) {
			    	cellContent = "0";
			    }
			    sumDebit += Double.parseDouble(cellContent);
			    
			    Cell cCredit = rs.getCell(col + 1, i);
			    cellContent = cCredit.getContents().replaceAll(",","");
			    if ( cellContent == null || cellContent.equals("") ) {
			    	cellContent = "0";
			    }
			    sumCredit += Double.parseDouble(cellContent);
			    
			    			    
			}
			
			Cell c = rs.getCell(col + 2, row-1);
			data[2] = c.getContents().replaceAll(",","");
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
		
		data[0] = String.format("%.2f",sumDebit);
		data[1] = String.format("%.2f",sumCredit);
		
		System.out.println("sumDebit:" + data[0] + " sumCredit: " + data[1] + " balance: " + data[2]);
 	
    	return data;
    }
    
    public String[] readerCEB(String filePath) {
    	String[] data = new String[3];
    	
		double sumDebit = 0;
        double sumCredit = 0;
    	
    	InputStream is = null;
    	Workbook rwb = null;
		try {
			is = new FileInputStream(filePath);
			rwb = Workbook.getWorkbook(is);
			
			Sheet rs = rwb.getSheet(0);
			
			int col = 2;
			int row = rs.getRows();
			System.out.println("row： " + row);
					
			for (int i = 2 ; i < row; ++i ) {
			    Cell cDebit = rs.getCell(col, i);
			    String cellContent = cDebit.getContents().replaceAll(",","");
			    if ( cellContent == null || cellContent.equals("") ) {
			    	cellContent = "0";
			    }
			    sumDebit += Double.parseDouble(cellContent);
			    
			    Cell cCredit = rs.getCell(col + 1, i);
			    cellContent = cCredit.getContents().replaceAll(",","");
			    if ( cellContent == null || cellContent.equals("") ) {
			    	cellContent = "0";
			    }
			    sumCredit += Double.parseDouble(cellContent);
			    
			    			    
			}
			
			Cell c = rs.getCell(col + 2, row-1);
			data[2] = c.getContents().replaceAll(",","");
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
		
		data[0] = String.format("%.2f",sumDebit);
		data[1] = String.format("%.2f",sumCredit);
		
		System.out.println("sumDebit:" + data[0] + " sumCredit: " + data[1] + " balance: " + data[2]);
 	
    	return data;
    }
        
    /**
     * 写入excel表
     * @param filePath
     * @param dataDebit
     * @param dataCredit
     * @param dataBalance
     * @param col
     * @param count
     */
    public void writeInXls(String filePath, String dataDebit[],String dataCredit[],String dataBalance[], int col,int count ) {
    	WritableWorkbook book = null;
    	Workbook rwb = null;
	    try {
	    	rwb = Workbook.getWorkbook(new File(filePath));
			book = Workbook.createWorkbook(new File(filePath),rwb );
			WritableSheet sh = book.getSheet(2); //
			
			for (int i = 0 ; i < count; i++) {
				WritableCell cellDebit = sh.getWritableCell(col, i+4);
				CellFormat cf = cellDebit.getCellFormat();
				Label label1 = new Label(col, i+4, dataDebit[i]);
				label1.setCellFormat(cf);
				sh.addCell(label1);
				
				WritableCell cellCredit = sh.getWritableCell(col+1, i+4);
				//CellFormat cf2 = cellCredit.getCellFormat();
				Label label2 = new Label(col+1, i+4, dataCredit[i]);
				label2.setCellFormat(cf);
				sh.addCell(label2);
				
				WritableCell cellBalance = sh.getWritableCell(col+2, i+4);
				//CellFormat cf3 = cellBalance.getCellFormat();
				Label label3 = new Label(col+2, i+4, dataBalance[i]);
				label3.setCellFormat(cf);
				sh.addCell(label3);
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (rwb !=null) {
				try {
					book.write();
					book.close();
					rwb.close();
				} catch (Exception e) {
					e.printStackTrace();
				} 
			}
		}		    	
    }
    
    /**
     * 去除逗号
     * @param data
     * @return
     */
    public String delComma(String data) {
    	if (data.equals("")) 
    		return "0";
    	return data.replaceAll(",","");
    }
    
//    public String getDigit(String data) {
//    	String tmp = data.replaceAll(",","").replaceAll(" ","").replaceAll("\"","");
//    	
//    	String reg = "[\u4e00-\u9fa5]";
//    	Pattern pat = Pattern.compile(reg);  
//    	Matcher mat=pat.matcher(tmp); 
//    	String out = mat.replaceAll("");
//    	
//    	return out;
//    }
    
    public String dealCMB(String data) {
    	String tmp = data.replaceAll(",,",",\"\",").replaceAll(",,",",\"\",");
    	tmp = tmp.replace("批量支付款项,", "批量支付款项,\"").replace("批量支付款项,\"\"", "批量支付款项,\"");
    	tmp = tmp.replace(".00",".00\"").replace(".00\"\"",".00\"");
    	return tmp;
    }
}
