package com.kh.toy.member.model.service;

import java.net.URLEncoder;
import java.sql.Connection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.kh.toy.common.db.JDBCTemplate;
import com.kh.toy.common.http.HttpConnector;
import com.kh.toy.common.http.RequestParams;
import com.kh.toy.common.mail.MailSender;
import com.kh.toy.member.model.dao.MemberDao;
import com.kh.toy.member.model.dto.Member;

//Service
//웹어플리케이션의 비지니스 로직 작성
//사용자의 요청을 컨트롤러로 부터 위임받아 해당 요청을 처리하기 위한 핵심적인 작업을 진행
//요청을 처리하기 위해 데이터베이스에 저장된 데이터가 필요하면 Dao에게 요청
//비지니스 로직을 Service가 담당하기 때문에 Transaction 관리도 Service가 담당.
//commit/rollback을 Service 클래스에서 처리
public class MemberService {

	private MemberDao memberDao = new MemberDao();
	private JDBCTemplate template = JDBCTemplate.getInstance();

	public Member memberAuthenticate(String userId, String password) {
		Member member = null;
		Connection conn = template.getConnection();

		try {
			member = memberDao.memberAuthenticate(userId, password, conn);

		} finally {
			template.close(conn);
		}

		return member;
	}

	public Member selectMemberById(String userId) {
		Connection conn = template.getConnection();
		Member member = null;

		try {

			member = memberDao.selectMemberById(userId, conn);

		} finally {
			template.close(conn);
		}

		return member;
	}

	public List<Member> selectMemberList() {
		Connection conn = template.getConnection();
		List<Member> memberList = null;
		try {
			memberList = memberDao.selectMemberList(conn);
		} finally {
			template.close(conn);
		}
		return memberList;
	}

	public int insertMember(Member member) {
		Connection conn = template.getConnection();
		int res = 0;
		try {
			// 회원가입 처리
			res = memberDao.insertMember(member, conn);
			// 회원가입 이후 자동 로그인 처리
			// 방금 가입한 회원의 정보를 다시 조회
			Member m = memberDao.selectMemberById(member.getUserId(), conn);
			// 다오를 통해 사용자 정보를 받아서 해당 정보로 로그인 처리 진행
			System.out.println(member.getUserId() + "의 로그인처리 로직이 동작했습니다.");
			template.commit(conn);
		} catch (Exception e) {
			template.rollback(conn);
			// 예전처럼 예외처리
			throw e;
		} finally {
			template.close(conn);
		}
		return res;
	}

	public int updateMember(Member member) {
		Connection conn = template.getConnection();
		int res = 0;
		try {
			res = memberDao.updateMember(member, conn);
			template.commit(conn);
		} catch (Exception e) {
			template.rollback(conn);
			e.printStackTrace();
		} finally {
			template.close(conn);
		}

		return res;
	}
	
	

	public int deleteMember(String userId) {
		Connection conn = template.getConnection();
		int res = 0;
		try {
			res = memberDao.deleteMember(userId, conn);
			template.commit(conn);
		} catch (Exception e) {
			template.rollback(conn);
			e.printStackTrace();
		} finally {
			template.close(conn);
		}
		return res;
	}

	public void authenticateEmail(Member member, String persistToken) {

		HttpConnector conn = new HttpConnector();
		
		String queryString = conn.urlEncodedForm(RequestParams.builder() 
				.params("mail-template", "join-auth-email")
				.params("persistToken", persistToken)
				.params("userId", member.getUserId())
				.build());
		
		
		
		
		String mailTemplate = conn.get("http://localhost:7070/mail?" + queryString);

		MailSender sender = new MailSender();

		sender.sendEmail(member.getEmail(), "환영합니다.", mailTemplate);

	}

}
