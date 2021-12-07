# 보안

> 원문: https://struts.apache.org/security/

* 보안 Tips
  * Config Browser 플러그인에 대한 접근을 제한할 것
  * 동일한 네임스페이스에서 다른 엑세스 수준을 혼합하지 말 것
  * JSP 파일을 직접 노출하지 말 것
  * devMode를 비활성화할 것
  * 로깅 수준을 줄일 것
  * UTF-8 인코딩을 사용할 것
  * 필요하지 않을 때 setter메서드를 정의하지 말 것
  * 유입된 값을 현지화 로직의 입력으로 사용하지 말 것
  * 유입된 신뢰할 수 없는 사용자 입력을 강제 표현식 평가에 사용하지 말 것
  * 원시 EL 표현식 대신 Struts 태그 사용할 것 
  * 사용자 정의 오류 페이지를 정의할 것
  * 쉽게 적용할 수 있는 경우 ONGL 표현식 주입 공격으로 부터 사전 예방적으로 보호할 것
    * 샌드박스 안에서 ONGL 표현식 실행
    * ONGL 표현식에 최대 허용 길이 적용
* 내부 보안 메커니즘(원리나 구조)
  * 정적 메서드 엑세스
  * ONGL을 사용하여 액션의 메서드 호출
  * 허용 / 제외 패턴
  * 엄격한 메서드 호출(Invocation)
  * 메타데티어 가져오기(Fetch Metadata)를 사용한 리소스 격리
  * COOP 및 COEP를 사용한 교차 출처(Cross Origin) 격리



## 보안 Tips

Apache Struts 2는 어떠한 보안 메커니즘도 제공하지 않습니다 - 순수한 웹 프레임워크일 뿐 입니다. 다음은 Apache Struts 2를 사용하여 애플리케이션을 개발하는 동안 고려해야 할 몇 가지 팁입니다. 



### Config Browser 플러그인에 대한 접근을 제한할 것

