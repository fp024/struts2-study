package org.fp024.struts2.study.wildcardmethod.service;

import lombok.extern.slf4j.Slf4j;
import org.fp024.struts2.study.wildcardmethod.dto.PersonDTO;
import org.fp024.struts2.study.wildcardmethod.model.Person;
import org.fp024.struts2.study.wildcardmethod.repository.PersonRepository;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;

@Slf4j
@Service
public class PersonService {
  private final PersonRepository personRepository;

  public PersonService(PersonRepository personRepository) {
    this.personRepository = personRepository;
  }

  /** ID 유무에 따라 신규 추가 또는 업데이트를 처리한다. */
  @Transactional
  public void save(PersonDTO personDTO) {
    if (personDTO.getId() == null) {
      personRepository.save(new Person(personDTO));
    } else {
      Person person = personRepository.findById(personDTO.getId());
      if (person == null) {
        return;
      }
      person.setByPersonDTO(personDTO);
      personRepository.save(person);
    }
  }

  public List<Person> getPersons() {
    return personRepository.list();
  }

  @Transactional
  public void deletePerson(PersonDTO personDTO) {
    personRepository.remove(personRepository.findById(personDTO.getId()));
  }

  public Person getPerson(PersonDTO personDTO) {
    return personRepository.findById(personDTO.getId());
  }
}
