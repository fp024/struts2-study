<%@ page contentType="text/html; charset=UTF-8" %>
<%@ taglib prefix="s" uri="/struts-tags" %>
<html>
  <head>
    <title>
      <s:text name="title.message" />
    </title>
  </head>

  <body>
    <h1>AliasInterceptor 이중 평가 테스트</h1>
    <form action="aliasTest.action" method="post">
      <textarea rows="10" cols="100" name="expression">a100</textarea>
      <button type="submit">제출</button>
    </form>

    <h4>expression 필드로 전달된 값</h4>
    <textarea rows="2" cols="100">${expression}</textarea>

    <h4>a100 필드로 전달된 값</h4>
    <textarea rows="2" cols="100">${a100}</textarea>

  </body>
</html>