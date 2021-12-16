<!DOCTYPE html>
<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="s" uri="/struts-tags" %>
<html lang="ko">
<head>
    <meta charset="UTF-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <title>Basic Struts 2 Application - Hello</title>
</head>

<body>
<h3><s:property value="message"/></h3>

<p><a href="<s:url action='index'  />">Return to home page</a>.</p>

</body>
</html>