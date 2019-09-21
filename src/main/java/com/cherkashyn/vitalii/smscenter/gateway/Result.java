package com.cherkashyn.vitalii.smscenter.gateway;

import org.simpleframework.xml.Element;
import org.simpleframework.xml.Root;

@Root(name="result", strict=false)
public class Result {
	@Element(name="id", required=false)
	private Integer id;
	
	@Element(name="error_code", required=false)
	private Integer errorCode;

	@Element(name="error", required=false)
	private String error;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Integer getErrorCode() {
		return errorCode;
	}

	public void setErrorCode(Integer errorCode) {
		this.errorCode = errorCode;
	}

	public String getError() {
		return error;
	}

	public void setError(String error) {
		this.error = error;
	}
	
}
