package org.fp024.struts2.study.edit.action;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;

import jakarta.servlet.ServletException;
import java.io.UnsupportedEncodingException;
import org.apache.struts2.ActionProxy;
import org.apache.struts2.action.Action;
import org.apache.struts2.dispatcher.mapper.ActionMapping;
import org.apache.struts2.junit.StrutsJUnit5TestCase;
import org.junit.jupiter.api.Test;

/**
 * StrutsJUnit5TestCase<테스트할 액션 클래스> 를 상속 받아 테스트 클래스를 만듦. <br>
 * 이번에 hamcrest 라이브러리를 추가했으니, 해당 Matcher 메서드를 사용해보자! <br>
 * ==> 바꾸고보니 단정문이 뭔가 자연스럽게 읽히는 것 같다. ㅎㅎ
 */
class EditActionTest extends StrutsJUnit5TestCase<EditAction> {
  @Test
  void getActionMapping() {
    ActionMapping mapping = getActionMapping("/edit.action");

    assertThat(mapping, notNullValue());
    assertThat(mapping.getNamespace(), equalTo("/"));
    assertThat(mapping.getName(), equalTo("edit"));
  }

  @Test
  void getActionProxy() throws Exception {
    // getActionProxy() 호출 전에 파라미터 설정
    initRequestParameter();

    ActionProxy proxy = getActionProxy("/save.action");
    assertThat(proxy, is(notNullValue()));

    EditAction action = (EditAction) proxy.getAction();
    assertThat(action, is(notNullValue()));

    String result = proxy.execute();
    assertThat(result, is(Action.SUCCESS));

    assertThat(action.getPersonBean().getLastName(), is("Van"));
    assertThat(action.getPersonBean().getFirstName(), is("Fleet"));
    assertThat(action.getPersonBean().getSport(), is("football"));
    assertThat(action.getPersonBean().getGender(), is("male"));
    assertThat(action.getPersonBean().getResidency(), is("FL"));
    assertThat(action.getPersonBean().isOver21(), is(true));
    assertThat(action.getPersonBean().getCarModels(), is(new String[] {"Ford", "Nissan"}));
    assertThat(action.getPersonBean().getEmail(), is("noreply@apache.org"));
    assertThat(action.getPersonBean().getPhoneNumber(), is("012-123-1234"));
  }

  /*
   파라미터를 지우는것이 아니고, 빈값으로 설정하는 식으로 처리해야한다.
   현재 프로젝트와 같이 기본값을 채워넣는 로직이 있을 때, 요청 파라미터를 지우는 식으로 하게되면,
   값이 그냥 업데이트되지 않을 뿐인 방식으로 처리된다.
  */
  @Test
  void getActionProxy_MissingEmail() throws Exception {
    // getActionProxy() 호출 전에 파라미터 설정
    initRequestParameter();
    request.setParameter("personBean.email", "");

    ActionProxy proxy = getActionProxy("/save.action");
    assertThat(proxy, is(notNullValue()));

    EditAction action = (EditAction) proxy.getAction();
    assertThat(action, is(notNullValue()));

    String result = proxy.execute();
    assertThat(result, is(Action.INPUT));
  }

  @Test
  void getValueFromStack() throws ServletException, UnsupportedEncodingException {
    initRequestParameter();
    executeAction("/save.action");
    String firstName = (String) findValueAfterExecute("personBean.firstName");
    assertThat(firstName, is("Fleet"));
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
  }
}
