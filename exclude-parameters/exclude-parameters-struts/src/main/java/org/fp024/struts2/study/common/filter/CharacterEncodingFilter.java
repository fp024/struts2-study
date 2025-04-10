package org.fp024.struts2.study.common.filter;

import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.FilterConfig;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import java.io.IOException;
import lombok.extern.slf4j.Slf4j;

/**
 * 문자셋 UTF-8 강제 설정
 *
 * <p>이 프로젝트에 jsp 파일들만 있어서 반드시 필요한 필터는 아니지만, 일단 넣었다. <br>
 * (모든 jsp 파일은 pageEncoding="UTF-8" 이 페이지 최상위에 설정되어있음) <br>
 * html, js 등의 파일에 대해서 jetty 환경에서 이 필터를 설정해야 UTF-8로 처리할 수 있다. <br>
 */
@Slf4j
public class CharacterEncodingFilter implements Filter {
  private String encoding = "UTF-8";

  @Override
  public void init(FilterConfig filterConfig) {
    encoding = filterConfig.getInitParameter("encoding");
  }

  @Override
  public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
      throws IOException, ServletException {
    LOGGER.info(">>> CharacterEncodingFilter Encoding: {}", encoding);
    request.setCharacterEncoding(encoding);
    response.setCharacterEncoding(encoding);
    chain.doFilter(request, response);
  }

  @Override
  public void destroy() {}
}
