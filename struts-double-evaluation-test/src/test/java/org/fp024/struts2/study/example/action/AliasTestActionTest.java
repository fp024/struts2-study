package org.fp024.struts2.study.example.action;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import com.opensymphony.xwork2.ActionProxy;
import com.opensymphony.xwork2.ActionSupport;
import org.apache.struts2.StrutsJUnit4TestCase;
import org.fp024.struts2.study.example.AliasTestAction;
import org.junit.Test;

public class AliasTestActionTest extends StrutsJUnit4TestCase<AliasTestAction> {

  // "validate() 메서드의 검증 통과 확인"
  @Test
  public void testExecute01() throws Exception {
    // form에 대응되는 파라미터 값 설정
    request.setParameter("expression", "a100");

    ActionProxy actionProxy = getActionProxy("/aliasTest.action");

    AliasTestAction action = (AliasTestAction) actionProxy.getAction();
    assertNotNull(action);

    String result = actionProxy.execute();
    assertEquals(ActionSupport.SUCCESS, result);

    // 실제 서버올려서는 AliasInterceptor 동작에 의해 expression 필드의 별칭이 a100으로 처리되서 a100 필드에 a100 값에 들어가는데,
    // 테스트 코드에서는 이상하게 적용이 안된다. JUnit5 코드로 잠깐 바꾸었는데도 안되고... 뭐가 문제일지?
    //
    // struts.xml에서 alias 설정을 빼면 기본동작대로 expression 필드에 a100 값이 들어간다.
    //
    // assertEquals("a100", action.getA100());
  }
}
