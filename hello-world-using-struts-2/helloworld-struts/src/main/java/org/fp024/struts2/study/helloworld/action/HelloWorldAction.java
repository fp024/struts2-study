package org.fp024.struts2.study.helloworld.action;

import lombok.Getter;
import org.apache.struts2.ActionSupport;
import org.fp024.struts2.study.helloworld.model.MessageStore;

/**
 * ActionSupport가 Serializable를 구현해서 Eclipse에서는 아래 경고가 노출된다. The serializable class HelloWorldAction
 * does not declare a static final serialVersionUID field of type long
 *
 * <p>액션 클래스에 대해 직렬화/역직렬화가 일어나진 않을 것이여서, 무시하는 어노테이션을 붙여도 될 것 같은데... IntelliJ 에서는 이런 경고를 노출하지 않는다.
 *
 * <p>그런데, struts-example의 helloworld 예제를 보면 아래 항목이 붙어있다. private static final long serialVersionUID
 * = 1L;
 */
public class HelloWorldAction extends ActionSupport {
  @Getter private transient MessageStore messageStore;

  @Override
  public String execute() {
    messageStore = new MessageStore();
    return SUCCESS;
  }
}
