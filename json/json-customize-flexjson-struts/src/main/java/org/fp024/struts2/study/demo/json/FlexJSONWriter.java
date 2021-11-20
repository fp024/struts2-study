package org.fp024.struts2.study.demo.json;

import flexjson.JSONSerializer;
import flexjson.transformer.DateTransformer;
import lombok.extern.slf4j.Slf4j;
import org.apache.struts2.json.JSONException;
import org.apache.struts2.json.JSONWriter;

import java.util.Collection;
import java.util.Date;
import java.util.regex.Pattern;

/** Flexjson 을 사용해서 JSONWriter 사용자 정의 */
@Slf4j
public class FlexJSONWriter implements JSONWriter {
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
    // *.class를 struts.xml로 전달해줄 수 없다.
    JSONSerializer serializer = new JSONSerializer().exclude("*.class");
    if (excludeProperties != null) {
      for (Pattern p : excludeProperties) {
        LOGGER.info(">>> exclude pattern: {}", p.pattern());
        serializer = serializer.exclude(p.pattern());
      }
    }
    if (includeProperties != null) {
      for (Pattern p : includeProperties) {
        LOGGER.info(">>> include pattern: {}", p.pattern());
        serializer = serializer.include(p.pattern());
      }
    }
    if (excludeNullProperties) {
      serializer = serializer.transform(new ExcludeTransformer(), void.class);
    }
    if (dateFormatter != null) {
      serializer = serializer.transform(new DateTransformer(dateFormatter), Date.class);
    }
    try {
      return serializer.serialize(object);
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
