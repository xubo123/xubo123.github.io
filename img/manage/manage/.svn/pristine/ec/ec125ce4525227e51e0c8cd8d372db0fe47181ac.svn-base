package com.hxy.core.contact.dao;

import java.util.List;
import java.util.Map;

import com.hxy.core.contact.entity.Contact;

public interface ContactMapper {
	
	List<Contact> query(Map<String, Object> map);

    long count(Map<String, Object> map);

    Contact getById(long id);

    void reply(Contact contact);

    void delete(List<Long> list);
}
