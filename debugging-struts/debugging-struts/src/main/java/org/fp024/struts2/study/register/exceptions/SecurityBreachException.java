package org.fp024.struts2.study.register.exceptions;

public class SecurityBreachException extends Exception {
  private static final long serialVersionUID = 1707013157615736865L;

  public SecurityBreachException() {
    super("Security Exception");
  }

  public SecurityBreachException(String message) {
    super(message);
  }
}
