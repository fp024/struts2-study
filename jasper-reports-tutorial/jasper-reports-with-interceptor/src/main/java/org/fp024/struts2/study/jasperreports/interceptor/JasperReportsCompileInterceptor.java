package org.fp024.struts2.study.jasperreports.interceptor;

import com.opensymphony.xwork2.ActionInvocation;
import com.opensymphony.xwork2.interceptor.AbstractInterceptor;
import java.io.File;
import java.util.Optional;
import lombok.extern.slf4j.Slf4j;
import net.sf.jasperreports.engine.JasperCompileManager;
import org.apache.struts2.ServletActionContext;

@Slf4j
public class JasperReportsCompileInterceptor extends AbstractInterceptor {
  @Override
  public String intercept(ActionInvocation invocation) throws Exception {
    final String compiledJasperFile =
        ServletActionContext.getServletContext()
            .getRealPath("/WEB-INF/jasper/our_compiled_template.jasper");

    if (!new File(compiledJasperFile).exists()) {
      try {
        LOGGER.info("=== Start JasperReport compile ===");
        JasperCompileManager.compileReportToFile(
            Optional.ofNullable(
                    JasperReportsCompileInterceptor.class.getResource(
                        "/jasper/our_jasper_template.jrxml"))
                .orElseThrow(
                    () -> new IllegalStateException("our_jasper_template.jrxml File not found."))
                .getFile(),
            compiledJasperFile);
        LOGGER.info("=== End JasperReport compile ===");
      } catch (Exception e) {
        throw new IllegalStateException("Failed to compile, " + e.getMessage(), e);
      }
    }

    return invocation.invoke();
  }
}
