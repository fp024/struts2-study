package org.fp024.struts2.study.helloworld.model;

import lombok.Getter;
import lombok.ToString;

@ToString
public class MessageStore {
  @Getter private String message;

  public MessageStore() {
    this.message = "Hello Struts User";
  }
}
