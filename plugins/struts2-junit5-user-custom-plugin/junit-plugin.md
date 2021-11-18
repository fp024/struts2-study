# JUnit 플러그인

> 원문: https://struts.apache.org/plugins/junit/

* 스프링을 사용하지 않는 Struts 액션
* 탬플릿
* 스프링을 사용한 Struts 액션

액션을 테스트하는 권장 방법은 작업 클래스를 인스턴스화하고 테스트하는 것입니다. JUnit 플러그인은 Struts 호출(invocation) 내에서 액션 테스트를 지원합니다. 즉, 전체 요청이 시뮬레이션되고 액션의 출력을 테스트할 수 있습니다.



## 스프링을 사용하지 않는 Struts 액션

Spring을 사용하지 않는 액션을 테스트하려면 StrutsTestCase를 상속하세요. 다음 예에서는 액션을 테스트하는 다양한 방법을 보여줍니다.

* 매핑

  ```xml
  <struts>
      <constant name="struts.objectFactory" value="spring"/>
      <package name="test" namespace="/test" extends="struts-default">
          <action name="testAction" class="org.apache.struts2.TestAction">
              <result type="freemarker">/template.ftl</result>
          </action>
      </package>
  </struts>
  ```

* 액션

  ```java
  public class TestAction extends ActionSupport {
      private String name;
  
      public String getName() {
          return name;
      }
  
      public void setName(String name) {
          this.name = name;
      }
  }
  ```

* JUnit

  ```java
  package org.apache.struts2;
  
  import org.apache.struts2.dispatcher.mapper.ActionMapping;
  
  import java.util.HashMap;
  import java.io.UnsupportedEncodingException;
  
  import com.opensymphony.xwork2.ActionProxy;
  import com.opensymphony.xwork2.Action;
  
  import javax.servlet.ServletException;
  
  public class StrutsTestCaseTest extends StrutsTestCase {
      public void testGetActionMapping() {
          ActionMapping mapping = getActionMapping("/test/testAction.action");
          assertNotNull(mapping);
          assertEquals("/test", mapping.getNamespace());
          assertEquals("testAction", mapping.getName());
      }
  
      public void testGetActionProxy() throws Exception {
          //set parameters before calling getActionProxy
          request.setParameter("name", "FD");
          
          ActionProxy proxy = getActionProxy("/test/testAction.action");
          assertNotNull(proxy);
  
          TestAction action = (TestAction) proxy.getAction();
          assertNotNull(action);
  
          String result = proxy.execute();
          assertEquals(Action.SUCCESS, result);
          assertEquals("FD", action.getName());
      }
  
      public void testExecuteAction() throws ServletException, UnsupportedEncodingException {
          String output = executeAction("/test/testAction.action");
          assertEquals("Hello", output);
      }
  
      public void testGetValueFromStack() throws ServletException, UnsupportedEncodingException {
          request.setParameter("name", "FD");
          executeAction("/test/testAction.action");
          String name = (String) findValueAfterExecute("name");
          assertEquals("FD", name);
      }
  }
  ```

  

## 탬플릿

JSP를 템플릿 엔진으로 사용하는 경우 컨테이너 외부에서 액션 출력을 테스트할 수 없습니다. [Embedded JSP Plugin](https://struts.apache.org/plugins/embedded-jsp/)을 사용하여 이러한 제한을 극복하고 클래스 경로 및 컨테이너 외부에서 JSP를 사용할 수 있습니다.

테스트를 용이하게 하는 데 사용할 수 있는 StrutsTestCase에 정의된 몇 가지 유틸리티 메서드와 Mock 객체가 있습니다.

- Methods:

  | 메서드 이름                          | 설명                                                         |
  | ------------------------------------ | ------------------------------------------------------------ |
  | executeAction(String)                | 액션에 대한 URL을 전달하면 액션의 출력이 반환됩니다. 이 출력은 "success"과 같은 액션 결과가 아니라 결과 스트림에 기록되는 것입니다. 이를 사용하려면 액션이 FreeMarker, Velocity 등과 같이 클래스 경로에서 읽을 수 있는 결과 유형을 사용해야 합니다(실험적인 [Embedded JSP 플러그인](https://struts.apache.org/plugins/embedded-jsp/)을 사용하는 경우 JSP도 사용할 수 있음) |
  | getActionProxy(String)               | 반환된 프록시 객체에서 execute()를 호출하여 액션을 호출하는 데 사용할 수 있는 Action Proxy를 빌드합니다. execute()의 반환 값은 "success"와 같은 액션 결과입니다. |
  | getActionMapping(String)             | URL에 대한 ActionMapping을 가져옵니다.                       |
  | injectStrutsDependencies(object)     | Struts 의존성을 객체에 주입합니다(의존성은 Inject로 표시됨). |
  | findValueAfterExecute(String)        | 액션이 실행된 후 Value Stack에서 객체를 찾습니다.            |
  | applyAdditionalParams(ActionContext) | 액션 호출 중에 사용되는 추가 파라미터 및 설정을 제공하기 위해 서브클래스에서 덮어쓸 수 있습니다. |
  | createAction(Class)                  | 프레임워크의 의존성을 주입해야 하는 액션을 인스턴스화하는 데 사용할 수 있습니다(예: ActionSupport를 상속하려면 일부 내부 의존성을 주입해야 함). |

  

- 필드

  | 필드                              | 설명                                                         |
  | --------------------------------- | ------------------------------------------------------------ |
  | MockHttpServletRequest request    | Struts에 전달할 request입니다. getActionProxy와 같은 메서드를 호출하기 전에 이 객체에 파라미터(request 파라미터)를 설정해야 합니다. |
  | MockHttpServletResponse response  | Struts에 전달된 response 객체, 이 클래스를 사용하여 출력, response 헤더 등을 테스트할 수 있습니다. |
  | MockServletContext servletContext | Struts에 전달된 서블릿 컨텍스트 객체                         |

  

## 스프링을 사용한 Struts 액션

`pom.xml`에 Spring 플러그인에 대한 종속성을 추가해야 합니다.

```xml
<dependency>
    <groupId>org.apache.struts</groupId>
    <artifactId>struts2-spring-plugin</artifactId>
    <version>STRUTS_VERSION</version>
</dependency>
```

스프링을 오브젝트 팩토리로 사용하는 경우 `StrutsSpringTestCase` 클래스를 사용하여 JUnit을 작성할 수 있습니다. 이 클래스는 `StrutsTestCase`를 상속하고 `ApplicationContext` 유형의 `applicationContext` 필드를 가집니다.

Spring 컨텍스트는 기본적으로 `classpath\*:applicationContext.xml`에서 로드됩니다. 다른 위치를 제공하려면 `getContextLocations`를 덮어씁니다(메서드 오버라이드해서 재정의 하라는 의미 같다).



---

## 의견

* 본문 말대로 JSP를 템플릿 엔진을 사용할 경우 컨테이너 외부에서 액션 출력을 테스트 할 수 없었다.
  * Freemaker 출력은 가능했음.
* Overwrite 덮어쓴다는 말이 몇번 나오는데, 상속한 클래스의 메서드를 오버라이브 해서, 재정의하란 의미 같다.

