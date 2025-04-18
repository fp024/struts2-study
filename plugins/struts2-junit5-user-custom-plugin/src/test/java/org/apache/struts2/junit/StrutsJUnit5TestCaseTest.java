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
package org.apache.struts2.junit;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import org.apache.struts2.ActionProxy;
import org.junit.jupiter.api.Test;

/** User: maurizio cucchiara Date: 8/15/11 Time: 7:04 PM */
class StrutsJUnit5TestCaseTest extends StrutsJUnit5TestCase<JUnitTestAction> {
  @Test
  void testExecuteActionAgainstCustomStrutsConfigFile() throws Exception {
    String output = executeAction("/test/testAction-2.action");
    assertEquals("Test-2", output);
  }

  @Test
  void testSessionInitialized() {
    ActionProxy proxy = getActionProxy("/test/testAction-2.action");
    assertNotNull(
        proxy.getInvocation().getInvocationContext().getSession(),
        "invocation session should being initialized");
  }

  @Override
  protected String getConfigPath() {
    return "struts-test.xml";
  }
}
