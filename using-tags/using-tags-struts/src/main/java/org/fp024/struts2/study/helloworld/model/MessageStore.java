package org.fp024.struts2.study.helloworld.model;

import lombok.Getter;
import lombok.Setter;

public class MessageStore {
  @Getter @Setter private String userName = "Default User";

  @Override
  public String toString() {
    return "Hello Struts User - %s (from toString)".formatted(userName);
  }
}
