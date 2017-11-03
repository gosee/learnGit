package com.sql.tools;

import java.util.HashMap;
import java.util.List;


import org.apache.commons.lang.StringUtils;

import com.hisun.atc.common.HiATCUtils;
import com.hisun.atc.common.HiAmt;
import com.hisun.database.HiDataBaseUtil;
import com.hisun.exception.HiException;
import com.hisun.message.HiETF;
import com.hisun.message.HiMessageContext;

/**
 * @description 账户余额建立
 * @author liaojie
 * 
 */
public class CreateAccountBalance {

	public static boolean execute(HiMessageContext ctx, String acTyp,
			String acNo, String capTyp, String acOrg, String ccy) throws HiException {

		HiETF GWA = HiATCUtils.getGWA(ctx);
		int retCod = 0;
		if (StringUtils.isBlank(acTyp)) {
			 
			return false;
		}
		if (StringUtils.isBlank(acNo)) {
			 
			return false;
		}
		if (StringUtils.isBlank(capTyp)) {
			 
			return false;
		}
		if (StringUtils.isBlank(acOrg)) {
		 
			return false;
		}
		if (StringUtils.isBlank(ccy)) {
		 
			return false;
		}
		return true;
	}
}
