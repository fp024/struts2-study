package org.fp024.struts2.study.edit.action;

import java.util.List;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.apache.struts2.ActionContext;
import org.apache.struts2.ActionSupport;
import org.apache.struts2.Preparable;
import org.apache.struts2.interceptor.parameter.StrutsParameter;
import org.fp024.struts2.study.edit.model.Person;
import org.fp024.struts2.study.edit.model.State;
import org.fp024.struts2.study.edit.service.CarModelsService;
import org.fp024.struts2.study.edit.service.CarModelsServiceHardCoded;
import org.fp024.struts2.study.edit.service.EditService;
import org.fp024.struts2.study.edit.service.EditServiceInMemory;

/**
 * Person 편집과 관련된 액션을 처리하는 컨트롤러 역할을 합니다.
 *
 * @author bruce phillips
 */
@Slf4j
public class EditAction extends ActionSupport implements Preparable {
  private static final long serialVersionUID = 1L;

  private final transient EditService editService = new EditServiceInMemory();
  private final transient CarModelsService carModelsService = new CarModelsServiceHardCoded();

  @Getter(onMethod_ = {@StrutsParameter(depth = 1)})
  @Setter
  private transient Person personBean;

  @Getter private final List<String> sports = List.of("football", "baseball", "basketball");

  @Getter private final List<String> genders = List.of("male", "female", "not sure");

  @Getter
  private final transient List<State> states =
      List.of(
          new State("AZ", "Arizona"),
          new State("CA", "California"),
          new State("FL", "Florida"),
          new State("KS", "Kansas"),
          new State("NY", "New York"));

  @Getter private String[] carModelsAvailable;

  @Override
  public void prepare() {
    LOGGER.info("In prepare method...");
    carModelsAvailable = carModelsService.getCarModels();
    setPersonBean(editService.getPerson());
    LOGGER.info("prepare - PersonBean: {}", getPersonBean());
  }

  public void prepareExecute() {
    LOGGER.info("In prepareExecute method...");
  }

  @Override
  public void validate() {
    LOGGER.info("In validate method...");
    LOGGER.info("validate - Http Params: {}", ActionContext.getContext().getParameters());
    LOGGER.info("validate - PersonBean: {}", getPersonBean());
  }

  @Override
  public String execute() {
    LOGGER.info("In execute method...");
    editService.savePerson(getPersonBean());
    return SUCCESS;
  }

  public void prepareInput() {
    LOGGER.info("In prepareInput method...");
  }

  @Override
  public String input() {
    LOGGER.info("In input method...");
    return INPUT;
  }
}
