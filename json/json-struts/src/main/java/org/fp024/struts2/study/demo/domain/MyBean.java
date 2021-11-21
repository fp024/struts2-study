package org.fp024.struts2.study.demo.domain;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@ToString
public class MyBean {
  private int counter;
  private List<String> names;

  public MyBean() {
    counter = 0;
    names = new ArrayList<>();
  }

  public MyBean(int counter, List<String> names) {
    this.counter = counter;
    this.names = names;
  }
}
