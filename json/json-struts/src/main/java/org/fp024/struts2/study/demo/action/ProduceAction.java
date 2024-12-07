package org.fp024.struts2.study.demo.action;

import java.util.Arrays;
import lombok.Getter;
import org.apache.struts2.ActionSupport;
import org.fp024.struts2.study.demo.domain.MyBean;

public class ProduceAction extends ActionSupport {
  @Getter private transient MyBean bean;

  @Override
  public String execute() {
    bean = new MyBean(2, Arrays.asList("Lukas", "Antonio"));
    return SUCCESS;
  }
}
