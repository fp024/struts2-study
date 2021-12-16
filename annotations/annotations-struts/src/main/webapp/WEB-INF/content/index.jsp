<!DOCTYPE html>
<%@ page contentType="text/html; charset=UTF-8" %>
<%@ taglib prefix="s" uri="/struts-tags" %>
<html lang="ko">
<head>
    <meta charset="UTF-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <title>Basic Struts 2 Application - Welcome</title>
</head>
<body>
<h1>Welcome To Struts 2!</h1>

<p><a href="<s:url action='hello'  />" >Get your hello.</a></p>
<p><a href="<s:url action='register-input'  />" > Register for the drawing.</a></p>
<p><a href="<s:url action="index" namespace="config-browser" />">Launch the configuration browser</a></p>

</body>
</html>