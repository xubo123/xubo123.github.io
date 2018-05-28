package com.hxy.util.mail;

import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.List;

import javax.mail.MessagingException;
import javax.mail.internet.AddressException;

public class Test {
	public static void main(String[] args) throws MalformedURLException {
		SimpleMailSender mailSender = new SimpleMailSender("xx", "xxx");
		// SimpleMailSender mailSender = MailSenderFactory.getSender1("xx",
		// "xxx","mailserver.znufe.edu.cn","25");
		List<String> list = new ArrayList<String>();
		list.add("307585689@qq.com");
		SimpleMail mail = new SimpleMail();
		mail.setSubject("你好");
		List<String> fileList = new ArrayList<String>();
		// fileList.add("http://219.140.177.108:8088/image/20150525/20150525131027_794.png");
		mail.setContent("你好");
		try {
			mailSender.send(list, null, null, mail, fileList);
		} catch (AddressException e) {
			e.printStackTrace();
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		} catch (MessagingException e) {
			e.printStackTrace();
		}
	}
}
