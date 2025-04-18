<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="s" uri="/struts-tags" %>
<!DOCTYPE html>
<html lang="en">
<head>
  <s:head/>
  <meta charset="UTF-8">
  <style type="text/css">
    .label {color:blue; font-style:normal; font-weight:bold}
</style>
  <title>Struts 2 Form Tags - Edit Person</title>
</head>
<body>
<h1>Update Information</h1>

<p>Use the form below to edit your information.</p>

<s:form action="save" method="post">
  <s:textfield key="personBean.firstName" /> 
  <s:textfield key="personBean.lastName" />
  <s:select key="personBean.sport" list="sports" />
  <s:radio key="personBean.gender" list="genders" />
  <s:select key="personBean.residency" list="states" listKey="stateAbbr" listValue="stateName" />
  <s:checkbox key="personBean.over21" />
  <s:checkboxlist key="personBean.carModels" list="carModelsAvailable" />
  <s:submit key="submit" />
</s:form>
</body>
</html>