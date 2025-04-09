package org.fp024.struts2.study.register.service;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;
import static org.hamcrest.Matchers.nullValue;

import org.fp024.struts2.study.register.model.Person;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;
import org.springframework.transaction.annotation.Transactional;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@SpringJUnitConfig(locations = {"classpath:applicationContext.xml"})
class RegisterServiceTest {
  @Autowired private RegisterService registerService;

  @Order(1)
  @Test
  void testRegisterService() {
    assertThat(registerService, is(notNullValue()));
  }

  @Order(2)
  @Test
  void testFindById() {
    Person person = registerService.findById(new Person("struts@apache.org"));
    assertThat(person, is(notNullValue()));
    assertThat(person.getLastName(), is("xwork"));
  }

  /**
   * applicationContext.xml 설정에 <tx:annotation-driven/> 이 설정 되었고, registerService.save() <br>
   * 메서드에 @Transactional 을 붙여서 트랜젝션 하에 실행이 되었다. <br>
   * 그런데 메인 소스에 @Transactional 로 트랜젝션이 적용된 경우는 커밋된다.
   *
   * <p>롤백을 하고 싶다면 아래 테스트 메서드 위에 @Transactional 을 붙이면 된다.
   */
  @Transactional
  @Order(3)
  @Test
  void testSaveThenFindById() {
    Person person = new Person("tomee@apache.org");
    person.setLastName("Tomcat");
    person.setFirstName("TomEE");
    person.setAge(30);

    registerService.save(person);

    assertThat(registerService.findById(person).getEmail(), is("tomee@apache.org"));
  }

  @Order(4)
  @Test
  void testIsNotRollback() {
    assertThat(
        "테스트 코드의 @Transactional 을 통해 트랜젝션이 이루어져서 롤백 되었다.",
        registerService.findById(new Person("tomee@apache.org")),
        is(nullValue()));
  }
}
