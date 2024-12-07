package org.fp024.struts2.study.edit.action;

import java.util.List;
import lombok.Getter;
import lombok.Setter;
import org.apache.struts2.ActionSupport;
import org.fp024.struts2.study.edit.model.Person;
import org.fp024.struts2.study.edit.model.State;
import org.fp024.struts2.study.edit.service.EditService;
import org.fp024.struts2.study.edit.service.EditServiceInMemory;

/**
 * Person 편집과 관련된 액션을 처리하는 컨트롤러 역할을 합니다.
 *
 * @author bruce phillips
 */
public class EditAction extends ActionSupport {
  private static final long serialVersionUID = 1L;

  private final transient EditService editService = new EditServiceInMemory();

  @Getter @Setter private transient Person personBean;

  @Getter
  private final transient List<String> sports = List.of("football", "baseball", "basketball");

  @Getter private final transient List<String> genders = List.of("male", "female", "not sure");

  @Getter
  private final transient List<State> states =
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
