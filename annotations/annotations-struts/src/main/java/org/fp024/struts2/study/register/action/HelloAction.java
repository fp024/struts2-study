package org.fp024.struts2.study.register.action;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.apache.struts2.ActionSupport;
import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Result;

/**
 * 사용자 등록과 관련된 액션을 처리하는 컨트롤러 역할을 합니다.
 *
 * @author bruce phillips
 */
@Slf4j
public class HelloAction extends ActionSupport {

  private static final long serialVersionUID = 1L;

  @Getter private String message;

  @Action(
      value = "/hello",
      results = {@Result(name = SUCCESS, location = "hello-success.jsp")})
  @Override
  public String execute() {
    LOGGER.info("In execute method of class Hello");
    message = "Hello from Struts 2 with no XML configuration.";
    return SUCCESS;
  }
}
