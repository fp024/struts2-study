package org.fp024.struts2.study.register.action;

import lombok.Getter;
import lombok.Setter;
import org.apache.struts2.ActionSupport;
import org.fp024.struts2.study.register.exceptions.SecurityBreachException;
import org.fp024.struts2.study.register.model.Person;

public class Register extends ActionSupport {
  private static final long serialVersionUID = 1L;

  @Getter @Setter private transient Person personBean;

  @Override
  public String execute() throws Exception {
    // call Service class to store personBean's state in database
    return SUCCESS;
  }

  public void throwException() throws Exception {
    throw new Exception("Exception thrown from throwException");
  }

  public void throwNullPointerException() throws NullPointerException {
    throw new NullPointerException("Null Pointer Exception thrown from " + Register.class);
  }

  public void throwSecurityException() throws SecurityBreachException {
    throw new SecurityBreachException(
        "Security breach exception thrown from throwSecurityException");
  }
}
