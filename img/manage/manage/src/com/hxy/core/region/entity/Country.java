package com.hxy.core.region.entity;

import java.io.Serializable;

public class Country implements Serializable {

	private static final long serialVersionUID = 1L;
	private Integer id;
	private String countryName;
	private Integer orderId;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getCountryName() {
		return countryName;
	}

	public void setCountryName(String countryName) {
		this.countryName = countryName;
	}

	public Integer getOrderId() {
		return orderId;
	}

	public void setOrderId(Integer orderId) {
		this.orderId = orderId;
	}

}
