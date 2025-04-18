package org.fp024.struts2.study.register.action;

import java.io.Serial;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.apache.struts2.ActionSupport;
import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Result;
import org.apache.struts2.interceptor.parameter.StrutsParameter;
import org.fp024.struts2.study.register.model.Person;
import org.fp024.struts2.study.register.service.RegisterService;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

/** struts.xml에서 registerAction 빈이름으로 접근할 수 있다. */
@Slf4j
@Component
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class RegisterAction extends ActionSupport {
  private final transient RegisterService registerService;

  public RegisterAction(RegisterService registerService) {
    this.registerService = registerService;
  }

  @Serial private static final long serialVersionUID = 1L;

  @Setter private transient Person personBean;

  @StrutsParameter(depth = 1)
  public Person getPersonBean() {
    return personBean;
  }

  @Override
  @Action(
      value = "/register-input",
      results = {@Result(name = INPUT, location = "register-input.jsp")})
  public String input() {
    LOGGER.info("In input method of class RegisterAction");
    return INPUT;
  }

  @Action(
      value = "/register",
      results = {
        @Result(name = INPUT, location = "register-input.jsp"),
        @Result(name = SUCCESS, location = "register-success.jsp")
      })
  @Override
  public String execute() {
    LOGGER.info("In execute method of class RegisterAction");
    try {
      registerService.save(personBean);
    } catch (Exception exception) {
      addActionError("User save failed.");
      return INPUT;
    }
    setPersonBean(registerService.findById(personBean));
    return SUCCESS;
  }

  @Override
  public void validate() {

    if (personBean.getEmail() == null || personBean.getEmail().isBlank()) {
      addFieldError("personBean.email", "Email is required.");
    }

    if (personBean.getFirstName() == null || personBean.getFirstName().isBlank()) {
      addFieldError("personBean.firstName", "First name is required.");
    }

    if (personBean.getAge() == null || personBean.getAge() < 18) {
      addFieldError("personBean.age", "Age is required and must be 18 or older");
    }

    if (registerService.findById(personBean) != null) {
      addFieldError("personBean.email", "Email is already registered.");
    }
  }
}
