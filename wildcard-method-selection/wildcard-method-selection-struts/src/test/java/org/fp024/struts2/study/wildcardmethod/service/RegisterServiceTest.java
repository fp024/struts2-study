package org.fp024.struts2.study.wildcardmethod.service;

import org.fp024.struts2.study.wildcardmethod.dto.PersonDTO;
import org.fp024.struts2.study.wildcardmethod.model.Person;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;

import javax.transaction.Transactional;

import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.nullValue;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@SpringJUnitConfig(locations = {"classpath:applicationContext.xml"})
class RegisterServiceTest {
  @Autowired private PersonService personService;

  @Order(1)
  @Test
  void testRegisterService() {
    assertNotNull(personService);
  }

  @Order(2)
  @Test
  void testFindById() {
    PersonDTO dto = new PersonDTO();
    dto.setId(1);
    Person person = personService.getPerson(dto);
    assertNotNull(person);
    assertEquals("Steve", person.getFirstName());
  }

  /**
   * applicationContext.xml 설정에 <tx:annotation-driven/> 이 설정 되었고, registerService.save() <br>
   * 메서드에 @Transactional 을 붙여서 트랜젝션 하에 실행이 되었다. <br>
   * 그런데 메인 소스에 @Transactional 로 트랜젝션이 적용된 경우는 커밋된다.
   *
   * <p>롤백을 하고 싶다면 아래 테스트 메서드 위에 @Transactional 을 붙이면 된다.
   */
  @Order(3)
  @Transactional
  @Test
  void testSaveThenFindById() {
    PersonDTO dto = new PersonDTO(null, "fp024", "lucy");
    personService.save(dto);
    List<Person> persons = personService.getPersons();
    dto.setId(persons.get(persons.size() - 1).getId());
    assertThat(personService.getPerson(dto).getLastName(), is("lucy"));
  }

  @Order(4)
  @Test
  void testIsNotRollback() {
    PersonDTO dto = new PersonDTO();
    dto.setId(4);
    assertThat(
        "테스트 메서드의 @Transactional 을 통해 트랜젝션 롤백되었다.", personService.getPerson(dto), is(nullValue()));
  }
}
