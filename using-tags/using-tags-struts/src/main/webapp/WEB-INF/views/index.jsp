<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="s" uri="/struts-tags" %>
<!DOCTYPE html>
<html lang="en">
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Basic Struts 2 Application - Welcome</title>
    </head>
    <body>
        <h1>Welcome To Struts 2!</h1>
        <p><a href="<s:url action='hello' encode='UTF-8'/>">Hello World</a></p>

        <s:url action="hello" var="helloLink" encode='UTF-8'>
            <s:param name="messageStore.userName">Bruce Phillips</s:param>
        </s:url>

        <p><a href="${helloLink}">Hello Bruce Phillips</a></p>

        <p>Get your own personal hello by filling out and submitting this form.</p>

        <s:form action="hello">
            <s:textfield name="messageStore.userName" label="Your name" />
            <s:submit value="Submit" />
        </s:form>
    </body>
</html>