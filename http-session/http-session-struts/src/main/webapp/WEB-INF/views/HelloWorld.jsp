<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="s" uri="/struts-tags" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>Hello World!</title>
</head>
<body>
    <h2><s:property value="messageStore.message" /></h2>
    <p>I've said hello to you <s:property value="#session.helloCount" /> times!</p>
    <p><s:property value="messageStore" /></p>
    <hr />
    <p><a href='<s:url action="index" />'>Return Home</a></p>
</body>
</html>