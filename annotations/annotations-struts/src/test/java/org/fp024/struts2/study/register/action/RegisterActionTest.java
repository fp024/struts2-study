package org.fp024.struts2.study.register.action;

import com.opensymphony.xwork2.ActionProxy;
import com.opensymphony.xwork2.ActionSupport;
import org.apache.struts2.StrutsSpringJUnit5TestCase;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;

import javax.transaction.Transactional;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;

/** StrutsSpringJUnit5TestCase<테스트할 액션 클래스> 를 상속 받아 테스트 클래스를 만듦. */
@SpringJUnitConfig(locations = {"classpath:applicationContext.xml"})
class RegisterActionTest extends StrutsSpringJUnit5TestCase<RegisterAction> {
  @DisplayName("validate() 메서드의 검증 통과 확인")
  @Transactional // DB에 저장이 되는 로직이기 때문에 롤백을 위해 @Transactional 어노테이션을 붙여준다.
  @Test
  void testExecuteValidationPasses() throws Exception {
    // form에 대응되는 파라미터 값 설정
    request.setParameter("personBean.firstName", "Bruce");
    request.setParameter("personBean.lastName", "Phillips");
    request.setParameter("personBean.email", "bphillips@ku.edu");
    request.setParameter("personBean.age", "19");

    ActionProxy actionProxy = getActionProxy("/register.action");

    RegisterAction action = (RegisterAction) actionProxy.getAction();
    assertThat(action, is(notNullValue()));

    String result = actionProxy.execute();
    assertThat(result, is(ActionSupport.SUCCESS));
  }

  @DisplayName("validate() 메서드의 검증 실패 확인 - 나이가 18세 미만")
  @Test
  void testExecuteValidationFailsAgeToYoung() throws Exception {
    request.setParameter("personBean.firstName", "Bruce");
    request.setParameter("personBean.lastName", "Phillips");
    request.setParameter("personBean.email", "bphillips@ku.edu");
    request.setParameter("personBean.age", "17"); // 18세 이상이 아니여서 validate() 에 실패해야한다.

    ActionProxy actionProxy = getActionProxy("/register.action");

    RegisterAction action = (RegisterAction) actionProxy.getAction();
    assertThat(action, is(notNullValue()));

    String result = actionProxy.execute();
    assertThat(result, is(ActionSupport.INPUT));
  }

  @DisplayName("validate() 메서드의 검증 실패 확인 - firstName 입력 누락")
  @Test
  void testExecuteValidationFailsMissingFirstName() throws Exception {
    request.setParameter("personBean.lastName", "Phillips");
    request.setParameter("personBean.email", "bphillips@ku.edu");
    request.setParameter("personBean.age", "19");

    ActionProxy actionProxy = getActionProxy("/register.action");

    RegisterAction action = (RegisterAction) actionProxy.getAction();
    assertThat(action, is(notNullValue()));

    String result = actionProxy.execute();
    assertThat(result, is(ActionSupport.INPUT));
  }
}
