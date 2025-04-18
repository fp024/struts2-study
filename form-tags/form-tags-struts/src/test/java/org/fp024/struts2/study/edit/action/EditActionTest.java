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
  }
}
