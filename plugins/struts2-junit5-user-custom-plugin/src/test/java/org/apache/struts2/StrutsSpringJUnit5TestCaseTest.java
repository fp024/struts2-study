/*
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *  http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */
package org.apache.struts2;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.io.UnsupportedEncodingException;

import javax.servlet.ServletException;

import org.apache.struts2.action.Action;
import org.apache.struts2.dispatcher.mapper.ActionMapping;
import org.junit.jupiter.api.Test;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;

@SpringJUnitConfig(locations = {"classpath*:applicationContext.xml"})
class StrutsSpringJUnit5TestCaseTest extends StrutsSpringJUnit5TestCase<JUnitTestAction> {

  @Test
  void getActionMapping() {
    ActionMapping mapping = getActionMapping("/test/testAction.action");
    assertNotNull(mapping);
    assertEquals("/test", mapping.getNamespace());
    assertEquals("testAction", mapping.getName());
  }

  @Test
  void getActionProxy() throws Exception {
    // set parameters before calling getActionProxy
    request.setParameter("name", "FD");

    ActionProxy proxy = getActionProxy("/test/testAction.action");
    assertNotNull(proxy);

    JUnitTestAction action = (JUnitTestAction) proxy.getAction();
    assertNotNull(action);

    String result = proxy.execute();
    assertEquals(Action.SUCCESS, result);
    assertEquals("FD", action.getName());
  }

  @Test
  void executeAction() throws ServletException, UnsupportedEncodingException {
    String output = executeAction("/test/testAction.action");
    assertEquals("Hello", output);
  }

  @Test
  void getValueFromStack() throws ServletException, UnsupportedEncodingException {
    request.setParameter("name", "FD");
    executeAction("/test/testAction.action");
    String name = (String) findValueAfterExecute("name");
    assertEquals("FD", name);
  }
}
