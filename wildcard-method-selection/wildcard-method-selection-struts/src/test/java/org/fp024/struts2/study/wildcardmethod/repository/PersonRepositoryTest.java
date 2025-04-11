package org.fp024.struts2.study.wildcardmethod.repository;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.util.List;
import org.fp024.struts2.study.wildcardmethod.dto.PersonDTO;
import org.fp024.struts2.study.wildcardmethod.model.PersonEntity;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;
import org.springframework.transaction.annotation.Transactional;

@SpringJUnitConfig(locations = {"classpath:applicationContext.xml"})
class PersonRepositoryTest {
  @Autowired private PersonRepository personRepository;

  @Test
  void testPersonRepository() {
    assertNotNull(personRepository);
  }

  @Test
  void testList() {
    List<PersonEntity> list = personRepository.list();
    assertThat(list.isEmpty(), is(false));
  }

  @Test
  @Transactional
  @Rollback // 테스트 환경에서는 기본이 롤백이여서 일부러 이 어노테이션을 붙여줄 필요는 없다. 반영이 필요하면 @Commit을 붙인다.
  void testFindByIdThenRemoveThenList() {
    personRepository.remove(personRepository.findById(1));
    assertThat(personRepository.list().isEmpty(), is(false));
    personRepository.remove(personRepository.findById(2));
    assertThat(personRepository.list().isEmpty(), is(false));
    personRepository.remove(personRepository.findById(3));
    assertThat(personRepository.list().isEmpty(), is(true));
  }

  @Order(3)
  @Transactional
  @Test
  void testSave() {
    PersonDTO dto = new PersonDTO();
    dto.setFirstName("eclipse");
    dto.setLastName("oxygen");
    PersonEntity person = new PersonEntity(dto);
    personRepository.save(person);

    List<PersonEntity> persons = personRepository.list();

    assertThat(
        personRepository.findById(persons.get(persons.size() - 1).getId()).getLastName(),
        is("oxygen"));
  }
}
