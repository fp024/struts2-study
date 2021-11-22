package org.fp024.struts2.study.demo.filter;

import lombok.extern.slf4j.Slf4j;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import java.io.IOException;

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
