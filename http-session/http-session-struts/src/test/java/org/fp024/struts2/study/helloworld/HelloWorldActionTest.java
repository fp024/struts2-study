package org.fp024.struts2.study.helloworld;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import com.opensymphony.xwork2.Action;
import com.opensymphony.xwork2.ActionProxy;
import java.util.Map;
import org.apache.struts2.StrutsSpringJUnit5TestCase;
import org.apache.struts2.dispatcher.SessionMap;
import org.apache.struts2.dispatcher.mapper.ActionMapping;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;
import org.springframework.test.util.ReflectionTestUtils;

@SpringJUnitConfig(locations = "classpath:applicationContext.xml")
class HelloWorldActionTest extends StrutsSpringJUnit5TestCase<HelloWorldAction> {
  @Test
  void testActionMapping() {
    ActionMapping mapping = getActionMapping("/hello.action");
    assertNotNull(mapping);
    assertEquals("/", mapping.getNamespace());
    assertEquals("hello", mapping.getName());
  }

  @Test
  void testIncreaseHelloCount() throws Exception {
    final String userName = "Van";

    request.setParameter("userName", userName);
    ActionProxy proxy = getActionProxy("/hello.action");
    assertNotNull(proxy);

    HelloWorldAction action = (HelloWorldAction) proxy.getAction();
    assertNotNull(action);

    String result = proxy.execute();

    assertEquals(userName, action.getUserName());
    @SuppressWarnings("unchecked")
    Map<String, Object> userSession =
        (Map<String, Object>) ReflectionTestUtils.getField(action, "userSession");
    assertNotNull(userSession);
    assertEquals(1, userSession.get("helloCount"));

    assertEquals("Hello Struts User " + userName, action.getMessageStore().getMessage());
    assertEquals(Action.SUCCESS, result);
  }

  @DisplayName("acceptableParameterName() 구현에 따라 파라미터가 액션으로 전달되지 않는지 확인한다.")
  @Test
  void testAcceptableParameterName() throws Exception {
    final String userName = "Van";
    request.setParameter("userName", userName);
    // acceptableParameterName()메서드 설정으로 session 파라미터를 전달하더라도 처리되지 않는다.
    request.setParameter("session", "not_add");
    executeAction("/hello.action");
    // 여기서의 세션은 위에 파라미터에서 설정하려한 session과는 관계가 없다.
    SessionMap sessionMap = (SessionMap) findValueAfterExecute("session");
    assertEquals(1, sessionMap.get("helloCount"));

    String message = (String) findValueAfterExecute("messageStore.message");
    assertEquals("Hello Struts User " + userName, message);
  }
}
