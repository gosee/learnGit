package com.sql.tools;

import org.apache.commons.lang.StringUtils;

import com.hisun.atc.common.HiAmt;
import com.hisun.crypt.mac.CryptUtilImpl;
import com.hisun.exception.HiException;

/**
 * @description 账户余额tag生成及校验
 * @author liaojie
 *
 */
public class BalTagUtils {

	public static boolean checkBalanceTag(String ac, String capTyp, HiAmt bal,
			String tag) throws HiException {
		String balanceTag;
		String amt = bal.add(new HiAmt("0.00")).toString();//将金额格式为小数点后2位
		CryptUtilImpl crp = new CryptUtilImpl();
		String cryptSeed = ac + capTyp + amt;
		balanceTag = crp.cryptMd5(cryptSeed, "chinaunicom");
		//余额tag校验先注释
		/*if (!StringUtils.equals(balanceTag, tag)) {
			return false;
		}*/
		return true;
	}

	public static String createBalanceTag(String ac, String capTyp, HiAmt bal)
			throws HiException {
		String amt = bal.add(new HiAmt("0.00")).toString();
		CryptUtilImpl crp = new CryptUtilImpl();
		String cryptSeed = ac + capTyp + amt;
		return crp.cryptMd5(cryptSeed, "chinaunicom");
	}
}
