package org.fp024.struts2.study.register.action;

import java.util.List;
import lombok.Getter;
import lombok.Setter;
import org.apache.struts2.ActionSupport;
import org.apache.struts2.interceptor.parameter.StrutsParameter;
import org.fp024.struts2.study.register.model.Person;

public class Register extends ActionSupport {
  private static final long serialVersionUID = 1L;

  @Getter(onMethod_ = {@StrutsParameter(depth = 1)})
  @Setter
  private transient Person personBean;

  @Getter(onMethod_ = {@StrutsParameter(depth = 1)})
  @Setter
  private List<Integer> options;

  public String execute2() {
    // call Service class to store personBean's state in database
    return SUCCESS;
  }

  @Override
  public String input() throws Exception {
    return INPUT;
  }

  public String cancel2() {
    return SUCCESS;
  }
}
