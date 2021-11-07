package org.fp024.struts2.study.edit.service;

import org.fp024.struts2.study.edit.model.Person;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;

/** 예제 원저자님이 In-Memory 형식으로 한 Person 객체의 조회와 수정을 보여주기 위해 만든 클래스 */
@Service("editService")
public class EditServiceInMemory implements EditService {
  private static Person person;
  private static String[] carModels = {"Ford", "Nissan"};

  @PostConstruct
  @Override
  public void initData() {
    person = new Person();
    person.setFirstName("Bruce");
    person.setLastName("Phillips");
    person.setSport("basketball");
    person.setGender("not sure");
    person.setResidency("KS");
    person.setOver21(true);
    person.setCarModels(carModels);
  }

  @Override
  public Person getPerson() {
    return EditServiceInMemory.person;
  }

  @Override
  public void savePerson(Person personBean) {
    EditServiceInMemory.person.setFirstName(personBean.getFirstName());
    EditServiceInMemory.person.setLastName(personBean.getLastName());
    EditServiceInMemory.person.setSport(personBean.getSport());
    EditServiceInMemory.person.setGender(personBean.getGender());
    EditServiceInMemory.person.setResidency(personBean.getResidency());
    EditServiceInMemory.person.setOver21(personBean.isOver21());
    EditServiceInMemory.person.setCarModels(personBean.getCarModels());
  }
}
