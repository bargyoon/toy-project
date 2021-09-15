package com.kh.toy.common.code;

public enum Config {

	//DOMAIN("http://www.pclass.com")
	DOMAIN("http://localhost:7070"),
	SMTP_AUTHENTIFICATION_ID("bargyoon@gmail.com"),
	SMTP_AUTHENTIFICATION_PASSWORD("147856aa"),
	COMPANY_EMAIL("bargyoon@gmail.com"),
	//UPLOAD_PATH("C:\\CODE\\upload\\"); //운영서버
	UPLOAD_PATH("C:\\CODE\\upload\\"); //개발서버
	
	public final String DESC;
	
	private Config(String desc) {
		this.DESC = desc;
	}
	
}
