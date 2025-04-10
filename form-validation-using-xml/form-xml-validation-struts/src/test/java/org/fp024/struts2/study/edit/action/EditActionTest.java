package org.fp024.struts2.study.edit.action;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import jakarta.servlet.ServletException;
import java.io.UnsupportedEncodingException;
import org.apache.struts2.ActionProxy;
import org.apache.struts2.action.Action;
import org.apache.struts2.dispatcher.mapper.ActionMapping;
import org.apache.struts2.junit.StrutsJUnit5TestCase;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

/** StrutsJUnit5TestCase<테스트할 액션 클래스> 를 상속 받아 테스트 클래스를 만듦. */
class EditActionTest extends StrutsJUnit5TestCase<EditAction> {
  @Test
  void getActionMapping() {
    ActionMapping mapping = getActionMapping("/edit.action");
    assertNotNull(mapping);
    assertEquals("/", mapping.getNamespace());
    assertEquals("edit", mapping.getName());
  }

  @Test
  void getActionProxy() throws Exception {
    // getActionProxy() 호출 전에 파라미터 설정
    initRequestParameter();

    ActionProxy proxy = getActionProxy("/save.action");
    assertNotNull(proxy);

    EditAction action = (EditAction) proxy.getAction();
    assertNotNull(action);

    String result = proxy.execute();
    assertEquals(Action.SUCCESS, result);
    assertEquals("Van", action.getPersonBean().getLastName());
    assertEquals("Fleet", action.getPersonBean().getFirstName());
    assertEquals("football", action.getPersonBean().getSport());
    assertEquals("male", action.getPersonBean().getGender());
    assertEquals("FL", action.getPersonBean().getResidency());
    assertTrue(action.getPersonBean().isOver21());
    assertArrayEquals(new String[] {"Ford", "Nissan"}, action.getPersonBean().getCarModels());
    assertEquals("noreply@apache.org", action.getPersonBean().getEmail());
    assertEquals("012-123-1234", action.getPersonBean().getPhoneNumber());
    assertEquals(35, action.getPersonBean().getAge());
  }

  @DisplayName("필수 입력값인 email만 제거해서 검증이 실패하도록 함")
  @Test
  void getActionProxy_MissingEmail() throws Exception {
    // getActionProxy() 호출 전에 파라미터 설정
    initRequestParameter();
    request.removeParameter("personBean.email");

    ActionProxy proxy = getActionProxy("/save.action");
    assertNotNull(proxy);

    EditAction action = (EditAction) proxy.getAction();
    assertNotNull(action);

    String result = proxy.execute();
    assertEquals(Action.INPUT, result);
  }

  @Test
  void getValueFromStack() throws ServletException, UnsupportedEncodingException {
    initRequestParameter();
    executeAction("/save.action");
    String firstName = (String) findValueAfterExecute("personBean.firstName");
    assertEquals("Fleet", firstName);
  }

  private void initRequestParameter() {
    request.setParameter("personBean.firstName", "Fleet");
    request.setParameter("personBean.lastName", "Van");
    request.setParameter("personBean.sport", "football");
    request.setParameter("personBean.gender", "male");
    request.setParameter("personBean.residency", "FL");
    request.setParameter("personBean.over21", "true");
    request.setParameter("personBean.carModels", "Ford", "Nissan");
    request.setParameter("personBean.email", "noreply@apache.org");
    request.setParameter("personBean.phoneNumber", "012-123-1234"); // [\d{3}-\d{3}-\d{4}]
    request.setParameter("personBean.age", "35");
  }
}
