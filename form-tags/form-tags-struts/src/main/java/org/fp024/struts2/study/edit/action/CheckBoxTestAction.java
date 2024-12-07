package org.fp024.struts2.study.edit.action;

import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.apache.struts2.ActionContext;
import org.apache.struts2.ActionSupport;
import org.apache.struts2.action.Action;

/** checkbox에 체크를 안하고 폼전송할 때, 제출을 하면 아예전송이 안되는지? */
@Slf4j
public class CheckBoxTestAction extends ActionSupport {
  @Getter @Setter private boolean aaaChecked = true;

  @Override
  public String execute() {
    Object paramAaaChecked = ActionContext.getContext().getParameters().get("aaaChecked");
    LOGGER.info("param aaaChecked: {}, field aaaChecked {}", paramAaaChecked, aaaChecked);
    return Action.NONE;
  }
}
