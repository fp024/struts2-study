package org.fp024.struts2.study.demo.json;

import flexjson.JSONException;
import flexjson.ObjectBinder;
import flexjson.ObjectFactory;
import flexjson.transformer.AbstractTransformer;

import java.lang.reflect.Type;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.Temporal;

/** flexjson.transformer.DateTransformer 을 참고해서 변환기를 만들어봤다. */
public class LocalDateTimeTransformer extends AbstractTransformer implements ObjectFactory {
  private final String dateFormat;
  /**
   * DateTransformer에서는 SimpleDateFormat을 ThreadLocal에 넣어서 사용하는 부분이 있었는데,<br>
   * DateTimeFormatter가 thread-safe해서, 멤버 필드로 두고 직접 접근해서 사용해서 해도 될 것 같다.<br>
   *
   * @see flexjson.transformer.DateTransformer
   */
  private final DateTimeFormatter formatter;

  public LocalDateTimeTransformer(String dateFormat) {
    this.dateFormat = dateFormat;
    this.formatter = DateTimeFormatter.ofPattern(dateFormat);
  }

  @Override
  public void transform(Object value) {
    if (value == null) {
      getContext().write("null");
      return;
    }

    if (!(value instanceof Temporal)) {
      throw new JSONException(
          String.format(
              "The value(%s) type is instanceof Temporal.", value.getClass().getSimpleName()));
    }
    getContext().writeQuoted(formatter.format((Temporal) value));
  }

  @Override
  public Object instantiate(
      ObjectBinder context, Object value, Type targetType, Class targetClass) {
    try {
      return LocalDateTime.parse(value.toString(), formatter);
    } catch (Exception e) {
      throw new JSONException(
          String.format(
              "%s: Failed to parse %s with %s pattern.",
              context.getCurrentPath(), value, dateFormat),
          e);
    }
  }
}
