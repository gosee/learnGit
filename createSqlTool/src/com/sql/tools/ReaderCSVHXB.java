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

public class ReaderCSVHXB {
	private int monthDay = 31;         //���£�31��
	private String [] dataDebit = new String[monthDay]; 
	private String [] dataCredit = new String[monthDay]; 
	private String [] dataBalance = new String[monthDay]; 
	
	private FileWriter fw ;
	private BufferedWriter bw ;
	
    public static void main(String[] args) {
    	
    	ReaderCSVHXB readerCSV = new ReaderCSVHXB();
 //   	readerCSV.executeICBC();  //����
 //   	readerCSV.executeCMB();   //����
 //   	readerCSV.executeCCB();
//    	String tmpPath = "D:/tmp/���� May/����0523.csv";
//    	readerCSV.reader(tmpPath);   	
//    	String tmpPath = "D:/tmp/bank/CCB/����0502.xls";
//    	
//      readerCSV.readerCCB(tmpPath);
    	readerCSV.executeSql("A_HXB");
	}
    
    public int executeSql(String bankName) {
    	
    	for (int i = 1 ; i <= 1 ; ++i ) {
			String date = "";
			if ( i < 10 ) {
				date = "0" + i ;
			} else {
				date = i + "";
			}
    		
			String path = "D:/tmp/bank/" + bankName +"/";   //·��
			path = "C:/My Data/0A-����ϵͳ/������˶�/Game of Thrones/����2016/";
    	    String filePath = path +"����0401-0430.xls";   //xls�ļ�·��
    	    String outSqlPath = path +"����0401-0430.sql";
    	    File fileSql = new File(outSqlPath);
    	    
    	    try {
				fileSql.createNewFile();
			} catch (IOException e) {
				e.printStackTrace();
			}   
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

			int col = rs.getColumns();       //����
			int row = rs.getRows();          //����
			System.out.println("row�� " + row);
			System.out.println("col�� " + row);
			String[] data = new String[col];
			
		    fw = new FileWriter(outSqlPath);	    	
	    	bw = new BufferedWriter(fw);

			for (int i = 1; i < row; ++i) {
				String txDt ="";
		        String txTm="";
		        
				for (int j = 0 ; j < col; ++j ) {
				    Cell cDebit = rs.getCell(j, i);
			        String cellContent = cDebit.getContents();
			        data[j] = cellContent.trim();
			        if(j == 3||j==4||j==2) {
			        	if(data[j].equals(""))
			        		data[j]="0";
			        }
			       /*
			        if (j == 2) {
			        	String [] tmp = data[j].split(" ");
			        	txDt = tmp[0];
			        	txTm = tmp[1];
			        }
			        */

			        	data[j] = data[j].replaceAll(",", "");
				}
				String sql = "insert into " + bankName + "_DATAS " +
						"(JRN_NO,TX_DT,TX_DESC,DR_AMT,CR_AMT,IDS,BAL,RMK,OPACNO,CUSNM,OPBNK) VALUES (" +
						"'" +i +"','" + data[0] +"',"+ "'" + data[1] +"',"+ "" + data[2]
								  
						+ "," + data[3] +""+",'" + data[4] +"'," + "" + data[5] +"," +
						 "'" + data[6] +"','" + data[7] +"'," + "'" + data[8]  +"'"
						 +"," + "'" + data[9]  +"'"+
						");" ;
				bw.write(sql + "\n");
				System.out.println(sql);
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
    		
    	    String path = "D:/tmp/bank/CCB/����05" + date + ".xls";
    	    String[] data = readerCCB( path );
    	    
    	    dataDebit[i-1] =  data[0];
			dataCredit[i-1] =  data[1];
			dataBalance[i-1] =  data[2];
    	}
    	
    	int col = 12;      //д���13��
		String writePath = "D:/tmp/bank/out/����-֧����˾���������ڱ�.xls";
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
			
			String path = "D:/tmp/bank/ICBC/����05" + date + ".csv";
			System.out.println(path);
			String[] data = readerICBC( path );
			dataDebit[i-1] =  data[0];
			dataCredit[i-1] =  data[1];
			dataBalance[i-1] =  data[2];
		}
		
		int col = 12;      //д���13��
		String writePath = "D:/tmp/bank/out/����-֧����˾���������ڱ�.xls";
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
    	
    	int col = 12;      //д���13��
		String writePath = "D:/tmp/bank/out/����-֧����˾���������ڱ�.xls";
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
    		
    	    String path = "D:/tmp/bank/CCB/����05" + date + ".xls";
    	    String[] data = readerCCB( path );
    	    
    	    dataDebit[i-1] =  data[0];
			dataCredit[i-1] =  data[1];
			dataBalance[i-1] =  data[2];
    	}
    	
    	int col = 12;      //д���13��
		String writePath = "D:/tmp/bank/out/����-֧����˾���������ڱ�.xls";
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
    		
    	    String path = "D:/tmp/bank/CCB/���05" + date + ".xls";
    	    String[] data = readerCEB( path );
    	    
    	    dataDebit[i-1] =  data[0];
			dataCredit[i-1] =  data[1];
			dataBalance[i-1] =  data[2];
    	}
    	
    	int col = 12;      //д���13��
		String writePath = "D:/tmp/bank/out/����-֧����˾���������ڱ�.xls";
		writeInXls(writePath,dataDebit,dataCredit,dataBalance, col,monthDay );		
		return 0;
    }
    
    
    public String[] readerICBC(String path) {
    	String[] data = new String[3];
    	 try {  
             BufferedReader reader = new BufferedReader(new FileReader(path));  //��������ļ��� 
            // reader.readLine();//��һ����Ϣ��Ϊ������Ϣ������,�����Ҫ��ע�͵� 
             String line = null;  
             int count = 0;
             double sumDebit = 0;
             double sumCredit = 0;
             
             //���ڲ���double���о�����ʧ��������ս������2λС�����������룬���б�Ҫ������BigDecimal����
             //BigDecimal testD = new BigDecimal(0);
             //BigDecimal testC = new BigDecimal(0);
             
             while((line=reader.readLine())!=null){  
            	 count++;
                 String item[] = line.split("\",\"");//CSV��ʽ�ļ�Ϊ���ŷָ����ļ���������ݶ����з�                 
                 String lastData = item[item.length-1];                                 
                 
                 //�ӵھ��п�ʼ������
                 if (count == 3) {
                     System.out.println("�� "+ delComma(lastData).replaceAll("\"","") ); 
                     data[2] = String.format("%.2f",Double.parseDouble(delComma(lastData).replaceAll("\"","")));        //���
                 }
                 
                 if (count >= 3) {	                 	                 
	                 sumDebit += Double.parseDouble( delComma(item[1]) );     //�跽������
	                 sumCredit += Double.parseDouble( delComma(item[2]) );    //����������
                 }                
             }  
             
             data[0] = String.format("%.2f",sumDebit);
             data[1] = String.format("%.2f",sumCredit);
             System.out.println("�跽������:" + data[0]);
             System.out.println("����������:" + data[1]);
             System.out.println("�� "+ data[2] ); 
             
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
			System.out.println("row�� " + row);
					
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
			System.out.println("row�� " + row);
					
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
			System.out.println("row�� " + row);
					
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
     * д��excel��
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
     * ȥ������
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
    	tmp = tmp.replace("����֧������,", "����֧������,\"").replace("����֧������,\"\"", "����֧������,\"");
    	tmp = tmp.replace(".00",".00\"").replace(".00\"\"",".00\"");
    	return tmp;
    }
}
