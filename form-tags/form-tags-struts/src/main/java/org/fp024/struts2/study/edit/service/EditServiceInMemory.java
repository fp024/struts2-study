package org.fp024.struts2.study.edit.service;

import org.fp024.struts2.study.edit.model.Person;

/**
 * Person 객체의 상태를 편집하고 저장하는 데 필요한 서비스를 구현합니다. <br>
 * 이 구현에서 Person 객체의 상태는 메모리에 저장됩니다.
 *
 * @author brucephillips
 */
public class EditServiceInMemory implements EditService {

  private static final Person person;
  private static final String[] carModels = {"Ford", "Nissan"};

  // 최초 서버 로드시 Person 초기값 설정
  static {
    person = new Person();
    person.setFirstName("Bruce");
    person.setLastName("Phillips");
    person.setSport("basketball");
    person.setGender("not sure");
    person.setResidency("KS");
    person.setOver21(true);
    person.setCarModels(carModels);
  }

  public Person getPerson() {
    return EditServiceInMemory.person;
  }

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
