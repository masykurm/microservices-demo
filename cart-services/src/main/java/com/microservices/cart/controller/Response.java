package com.microservices.cart.controller;

public class Response {

	private String code;
	private String message;
	public final static String SUCCESS="001";
	public final static String ERROR="002";
	
	public Response(String code, String message){
		this.code= code;
		this.message=message;
	}
	
	public String getCode() {
		return code;
	}
	public void setCode(String code) {
		this.code = code;
	}
	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
	}
		
	
}

