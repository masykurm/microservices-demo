package com.ata.microservice.representation;

public class CommonResponse {
	
	private String errCode;
	private String errMessage;
	private String errDescription;
	private String data;
	
	
	public String getErrCode() {
		return errCode;
	}
	public void setErrCode(String errCode) {
		this.errCode = errCode;
	}
	public String getErrMessage() {
		return errMessage;
	}
	public void setErrMessage(String errMessage) {
		this.errMessage = errMessage;
	}
	public String getErrDescription() {
		return errDescription;
	}
	public void setErrDescription(String errDescription) {
		this.errDescription = errDescription;
	}
	public String getData() {
		return data;
	}
	public void setData(String data) {
		this.data = data;
	}
	
}
