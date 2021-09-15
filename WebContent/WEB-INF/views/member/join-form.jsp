<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<%@ include file="/WEB-INF/views/include/head.jsp" %>
<style type="text/css">

.tit{
   display:block; 
   width:150px;
}
.valid-msg{
	color:red;   
	font-size:0.5vw;
}

input{
	display: block;
	width: 300px;
}

/* tr>td:nth-child(1){
	background-color: red;
	color: white;
} */



/* 
input[type=submit]{
	width: 100%;
} */


</style>
</head>
<body>
<h1>회원 가입 양식</h1>
    <form action="/member/join" method="post" id="frm_join">
     <table>
        <tr>
           <td>ID : </td>
           <td>
                <input type="text" name="userId" id="userId" size="10" 
                <c:if test="${not empty param.err and empty joinValid.userId}">
 					value="${joinForm.userId}"
                </c:if>
                 required/>
                <span class="valid-msg" id="idCheck">
                <c:if test="${not empty joinValid.userId }">
 					이미 존재하는 아이디 입니다.
                </c:if>
                </span>
           </td>
           <td>
                <button type="button" id="btnIdCheck">check</button>
              
           </td>
        </tr>
        <tr>
           <td>PASSWORD : </td>
           <td>
                <input type="password" name="password" id="password"
                placeholder="영어,숫자,특수문자 조합의 8글자 이상의 문자열입니다." 
                <c:if test="${not empty param.err and empty joinValid.password}">
 					value="${joinForm.password}"
                </c:if>
                 required/>
                <span id="pwCheck" class="valid-msg">
                <c:if test="${not empty param.err and not empty joinValid.password}">
                	비밀번호는 영어,숫자,특수문자 조합의 8글자 이상의 문자열입니다.
                </c:if>
                </span>
           </td>
        </tr>
        <tr>
           <td>휴대폰번호 : </td>
           <td>
                <input type="tel" name="tell" id="tell" placeholder="숫자만 입력하세요" 
                <c:if test="${not empty param.err and empty joinValid.tell}">
 					value="${joinForm.tell}"
                </c:if>
                required/>
                <span id="tellCheck" class="valid-msg">
                <c:if test="${not empty param.err and not empty joinValid.tell}">
                	휴대폰 번호는 9-11자리의 숫자입니다.
                </c:if>
                </span>
           </td>
        </tr>
        <tr>
           <td>email : </td>
           <td>
                <input type="email" name="email" required/>
                <c:if test="${not empty param.err and not empty joinValid.email}">
                	value="${joinForm.email}"
                </c:if>
           </td>
        </tr>
        <tr>
          <td></td>
           <td colspan="2">
              <input type="submit" value="가입" />
           </td>
       </tr>
   </table>
   </form>

	<script type="text/javascript" src="/resources/js/member/joinForm.js"></script>




</body>
</html>