package org.fp024.struts2.study.helloworld;

import com.opensymphony.xwork2.ActionSupport;
import com.opensymphony.xwork2.interceptor.ParameterNameAware;
import lombok.Getter;
import lombok.Setter;
import org.apache.struts2.interceptor.SessionAware;
import org.fp024.struts2.study.model.MessageStore;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.util.Map;

/**
 * Bruce Phillips 님의 코드를 참고하여,<br>
 * 현재 프로젝트 환경에 맞게 수정한 클래스 입니다.
 */
@Component("helloWorldAction") // 이렇게 따로 적어주지 않아도 빈 이름은 첫글자를 소문자로한 클래스명이 된다.
@Scope("prototype")
public class HelloWorldAction extends ActionSupport implements SessionAware, ParameterNameAware {
  private static final long serialVersionUID = 1L;

  /** 뷰에 표시할 메시지를 저장하는 모델 클래스 */
  @Getter @Setter private MessageStore messageStore;

  private Map<String, Object> userSession;

  private static final String HELLO_COUNT = "helloCount";

  @Getter @Setter private String userName;

  /**
   * MessageStore 모델 객체를 생성하고 HTTP 세션에 저장된 helloCount를 1만큼 증가시키고 성공을 반환합니다.<br>
   * MessageStore 모델 객체는 뷰에서 사용할 수 있습니다.<br>
   *
   * @see com.opensymphony.xwork2.ActionSupport#execute()
   */
  @Override
  public String execute() {
    messageStore = new MessageStore();
    // 액션에는 userName 쿼리 파라미터 또는 이름이 userName인 폼 필드가 포함되어 있습니다.
    if (userName != null) {
      messageStore.setMessage(messageStore.getMessage() + " " + userName);
    }
    increaseHelloCount();

    return SUCCESS;
  }

  /** 사용자의 HTTP 세션에 저장된 HELLO_COUNT의 값을 늘립니다. */
  private void increaseHelloCount() {
    Integer helloCount = (Integer) userSession.get(HELLO_COUNT);
    if (helloCount == null) {
      helloCount = 1;
    } else {
      helloCount++;
    }
    userSession.put(HELLO_COUNT, helloCount);
  }

  /**
   * 파라미터 허용 여부를 판단하는 로직을 구현하는 메서드<br>
   * 전달된 파라미터 이름에 session이나 request가 포함되면 허용되지 않은 것으로 처리하고 있다.
   *
   * @param parameterName 파라미터 이름
   * @return 허용된 경우 true, 아니면 false
   */
  @Override
  public boolean acceptableParameterName(String parameterName) {
    return !parameterName.contains("session") && !parameterName.contains("request");
  }

  /** ActionContext.getContext().getSession() 으로도 session 맵을 얻을수도 있는 것 같다. */
  @Override
  public void setSession(Map<String, Object> session) {
    userSession = session;
  }
}
