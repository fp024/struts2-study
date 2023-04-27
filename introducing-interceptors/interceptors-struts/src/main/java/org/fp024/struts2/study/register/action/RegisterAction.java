package org.fp024.struts2.study.register.action;

import com.opensymphony.xwork2.ActionSupport;
import java.io.Serial;
import java.time.LocalDateTime;
import lombok.Getter;
import lombok.Setter;
import org.fp024.struts2.study.register.model.Person;
import org.fp024.struts2.study.register.service.RegisterService;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

/** struts.xml에서 registerAction 빈이름으로 접근할 수 있다. */
@Component
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class RegisterAction extends ActionSupport {
  private final RegisterService registerService;

  public RegisterAction(RegisterService registerService) {
    this.registerService = registerService;
  }

  @Serial private static final long serialVersionUID = 1L;

  @Getter @Setter private Person personBean;

  @Getter @Setter private LocalDateTime accessDate;

  @Override
  public String execute() {
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
