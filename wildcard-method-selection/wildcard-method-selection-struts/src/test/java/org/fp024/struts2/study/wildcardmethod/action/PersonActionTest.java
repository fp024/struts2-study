package org.fp024.struts2.study.wildcardmethod.action;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;
import static org.hamcrest.Matchers.nullValue;
import static org.junit.jupiter.api.Assertions.assertThrows;

import jakarta.transaction.Transactional;
import org.apache.struts2.ActionProxy;
import org.apache.struts2.action.Action;
import org.apache.struts2.config.ConfigurationException;
import org.apache.struts2.dispatcher.mapper.ActionMapping;
import org.apache.struts2.junit.StrutsSpringJUnit5TestCase;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;

/** StrutsSpringJUnit5TestCase<테스트할 액션 클래스> 를 상속 받아 테스트 클래스를 만듦. */
@SpringJUnitConfig(locations = {"classpath:applicationContext.xml"})
class PersonActionTest extends StrutsSpringJUnit5TestCase<PersonAction> {

  @DisplayName("와일드카드 설정에 의해 접근 가능한 모든 액션 테스트")
  @ParameterizedTest
  @ValueSource(
      strings = {"Person", "createPerson", "editPerson", "saveOrUpdatePerson", "deletePerson"})
  void getActionMapping_Person_Action(String actionName) {
    ActionMapping mapping = getActionMapping("/" + actionName + ".action");
    assertThat(mapping, notNullValue());
    assertThat(mapping.getNamespace(), equalTo("/"));
    assertThat(mapping.getName(), equalTo(actionName));
  }

  @Test
  void getActionProxy_execute() throws Exception {
    ActionProxy proxy = getActionProxy("/Person.action");
    assertThat(proxy, is(notNullValue()));

    // (참고) 사용할 수 없거나 문제가 있을 때는, ActionSupport 타입 객체로 반환되서 형변환이 실패 되는것 같다.
    PersonAction action = (PersonAction) proxy.getAction();
    assertThat(action, is(notNullValue()));

    // 액션 실행
    String result = proxy.execute();
    assertThat(action.getPersonDTO(), is(nullValue()));
    assertThat(result, is(Action.SUCCESS));
  }

  @ParameterizedTest
  @ValueSource(strings = {"createPerson"})
  void getActionProxy_create(String actionName) throws Exception {
    ActionProxy proxy = getActionProxy("/" + actionName + ".action");
    assertThat(proxy, is(notNullValue()));

    PersonAction action = (PersonAction) proxy.getAction();
    assertThat(action, is(notNullValue()));

    // 액션 실행
    String result = proxy.execute();
    assertThat(action.getPersonDTO(), is(notNullValue()));
    assertThat(result, is(Action.INPUT));
  }

  /*
   runCreateThis 의 경우는 DMI 예시를 설명하신 것 같은데, 잘 안된다.
   관련해서 보안문제 때문에 다 막아둔 것 같다.
   struts.enable.DynamicMethodInvocation 를 true로 하더라도 허용되지 않은 메서드로 예외가 발생한다.
  */
  @ParameterizedTest
  @ValueSource(strings = {"person!runCreateThis"})
  void getActionProxy_runCreateThis(String actionName) {
    assertThrows(ConfigurationException.class, () -> getActionProxy("/" + actionName + ".action"));
  }

  @ParameterizedTest
  @ValueSource(strings = {"editPerson"})
  void getActionProxy_edit(String actionName) throws Exception {
    request.setParameter("personDTO.id", "1");

    ActionProxy proxy = getActionProxy("/" + actionName + ".action");
    assertThat(proxy, is(notNullValue()));

    PersonAction action = (PersonAction) proxy.getAction();
    assertThat(action, is(notNullValue()));

    // 액션 실행
    String result = proxy.execute();
    assertThat(action.getPersonDTO(), is(notNullValue()));
    assertThat(action.getPersonDTO().getFirstName(), is("Steve"));

    assertThat(result, is(Action.INPUT));
  }

  @Transactional
  @ParameterizedTest
  @ValueSource(strings = {"saveOrUpdatePerson"})
  void getActionProxy_save(String actionName) throws Exception {
    request.setParameter("personDTO.firstName", "fp024");
    request.setParameter("personDTO.lastName", "lucy");

    ActionProxy proxy = getActionProxy("/" + actionName + ".action");
    assertThat(proxy, is(notNullValue()));

    PersonAction action = (PersonAction) proxy.getAction();
    assertThat(action, is(notNullValue()));

    // 액션 실행
    String result = proxy.execute();
    assertThat(action.getPersonList().isEmpty(), is(false));
    assertThat(
        action.getPersonList().get(action.getPersonList().size() - 1).getFirstName(), is("fp024"));

    assertThat(result, is(Action.SUCCESS));
  }

  @Transactional
  @ParameterizedTest
  @ValueSource(strings = {"saveOrUpdatePerson"})
  void getActionProxy_update(String actionName) throws Exception {
    request.setParameter("personDTO.id", "3");
    request.setParameter("personDTO.firstName", "struts2");
    request.setParameter("personDTO.lastName", "xwork2");

    ActionProxy proxy = getActionProxy("/" + actionName + ".action");
    assertThat(proxy, is(notNullValue()));

    PersonAction action = (PersonAction) proxy.getAction();
    assertThat(action, is(notNullValue()));

    // 액션 실행
    String result = proxy.execute();
    assertThat(action.getPersonList().isEmpty(), is(false));
    assertThat(
        action.getPersonList().get(action.getPersonList().size() - 1).getFirstName(),
        is("struts2"));

    assertThat(result, is(Action.SUCCESS));
  }

  @Transactional
  @ParameterizedTest
  @ValueSource(strings = {"deletePerson"})
  void getActionProxy_delete(String actionName) throws Exception {
    request.setParameter("personDTO.id", "1");

    ActionProxy proxy = getActionProxy("/" + actionName + ".action");
    assertThat(proxy, is(notNullValue()));

    PersonAction action = (PersonAction) proxy.getAction();
    assertThat(action, is(notNullValue()));

    // 액션 실행
    String result = proxy.execute();
    assertThat(action.getPersonList().isEmpty(), is(false));
    assertThat(action.getPersonList().get(0).getFirstName(), is("Sue"));
    assertThat(result, is(Action.SUCCESS));
  }
}
