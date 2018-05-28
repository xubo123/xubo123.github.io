package com.hxy.system;

import java.util.HashMap;
import java.util.Map;

import org.apache.log4j.Logger;


import com.taobao.api.DefaultTaobaoClient;
import com.taobao.api.TaobaoClient;
import com.taobao.api.request.AlibabaAliqinFcSmsNumSendRequest;
import com.taobao.api.response.AlibabaAliqinFcSmsNumSendResponse;

public class SMSUtil {

	private static final Logger logger = Logger.getLogger(SMSUtil.class);
	
	//短信发送
	public static boolean smsSend(String recNum, Map<String, String> configMap)
	{
		try
		{
			String url 				= configMap.get("url");
			String appkey 			= configMap.get("app_key");
			String secret 			= configMap.get("secret");
			String smsType 			= configMap.get("sms_type");
			String smsFreeSignName 	= configMap.get("sms_free_sign_name");
			String smsParam 		= configMap.get("sms_param");
			String smsTemplateCode 	= configMap.get("sms_template_code");
			
			//extend //回传参数
			//smsType //短信类型，传入值请填写normal
			//smsFreeSignName //短信签名
			//smsParam //短信模板变量，传参规则{"key":"value"}
			//recNum //短信接收号码
			//smsTemplateCode //短信模板ID
			
			TaobaoClient client = new DefaultTaobaoClient(url, appkey, secret);
			AlibabaAliqinFcSmsNumSendRequest request = new AlibabaAliqinFcSmsNumSendRequest();
			request.setExtend(recNum);
			request.setSmsType(smsType);
			request.setSmsFreeSignName(smsFreeSignName);
			request.setSmsParam(smsParam);
			request.setRecNum(recNum);
			request.setSmsTemplateCode(smsTemplateCode);
			
			AlibabaAliqinFcSmsNumSendResponse response = client.execute(request);
			
			logger.info(response.getErrorCode());
			logger.info(response.getBody());
			logger.info(response.getMsg());
			
		
			
			if(response == null || response.isSuccess() == false)
			{
				return false;
			}

			
		}
		catch(Exception e)
		{
			e.fillInStackTrace();
			return false;
		}
		
		return true;
	}
	
	
	//通知消息短信发送
	public static boolean sendSMSForNotificationMessage(String recNum, Map<String, String> configMap)
	{
		try
		{
			String url 				= configMap.get("url");
			String appkey 			= configMap.get("app_key");
			String secret 			= configMap.get("secret");
			String smsType 			= configMap.get("sms_type");
			String smsFreeSignName 	= configMap.get("sms_free_sign_name");
			String smsParam 		= configMap.get("sms_param");
			String smsTemplateCode 	= configMap.get("sms_template_code");
			
			
			TaobaoClient client = new DefaultTaobaoClient(url, appkey, secret);
			AlibabaAliqinFcSmsNumSendRequest request = new AlibabaAliqinFcSmsNumSendRequest();
			request.setExtend(recNum);
			request.setSmsType(smsType);
			request.setSmsFreeSignName(smsFreeSignName);
			request.setSmsParam(smsParam);
			request.setRecNum(recNum);
			request.setSmsTemplateCode(smsTemplateCode);
			
			AlibabaAliqinFcSmsNumSendResponse response = client.execute(request);
			
			logger.info(response.getErrorCode());
			logger.info(response.getBody());
			logger.info(response.getMsg());
			
		
			
			if(response == null || response.isSuccess() == false)
			{
				return false;
			}

			
		}
		catch(Exception e)
		{
			e.fillInStackTrace();
			return false;
		}
		
		return true;
	}	
	
	
	public static void main(String[] args) 
	{
		
		String recNum = "18071065658";
		Map<String, String> configMap = new HashMap<String,String>();
		configMap.put("url","http://gw.api.taobao.com/router/rest");
		configMap.put("app_key","23330227");
		configMap.put("secret","005cd994ff7a20ecf7f4f94aa106741a");
		configMap.put("sms_type","normal");
		configMap.put("sms_free_sign_name","注册验证");
		configMap.put("sms_param","{\"code\":\"12345678\",\"product\":\"慧校友的\"}");
		configMap.put("sms_template_code","SMS_6335618");
		
		SMSUtil.smsSend(recNum, configMap);
		
	}
	

}
