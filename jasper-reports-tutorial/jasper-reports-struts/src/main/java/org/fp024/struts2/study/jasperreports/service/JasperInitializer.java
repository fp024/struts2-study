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
public class JasperInitializer {
  private final String compiledJasperFilePath;

  public JasperInitializer(ServletContext servletContext) {
    this.compiledJasperFilePath =
        servletContext.getRealPath("/WEB-INF/jasper/") + "our_compiled_template.jasper";
    LOGGER.info(
        "### {}{}", servletContext.getRealPath("/WEB-INF/jasper/"), "our_compiled_template.jasper");
  }

  @PostConstruct
  public void afterPropertiesSet() throws Exception {
    try {
      LOGGER.info("=== Start JasperReport compile ===");
      JasperCompileManager.compileReportToFile(
          Optional.ofNullable(
                  JasperInitializer.class.getResource("/jasper/our_jasper_template.jrxml"))
              .orElseThrow(
                  () -> new IllegalStateException("our_jasper_template.jrxml File not found."))
              .getFile(),
          compiledJasperFilePath);
      LOGGER.info("=== End JasperReport compile ===");
    } catch (Exception e) {
      throw new IllegalStateException("Failed to compile, " + e.getMessage(), e);
    }
  }

  @PreDestroy
  public void destroy() throws Exception {
    File templateFile = new File(compiledJasperFilePath);
    LOGGER.info(
        "=== Compiled JasperReport file ({}) delete result: {} ===",
        templateFile.getAbsolutePath(),
        templateFile.delete());
  }
}
