package org.fp024.struts2.study.register.interceptor;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import lombok.extern.slf4j.Slf4j;
import org.apache.struts2.ActionInvocation;
import org.apache.struts2.interceptor.AbstractInterceptor;
import org.fp024.struts2.study.register.action.RegisterAction;

@Slf4j
public class SimpleInterceptor extends AbstractInterceptor {
  /** DateTimeFormatter 클래스는 스레드 세이프 함 */
  private static final DateTimeFormatter DATE_TIME_FORMATTER =
      DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

  public String intercept(ActionInvocation invocation) throws Exception {
    Object action = invocation.getAction();
    if (action.getClass() == RegisterAction.class) {
      LocalDateTime accessDateTime = LocalDateTime.now();
      LOGGER.info("{} 인터셉터 접근: {}", getClass(), accessDateTime.format(DATE_TIME_FORMATTER));
      ((RegisterAction) action).setAccessDate(LocalDateTime.now());
    }
    return invocation.invoke();
  }
}
