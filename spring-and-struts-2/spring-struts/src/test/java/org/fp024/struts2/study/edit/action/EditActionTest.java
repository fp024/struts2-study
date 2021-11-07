package org.fp024.struts2.study.edit.action;

import com.opensymphony.xwork2.Action;
import com.opensymphony.xwork2.ActionProxy;
import org.apache.struts2.StrutsSpringJUnit5TestCase;
import org.apache.struts2.dispatcher.mapper.ActionMapping;
import org.junit.jupiter.api.Test;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;

import javax.servlet.ServletException;
import java.io.UnsupportedEncodingException;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringJUnitConfig(locations = "classpath:applicationContext.xml")
class EditActionTest extends StrutsSpringJUnit5TestCase<EditAction> {

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

  /**
   * JSP를 뷰로 사용할 때는 executeAction()의 결과가 "" 이지만... freemarker를 사용하면, 원문 그대로 출력이된다.<br>
   * 그런데 ftl안에 Struts 2 태그라이브러리 정의를 어떻게 하는지 잘모르겠다. 원래 안되는건가?
   */
  @Test
  void executeAction() throws ServletException, UnsupportedEncodingException {
    String expect =
        "<!DOCTYPE html>\n"
            + "<html>\n"
            + "<head>\n"
            + "    <meta charset=\"UTF-8\">\n"
            + "    <title>Basic Struts 2 Application - Welcome</title>\n"
            + "</head>\n"
            + "<body>\n"
            + "<h1>Welcome To Struts 2!</h1>\n"
            + "\n"
            + "<p><a href='edit.action' >Edit your information</a></p>\n"
            + "</body>\n"
            + "</html>";

    String output = executeAction("/index.action");
    assertEquals(expect, output.replace("\r", ""));
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
