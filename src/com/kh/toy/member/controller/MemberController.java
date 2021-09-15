package com.kh.toy.member.controller;

import java.io.IOException;
import java.util.UUID;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.kh.toy.common.exception.PageNotFoundException;
import com.kh.toy.member.model.dto.Member;
import com.kh.toy.member.model.service.MemberService;

/**
 * Servlet implementation class MemberController
 */
@WebServlet("/member/*")
public class MemberController extends HttpServlet {
	private static final long serialVersionUID = 1L;

	private MemberService memberService = new MemberService();

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public MemberController() {
		super();
		// TODO Auto-generated constructor stub
	}

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		
		
		
		
		String[] uriArr = request.getRequestURI().split("/");
		
		switch(uriArr[uriArr.length-1]) {
		case "login-form":
			loginForm(request,response);
			break;
		case "login":
			login(request,response);
			break;
		case "logout":
			logout(request,response);
			break;
		case "join-form":
			joinForm(request,response);
			break;
		case "join":
			join(request,response);
			break;
		case "join-impl":
			joinImpl(request,response);
			break;
		case "id-check":
			checkID(request,response);
			break;		
		case "mypage":
			mypage(request,response);
			break;
			
		default: throw new PageNotFoundException();
		
		}
	
	}

	private void mypage(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		request.getRequestDispatcher("/member/mypage").forward(request, response);
		
	}

	private void logout(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		request.getSession().removeAttribute("authentication");
		response.sendRedirect("/");
	}

	private void checkID(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		String userId = request.getParameter("userId");

		Member member = memberService.selectMemberById(userId);

		if (member == null) {
			response.getWriter().print("available");
		} else {
			response.getWriter().print("disable");
		}

	}

	private void join(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

		String userId = request.getParameter("userId");
		String password = request.getParameter("password");
		String tell = request.getParameter("tell");
		String email = request.getParameter("email");

		Member member = new Member();
		member.setUserId(userId);
		member.setPassword(password);
		member.setTell(tell);
		member.setEmail(email);

		String persistToken = UUID.randomUUID().toString();
		request.getSession().setAttribute("persistUser", member);
		request.getSession().setAttribute("persist-token", persistToken);

		memberService.authenticateEmail(member, persistToken);

		request.setAttribute("msg", "회원가입을 위한 이메일이 발송되었습니다.");
		request.setAttribute("url", "/member/login-form");
		request.getRequestDispatcher("/common/result").forward(request, response);

		/*
		 * String email = request.getParameter("email"); MailSender mailSender = new
		 * MailSender(); mailSender.sendEmail(email, "회원가입을 축하합니다.",
		 * "<h1>어서오세요! 환영합니다!</h1>");
		 */

	}

	private void joinImpl(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		HttpSession session = request.getSession();
		memberService.insertMember((Member) session.getAttribute("persistUser"));

		// 예전처럼 예외처리를 했을 경우 직접 request.setAttribute를 사용해 메시지를 입력해서 넣어줬다.
		// 지금과의 차이점이 무엇일까
		// 가독성의 차이. 예외상황이라는 것이 더 명확하게 보인다.

		// 같은 persistUser값이 두 번 DB에 입력되지 않도록 사용자 정보와 인증을 만료시킴
		session.removeAttribute("persistUser");
		session.removeAttribute("persist-token");
		response.sendRedirect("/member/login-form");

	}

	private void joinForm(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		request.getRequestDispatcher("/member/join-form").forward(request, response);
	}

	private void login(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String userId = request.getParameter("userId");
		String password = request.getParameter("password");

		Member member = null;

		// 1. 시스템에서 문제가 생겨서(DB가 뻗었다던데... 외부 API서버가 죽었다던가...)
		// 예외가 발생하는 경우. => 예외처리를 service 단에서 처리
		member = memberService.memberAuthenticate(userId, password);

		/*
		 * request.setAttribute("msg", "회원정보를 조회하는 도중 예외가 발생했습니다.");
		 * request.setAttribute("url", "/index");
		 * request.getRequestDispatcher("/error/result").forward(request, response);
		 * return;
		 */

		// 2. 사용자가 잘못된 아이디나 비밀번호를 입력한 경우.
		// 사용자에게 아이디나 비밀번호가 틀렸음을 알림, login-form 으로 redirect

		if (member == null) {
			response.sendRedirect("/member/login-form?err=1");
			return;
		}

		request.getSession().setAttribute("authentication", member);
		response.sendRedirect("/index");

	}

	private void loginForm(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		request.getRequestDispatcher("/member/login").forward(request, response);
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */

	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		// TODO Auto-generated method stub
		doGet(request, response);
	}

}
