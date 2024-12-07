package org.fp024.struts2.study.edit.action;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import org.apache.struts2.ActionProxy;
import org.apache.struts2.StrutsJUnit5TestCase;
import org.apache.struts2.action.Action;
import org.apache.struts2.dispatcher.mapper.ActionMapping;
import org.junit.jupiter.api.Test;

/** StrutsJUnit5TestCase<테스트할 액션 클래스> 를 상속 받아 테스트 클래스를 만듦. */
class CheckBoxTestActionTest extends StrutsJUnit5TestCase<CheckBoxTestAction> {
  @Test
  void getActionMapping() {
    ActionMapping mapping = getActionMapping("/checkboxSave.action");
    assertNotNull(mapping);
    assertEquals("/", mapping.getNamespace());
    assertEquals("checkboxSave", mapping.getName());
  }

  @Test
  void getActionProxy_pass_false() throws Exception {
    // getActionProxy() 호출 전에 파라미터 설정
    // 명시적 false 전송
    request.setParameter("aaaChecked", "false");

    ActionProxy proxy = getActionProxy("/checkboxSave.action");
    assertNotNull(proxy);

    CheckBoxTestAction action = (CheckBoxTestAction) proxy.getAction();
    assertNotNull(action);

    String result = proxy.execute();
    assertEquals(Action.NONE, result);
  }

  @Test
  void getActionProxy_pass_none() throws Exception {
    // getActionProxy() 호출 전에 파라미터 설정
    // 파라미터 설정 없음

    ActionProxy proxy = getActionProxy("/checkboxSave.action");
    assertNotNull(proxy);

    CheckBoxTestAction action = (CheckBoxTestAction) proxy.getAction();
    assertNotNull(action);

    String result = proxy.execute();
    assertEquals(Action.NONE, result);
  }

  @Test
  void getActionProxy_pass_true() throws Exception {
    // getActionProxy() 호출 전에 파라미터 설정
    // true 전송
    request.setParameter("aaaChecked", "true");

    ActionProxy proxy = getActionProxy("/checkboxSave.action");
    assertNotNull(proxy);

    CheckBoxTestAction action = (CheckBoxTestAction) proxy.getAction();
    assertNotNull(action);

    String result = proxy.execute();
    assertEquals(Action.NONE, result);
  }
}
