<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="s" uri="/struts-tags" %>
<!DOCTYPE html>
<html lang="en">
<head>
  <meta charset="UTF-8">
  <title>Edit Person</title>
  <s:head/>
</head>
<body>
<s:if test="id > 0">
  <h3>Edit Person</h3>
</s:if>
<s:else>
  <h3>Create Person</h3>

</s:else>

<s:form action='saveOrUpdatePerson'>
  <p>
    <s:textfield name="personDTO.firstName" label="First name"/> <br/>
    <s:textfield name="personDTO.lastName" label="Last name"/>
  </p>
  <s:hidden name="personDTO.id"/>
  <s:submit value="Save"/>
</s:form>

</body>
</html>