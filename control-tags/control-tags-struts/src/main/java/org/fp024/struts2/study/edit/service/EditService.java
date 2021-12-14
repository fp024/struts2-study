package org.fp024.struts2.study.edit.service;

import org.fp024.struts2.study.edit.model.Person;

public interface EditService {

  Person getPerson();

  void savePerson(Person personBean);
}
