package org.fp024.struts2.study.register.action;

import lombok.Getter;
import lombok.Setter;
import org.apache.struts2.ActionSupport;
import org.apache.struts2.interceptor.parameter.StrutsParameter;
import org.fp024.struts2.study.register.model.Person;

public class Register extends ActionSupport {
  private static final long serialVersionUID = 1L;

  @Getter(onMethod_ = @StrutsParameter(depth = 1))
  @Setter
  private transient Person personBean;

  @Override
  public void validate() {
    if (personBean.getFirstName().isEmpty()) {
      addFieldError("personBean.firstName", "First name is required.");
    }

    if (personBean.getEmail().isEmpty()) {
      addFieldError("personBean.email", "Email is required.");
    }

    if (personBean.getAge() < 18) {
      addFieldError("personBean.age", "Age is required and must be 18 or older");
    }
  }

  @Override
  public String execute() throws Exception {
    // call Service class to store personBean's state in database
    return SUCCESS;
  }
}
