package org.fp024.struts2.study.edit.action;

import org.fp024.struts2.study.edit.service.EditService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

@SpringJUnitConfig(locations = "classpath:applicationContext.xml")
class EditActionTest {
  @Autowired private ApplicationContext ctx;

  @Test
  @DisplayName("빈으로 설정한 액션이 프로토타입으로 잘 동작하는지?")
  void testPrototype() {
    EditAction act1 = (EditAction) ctx.getBean("editAction");
    EditAction act2 = (EditAction) ctx.getBean("editAction");

    assertNotEquals(act1, act2);
  }

  @Test
  @DisplayName("빈으로 설정한 서비스가 싱글톤으로 잘 동작하는지?")
  void testSingleton() {
    EditService es1 = (EditService) ctx.getBean("editService");
    EditService es2 = (EditService) ctx.getBean("editService");

    assertEquals(es1, es2);
  }
}
