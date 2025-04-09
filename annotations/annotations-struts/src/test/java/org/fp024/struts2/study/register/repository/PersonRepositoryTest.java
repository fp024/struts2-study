package org.fp024.struts2.study.register.repository;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;

import java.util.List;
import org.fp024.struts2.study.register.model.Person;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;
import org.springframework.transaction.annotation.Transactional;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@SpringJUnitConfig(locations = {"classpath:applicationContext.xml"})
class PersonRepositoryTest {
  @Autowired private PersonRepository personRepository;

  @Order(1)
  @Test
  void testPersonRepository() {
    assertThat(personRepository, is(notNullValue()));
  }

  @Order(2)
  @Test
  void testList() {
    List<Person> list = personRepository.list();
    assertThat(list.isEmpty(), is(false));
  }

  @Order(2)
  @Test
  @Transactional
  @Rollback // 테스트 환경에서는 기본이 롤백이여서 일부러 이 어노테이션을 붙여줄 필요는 없다. 반영이 필요하면 @Commit을 붙인다.
  void testFindByIdThenRemoveThenList() {
    personRepository.remove(personRepository.findById("struts@apache.org"));
    assertThat(personRepository.list().isEmpty(), is(true));
  }

  @Order(3)
  @Transactional
  @Test
  void testSave() {
    Person person = new Person("eclipse@eclipse.org");
    person.setFirstName("oxygen");
    person.setLastName("eclipse");
    person.setAge(21);
    personRepository.save(person);
    assertThat(personRepository.findById("eclipse@eclipse.org").getFirstName(), is("oxygen"));
  }
}
