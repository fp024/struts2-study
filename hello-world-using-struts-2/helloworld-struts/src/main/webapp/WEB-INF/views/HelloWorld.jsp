<!DOCTYPE html>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="s" uri="/struts-tags" %>
<html lang="en">
  <head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <title>Hello World!</title>
  </head>
  <body>
    <h2>property toString: <s:property value="messageStore" /></h2>
    <h2>property: <s:property value="messageStore.message" /></h2>    
    <h2>jsp: ${messageStore.message}</h2>
  </body>
</html>