package org.fp024.struts2.study.wildcardmethod.service;

import java.util.List;
import org.fp024.struts2.study.wildcardmethod.dto.PersonDTO;
import org.fp024.struts2.study.wildcardmethod.model.PersonEntity;
import org.fp024.struts2.study.wildcardmethod.repository.PersonRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
      personRepository.save(new PersonEntity(personDTO));
    } else {
      PersonEntity person = personRepository.findById(personDTO.getId());
      if (person == null) {
        return;
      }
      person.setByPersonDTO(personDTO);
      personRepository.save(person);
    }
  }

  public List<PersonDTO> getPersons() {
    return personRepository.list().stream().map(PersonDTO::new).toList();
  }

  @Transactional
  public void deletePerson(PersonDTO personDTO) {
    personRepository.remove(personRepository.findById(personDTO.getId()));
  }

  public PersonDTO getPerson(PersonDTO personDTO) {
    PersonEntity personEntity = personRepository.findById(personDTO.getId());
    return personEntity == null ? null : new PersonDTO(personEntity);
  }
}
