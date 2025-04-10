package org.fp024.struts2.study.jasperreports.contextlistener;

import jakarta.servlet.ServletContextEvent;
import jakarta.servlet.ServletContextListener;
import java.io.File;
import java.util.Optional;
import lombok.extern.slf4j.Slf4j;
import net.sf.jasperreports.engine.JasperCompileManager;

@Slf4j
public class JasperReportsContextListener implements ServletContextListener {
  @Override
  public void contextInitialized(ServletContextEvent sce) {
    deleteCompiledJasperFile(sce);
    try {
      LOGGER.info("=== Start JasperReport compile ===");
      LOGGER.info(
          "### {}, {}",
          sce.getServletContext().getRealPath("/WEB-INF/jasper/README.md"),
          "our_compiled_template.jasper");
      JasperCompileManager.compileReportToFile(
          Optional.ofNullable(
                  JasperReportsContextListener.class.getResource(
                      "/jasper/our_jasper_template.jrxml"))
              .orElseThrow(
                  () -> new IllegalStateException("our_jasper_template.jrxml File not found."))
              .getFile(),
          sce.getServletContext().getRealPath("/WEB-INF/jasper/") + "our_compiled_template.jasper");
      LOGGER.info("=== End JasperReport compile ===");
    } catch (Exception e) {
      throw new IllegalStateException("Failed to compile, " + e.getMessage(), e);
    }
  }

  @Override
  public void contextDestroyed(ServletContextEvent sce) {
    deleteCompiledJasperFile(sce);
  }

  private void deleteCompiledJasperFile(ServletContextEvent sce) {
    File compiledJasperFile =
        new File(
            sce.getServletContext().getRealPath("/WEB-INF/jasper/")
                + "our_compiled_template.jasper");
    LOGGER.info(
        "=== Compiled JasperReport file ({}) delete result: {} ===",
        compiledJasperFile.getAbsolutePath(),
        compiledJasperFile.delete());
  }
}
