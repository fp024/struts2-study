package org.fp024.struts2.study.demo.action;

import java.io.InputStream;
import java.time.Duration;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.apache.struts2.ActionSupport;
import org.apache.struts2.ServletActionContext;
import org.apache.struts2.interceptor.parameter.StrutsParameter;
import org.webjars.WebJarAssetLocator;

@Slf4j
public class WebJarsAction extends ActionSupport {
  @Setter(onMethod_ = @StrutsParameter)
  private String webjar;

  @Setter(onMethod_ = @StrutsParameter)
  private String path;

  @Getter private transient InputStream inputStream;

  @Getter private String contentType;

  @Override
  public String execute() {
    try {
      // WebJarAssetLocator를 활용하여 경로 찾기
      WebJarAssetLocator locator = new WebJarAssetLocator();
      String fullPath = locator.getFullPath(webjar, path);

      // 리소스를 ClassPath에서 로드
      ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
      this.inputStream = classLoader.getResourceAsStream(fullPath);

      // MIME 타입 설정
      this.contentType = ServletActionContext.getServletContext().getMimeType(fullPath);

      ServletActionContext.getResponse()
          .setHeader("Cache-Control", "max-age=" + Duration.ofDays(7).getSeconds());
      return SUCCESS;

    } catch (Exception e) {
      ServletActionContext.getResponse().setStatus(404);
      return NONE;
    }
  }
}
