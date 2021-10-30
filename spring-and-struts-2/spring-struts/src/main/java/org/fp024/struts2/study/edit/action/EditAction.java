package org.fp024.struts2.study.edit.action;

import com.opensymphony.xwork2.ActionSupport;
import lombok.Getter;
import lombok.Setter;
import org.fp024.struts2.study.edit.model.Person;
import org.fp024.struts2.study.edit.model.State;
import org.fp024.struts2.study.edit.service.EditService;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.List;

@Component
@Scope("prototype")
public class EditAction extends ActionSupport {
  private static final long serialVersionUID = 1L;

  /**
   * XML 설정에서 빈을 명시적으로 설정하면 액션에서 Setter만 만들어주면 되는데,<br>
   * Component-scan 할 경우는 @Autowired를 필드에 지정하거나 생성자로 받아야한다.
   */
  private EditService editService;

  public EditAction(EditService editService) {
    this.editService = editService;
  }

  @Getter @Setter private Person personBean;

  private String[] sports = {"football", "baseball", "basketball"};

  public List<String> getSports() {
    return Arrays.asList(sports);
  }

  private String[] genders = {"male", "female", "not sure"};

  public List<String> getGenders() {
    return Arrays.asList(genders);
  }

  private List<State> states =
      List.of(
          new State("AZ", "Arizona"),
          new State("CA", "California"),
          new State("FL", "Florida"),
          new State("KS", "Kansas"),
          new State("NY", "New York"));

  public List<State> getStates() {
    return states;
  }

  private String[] carModelsAvailable = {"Ford", "Chrysler", "Toyota", "Nissan"};

  public String[] getCarModelsAvailable() {
    return carModelsAvailable;
  }

  public String execute() throws Exception {
    editService.savePerson(getPersonBean());
    return SUCCESS;
  }

  public String input() throws Exception {
    setPersonBean(editService.getPerson());
    return INPUT;
  }
}
