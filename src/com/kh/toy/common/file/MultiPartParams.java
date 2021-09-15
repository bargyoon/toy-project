package com.kh.toy.common.file;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.management.RuntimeErrorException;
import javax.servlet.http.HttpServletRequest;



public class MultiPartParams {
	private Map<String,List> params;
	
	
	
	public MultiPartParams(Map<String,List> params) {
		this.params = params;
	}
	
	public String getParameter(String name) {
		if(name.equals("com.kh.toy.files")) {
			throw new RuntimeException("com.kh.toy.files는 사용할 수 없는 파라미터 명입니다. ");
		}
		
		System.out.println(">>>" + params);
		return (String) params.get(name).get(0);
	}
	
	public String[] getParameterValues(String name) {
		
		if(name.equals("com.kh.toy.files")) {
			throw new RuntimeException("com.kh.toy.files는 사용할 수 없는 파라미터 명입니다. ");
		}
		List<String> res = params.get(name);
		return res.toArray(new String[res.size()]);
	}
	
	public List<FileDTO> getFilesInfo(){
		return params.get("com.kh.toy.files");
	}
	
	
}
