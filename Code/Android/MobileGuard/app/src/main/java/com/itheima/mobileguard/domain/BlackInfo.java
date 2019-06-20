package com.itheima.mobileguard.domain;

public class BlackInfo {
	private String number;
	/**
	 * 拦截模式 0不拦截 1全部拦截 2短信拦截 3电话拦截
	 */
	private String mode;
	public String getNumber() {
		return number;
	}
	public void setNumber(String number) {
		this.number = number;
	}
	public String getMode() {
		return mode;
	}
	public void setMode(String mode) {
		if("1".equals(mode) || "2".equals(mode)  || "3".equals(mode) ){
			this.mode = mode;
		}else{
			this.mode = "0";
		}
	}
	public BlackInfo(String number, String mode) {
		super();
		this.number = number;
		this.mode = mode;
	}
	public BlackInfo() {
		super();
	}
	
}
