package org.fp024.struts2.study.register.service;

import org.fp024.struts2.study.register.model.Person;
import org.fp024.struts2.study.register.repository.PersonRepository;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.time.LocalDateTime;

@Service
public class RegisterService {
  private final PersonRepository personRepository;

  public RegisterService(PersonRepository personRepository) {
    this.personRepository = personRepository;
  }

  @Transactional
  public void save(Person person) {
    person.setRegisterDate(LocalDateTime.now());
    personRepository.save(person);
  }

  public Person findById(Person person) {
    return personRepository.findById(person.getEmail());
  }
}
