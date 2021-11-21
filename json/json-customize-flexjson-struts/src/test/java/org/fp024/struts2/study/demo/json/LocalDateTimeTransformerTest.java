package org.fp024.struts2.study.demo.json;

import flexjson.JSONContext;
import flexjson.ObjectBinder;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mockStatic;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class LocalDateTimeTransformerTest {
  /** JSON 문자열을 역직렬화하여 LocalDateTime 객체로 인스턴스화 */
  @Test
  void testInstantiate(@Mock ObjectBinder objectBinder) {
    String dateFormat = "yyyy-MM-dd HH:mm:ss";
    LocalDateTimeTransformer transformer = new LocalDateTimeTransformer(dateFormat);
    Object object = transformer.instantiate(objectBinder, "2021-11-21 15:53:50", null, null);

    assertEquals(LocalDateTime.of(2021, 11, 21, 15, 53, 50, 0), object);
  }

  /** LocalDateTime 객체를 직렬화하여 JSON 문자열로 만듦. */
  @Test
  void testTransform(@Mock JSONContext context) {
    String dateFormat = "yyyy-MM-dd HH:mm:ss";
    LocalDateTimeTransformer transformer = new LocalDateTimeTransformer(dateFormat);

    try (MockedStatic<JSONContext> mockedJSONContext =
        mockStatic(JSONContext.class, Mockito.CALLS_REAL_METHODS); ) {
      mockedJSONContext.when(() -> JSONContext.get()).thenReturn(context);

      transformer.transform(LocalDateTime.of(2021, 11, 21, 15, 53, 50, 0));
      verify(context, times(1)).writeQuoted("2021-11-21 15:53:50");
    }
  }
}
