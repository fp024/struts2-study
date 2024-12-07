package org.fp024.struts2.study.demo.action;

import javax.servlet.http.HttpServletRequest;
import lombok.Getter;
import org.apache.struts2.ActionSupport;
import org.apache.struts2.action.ServletRequestAware;
import org.fp024.struts2.study.demo.domain.MyBean;

public class ConsumeAction extends ActionSupport implements ServletRequestAware {
  @Getter private final transient MyBean bean = new MyBean();

  private boolean responseAsJson = true;

  @Override
  public String execute() {
    if (responseAsJson) {
      return "JSON";
    }
    return SUCCESS;
  }

  @Override
  public void withServletRequest(HttpServletRequest request) {
    responseAsJson = request.getHeader("Accept").contains("application/json");
  }
}
