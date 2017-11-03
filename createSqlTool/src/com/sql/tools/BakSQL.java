package com.sql.tools;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Types;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
/**
 * 
 * @author guanyifeng
 * @description ��ѯ���ݲ��������ļ���ʵ�ֳ�ʼ������ǰ�ı��ݣ�
 */

public class BakSQL {
	private static String tm_smp;
	private static String dateStr;
	private static Connection conn = null;
	private static Statement sm = null;
	private static String schema = "FJSTL";// ģʽ��
	private static String select = "SELECT * FROM";// ��ѯsql
	private static String insert = "INSERT INTO";// ����sql
	private static String values = "VALUES";// values�ؼ���
	// private static String[] table = { "acmtinif" };// table����
	private static String[] table =new String[1];// table����
	private static List<String> insertList = new ArrayList<String>();// ȫ�ִ��insertsql�ļ�������
	//private static String filePath = "D://createSql/bak/";// ����·�� �������ݵ��ļ�

	/**
	 * * �������ݿ�� *
	 * 
	 * @param args
	 * @throws SQLException
	 */

	public static void main(String[] args) throws Exception {		
		String tableName = "acmtinif";
		startBak(tableName);	
	}

	public static void startBak(String tableName) throws Exception {
		System.out.println("----------------------------------------------------------------");
		System.out.println("����ԭ���ݿ�ʼ��������"+tableName);
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
		tm_smp = sdf.format(new Date());
		dateStr = tm_smp.substring(0, 14);
		
		table[0] = tableName;

		List<String> listSQL = new ArrayList<String>();
		// String devurl ="jdbc:oracle:thin:@192.168.9.49:1521:sitdb" ;
		String siturl = Constants.SITURL;
		String driver = Constants.DRIVER;
		String user = "";
		String password = "";
		if (tableName.substring(0, 3).equals("act")) {
			user = Constants.ACTUSER;
			password = Constants.ACTPASS;
		} else {
			user = Constants.ACMUSER;
			password = Constants.ACMPASS;
		}

		connectSQL(driver, siturl, user, password);// �������ݿ�
		listSQL = createSQL();// ������ѯ���
		System.out.println(listSQL);
		executeSQL(conn, sm, listSQL);// ִ��sql��ƴװ
		createFile(tableName);// �����ļ�
	}

