<!DOCTYPE html>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="s" uri="/struts-tags" %>
<html>
  <head>
    <meta charset="UTF-8">
    <title>Exception Handling - Hello World!</title>
  </head>
  <body>
    <h2><s:property value="messageStore.message" /></h2>
    <p>I've said hello <s:property value="helloCount" /> times!</p>
    <p><s:property value="messageStore" /></p>
    <p><a href="<s:url action='index'/>">Home Page</a></p>
    <hr>

    <s:debug/><%-- 이번 디버깅 예제의 중요 부분 --%>
  </body>
</html>