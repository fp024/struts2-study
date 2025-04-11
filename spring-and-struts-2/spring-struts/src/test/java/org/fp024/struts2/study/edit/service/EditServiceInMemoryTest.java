package org.fp024.struts2.study.edit.service;

import static org.junit.jupiter.api.Assertions.assertEquals;

import jakarta.annotation.Resource;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;

@SpringJUnitConfig(locations = "classpath:applicationContext.xml")
class EditServiceInMemoryTest {
  @Resource(name = "editService")
  private EditService editService;

  @BeforeEach
  void beforeEach() {
    editService.initData();
  }

  @Test
  void testGetPerson() {
    assertEquals("Bruce", editService.getPerson().getFirstName());
  }

  @Test
  void testSavePerson() {
    editService.getPerson().setLastName("필립스");
    assertEquals("필립스", editService.getPerson().getLastName());
  }
}
