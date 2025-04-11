package org.fp024.struts2.study.edit.service;

import jakarta.annotation.PostConstruct;
import org.fp024.struts2.study.edit.model.Person;
import org.springframework.stereotype.Service;

/** 예제 원저자님이 In-Memory 형식으로 한 Person 객체의 조회와 수정을 보여주기 위해 만든 클래스 */
@Service("editService")
public class EditServiceInMemory implements EditService {
  private Person person;
  private String[] carModels = {"Ford", "Nissan"};

  @PostConstruct
  @Override
  public void initData() {
    this.person = new Person();
    this.person.setFirstName("Bruce");
    this.person.setLastName("Phillips");
    this.person.setSport("basketball");
    this.person.setGender("not sure");
    this.person.setResidency("KS");
    this.person.setOver21(true);
    this.person.setCarModels(carModels);
  }

  @Override
  public Person getPerson() {
    return this.person;
  }

  @Override
  public void savePerson(Person personBean) {
    this.person.setFirstName(personBean.getFirstName());
    this.person.setLastName(personBean.getLastName());
    this.person.setSport(personBean.getSport());
    this.person.setGender(personBean.getGender());
    this.person.setResidency(personBean.getResidency());
    this.person.setOver21(personBean.isOver21());
    this.person.setCarModels(personBean.getCarModels());
  }
}
