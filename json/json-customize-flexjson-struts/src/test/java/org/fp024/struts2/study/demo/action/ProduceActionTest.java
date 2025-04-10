package org.fp024.struts2.study.demo.action;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.mockStatic;

import jakarta.servlet.ServletException;
import java.io.UnsupportedEncodingException;
import java.time.LocalDateTime;
import org.apache.struts2.dispatcher.mapper.ActionMapping;
import org.apache.struts2.junit.StrutsJUnit5TestCase;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;
import org.mockito.Mockito;

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
    // 액션에서 LocalDateTime.now()를 사용하기 때문에, now()에 대해 mock을 사용할 필요가 있다.
    LocalDateTime lastLogin = LocalDateTime.of(2021, 11, 21, 0, 0, 00, 0);
    String expect =
        "{\"addresses\":[{\"city\":\"Stratford-upon-Avon\",\"street\":\"Henley\",\"zipcodes\":[{\"code\":\"CV37\"}]}],\"birthday\":\"04/26/1564\",\"lastLogin\":\"11/21/2021\",\"username\":\"WillShak\",\"name\":\"William"
            + " Shakespeare\",\"password\":\"******\",\"phoneNumbers\":[{\"name\":\"cell\",\"number\":\"555-123-4567\"},{\"name\":\"home\",\"number\":\"555-987-6543\"},{\"name\":\"work\",\"number\":\"555-678-3542\"}]}";
    try (MockedStatic<LocalDateTime> mockedJSONContext =
        mockStatic(LocalDateTime.class, Mockito.CALLS_REAL_METHODS); ) {
      mockedJSONContext.when(LocalDateTime::now).thenReturn(lastLogin);

      String output = executeAction("/produce.action");
      assertEquals(expect, output);
    }
  }
}
