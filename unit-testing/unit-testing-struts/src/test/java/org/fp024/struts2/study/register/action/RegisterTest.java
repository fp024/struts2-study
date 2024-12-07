package org.fp024.struts2.study.register.action;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import org.apache.struts2.ActionProxy;
import org.apache.struts2.ActionSupport;
import org.apache.struts2.StrutsJUnit5TestCase;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

/** StrutsJUnit5TestCase<테스트할 액션 클래스> 를 상속 받아 테스트 클래스를 만듦. */
class RegisterTest extends StrutsJUnit5TestCase<Register> {

  @DisplayName("validate() 메서드의 검증 통과 확인")
  @Test
  void testExecuteValidationPasses() throws Exception {
    // form에 대응되는 파라미터 값 설정
    request.setParameter("personBean.firstName", "Bruce");
    request.setParameter("personBean.lastName", "Phillips");
    request.setParameter("personBean.email", "bphillips@ku.edu");
    request.setParameter("personBean.age", "19");

    ActionProxy actionProxy = getActionProxy("/register.action");

    Register action = (Register) actionProxy.getAction();
    assertNotNull(action, "The action is null but should not be.");

    String result = actionProxy.execute();
    assertEquals(
        ActionSupport.SUCCESS,
        result,
        "The execute method did not return " + ActionSupport.SUCCESS + " but should have.");
  }

  @DisplayName("validate() 메서드의 검증 실패 확인 - 나이가 18세 미만")
  @Test
  void testExecuteValidationFailsAgeToYoung() throws Exception {
    request.setParameter("personBean.firstName", "Bruce");
    request.setParameter("personBean.lastName", "Phillips");
    request.setParameter("personBean.email", "bphillips@ku.edu");
    request.setParameter("personBean.age", "17"); // 18세 이상이 아니여서 validate() 에 실패해야한다.

    ActionProxy actionProxy = getActionProxy("/register.action");
    Register action = (Register) actionProxy.getAction();

    assertNotNull(action, "The action is null but should not be.");

    String result = actionProxy.execute();
    assertEquals(
        ActionSupport.INPUT,
        result,
        "The execute method did not return " + ActionSupport.INPUT + " but should have.");
  }

  @DisplayName("validate() 메서드의 검증 실패 확인 - firstName 입력 누락")
  @Test
  void testExecuteValidationFailsMissingFirstName() throws Exception {
    request.setParameter("personBean.lastName", "Phillips");
    request.setParameter("personBean.email", "bphillips@ku.edu");
    request.setParameter("personBean.age", "19");

    ActionProxy actionProxy = getActionProxy("/register.action");
    Register action = (Register) actionProxy.getAction();
    assertNotNull(action, "The action is null but should not be.");
    String result = actionProxy.execute();

    assertEquals(
        ActionSupport.INPUT,
        result,
        "The execute method did not return " + ActionSupport.INPUT + " but should have.");
  }
}
