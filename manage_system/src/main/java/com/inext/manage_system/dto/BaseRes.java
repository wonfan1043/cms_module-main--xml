package com.inext.manage_system.dto;

public class BaseRes {

    private int code;

	private String message;

	public BaseRes() {
		super();
	}

	public BaseRes(int code, String message) {
		super();
		this.code = code;
		this.message = message;
	}

	
	public BaseRes(int code) {
		super();
		this.code = code;
	}


	public int getCode() {
		return code;
	}

	public void setCode(int code) {
		this.code = code;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}
}
