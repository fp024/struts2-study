package org.fp024.struts2.study.jasperreports.service;

import java.io.File;
import java.util.Optional;
import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.servlet.ServletContext;
import lombok.extern.slf4j.Slf4j;
import net.sf.jasperreports.engine.JasperCompileManager;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class JapserInitializer {
  private final String compiledJapserFilePath;

  public JapserInitializer(ServletContext servletContext) {
    this.compiledJapserFilePath =
        servletContext.getRealPath("/WEB-INF/jasper/our_compiled_template.jasper");
  }

  @PostConstruct
  public void afterPropertiesSet() throws Exception {
    try {
      LOGGER.info("=== Start JasperReport compile ===");
      JasperCompileManager.compileReportToFile(
          Optional.ofNullable(
                  JapserInitializer.class.getResource("/jasper/our_jasper_template.jrxml"))
              .orElseThrow(
                  () -> {
                    throw new IllegalStateException("our_jasper_template.jrxml File not found.");
                  })
              .getFile(),
          compiledJapserFilePath);
      LOGGER.info("=== End JasperReport compile ===");
    } catch (Exception e) {
      throw new IllegalStateException("Failed to compile, " + e.getMessage(), e);
    }
  }

  @PreDestroy
  public void destroy() throws Exception {
    File templteFile = new File(compiledJapserFilePath);
    LOGGER.info(
        "=== Compiled JasperReport file ({}) delete result: {} ===",
        templteFile.getAbsolutePath(),
        templteFile.delete());
  }
}
