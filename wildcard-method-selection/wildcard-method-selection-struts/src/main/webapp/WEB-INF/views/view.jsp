<!DOCTYPE html>
<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="s" uri="/struts-tags" %>
<html>
<head>
  <meta charset="UTF-8">
  <title>View People</title>
</head>
<body>
<h3>People</h3>
<s:if test="personList.size > 0">
  <ol>
    <s:iterator value="personList">
      <li>
        <s:property value="firstName"/>

        <s:property value="lastName"/>
        <%--
            Person 앞에 호출하려는 메서드의 이름을 입력합니다.
            이 액션은 edit라는 메서드가 ActionSupport 클래스에서
            호출되도록 합니다(struts.xml 참조).
        --%>
        <s:url action="editPerson" var="editUrl">
          <s:param name="personDTO.id" value="id"/>
        </s:url>

        <s:url action="deletePerson" var="deleteUrl">
          <s:param name="personDTO.id" value="id"/>
        </s:url>

        <a href="<s:property value='#editUrl' />">Edit</a>

        <a href="<s:property value='#deleteUrl' />">Delete</a>
      </li>
    </s:iterator>
  </ol>
</s:if>

<%--
  Person 앞에 호출하려는 메서드의 이름을 입력합니다.
  이 액션은 create라는 메서드가 ActionSupport 클래스에서
  호출되도록 합니다(struts.xml 참조).
--%>
<s:url action="createPerson" var="newUrl">
  <s:param name="id" value="0"/>
</s:url>

<p><a href="<s:property value='#newUrl' />"> Create new person.</a></p>
</body>
</html>