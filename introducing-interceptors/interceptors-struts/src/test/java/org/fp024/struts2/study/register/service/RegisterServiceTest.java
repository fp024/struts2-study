package org.fp024.struts2.study.register.service;

import org.fp024.struts2.study.register.model.Person;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@SpringJUnitConfig(locations = {"classpath:applicationContext.xml"})
class RegisterServiceTest {
  @Autowired private RegisterService registerService;

  @Order(1)
  @Test
  void testRegisterService() {
    assertNotNull(registerService);
  }

  @Order(2)
  @Test
  void testFindById() {
    Person person = registerService.findById(new Person("struts@apache.org"));
    assertNotNull(person);
    assertEquals("xwork", person.getLastName());
  }

  /**
   * applicationContext.xml 설정에 <tx:annotation-driven/> 이 설정 되었고, registerService.save() <br>
   * 메서드에 @Transactional 을 붙여서 트랜젝션 하에 실행이 되었다. <br>
   * 그런데 메인 소스에 @Transactional 로 트랜젝션이 적용된 경우는 커밋된다.
   *
   * <p>롤백을 하고 싶다면 아래 테스트 메서드 위에 @Transactional 을 붙이면 된다.
   */
  @Order(3)
  @Test
  void testSaveThenFindById() {
    Person person = new Person("tomee@apache.org");
    person.setLastName("Tomcat");
    person.setFirstName("TomEE");
    person.setAge(30);
    person.setRegisterDate(LocalDateTime.now());

    registerService.save(person);

    assertEquals("tomee@apache.org", registerService.findById(person).getEmail());
  }

  @Order(4)
  @Test
  void testIsNotRollback() {
    assertNotNull(
        registerService.findById(new Person("tomee@apache.org")),
        "서비스 코드의 @Transactional 을 통해 트랜젝션이 이루어져서 롤백이 안되었다.");
  }
}
