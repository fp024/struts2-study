package org.fp024.struts2.study.edit.action;

import com.opensymphony.xwork2.ActionSupport;
import lombok.Getter;
import lombok.Setter;
import org.fp024.struts2.study.edit.model.Person;
import org.fp024.struts2.study.edit.model.State;
import org.fp024.struts2.study.edit.service.EditService;
import org.fp024.struts2.study.edit.service.EditServiceInMemory;

import java.util.List;

/**
 * Person 편집과 관련된 액션을 처리하는 컨트롤러 역할을 합니다.
 *
 * @author bruce phillips
 */
public class EditAction extends ActionSupport {
  private static final long serialVersionUID = 1L;

  private final EditService editService = new EditServiceInMemory();

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

  @Getter private final String[] carModelsAvailable = {"Ford", "Chrysler", "Toyota", "Nissan"};

  @Override
  public String execute() {
    editService.savePerson(getPersonBean());
    return SUCCESS;
  }

  @Override
  public String input() {
    setPersonBean(editService.getPerson());
    return INPUT;
  }
}
