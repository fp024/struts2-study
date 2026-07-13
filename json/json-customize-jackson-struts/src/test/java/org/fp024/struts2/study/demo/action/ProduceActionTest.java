package org.fp024.struts2.study.demo.action;

import static net.javacrumbs.jsonunit.assertj.JsonAssertions.assertThatJson;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import jakarta.servlet.ServletException;
import java.io.UnsupportedEncodingException;
import java.time.LocalDateTime;
import java.time.Month;
import lombok.extern.slf4j.Slf4j;
import org.apache.struts2.dispatcher.mapper.ActionMapping;
import org.apache.struts2.junit.StrutsJUnit5TestCase;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;
import org.mockito.Mockito;

@Slf4j
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
    LocalDateTime lastLogin = LocalDateTime.of(2021, Month.NOVEMBER, 21, 0, 0, 0, 0);
    String output;
    try (MockedStatic<LocalDateTime> mockedNow =
        Mockito.mockStatic(LocalDateTime.class, Mockito.CALLS_REAL_METHODS)) {
      mockedNow.when(LocalDateTime::now).thenReturn(lastLogin);
      output = executeAction("/produce.action");
    }

    LOGGER.info("output: {}", output);

    assertNotNull(output);

    assertThatJson(output).node("nickname").isAbsent();
    assertThatJson(output).node("hashedPassword").isAbsent();
    assertThatJson(output).node("addresses[0].name").isAbsent();

    assertThatJson(output).node("name").isEqualTo("William Shakespeare");
    assertThatJson(output).node("username").isEqualTo("WillShak");
    assertThatJson(output).node("password").isEqualTo("******");
    assertThatJson(output).node("birthday").isEqualTo("04/26/1564");

    assertThatJson(output).node("addresses[0].city").isEqualTo("Stratford-upon-Avon");
    assertThatJson(output).node("addresses[0].street").isEqualTo("Henley");
    assertThatJson(output).node("addresses").isArray().hasSize(1);

    assertThatJson(output).node("lastLogin").isEqualTo("11/21/2021");
  }
}
