package com.sql.tools;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.poi.hssf.usermodel.HSSFDateUtil;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.DateUtil;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

/**
 * Title:[POI�����ϵ�Excel���ݶ�ȡ����]
 * Description: [֧��Excell2003,Excell2007,�Զ���ʽ����ֵ������,�Զ���ʽ������������]
 * Copyright 2009 RoadWay Co., Ltd.
 * All right reserved.
 * Midified by [modifier] [modified time]
 * 
 * ����Jar���б�
 * poi-3.6-20091214.jar
 * poi-contrib-3.6-20091214.jar
 * poi-examples-3.6-20091214.jar
 * poi-ooxml-3.6-20091214.jar
 * poi-ooxml-schemas-3.6-20091214.jar
 * poi-scratchpad-3.6-20091214.jar
 * xmlbeans-2.3.0.jar
 * 
 * @version 1.0
 */
public class ReadExcelPOI
{
    /** ������ */
    private int totalRows = 0;   
    /** ������ */
    private int totalCells = 0;  
    /** ���췽�� */
    
    public static int sheetNum = 0 ;
    public ReadExcelPOI(int sheetNum){
    	this.sheetNum= sheetNum;
    }
    
    /**
     * Description:[�����ļ�����ȡexcel�ļ�]
     * Created by [Huyvanpull] [Jan 20, 2010]
     * Midified by [modifier] [modified time]
     * @param fileName
     * @return
     * @throws Exception
     */
    public List<ArrayList<String>> read(String fileName)
    {
        List<ArrayList<String>> dataLst = new ArrayList<ArrayList<String>>();
        
        /** ����ļ����Ƿ�Ϊ�ջ����Ƿ���Excel��ʽ���ļ� */
        if (fileName == null || !fileName.matches("^.+\\.(?i)((xls)|(xlsx))$")) {
            return null;
        }
        
        boolean isExcel2003 = true;
        /** ���ļ��ĺϷ��Խ�����֤ */
        if (fileName.matches("^.+\\.(?i)(xlsx)$")) {
            isExcel2003 = false;
        }
        
        /** ����ļ��Ƿ���� */
        File file = new File(fileName);
        if (file == null || !file.exists()) {
            return null;
        }
        
        try {
            /** ���ñ����ṩ�ĸ�������ȡ�ķ��� */
            dataLst = read(new FileInputStream(file), isExcel2003);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        /** ��������ȡ�Ľ�� */
        return dataLst;
    }
    
    /**
     * Description:[��������ȡExcel�ļ�]
     * Created by [Huyvanpull] [Jan 20, 2010]
     * Midified by [modifier] [modified time]
     *  
     * @param inputStream
     * @param isExcel2003
     * @return
     */
    public List<ArrayList<String>> read(InputStream inputStream,boolean isExcel2003) {
        List<ArrayList<String>> dataLst = null;
        try {
            /** ���ݰ汾ѡ�񴴽�Workbook�ķ�ʽ */
            Workbook wb =  isExcel2003 ? new HSSFWorkbook(inputStream) : new XSSFWorkbook(inputStream);
            dataLst = read(wb);
        }
        catch (IOException e){
            e.printStackTrace();
        }
        return dataLst;
    }
    
    /**
     * Description:[�õ�������]
     * Created by [Huyvanpull] [Jan 20, 2010]
     * Midified by [modifier] [modified time]
     * 
     * @return
     */
    public int getTotalRows() {
        return totalRows;
    }
    
    /**
     * Description:[�õ�������]
     * Created by [Huyvanpull] [Jan 20, 2010]
     * Midified by [modifier] [modified time]
     * 
     * @return
     */
    public int getTotalCells() {
        return totalCells;
    }
    
    /**
     * Description:[��ȡ����]
     * Created by [Huyvanpull] [Jan 20, 2010]
     * Midified by [modifier] [modified time]
     * 
     * @param wb
     * @return
     */
    private List<ArrayList<String>> read(Workbook wb) {
        List<ArrayList<String>> dataLst = new ArrayList<ArrayList<String>>();
        
    	/** �õ���һ��shell */
        Sheet sheet = wb.getSheetAt(sheetNum);
        this.totalRows = sheet.getPhysicalNumberOfRows();
        if (this.totalRows >= 1 && sheet.getRow(0) != null) {
            this.totalCells = sheet.getRow(0).getPhysicalNumberOfCells();
        }
        
        /** ѭ��Excel���� */
        for (int i = 0; i < this.totalRows; i++) {
            Row row = sheet.getRow(i);
            if (row == null) {
                continue;
            }
            
            ArrayList<String> rowLst = new ArrayList<String>();
            /** ѭ��Excel���� */
            for (short j = 0; j < this.getTotalCells(); j++) {
                Cell cell = row.getCell(j);
                String cellValue = "";
                if (cell == null) {
                    rowLst.add(cellValue);
                    continue;
                }
                
                /** ���������͵�,�Զ�ȥ�� */
                if (Cell.CELL_TYPE_NUMERIC == cell.getCellType()) {
                    /** ��excel��,����Ҳ������,�ڴ�Ҫ�����ж� */
                    if (HSSFDateUtil.isCellDateFormatted(cell)) {
                        Date date = cell.getDateCellValue();
                        DateFormat formater = new SimpleDateFormat("yyyyMMdd");  
                        cellValue= formater.format(date);  
                        
                    } else {
                        cellValue = getRightStr(cell.getNumericCellValue() + "");
                    }
                } else if (Cell.CELL_TYPE_STRING == cell.getCellType()) {
                    /** �����ַ����� */
                	cellValue = cell.getStringCellValue();
                } else if (Cell.CELL_TYPE_BOOLEAN == cell.getCellType()) {
                    /** �������� */
                	cellValue = cell.getBooleanCellValue() + "";
                }  else {
                	/** ������,�����ϼ����������� */
                    cellValue = cell.toString() + "";
                }
                rowLst.add(cellValue);
            }
            dataLst.add(rowLst);
        }
        return dataLst;
    }
    
    /**
     * Description:[��ȷ�ش����������Զ���������]
     * Created by [Huyvanpull] [Jan 20, 2010]
     * Midified by [modifier] [modified time]
     * 
     * @param sNum
     * @return
     */
    private String getRightStr(String sNum) {
        DecimalFormat decimalFormat = new DecimalFormat("#.000000");
        String resultStr = decimalFormat.format(new Double(sNum));
        if (resultStr.matches("^[-+]?\\d+\\.[0]+$")) {
            resultStr = resultStr.substring(0, resultStr.indexOf("."));
        }
        return resultStr;
    }
    
    /**
     * Description:[����main����]
     * Created by [Huyvanpull] [Jan 20, 2010]
     * Midified by [modifier] [modified time]
     * 
     * @param args
     * @throws Exception
     */
    public static void main(String[] args) throws Exception {
        List<ArrayList<String>> dataLst = new ReadExcelPOI(0)
                .read("e:/��Ŀ����������֧�������ڽ��������˻���0627.xlsx");
        for (ArrayList<String> innerLst : dataLst) {
            StringBuffer rowData = new StringBuffer();
            for (String dataStr : innerLst) {
                rowData.append(",").append(dataStr);
            }
            if (rowData.length() > 0) {
                System.out.println(rowData.deleteCharAt(0).toString());
            }
        }
    }
}