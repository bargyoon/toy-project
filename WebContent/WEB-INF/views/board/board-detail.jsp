<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<%@ include file="/WEB-INF/views/include/head.jsp" %>
<link rel="stylesheet" href="/resources/css/board/board.css">

<style type="text/css">
.info{font-size:1.3vw; border-bottom:1px solid}
.info span{padding-right: 3vw;}
.info li{font-size:0.5vw;}

.btn_section{
   padding-top:2vh;
   display:flex;
   flex-direction:row;
   justify-content:flex-end;
   width:100%;
}
.btn_section > button{margin:0 1vw 0 1vw; font-size:1.5vw;}
.wrap_board{
   overflow:hidden;
   width:70%;
   left:30%;
}

.btn_down-file{margin-left:1%;   z-index:999;}
.article_content{min-height: 50vh; border-bottom: 1px solid;}
</style>
</head>
<body>
<div class="content">
	<h2 class='tit'>게시판</h2>
	<div class='info'>
		<span>번호 : <c:out value="${datas.board.bdIdx}"/></span>
		<span>제목 : <c:out value="${datas.board.title}"/></span>
		<span>등록일 : <c:out value="${datas.board.regDate}"/></span>
		<span>작성자 : <c:out value="${datas.board.userId}"/></span>
	</div>
	<div class="info file_info">
		<ol>
		<c:forEach items="${datas.files}" var="file">
			<li><a href="${file.downloadURL}">${file.originFileName} </a></li>
		</c:forEach>
		</ol>
	</div>
	<div class="article_content">
		<pre>
			<c:out value="${datas.board.content}"></c:out>
		</pre>
	</div>
	
	
</div>


</body>
</html>