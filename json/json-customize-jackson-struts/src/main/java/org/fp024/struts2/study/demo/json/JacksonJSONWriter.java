package org.fp024.struts2.study.demo.json;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import lombok.extern.slf4j.Slf4j;
import org.apache.struts2.json.JSONException;
import org.apache.struts2.json.JSONWriter;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Collection;
import java.util.regex.Pattern;

@Slf4j
public class JacksonJSONWriter implements JSONWriter {
  private String dateFormatter;

  @Override
  public String write(Object object) throws JSONException {
    return this.write(object, null, null, false);
  }

  @Override
  public String write(
      Object object,
      Collection<Pattern> excludeProperties,
      Collection<Pattern> includeProperties,
      boolean excludeNullProperties)
      throws JSONException {

    // https://github.com/FasterXML/jackson-modules-java8
    // https://github.com/FasterXML/jackson-modules-java8/tree/2.14/datetime

    ObjectMapper mapper = new ObjectMapper();
    JavaTimeModule javaTimeModule = new JavaTimeModule();

    // null 인 필드 제외
    // Flexjson 의 경우처럼 아무일도 하지 않는 ExcludeTransformer 를 따로 만들필요는 없는 것 같다.
    // https://www.baeldung.com/jackson-ignore-null-fields
    if (excludeNullProperties) {
      mapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
    }

    if (dateFormatter != null) {
      DateFormat dateFormat = new SimpleDateFormat(dateFormatter);
      // 날짜 형식 지정, Date 타입에 대해서만 적용된다.
      mapper.setDateFormat(dateFormat);

      // LocalDateTime의 포멧은 별도 지정해줘야한다.
      DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern(dateFormatter);
      javaTimeModule.addSerializer(
          LocalDateTime.class, new LocalDateTimeSerializer(dateTimeFormatter));
    }
    // dateFormatter를 지정하지 않았을 때, LocalDateTime 기본 형식은 2011-12-03T10:15:30 이런 모양이 된다.
    // (DateTimeFormatter.ISO_LOCAL_DATE_TIME)
    mapper.registerModule(javaTimeModule);

    ObjectWriter writer = mapper.writer();

    // 제외 프로퍼티 설정
    // TODO: 이름으로 런타임에 효율적으로 제외시키는 방법을 모르겠으니 일단 UnsupportedOperationException를 던지도록 하자
    if (excludeProperties != null) {
      for (Pattern p : excludeProperties) {
        LOGGER.info("pattern: {}", p.pattern());
        throw new UnsupportedOperationException("'excludeProperties' is unsupported properties.");
      }
    }

    // 포함 규칙을 코드 로직으로 설정할 때는, jackson 으로 어떻게 설정을 해야할지 애매해서
    // struts.xml에 includeProperties 설정할 때는 예외를 던지게 했다.
    if (includeProperties != null) {
      throw new UnsupportedOperationException("'includeProperties' is unsupported properties.");
    }

    try {
      return writer.writeValueAsString(object);
    } catch (Exception exception) {
      throw new JSONException(exception);
    }
  }

  @Override
  public void setIgnoreHierarchy(boolean ignoreHierarchy) {}

  @Override
  public void setEnumAsBean(boolean enumAsBean) {}

  @Override
  public void setDateFormatter(String dateFormatter) {
    this.dateFormatter = dateFormatter;
  }

  @Override
  public void setCacheBeanInfo(boolean cacheBeanInfo) {}

  @Override
  public void setExcludeProxyProperties(boolean excludeProxyProperties) {}
}
