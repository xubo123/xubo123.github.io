package com.hxy.core.smsCode.dao;

import java.util.Map;

import com.hxy.core.smsCode.entity.SmsCode;


public interface SmsCodeMapper
{
	int addSmsCode(SmsCode smsCode);

	SmsCode selectByTelId(Map<String, Object> map);

}
