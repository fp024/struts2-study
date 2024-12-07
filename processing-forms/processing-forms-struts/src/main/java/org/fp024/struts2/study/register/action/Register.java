package org.fp024.struts2.study.register.action;

import lombok.Getter;
import lombok.Setter;
import org.apache.struts2.ActionSupport;
import org.fp024.struts2.study.register.model.Person;

public class Register extends ActionSupport {
  private static final long serialVersionUID = 1L;

  @Getter @Setter private transient Person personBean;

  @Override
  public String execute() throws Exception {
    // call Service class to store personBean's state in database
    return SUCCESS;
  }
}
