package org.apache.struts2.junit;

import org.apache.struts2.XWorkJUnit4TestCase;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;

public abstract class XWorkJUnit5UserCustomTestCase extends XWorkJUnit4TestCase {

  @BeforeEach
  public void setUp() throws Exception {
    super.setUp();
  }

  @AfterEach
  public void tearDown() throws Exception {
    super.tearDown();
  }
}