	/** * ����insert��䲢�������� */
	private static void createFile(String tableName) {
		ExportUntils.createFolder(Constants.BAKFILEPATH) ;//�����ļ���
		
		String fPath = Constants.BAKFILEPATH + tableName + "_" + dateStr + ".sql";
		System.out.println("�����ļ�·����" + fPath);
		System.out.println("�����ļ���д������...");
		File file = new File(fPath);
		if (!file.exists()) {
			try {
				file.createNewFile();
			} catch (Exception e) {
				System.out.println("�����ļ�ʧ�ܣ���");
				e.printStackTrace();
			}
		}
		FileWriter fw = null;

		BufferedWriter bw = null;
		try {
			fw = new FileWriter(file);
			bw = new BufferedWriter(fw);
			if (insertList.size() > 0) {
				for (int i = 0; i < insertList.size(); i++) {
					bw.append(insertList.get(i));
					bw.append("\n");
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				bw.close();
				fw.close();
				System.out.println("--------��ϲ����"+tableName+"������ɣ�--------");
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	/**
	 * * ƴװ��ѯ��� *
	 * 
	 * @return ����select����
	 */
	private static List<String> createSQL() {
		List<String> listSQL = new ArrayList<String>();
		for (int i = 0; i < table.length; i++) {
			StringBuffer sb = new StringBuffer();
			sb.append(select).append("  ")// .append(schema).append(".")
					.append(table[i]);
			listSQL.add(sb.toString());
		}
		return listSQL;
	}

	/**
	 * * �������ݿ� ����statement���� *
	 * 
	 * @param driver
	 * @param url
	 * @param UserName
	 * @param Password
	 */
	public static void connectSQL(String driver, String url, String UserName,
			String Password) {
		try {
			Class.forName(driver).newInstance();
			conn = DriverManager.getConnection(url, UserName, Password);
			sm = conn.createStatement();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * * ִ��sql�����ز���sql
	 * 
	 * @param conn
	 * @param sm
	 * 
	 * @param listSQL
	 * @throws SQLException
	 */
	public static void executeSQL(Connection conn, Statement sm, List listSQL)
			throws SQLException {
		List<String> insertSQL = new ArrayList<String>();
		ResultSet rs = null;
		try {
			rs = getColumnNameAndColumeValue(sm, listSQL, rs);
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			rs.close();
			sm.close();
			conn.close();
		}
	}

	/**
	 * * ��ȡ��������ֵ *
	 * 
	 * @param sm
	 * @param listSQL
	 * @param rs
	 * @return
	 * @throws SQLException
	 */
	private static ResultSet getColumnNameAndColumeValue(Statement sm,
			List listSQL, ResultSet rs) throws SQLException {
		if (listSQL.size() > 0) {
			for (int j = 0; j < listSQL.size(); j++) {
				String sql = String.valueOf(listSQL.get(j));
				rs = sm.executeQuery(sql);
				ResultSetMetaData rsmd = rs.getMetaData();
				int columnCount = rsmd.getColumnCount();
				//System.out.println("columnCount="+columnCount);
				while (rs.next()) {
					StringBuffer ColumnName = new StringBuffer();
					StringBuffer ColumnValue = new StringBuffer();
					for (int i = 1; i <= columnCount; i++) {
						String value = rs.getString(i).trim();
						if ("".equals(value)) {
							value = " ";
						}
						if (i == 1 || i == columnCount) {
							if (Types.CHAR == rsmd.getColumnType(i)
									|| Types.VARCHAR == rsmd.getColumnType(i)
									|| Types.LONGVARCHAR == rsmd
											.getColumnType(i)) {
								ColumnValue.append("'").append(value)
										.append("',");
							} else if (Types.SMALLINT == rsmd.getColumnType(i)
									|| Types.INTEGER == rsmd.getColumnType(i)
									|| Types.BIGINT == rsmd.getColumnType(i)
									|| Types.FLOAT == rsmd.getColumnType(i)
									|| Types.DOUBLE == rsmd.getColumnType(i)
									|| Types.NUMERIC == rsmd.getColumnType(i)
									|| Types.DECIMAL == rsmd.getColumnType(i)) {
								ColumnValue.append(value).append(",");
							} else if (Types.DATE == rsmd.getColumnType(i)
									|| Types.TIME == rsmd.getColumnType(i)
									|| Types.TIMESTAMP == rsmd.getColumnType(i)) {
								ColumnValue.append("timestamp'").append(value)
										.append("',");
							} else {
								ColumnValue.append(value).append(",");
							}
							if(i == 1){
								ColumnName.append(rsmd.getColumnName(i));
							}else{
								ColumnName.append(",").append(rsmd.getColumnName(i));
								//System.out.println(i+"��"+ColumnName.toString());							
								ColumnValue.deleteCharAt(ColumnValue.length()-1) ;//ȥ�����һ������						
								//System.out.println(i+"��"+ColumnValue.toString());
							}
						} else {
							ColumnName.append("," + rsmd.getColumnName(i));
							if (Types.CHAR == rsmd.getColumnType(i)
									|| Types.VARCHAR == rsmd.getColumnType(i)
									|| Types.LONGVARCHAR == rsmd
											.getColumnType(i)) {
								ColumnValue.append("'").append(value)
										.append("'").append(",");
							} else if (Types.SMALLINT == rsmd.getColumnType(i)
									|| Types.INTEGER == rsmd.getColumnType(i)
									|| Types.BIGINT == rsmd.getColumnType(i)
									|| Types.FLOAT == rsmd.getColumnType(i)
									|| Types.DOUBLE == rsmd.getColumnType(i)
									|| Types.NUMERIC == rsmd.getColumnType(i)
									|| Types.DECIMAL == rsmd.getColumnType(i)) {
								ColumnValue.append(value).append(",");
							} else if (Types.DATE == rsmd.getColumnType(i)
									|| Types.TIME == rsmd.getColumnType(i)
									|| Types.TIMESTAMP == rsmd.getColumnType(i)) {
								ColumnValue.append("timestamp'").append(value)
										.append("',");
							} else {
								ColumnValue.append(value).append(",");
							}
						}
						//System.out.println(i+"��"+ColumnName.toString());
						//System.out.println(i+"��"+ColumnValue.toString());
					}
					//System.out.println(ColumnName.toString());
					//System.out.println(ColumnValue.toString());
					insertSQL(ColumnName, ColumnValue);
				}
			}
		}
		return rs;
	}

	/**
	 * * ƴװinsertsql �ŵ�ȫ��list���� *
	 * 
	 * @param ColumnName
	 * @param ColumnValue
	 * 
	 * */
	private static void insertSQL(StringBuffer ColumnName,
			StringBuffer ColumnValue) {
		for (int i = 0; i < table.length; i++) {
			StringBuffer insertSQL = new StringBuffer();
			insertSQL.append(insert)
					.append(" ")
					// .append(schema).append(".")
					.append(table[i]).append("(").append(ColumnName.toString())
					.append(")").append(values).append("(")
					.append(ColumnValue.toString()).append(");");
			insertList.add(insertSQL.toString());
			//System.out.println(insertSQL.toString());
		}
	}
}