<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="s" uri="/struts-tags" %>
<!DOCTYPE html>
<html lang="en">
  <head>
    <meta charset="UTF-8">
    <title>Basic Struts 2 Application - Welcome</title>
  </head>
  <body>
    <h1>Welcome To Struts 2!</h1>
    <p><a href="<s:url action='hello'/>">Hello World</a></p>
    <%-- userName 쿼리 스트링이 포함된 링크 --%>
    <s:url action="hello" var="helloLink">
      <s:param name="userName">Bruce Phillips</s:param>
    </s:url>
    <p><a href="${helloLink}">Hello Bruce Phillips</a></p>

    <p>Get your own personal hello by filling out and submitting this form.</p>

    <%-- userName 필드가 포함된 폼 --%>
    <s:form action="hello">
      <s:textfield name="userName" label="%{'Your name'}" />
      <s:submit value="%{'Submit'}" />
    </s:form>
  </body>
</html>