package org.fp024.struts2.study.wildcardmethod.action;

import java.util.List;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.apache.struts2.ActionSupport;
import org.fp024.struts2.study.wildcardmethod.dto.PersonDTO;
import org.fp024.struts2.study.wildcardmethod.model.Person;
import org.fp024.struts2.study.wildcardmethod.service.PersonService;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

/** struts.xml에서 registerAction 빈이름으로 접근할 수 있다. */
@Slf4j
@Component
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class PersonAction extends ActionSupport {
  private final transient PersonService personService;

  public PersonAction(PersonService personService) {
    this.personService = personService;
    this.personList = personService.getPersons();
  }

  private static final long serialVersionUID = 1L;

  @Getter @Setter private transient PersonDTO personDTO;

  @Getter private transient List<Person> personList;

  @Override
  public String execute() {
    LOGGER.debug("In execute method");
    return SUCCESS;
  }

  public String create() {
    LOGGER.debug("In create method");
    personDTO = new PersonDTO();
    return INPUT;
  }

  public String runCreateThis() {
    LOGGER.debug("In create method");
    personDTO = new PersonDTO();
    return INPUT;
  }

  public String edit() {
    LOGGER.debug("In edit method");
    personDTO = new PersonDTO(personService.getPerson(personDTO));
    return INPUT;
  }

  public String saveOrUpdate() {
    LOGGER.debug("In saveOrUpdate method");
    personService.save(personDTO);
    personList = personService.getPersons();
    return SUCCESS;
  }

  public String delete() {
    LOGGER.debug("In delete method");
    personService.deletePerson(personDTO);
    personList = personService.getPersons();
    return SUCCESS;
  }
}
