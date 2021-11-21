package org.fp024.struts2.study.demo.action;

import com.opensymphony.xwork2.ActionSupport;
import lombok.Getter;
import org.fp024.struts2.study.demo.domain.MyBean;

import java.util.Arrays;

public class ProduceAction extends ActionSupport {
  @Getter private MyBean bean;

  @Override
  public String execute() {
    bean = new MyBean(2, Arrays.asList("Lukas", "Antonio"));
    return SUCCESS;
  }
}
