package com.sql.tools;

import java.io.File;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import org.apache.commons.lang.StringUtils;
import org.dom4j.Attribute;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;
/**
 * 
 * @description 用于检查错误信息码，目前代码取主干代码，连接开发环境数据库
 *
 */
public class CheckMsg {
	/*
	public static String shortPath = "D:/myeclipse_MR_workspace/trunk/acmadm";

	public static String longPath = "D:/myeclipse_MR_workspace/trunk/acmadm/app/";
	*/
	public static String shortPath = "D:/myeclipse_MR_workspace/trunk/actadm/";

	public static String longPath = "D:/myeclipse_MR_workspace/trunk/actadm/app/";
	
	public static String module = "cmm";

	/**
	 * @param args
	 */
	public static void main(String[] args) {

		String[] mods = { "act" };
		for (String module2 : mods) {
			module = module2;
			// longPath = "D:/EclipseWorkSpaces/curworkspace/cpuTwo_uat/app/";
			longPath += module + "/";
			System.out.println(module);
			getAllCtl(longPath + "/etc/SVRLST.XML");
		}

	}

	public static void getAllTransaction(String ctlPath) {
		SAXReader saxReader = new SAXReader();
		File f = new File(ctlPath);
		org.dom4j.Document doc;

		try {
			doc = saxReader.read(f);
			Element root = doc.getRootElement();
			List<Element> list = root.elements();// 得到下一级元素集合 elementIterator()
			for (Element el : list)//
			{
				List<Attribute> attributes = el.attributes();// 属性的集合
				for (Attribute attribute : attributes) {
					String attName = attribute.getName();
					String attValue = attribute.getValue();

					if (StringUtils.equals(attName, "file")) {
						if (StringUtils.equals(attValue.substring(0, 13),
								"app/" + module + "/etc/O")) {
							attValue = shortPath + attValue;
							addTransaction(attValue);
							// System.out.println("file:"+attValue);
						} else if (StringUtils.equals(attValue.substring(0, 5),
								"etc/O")) {
							attValue = longPath + attValue;
							addTransaction(attValue);
						}
					}
				}
			}

		} catch (org.dom4j.DocumentException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	public static void getAllCtl(String svrLst) {
		SAXReader saxReader = new SAXReader();
		File f = new File(svrLst);
		org.dom4j.Document doc;

		try {
			doc = saxReader.read(f);
			Element root = doc.getRootElement();
			List<Element> list = root.elements();// 得到下一级元素集合 elementIterator()
			for (Element el : list)//
			{
				// 得到people的下一级元素
				List<Element> elPros = el.elements();
				for (Element pro : elPros) {
					String elProName = pro.getName();
					String elProValue = pro.getTextTrim();
					if (StringUtils.equals(elProName, "Server")) {
						List<Attribute> serAttrs = pro.attributes();// 属性的集合
						for (Attribute attribute : serAttrs) {
							String attName = attribute.getName();
							String attValue = attribute.getValue();
							if (StringUtils.equals(attName, "config_file")) {
								if (StringUtils.equals(
										attValue.substring(0, 3), "app")) {
									attValue = shortPath + attValue;
									attValue = attValue.replace("ATR", "CTL");
									getAllTransaction(attValue);
									// System.out.println(attName + "---" +
									// attValue);
								} else if (StringUtils.equals(
										attValue.substring(0, 3), "etc")
										&& StringUtils.equals(
												attValue.substring(4, 5), "O")
										&& StringUtils
												.contains(attValue, "PUB")) {

									attValue = longPath + attValue;
									attValue = attValue.replace("ATR", "CTL");
									getAllTransaction(attValue);
									System.out.println("03" + attName + "---"
											+ attValue);
								}
							}
						}
					}
				}
			}

		} catch (org.dom4j.DocumentException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public static void getAllLst(String svrLst) {
		SAXReader saxReader = new SAXReader();
		File f = new File(svrLst);
		org.dom4j.Document doc;

		try {
			doc = saxReader.read(f);
			Element root = doc.getRootElement();
			List<Element> list0 = root.elements();// 得到下一级元素集合 elementIterator()
			for (Element e0 : list0)//
			{

				List<Element> list = e0.elements();
				for (Element el : list) {
					List<Attribute> attributes = el.attributes();// 属性的集合

					for (Attribute attribute : attributes) {
						String attName = attribute.getName();
						String attValue = attribute.getValue();
						if (StringUtils.equals(attName, "config_file")) {
							if (StringUtils.equals(attValue.substring(0, 3),
									"app")) {
								attValue = shortPath + attValue;
								getAllCtl(attValue);
								System.out.println("01" + attName + "---"
										+ attValue);
							} else if (StringUtils.equals(
									attValue.substring(0, 3), "etc")) {
								attValue = longPath + attValue;
								getAllCtl(attValue);
								System.out.println("02" + attName + "---"
										+ attValue);
							}
						}
					}
				}
			}

		} catch (org.dom4j.DocumentException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public static void addTransaction(String filePath) {
		SAXReader saxReader = new SAXReader();
		File f = new File(filePath);
		org.dom4j.Document doc;
		String tx_cd = "";
		String tx_nm = "";

		try {
			doc = saxReader.read(f);
			Element root = doc.getRootElement(); // 按照树的思想进行解析
			List<Element> list = root.elements();// 得到下一级元素集合 elementIterator()
			for (Element el : list)//
			{
				List<Attribute> attributes = el.attributes();// 属性的集合

				for (Attribute attribute : attributes) {
					String attName = attribute.getName();
					String attValue = attribute.getValue();
					if (StringUtils.equals(attName, "code"))
						tx_cd = attValue;
					else if (StringUtils.equals(attName, "desc"))
						tx_nm = attValue;
				}

				// 得到people的下一级元素
				List<Element> elPros = el.elements();
				for (Element pro : elPros) {
					listAllAttr(pro, filePath);
				}
			}

		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	public static void listAllAttr(Element e, String filePath) {

		List<Element> elPros = e.elements();
		for (Element pro : elPros) {
			String elProName = pro.getName();
			String elProValue = pro.getTextTrim();
			List<Attribute> attributes = pro.attributes();// 属性的集合
			for (Attribute attribute : attributes) {
				String attName = attribute.getName();
				String attValue = attribute.getValue();
				if (StringUtils.equals(attName, "func")) {

					if (StringUtils.equals(attValue, "PUB:CmpSetMsg")
							|| StringUtils.equals(attValue, "PUB:GWASetMsg")) {

						List<Element> msges = pro.elements();
						for (Element msge : msges) {

							List<Attribute> msgAttributes = msge.attributes();// 属性的集合
							for (Attribute msgAttribute : msgAttributes) {
								String msgName = msgAttribute.getName();
								String msgValue = msgAttribute.getValue();
								if (StringUtils.equals(msgName, "name")
										&& !StringUtils.equals(msgValue,
												"MsgCd")) {
									break;
								}
								if (StringUtils.equals(msgName, "value")
										&& !StringUtils.equals(msgValue,
												"@O.MSG_CD")
										&& !StringUtils.equals(msgValue,
												"$GWA.MSG_CD")
										&& !StringUtils.equals(
												msgValue.substring(3), "00000")) {
									if (!findMsgCdExists(msgValue)) {
										System.out.println("文件：" + filePath
												+ "" + "---" + msgValue);
									}
								}

							}
						}

					}
				}

				listAllAttr(pro, filePath);
			}
		}
	}

	public static boolean findMsgCdExists(String msgCd) {
		Connection oracle_conn = null;

		try {
			Class.forName("oracle.jdbc.driver.OracleDriver");
			/*oracle_conn = DriverManager.getConnection(
					"jdbc:oracle:thin:@192.168.9.49:1521:devdb", "acmadm",
					"acmadm");*/
			oracle_conn = DriverManager.getConnection(
					"jdbc:oracle:thin:@192.168.9.49:1521:devdb", "actadm",
					"actadm");

			PreparedStatement pState = oracle_conn
					.prepareStatement("select * from PUBTMSG where MSG_CD=?");
			pState.setString(1, msgCd);
			ResultSet rs = pState.executeQuery();
			if (rs.next()) {
				pState.close();
				oracle_conn.close();
				return true;
			} else {
				pState.close();
				oracle_conn.close();
				return false;
			}

		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return false;
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return false;
		}

	}
}
