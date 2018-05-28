package com.hxy.core.smsCode.service;

import com.hxy.core.smsCode.entity.SmsCode;


public interface SmsCodeService {
	/**
	 * 通过电话号码和验证码获取短信验证码对象
	 * 
	 * @param telId
	 * @param code
	 * @return
	 */
	public SmsCode selectByTelId(String telId,String code);
}
