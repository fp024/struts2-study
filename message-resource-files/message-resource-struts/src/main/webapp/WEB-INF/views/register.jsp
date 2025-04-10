<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html lang="en">
<head>
  <meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
  <title>Register</title>
  <s:head />
</head>
<body>
<h3><s:text name="instructions" /></h3>

<s:form action="register">
  <s:textfield key="personBean.firstName" />
  <s:textfield key="personBean.lastName" />
  <s:textfield key="personBean.email" />
  <s:textfield key="personBean.age"/>
  <s:submit/>
</s:form>
<hr />
<s:text name="contact" />
</body>
</html>