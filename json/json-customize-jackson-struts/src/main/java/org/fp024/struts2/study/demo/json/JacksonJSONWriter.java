package org.fp024.struts2.study.demo.json;

import com.fasterxml.jackson.annotation.JsonFilter;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.ArrayDeque;
import java.util.Collection;
import java.util.Deque;
import java.util.regex.Pattern;
import lombok.extern.slf4j.Slf4j;
import org.apache.struts2.json.JSONException;
import org.apache.struts2.json.JSONWriter;
import tools.jackson.core.JsonGenerator;
import tools.jackson.databind.MapperFeature;
import tools.jackson.databind.ObjectMapper;
import tools.jackson.databind.ObjectWriter;
import tools.jackson.databind.SerializationContext;
import tools.jackson.databind.cfg.MapperBuilder;
import tools.jackson.databind.ser.PropertyFilter;
import tools.jackson.databind.ser.PropertyWriter;
import tools.jackson.databind.ser.std.SimpleFilterProvider;

@Slf4j
public class JacksonJSONWriter implements JSONWriter {
  private static final String RUNTIME_PROPERTY_FILTER_ID = "runtimePropertyFilter";

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

    MapperBuilder<ObjectMapper, ?> builder = new ObjectMapper().rebuild();

    // 💡Jackson 3.x는 알파벳 순서로 프로퍼티를 정렬하기 때문에, Jackson 2처럼 일단은 꺼보자!
    builder.disable(MapperFeature.SORT_PROPERTIES_ALPHABETICALLY);

    // null 인 필드 제외
    // Flexjson의 경우처럼 아무일도 하지 않는 ExcludeTransformer 를 따로 만들필요는 없는 것 같다.
    // https://www.baeldung.com/jackson-ignore-null-fields
    if (excludeNullProperties) {
      builder.changeDefaultPropertyInclusion(
          handler -> //
          handler.withValueInclusion(JsonInclude.Include.NON_NULL));
    }

    if (dateFormatter != null) {
      DateFormat dateFormat = new SimpleDateFormat(dateFormatter);
      // 날짜 형식 지정, Date 타입에 대해서만 적용된다.
      builder.defaultDateFormat(dateFormat);

      // LocalDateTime의 포멧은 별도 지정해줘야한다.
      builder.withConfigOverride(
          LocalDateTime.class, //
          cfg -> cfg.setFormat(JsonFormat.Value.forPattern("MM/dd/yyyy")));
    }
    // dateFormatter를 지정하지 않았을 때, LocalDateTime 기본 형식은 2011-12-03T10:15:30 이런 모양이 된다.
    // (DateTimeFormatter.ISO_LOCAL_DATE_TIME)

    builder.addMixIn(Object.class, DynamicPropertyFilterMixIn.class);
    ObjectMapper mapper = builder.build();
    ObjectWriter writer = mapper.writer(createFilterProvider(excludeProperties, includeProperties));

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

  private SimpleFilterProvider createFilterProvider(
      Collection<Pattern> excludeProperties, Collection<Pattern> includeProperties) {
    return new SimpleFilterProvider()
        .setFailOnUnknownId(false)
        .addFilter(
            RUNTIME_PROPERTY_FILTER_ID,
            new RuntimePropertyFilter(excludeProperties, includeProperties));
  }

  @JsonFilter(RUNTIME_PROPERTY_FILTER_ID)
  private interface DynamicPropertyFilterMixIn {}

  private static final class RuntimePropertyFilter implements PropertyFilter {
    private final Collection<Pattern> excludeProperties;
    private final Collection<Pattern> includeProperties;
    private final Deque<String> propertyPath = new ArrayDeque<>();

    private RuntimePropertyFilter(
        Collection<Pattern> excludeProperties, Collection<Pattern> includeProperties) {
      this.excludeProperties = excludeProperties;
      this.includeProperties = includeProperties;
    }

    @Override
    public void serializeAsProperty(
        Object pojo, JsonGenerator generator, SerializationContext provider, PropertyWriter writer)
        throws Exception {

      String currentPath = toPropertyPath(writer.getName());
      if (!shouldSerialize(currentPath)) {
        writer.serializeAsOmittedProperty(pojo, generator, provider);
        return;
      }

      propertyPath.addLast(writer.getName());
      try {
        writer.serializeAsProperty(pojo, generator, provider);
      } finally {
        propertyPath.removeLast();
      }
    }

    @Override
    public void serializeAsElement(
        Object elementValue,
        JsonGenerator generator,
        SerializationContext provider,
        PropertyWriter writer)
        throws Exception {
      writer.serializeAsElement(elementValue, generator, provider);
    }

    @Override
    public void depositSchemaProperty(
        PropertyWriter writer,
        tools.jackson.databind.jsonFormatVisitors.JsonObjectFormatVisitor visitor,
        SerializationContext provider) {
      writer.depositSchemaProperty(visitor, provider);
    }

    @Override
    public PropertyFilter snapshot() {
      return new RuntimePropertyFilter(excludeProperties, includeProperties);
    }

    private boolean shouldSerialize(String propertyPath) {
      if (matches(excludeProperties, propertyPath)) {
        return false;
      }

      if (includeProperties == null || includeProperties.isEmpty()) {
        return true;
      }

      return matches(includeProperties, propertyPath) || hasIncludedDescendant(propertyPath);
    }

    private boolean matches(Collection<Pattern> patterns, String propertyPath) {
      if (patterns == null || patterns.isEmpty()) {
        return false;
      }

      for (Pattern pattern : patterns) {
        if (pattern.matcher(propertyPath).matches()) {
          return true;
        }
      }
      return false;
    }

    private boolean hasIncludedDescendant(String propertyPath) {
      for (Pattern pattern : includeProperties) {
        String candidate = pattern.pattern();
        if (candidate.startsWith(propertyPath + ".")
            || candidate.startsWith(propertyPath + "\\.")
            || candidate.startsWith(propertyPath + "[")
            || candidate.startsWith(propertyPath + "\\[")) {
          return true;
        }
      }
      return false;
    }

    private String toPropertyPath(String propertyName) {
      if (propertyPath.isEmpty()) {
        return propertyName;
      }

      StringBuilder builder = new StringBuilder();
      for (String pathSegment : propertyPath) {
        if (!builder.isEmpty()) {
          builder.append('.');
        }
        builder.append(pathSegment);
      }
      builder.append('.').append(propertyName);
      return builder.toString();
    }
  }
}
