package org.fp024.struts2.study.model;

import lombok.Getter;
import lombok.Setter;

/** 메시지를 저장하는 모델 클래스 */
public class MessageStore {
  @Getter @Setter private String message;

  public MessageStore() {
    setMessage("Hello Struts User");
  }

  @Override
  public String toString() {
    return message + " (from toString)";
  }
}
