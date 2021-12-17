package org.fp024.struts2.study.edit.action;

import com.opensymphony.xwork2.ActionContext;
import com.opensymphony.xwork2.ActionSupport;
import com.opensymphony.xwork2.Preparable;
import com.opensymphony.xwork2.security.DefaultExcludedPatternsChecker;
import com.opensymphony.xwork2.security.ExcludedPatternsChecker;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.fp024.struts2.study.edit.model.Person;
import org.fp024.struts2.study.edit.model.State;
import org.fp024.struts2.study.edit.service.CarModelsService;
import org.fp024.struts2.study.edit.service.CarModelsServiceHardCoded;
import org.fp024.struts2.study.edit.service.EditService;
import org.fp024.struts2.study.edit.service.EditServiceInMemory;

import java.util.List;

/**
 * Person 편집과 관련된 액션을 처리하는 컨트롤러 역할을 합니다.
 *
 * @author bruce phillips
 */
@Slf4j
public class EditAction extends ActionSupport implements Preparable {
  private static final long serialVersionUID = 1L;

  private final EditService editService = new EditServiceInMemory();
  private final CarModelsService carModelsService = new CarModelsServiceHardCoded();

  @Getter @Setter private Person personBean;

  @Getter private final List<String> sports = List.of("football", "baseball", "basketball");

  @Getter private final List<String> genders = List.of("male", "female", "not sure");

  @Getter
  private final List<State> states =
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