[Config Browser Plugin](https://struts.apache.org/plugins/config-browser/)은 내부 구성을 노출하며 개발 단계에서만 사용해야 합니다. 프로덕션 사이트에서 사용해야 하는 경우 엑세스를 제한하는 것이 좋습니다. - 기본 인증 또는 기타 보안 메커니즘을 사용할 수 있습니다. (예: [Apache Shiro](https://shiro.apache.org/))



### 동일한 네임스페이스에서 다른 엑세스 수준을 혼합하지 말 것

다른 리소스에 대한 액세스는 URL 패턴을 기반으로 제어되는 경우가 매우 많습니다. 아래 조각을 참조해보세요. 이 때문에 동일한 네임스페이스에서 보안 수준이 다른 액션을 혼합할 수 없습니다. 항상 보안 수준별로 하나의 네임스페이스에서 액션을 그룹화 하도록 합니다.

```xml
<security-constraint>
    <web-resource-collection>
        <web-resource-name>admin</web-resource-name>
        <url-pattern>/secure/*</url-pattern>
    </web-resource-collection>
    <auth-constraint>
        <role-name>admin</role-name>
    </auth-constraint>
</security-constraint>
```



### JSP 파일을 직접 노출하지 말 것

JSP 파일에 직접 액세스하는 것이 허용되지 않도록 액션 뒤에 항상 JSP 파일을 숨겨서, 예측할 수 없는 보안 취약점으로 이어지지 않게 해야합니다. 모든 JSP 파일을 `WEB-INF` 폴더 아래에 넣으면 그렇게 할 수 있습니다.

* 대부분의 Java EE 컨테이너는 `WEB-INF` 폴더 아래에 있는 파일에 대한 액세스를 제한합니다. 두 번째 옵션은 `web.xml` 파일에 보안 제약 조건을 추가하는 것입니다 :

  ```xml
  <!-- 순수 JSP 파일에 대한 액세스 제한 - Struts 액션을 통해서만 액세스 가능 -->
  <security-constraint>
      <display-name>No direct JSP access</display-name>
      <web-resource-collection>
          <web-resource-name>No-JSP</web-resource-name>
          <url-pattern>*.jsp</url-pattern>
      </web-resource-collection>
      <auth-constraint>
          <role-name>no-users</role-name>
      </auth-constraint>
  </security-constraint>
  
  <security-role>
      <description>Don't assign users to this role</description>
      <role-name>no-users</role-name>
  </security-role>
  ```

가장 좋은 방법은 두 솔루션을 모두 사용하는 것입니다.



### devMode를 비활성화할 것

`devMode`는 개발 시간 동안 매우 유용한 옵션으로, 앱에 대한 깊은 자기 관찰(introspection)과 디버깅을 허용합니다. 

그러나 프로덕션에서는 애플리케이션이 애플리케이션 내부에 너무 많은 정보를 표시하거나 위험한 파라미터 표현식을 평가할 수 있습니다. 프로덕션 환경에 애플리케이션을 배포하기 전에 항상 `devMode`를 비활성화하십시오. 기본적으로 비활성화되어 있지만 `struts.xml`에 `true`로 설정하는 줄이 포함되어 있을 수 있습니다. 가장 좋은 방법은 프로덕션 배포를 위해 `struts.xml`에 다음 설정이 적용되었는지 확인하는 것입니다 :

```xml
<constant name ="struts.devMode" value="false" />
```



### 로깅 수준을 줄일 것

**DEBUG**에서 **INFO** 이하로 로깅 수준을 줄이는 것이 좋습니다. 프레임워크의 클래스는 로그 파일을 오염시키는 많은 로깅 항목을 생성할 수 있습니다. 프레임워크에 속한 클래스에 대해 로깅 수준을 **WARN**으로 설정할 수도 있습니다, 아래 Log4j2 구성 참조 예제를 확인하세요.

```xml
<?xml version="1.0" encoding="UTF-8"?>
<Configuration>
    <Appenders>
        <Console name="STDOUT" target="SYSTEM_OUT">
            <PatternLayout pattern="%d %-5p [%t] %C{2} (%F:%L) - %m%n"/>
        </Console>
    </Appenders>
    <Loggers>
        <Logger name="com.opensymphony.xwork2" level="warn"/>
        <Logger name="org.apache.struts2" level="warn"/>
        <Root level="info">
            <AppenderRef ref="STDOUT"/>
        </Root>
    </Loggers>
</Configuration>
```



### UTF-8 인코딩을 사용할 것

Apache Struts 2로 애플리케이션을 빌드할 때 항상 `UTF-8` 인코딩을 사용하세요. JSP를 사용할 때 각 JSP 파일에 다음 헤더를 추가하세요.

```jsp
<%@ page contentType="text/html; charset=UTF-8" %>
```



### 필요하지 않을 때 setter메서드를 정의하지 말 것

setter 및 getter를 통해 어떤 것도 노출되지 않도록 신중하게 액션을 설계하지 않으면 잠재적인 보안 취약점이 발생할 수 있습니다. 어느 액션의 setter라도 신뢰할 수 없는 사용자의 의심스러운 표현식을 포함할 수 있는 유입된 값을 설정하는데 사용될 수 있습니다. 일부 Struts 결과는 ValueStack의 값을 기반으로 파라미터를 자동으로 채웁니다(대부분의 경우 작업은 루트임). 이는 유입 값이 이 프로세스 동안 표현식으로 평가됨을 의미합니다.



### 유입된 값을 현지화 로직의 입력으로 사용하지 말 것

모든 `TextProvider`의 `getText(...)` 메서드(예: `ActionSupport`에 있음)는 텍스트를 적절하게 현지화하기 위해 메시지에 포함된 파라미터의 평가를 수행합니다. 이는 `getText(...)` 메서드와 함께 유입된 요청 파라미터를 사용하는 것은 잠재적으로 위험하므로 피해야 합니다. 

액션이 `message` 프로퍼티(클래스 멤버 필드) 에 대한 getter 및 setter를 구현한다고 가정하고 아래 예를 참조해보세요. 아래 코드는 OGNL 표현식을 삽입할 수 있습니다.

```java
public String execute() throws Exception {
    message = getText(getMessage());
    return SUCCESS;
}
```

**유입된 요청 파라미터의 값을 현지화 로직의 일부로서 사용하지 마세요.**



### 유입된 신뢰할 수 없는 사용자 입력을 강제 표현식 평가에 사용하지 말 것

`%{...}` 구문을 사용하여 많은 태그의 속성에서 강제 표현식 평가를 사용할 수 있습니다. 이것은 매우 강력한 옵션이지만 잘못된 데이터와 함께 사용하면 원격 코드 실행으로 이어질 수 있습니다. 입력을 확인하지 않았거나 사용자에의해 전달될 수 있는 경우 강제 표현식 평가를 사용하지 마세요.



### 원시 EL 표현식 대신 Struts 태그 사용할 것 

JSP EL은 어떤 종류의 이스케이프도 수행하지 않으므로 전용 함수를 사용하여 이 작업을 수행해야 합니다. [이 예](https://stackoverflow.com/questions/6134411/jstl-escaping-special-characters/6135001#6135001)를 참조하세요. 유입 값에 원시 `${}` EL 표현식을 사용하지 마십시오. 페이지에 악성 코드를 삽입할 수 있습니다.

가장 안전한 옵션은 Struts 태그를 대신 사용하는 것입니다.



### 사용자 정의 오류 페이지를 정의할 것

[S2-006](https://cwiki.apache.org/confluence/display/WW/S2-006)에서 언급했듯이 고유한 오류 페이지를 정의하는 것이 좋습니다. 이것은 Struts가 자동으로 생성된 오류 페이지에서 액션의 이름을 이스케이프 하지 않기 때문에, 고유한 오류페이지를 정의하게되면 XSS 공격에 사용자를 노출시키는 것을 방지합니다.

아래와 같이 [DMI](https://struts.apache.org/core-developers/action-configuration#dynamic-method-invocation)를 비활성화하거나

```xml
<constant name="struts.enable.DynamicMethodInvocation" value="false" />
```

에러 페이지를 정의합니다.

```xml
<global-results>
  <result name="error">/error_page.jsp</result>
</global-results>
 
<global-exception-mappings>
  <exception-mapping exception="java.lang.Exception" result="error"/>
</global-exception-mappings>
```



### 쉽게 적용할 수 있는 경우 ONGL 표현식 주입 공격으로 부터 사전 예방적으로 보호할 것

프레임워크에는 OGNL 기술 사용과 관련된 중요한 보안 버그의 역사가 있습니다; 실행 코드를 생성하거나 변경할 수 있는 능력으로 인해 OGNL은 이를 사용하는 모든 프레임워크에 중요한 보안 결함을 들여올 수 있습니다. 여러 Struts 2 버전이 OGNL 보안 결함에 취약했습니다. 결과적으로 우리는 OGNL 3.1.24 및 Struts 2.5.22 이후로 OGNL과 프레임워크에 다음과 같은 사전 예방적 옵션 가능성을 갖추었습니다. 이 옵션들은 기본적으로 비활성화되어 있지만 활성화하면 아직 알려지지 않은 잠재적인 OGNL 표현식 주입 결함으로부터 사전 예방적으로 보호할 수 있습니다.

> 참고: 현재 앱 기능이 중단될 수 있습니다. 프로덕션 환경에서 사용하기 전에 앱 UI 및 기능을 활성화한 상태에서 종합적으로 테스트하는 것이 좋습니다.

#### 샌드박스 안에서 ONGL 표현식 실행

이는 JVM 인수에 `-Dognl.security.manager`를 추가하여 간단히 수행할 수 있습니다. 따라서 OGNL은 Java Security Manager를 사용하여 권한 없이 샌드박스 내에서 OGNL 표현식(당신의 액션을 포함해서!)을 실행합니다. OGNL 표현식 실행에만 영향을 미치고 OGNL은 Java Security Manager를 이전 상태로 되돌립니다.



#### ONGL 표현식에 최대 허용 길이 적용

Struts 구성 키 `struts.ognl.expressionMaxLength`를 통해 이 기능을 사용하도록 설정할 수 있습니다. 따라서 OGNL은 지정된 값보다 긴 표현식을 평가하지 않습니다. 애플리케이션 내에서 사용되는 모든 유효한 OGNL 표현식을 허용하기에 충분히 큰 값을 선택합니다. 200-400 범위보다 큰 값은 보안 값이 감소합니다(이 시점에서 실제로 응용 프로그램의 긴 OGNL 표현식에 대한 "스타일 가드"일 뿐입니다).





## 내부 보안 메커니즘

Apache Struts 2에는 특정 클래스 및 Java 패키지에 대한 액세스를 차단하는 내부 보안 관리자가 포함되어 있습니다 - 이는 프레임워크의 모든 측면(유입 파라미터, JSP에서 사용되는 표현식 등)에 영향을 미치는 OGNL 전체 메커니즘입니다. 

제외된 패키지 및 클래스를 구성하는 데 사용할 수 있는 세 가지 옵션이 있습니다.

* `struts.excludedClasses` - 쉼표로 구분된 제외된 클래스 목록
* `struts.excludedPackageNamePatterns` - RegEx를 기반으로 패키지를 제외하는 데 사용되는 패턴 - 이 옵션은 단순 문자열 비교보다 느리지만 더 유연합니다.
* `struts.excludedPackageNames` - 쉼표로 구분된 제외 패키지 목록, `startsWith` 및 `equals`를 통한 간단한 문자열 비교에 사용

기본값은 다음과 같습니다.

```xml
<constant name="struts.excludedClasses"
          value="com.opensymphony.xwork2.ActionContext" />

<!-- 올바른 정규식이어야 하며 패키지 이름의 각 '.'는 이스케이프해야 합니다! -->
<!-- 단순한 문자열 비교보다는 유연하지만 느립니다. -->
<!-- constant name="struts.excludedPackageNamePatterns" value="^java\.lang\..*,^ognl.*,^(?!javax\.servlet\..+)(javax\..+)" / -->

<!-- 이것은 문자열 비교와 함께 사용되는 위의 간단한 버전입니다. -->
<constant name="struts.excludedPackageNames" value="java.lang,ognl,javax" />
```

다음 중 하나로 평가되는 모든 표현식 또는 대상이 차단되고 로그에 WARN이 표시됩니다.

```
[WARNING] Target class [class example.MyBean] or declaring class of member type [public example.MyBean()] are excluded!
```

이 경우 new MyBean()이 클래스의 새 인스턴스(JSP 내부)를 생성하는 데 사용되었습니다. 이러한 표현식의 대상이 java.lang.Class로 평가되기 때문에 차단됩니다.

struts.xml에서 위의 상수를 재정의하는 것이 가능하지만 이것을 피하고 애플리케이션의 디자인을 변경하세요! <br>(아마도 JSP내에서 ONGL 표현식으로 객체 생성을 할 때 이렇다는 것인지? 해봐야 확실히 알 것 같다.)



### 정적 메서드 엑세스

표현식에서 정적 메서드에 액세스하는 지원이 곧 비활성화됩니다. 추가 문제를 피하기 위해 애플리케이션을 리팩토링하는 것을 고려하세요! [WW-4348](https://issues.apache.org/jira/browse/WW-4348)을 확인하세요.



### ONGL을 사용하여 액션의 메서드 호출

이는 상속 계층이 크고 계층 전체에서 동일한 메서드 이름을 사용하는 작업에 영향을 줄 수 있으며, 이는 [WW-4405](https://issues.apache.org/jira/browse/WW-4405) 문제로 보고되었습니다. 아래 예를 참조해보세요:

```java
public class RealAction extends BaseAction {  
    @Action("save")
    public String save() throws Exception {
        super.save();
        return SUCCESS;
    }
}

public class BaseAction extends AbstractAction {
    public String save() throws Exception {
        save(Double.MAX_VALUE);
        return SUCCESS;
    }
}

public abstract class AbstractAction extends ActionSupport {
    protected void save(Double val) {
        // some logic
    }
}
```

이러한 경우 OGNL은 요청이 올 때 호출할 메서드를 적절하게 매핑할 수 없습니다. 이것은 ONGL 제한입니다. 문제를 해결하려면 계층 구조를 통해 동일한 메서드의 이름을 사용하지 마세요. 단순히 액션의 메서드를 `save()`에서 `saveAction()`으로 변경하고 `@Action("save")`어노테이션을 그대로 두어 `/save.action` 요청을 통해 이 작업을 호출할 수 있습니다.



### 허용 / 제외 패턴

2.3.20 버전부터 프레임워크는 파라미터 이름과 값을 수락/제외하는 데 사용되는 기본 구현이 있는 두 개의 새로운 인터페이스  [AcceptedPatternsChecker](https://struts.apache.org/maven/struts2-core/apidocs/com/opensymphony/xwork2/security/AcceptedPatternsChecker.html) 와 [ExcludedPatternsChecker](https://struts.apache.org/maven/struts2-core/apidocs/com/opensymphony/xwork2/security/ExcludedPatternsChecker.html)를 제공합니다. 이 두 인터페이스는 [Parameters Interceptor](https://struts.apache.org/core-developers/parameters-interceptor.html)와 [Cookie Interceptor](https://struts.apache.org/core-developers/cookie-interceptor.html)에서 파라미터가 허용 또는 제외되야는지 확인하는 데 사용됩니다. 이전에 `excludeParams`를 사용했다면 기본 구현에서 프레임워크에서 제공한 패턴과 사용자가 사용한 패턴을 비교해보세요.



### 엄격한 메서드 호출(Invocation)

이 메커니즘은 버전 2.5에서 도입되었습니다. [Dynamic Method Invocation](https://struts.apache.org/core-developers/action-configuration.html#dynamic-method-invocation)을 통한 연산자  쾅(bang) "!"으로 액세스할 수 있는 메서드를 제어할 수 있습니다. [Action Configuration](https://struts.apache.org/core-developers/action-configuration.html)의 Strict Method Invocation 섹션에서 자세히 읽어보세요.



### 메타데이터 가져오기(Fetch Metadata)를 사용한 리소스 격리

메타데이터 가져오기는 CSRF(Cross-Site Request Forgery:사이트간 요청 위조)와 같은 일반적인 교차 출처 공격에 대한 완화 기능입니다. 서버가 선호하는 리소스 격리 정책을 기반으로 교차 출처 공격으로부터 스스로를 방어할 수 있도록 설계된 웹 플랫폼 보안 기능입니다. 브라우저는 `Sec-Fetch-*` 헤더 집합에서 HTTP 요청의 컨텍스트에 대한 정보를 제공합니다. 이렇게 하면 요청을 처리하는 서버가 사용 가능한 리소스 격리 정책에 따라 요청을 수락할지 거부할지 결정할 수 있습니다.

리소스 격리 정책은 서버의 리소스가 외부 웹 사이트에서 요청되는 것을 방지합니다. 이 정책은 애플리케이션의의 모든 엔드포인트에 대해 이 정책을 사용하도록 설정하거나 교차 사이트 컨텍스트로 로드되어야 하는 엔드포인트는 정책 적용을 제외할 수 있습니다. 메타데이터 가져오기 및 리소스 분리에 대한 자세한 내용은 [여기](https://web.dev/fetch-metadata/)를 참조해보세요.



### COOP 및 COEP를 사용한 교차 출처(Cross Origin) 격리

> *참고: Struts 2.6 이후.*

[Cross-Origin Opener Policy (COOP)](https://developer.mozilla.org/en-US/docs/Web/HTTP/Headers/Cross-Origin-Opener-Policy)는 개발자가 사이드 채널 공격과 정보 유출에 대비하여 리소스를 격리할 수 있도록 하는 보안 완화 정책입니다. COOP 응답 헤더를 사용하면 문서가 신뢰할 수 없는 다른 출처로부터 더 잘 격리되도록 새 탐색 컨텍스트 그룹을 요청할 수 있습니다.

[Cross-Origin Embedder Policy (COEP)](https://developer.mozilla.org/en-US/docs/Web/HTTP/Headers/Cross-Origin-Embedder-Policy)는 문서가 로드할 권한을 명시적으로 부여하지 않는 cross-origin 리소스를 문서가 로드하는 것을 방지합니다.

COOP와 COEP는 개별적으로 활성화, 테스트 및 배포할 수 있는 독립적인 메커니즘입니다. 하나를 활성화하면 개발자가 다른 하나를 활성화할 필요가 없지만 COOP와 COEP를 함께 설정하면 개발자가 [스펙터](https://meltdownattack.com/)와 같은 사이드 채널 공격에 대한 걱정 없이 강력한 기능(예: `SharedArrayBuffer`, `performance.measureMemory()` 및 JS Self-Profiling API)을 안전하게 사용할 수 있습니다.  [COOP/COEP](https://docs.google.com/document/d/1zDlfvfTJ_9e8Jdc8ehuV4zMEu9ySMCiTGMS9y0GU92k/edit#bookmark=id.uo6kivyh0ge2) 및 [교차 출처 격리가 필요한 이유](https://web.dev/why-coop-coep/)에 대한 추가 정보를 읽어보세요.

정책에 권장되는 구성은 다음과 같습니다.

```
Cross-Origin-Embedder-Policy: require-corp;
Cross-Origin-Opener-Policy: same-origin;
```

COOP 및 COEP는 [CoopInterceptor](https://struts.apache.org/core-developers/coop-interceptor.html) 및 [CoepInterceptor](https://struts.apache.org/core-developers/coep-interceptor.html)를 사용하여 Struts에서 구현됩니다.