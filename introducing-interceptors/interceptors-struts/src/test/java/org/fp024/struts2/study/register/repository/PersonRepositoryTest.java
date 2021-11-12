package org.fp024.struts2.study.register.repository;

import org.fp024.struts2.study.register.model.Person;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;

import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@SpringJUnitConfig(locations = {"classpath:applicationContext.xml"})
class PersonRepositoryTest {
  @Autowired private PersonRepository personRepository;

  @Order(1)
  @Test
  void testPersonRepository() {
    assertNotNull(personRepository);
  }

  @Order(2)
  @Test
  void testList() {
    List<Person> list = personRepository.list();
    assertFalse(list.isEmpty());
  }

  @Order(2)
  @Test
  @Transactional
  @Rollback // 테스트 환경에서는 기본이 롤백이여서 일부러 이 어노테이션을 붙여줄 필요는 없다. 반영이 필요하면 @Commit을 붙인다.
  void testFindByIdThenRemoveThenList() {
    personRepository.remove(personRepository.findById("struts@apache.org"));
    assertTrue(personRepository.list().isEmpty());
  }

  @Order(3)
  @Transactional
  @Test
  void testSave() {
    Person person = new Person("eclipse@eclipse.org");
    person.setFirstName("oxygen");
    person.setLastName("eclipse");
    person.setAge(21);
    person.setRegisterDate(LocalDateTime.now());
    personRepository.save(person);
    assertEquals("oxygen", personRepository.findById("eclipse@eclipse.org").getFirstName());
  }
}
