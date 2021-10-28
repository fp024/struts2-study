<!DOCTYPE html>
<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<html>
<head>
  <meta charset="UTF-8">
  <title>Exception Handling - Security Error</title>
</head>
<body>
<h4>There has been a security error.</h4>

<p> Please contact technical support with the following information:</p>

<!--
    Struts2의 예외 인터셉터에 의해 만들어진 exception과 exceptionStack 빈 프로퍼티.
    (관련해서 86쪽을 보라고 하는데... 어떤 문서의 86쪽인지 모르겠다.)
-->
<h4>Exception Name: <s:property value="exception"/></h4>
<h4>Exception Details: <s:property value="exceptionStack"/></h4>

<p><a href="index.jsp">Return to the home page.</a></p>
</body>
</html>
