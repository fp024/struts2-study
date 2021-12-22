package org.fp024.struts2.study.jasperreports.service;

import lombok.extern.slf4j.Slf4j;
import net.sf.jasperreports.engine.JasperCompileManager;
import org.apache.struts2.ServletActionContext;

import java.util.Optional;

@Slf4j
public class JasperTemplateInitializer {
  public static final boolean isInit = JasperTemplateHolder.isInit;

  private static class JasperTemplateHolder {
    private static final boolean isInit = init();

    private static boolean init() {
      try {
        LOGGER.info("=== Jasper 컴파일 시작 ===");
        JasperCompileManager.compileReportToFile(
            Optional.ofNullable(
                    JasperTemplateHolder.class.getResource("/jasper/our_jasper_template.jrxml"))
                .orElseThrow(
                    () -> {
                      throw new IllegalStateException("our_jasper_template.jrxml 파일을 찾을 수 없습니다.");
                    })
                .getFile(),
            ServletActionContext.getServletContext().getRealPath("/WEB-INF/jasper")
                + "/our_compiled_template.jasper");
        LOGGER.info("=== Jasper 컴파일 종료 ===");
        return true;
      } catch (Exception e) {
        throw new IllegalStateException("jasper 컴파일 초기화 실패, " + e.getMessage(), e);
      }
    }
  }
}
