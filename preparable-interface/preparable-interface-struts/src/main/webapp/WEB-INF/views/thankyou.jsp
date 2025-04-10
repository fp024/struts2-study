<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="s" uri="/struts-tags" %>
<!DOCTYPE html>
<html lang="en">
<head>
  <meta charset="UTF-8">
  <title>Update Successful</title>
</head>
<body>
<h1>Updated Information</h1>

<p>Your information: <s:property value="personBean"/></p>

<p><a href="<s:url action='index' />">Return to home page</a>.</p>

</body>
</html>