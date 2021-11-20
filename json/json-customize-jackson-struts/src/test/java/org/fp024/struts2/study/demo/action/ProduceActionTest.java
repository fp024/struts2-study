package org.fp024.struts2.study.demo.action;

import org.apache.struts2.StrutsJUnit5TestCase;
import org.apache.struts2.dispatcher.mapper.ActionMapping;
import org.junit.jupiter.api.Test;

import javax.servlet.ServletException;
import java.io.UnsupportedEncodingException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

class ProduceActionTest extends StrutsJUnit5TestCase<ProduceAction> {
  @Test
  void getActionMapping() {
    ActionMapping mapping = getActionMapping("/produce.action");
    assertNotNull(mapping);
    assertEquals("/", mapping.getNamespace());
    assertEquals("produce", mapping.getName());
  }

  @Test
  void executeAction() throws ServletException, UnsupportedEncodingException {
    String expect =
        "{\"name\":\"William Shakespeare\",\"birthday\":\"04/26/1564\",\"addresses\":[{\"name\":\"home\",\"street\":\"Henley\",\"city\":\"Stratford-upon-Avon\",\"zipcodes\":[{\"code\":\"CV37\"}]}],\"phoneNumbers\":[{\"name\":\"cell\",\"number\":\"555-123-4567\"},{\"name\":\"home\",\"number\":\"555-987-6543\"},{\"name\":\"work\",\"number\":\"555-678-3542\"}],\"lastLogin\":\"11/21/2021\",\"password\":\"******\",\"username\":\"WillShak\"}";
    String output = executeAction("/produce.action");
    assertEquals(expect, output);
  }
}
