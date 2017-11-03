package com.sql.tools;

public class Test {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		StringBuffer ColumnValue = new StringBuffer();
		ColumnValue.append("A、").append("B、").append(",") ;
		System.out.println(ColumnValue.toString());
		ColumnValue.deleteCharAt(ColumnValue.length()-1) ;//去掉最后一个逗号	
		System.out.println(ColumnValue.toString());

	}

}
