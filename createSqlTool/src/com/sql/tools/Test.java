package com.sql.tools;

public class Test {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		StringBuffer ColumnValue = new StringBuffer();
		ColumnValue.append("A��").append("B��").append(",") ;
		System.out.println(ColumnValue.toString());
		ColumnValue.deleteCharAt(ColumnValue.length()-1) ;//ȥ�����һ������	
		System.out.println(ColumnValue.toString());

	}

}
