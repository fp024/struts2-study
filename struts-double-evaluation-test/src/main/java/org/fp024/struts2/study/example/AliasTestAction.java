package org.fp024.struts2.study.example;

import com.opensymphony.xwork2.ActionSupport;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class AliasTestAction extends ActionSupport {
  /** 메시지 프로퍼티의 기본 값 */
  private static final String MESSAGE = "title.message";

  /** 폼으로 부터 직접 입력받는 값 */
  @Getter @Setter private String expression;

  /** 필드명이 AliasInterceptor에의해 계산되는지 확인 */
  @Getter @Setter private String a100;

  /** 메시지 프로퍼티 필드 */
  @Getter @Setter private String message;

  @Override
  public String execute() {
    String message = getText(MESSAGE);
    LOGGER.info("message: {}", message);
    setMessage(message);

    LOGGER.info("expression: {}", expression);

    LOGGER.info("a100: {}", a100);
    return SUCCESS;
  }
}
