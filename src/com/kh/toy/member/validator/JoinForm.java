package com.kh.toy.member.validator;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;

import javax.servlet.ServletRequest;
import javax.servlet.http.HttpServletRequest;

import com.kh.toy.member.model.service.MemberService;

public class JoinForm {

	private String userId;
	private String password;
	private String email;
	private String tell;
	private HttpServletRequest request;
	
	private MemberService memberService = new MemberService();
	
	private Map<String,String>failedValidation = new HashMap<String,String>();
	
	public JoinForm(ServletRequest request) {
		this.request = (HttpServletRequest) request;
		this.userId = request.getParameter("userId");
		this.password = request.getParameter("password");
		this.tell= request.getParameter("tell");
		this.email = request.getParameter("email");
	}
	
	public boolean test() {
		boolean isFailed = false;
		
		//사용자 아이디가 DB에 이미 존재하는 지 확인
		if(memberService.selectMemberById(userId) != null || userId.equals("")) {
			failedValidation.put("userId",userId);
			System.out.println("아이디");
			isFailed = true;
		}
		
		//비밀번호가 영어, 숫자, 특수문자 조합의 8자리 이상의 문자열인지 확인
		if(!Pattern.matches("(?=.*[a-zA-Z])(?=.*[0-9])(?=.*[^a-zA-Zㄱ-힣0-9]).{8,}", password)) {
			failedValidation.put("password",password);
			System.out.println("뭐가 아닌거지");
			isFailed = true;
		}
		
		
		//전화번호가 숫자로만 이루어져 있는 지 확인
		if(!Pattern.matches("\\d{9,11}", tell)) {
			failedValidation.put("tell",tell);
			isFailed = true;
			System.out.println("숫자");
		}
		
		
		
		if(isFailed) {
			request.getSession().setAttribute("joinValid", failedValidation);
			request.getSession().setAttribute("joinForm", this);
			return false;
		}else {
			request.getSession().removeAttribute("joinValid");
			request.getSession().removeAttribute("joinForm");
			return true;
		}
	}
	
	

	public String getUserId() {
		return userId;
	}

	public String getPassword() {
		return password;
	}

	public String getEmail() {
		return email;
	}

	public String getTell() {
		return tell;
	}
	
	
	
}
