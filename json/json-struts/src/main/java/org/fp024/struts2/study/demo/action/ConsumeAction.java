package org.fp024.struts2.study.demo.action;

import com.opensymphony.xwork2.ActionSupport;
import lombok.Getter;
import org.apache.struts2.interceptor.ServletRequestAware;
import org.fp024.struts2.study.demo.domain.MyBean;

import javax.servlet.http.HttpServletRequest;

public class ConsumeAction extends ActionSupport implements ServletRequestAware {
  @Getter private final MyBean bean = new MyBean();

  private boolean responseAsJson = true;

  @Override
  public String execute() {
    if (responseAsJson) {
      return "JSON";
    }
    return SUCCESS;
  }

  public void setServletRequest(HttpServletRequest request) {
    responseAsJson = request.getHeader("Accept").contains("application/json");
  }
}
