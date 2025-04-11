<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html lang="en">
<head>
  <meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
  <title>Register</title>
</head>
<body>
<h3>Register for a prize by completing this form.</h3>

<s:form id="registerForm" action="register-submit">
  <s:textfield name="personBean.firstName" label="First name" />
  <s:textfield name="personBean.lastName" label="Last name" />
  <s:textfield name="personBean.email"  label ="Email"/>
  <s:textfield name="personBean.age"  label="Age"  />
  <s:checkbox name="options[0]" fieldValue="1" label="1" />
  <s:checkbox name="options[1]" fieldValue="2" label="2" />
  <s:checkbox name="options[2]" fieldValue="3" label="3" />
  <s:submit />
  <s:submit type="button" value="Cancel" onclick="changeFormAction('register-cancel')" />
</s:form>
</body>
<script>
  function changeFormAction(actionValue) {
    document.getElementById('registerForm').action = actionValue;
    document.getElementById('registerForm').submit();
  }
</script>
</html>