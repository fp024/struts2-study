package org.fp024.struts2.study.register.action;

import com.opensymphony.xwork2.ActionProxy;
import com.opensymphony.xwork2.ActionSupport;
import org.apache.struts2.StrutsSpringJUnit5TestCase;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;
import org.mockito.Mockito;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;

import javax.transaction.Transactional;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.mockStatic;

/** StrutsSpringJUnit5TestCase<테스트할 액션 클래스> 를 상속 받아 테스트 클래스를 만듦. */
@SpringJUnitConfig(locations = {"classpath:applicationContext.xml"})
class RegisterActionTest extends StrutsSpringJUnit5TestCase<RegisterAction> {
  @DisplayName("validate() 메서드의 검증 통과 확인")
  @Transactional // DB에 저장이 되는 로직이기 때문에 롤백을 위해 @Transactional 어노테이션을 붙여준다.
  @Test
  void testExecuteValidationPasses() throws Exception {
    // Mock 적용 외부에서 미리 기대 날짜를 만들어두자!
    LocalDateTime expectAccessTime = LocalDateTime.of(2021, 11, 13, 0, 0, 0, 0);

    // form에 대응되는 파라미터 값 설정
    request.setParameter("personBean.firstName", "Bruce");
    request.setParameter("personBean.lastName", "Phillips");
    request.setParameter("personBean.email", "bphillips@ku.edu");
    request.setParameter("personBean.age", "19");

    try (MockedStatic<LocalDateTime> mockedLocalDateTime =
        mockStatic(LocalDateTime.class, Mockito.CALLS_REAL_METHODS)) {
      /*
      (중요) LocalDateTime 을 Mock 으로 만들고 now()만 동작을 정의해 두었을 때,
       해당 실행범위 내에서 프레임워크가 LocalDateTime의 now()이외의 메서드를 사용하는 일이 있을 때...
       NPE가 발생할 수 있다.

       그래서 CALLS_REAL_METHODS 를 지정해주면 스텁되지 않은 메서드는 실제 메서드를 호출한다.

       그래도 왠만하면 Mock의 범위를 길게가져가지 않는게 나을 것 같다.
      */
      mockedLocalDateTime.when(() -> LocalDateTime.now()).thenReturn(expectAccessTime);
      ActionProxy actionProxy = getActionProxy("/register.action");

      RegisterAction action = (RegisterAction) actionProxy.getAction();
      assertNotNull(action, "The action is null but should not be.");

      String result = actionProxy.execute();

      assertEquals(
          ActionSupport.SUCCESS,
          result,
          "The execute method did not return " + ActionSupport.SUCCESS + " but should have.");

      assertEquals(expectAccessTime, action.getAccessDate());
    }
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
    RegisterAction action = (RegisterAction) actionProxy.getAction();
    assertNotNull(action, "The action is null but should not be.");
    String result = actionProxy.execute();

    assertEquals(
        ActionSupport.INPUT,
        result,
        "The execute method did not return " + ActionSupport.INPUT + " but should have.");
  }
}
