# 예외 처리

> 원문 : https://struts.apache.org/getting-started/exception-handling.html
>
> * ✨ Jetty의 ContextPath 설정을 프로젝트 이름 대신에 루트로 하기로해서 스크린샷의 브라우저 URL과 문서 내의 테스트 URL이 다를 수 있는데, 이부분 참고 부탁합니다.

* 소개
* 전역 예외 처리
* 액션 별 예외 처리
* 예외 로깅
* 브라우저에 예외 정보 표시
* 요약

이 튜토리얼의 코드인 **exception-handling**은 [struts-examples](https://github.com/apache/struts-examples)에서 체크아웃 할 수 있습니다.



### 소개

이 튜토리얼에서는 Struts 2 프레임워크가 웹 애플리케이션에서 생성된 잡히지 않은 예외를 처리할 수 있도록 하는 방법에 대해 알아볼 것입니다.<br>Struts 2는 잡히지 않은 예외를 자동으로 로깅하고 사용자를 오류 웹페이지로 리다이렉트하는 기능을 포함하는 강력한 예외 처리를 제공합니다.

Struts 2 [사용자 메일링](http://struts.apache.org/mail.html) 리스트는 도움을 받을 수 있는 훌륭한 장소입니다. 튜토리얼 예제 애플리케이션을 작동시키는데 문제가 있는 경우 Struts 2 메일리스트를 검색하세요. 문제에 대한 답을 찾지 못한 경우 메일링 리스트에 질문을 게시하세요.



### 전역 예외 처리

Struts 2 프레임워크를 사용하면 프레임워크가 잡히지 않은 예외를 처리하는 방법을 struts.xml에 지정할 수 있습니다. 처리 로직은 모든 액션(전역 예외 처리) 또는 특정 액션에 적용할 수 있습니다. 먼저 전역 예외 처리를 활성화하는 방법에 대해 논의하겠습니다.

전역 예외처리를 활성화하려면 `struts.xml`에 `global-exception-mapping` 및 `global-results`의 두 노드를 추가해야합니다.<br>예를 들어 exception-handling 프로젝트에서 `struts.xml` 을 검토해보세요.

```xml
<global-results>
    <result name="securityerror">/securityerror.jsp</result>
    <result name="error">/WEB-INF/views/error.jsp</result>
</global-results>

<global-exception-mappings>
    <exception-mapping exception="org.apache.struts.register.exceptions.SecurityBreachException" result="securityerror" />
    <exception-mapping exception="java.lang.Exception" result="error" />
</global-exception-mappings>
```

전역 예외 매핑 노드는 Struts 2에게 특정 타입(또는 해당 타입의 자식)의 잡히지 않은 예외가 애플리케이션에 의해 던져질 경우 수행할 작업을 알려줍니다. 예를 들어 SecurityBreachException 이 발생했지만 집히지 않았다면 Struts 2 액션 클래스는 "securityerror"의 결과를 반환합니다. 잡히지 않은 다른 모든 예외는 Struts 2 액션 클래스가 "error"의 결과를 반환하도록 합니다.

전역 결과 매핑 노드는 결과 값을 특정 뷰 페이지와 연결합니다. 예를 들어 "securityerror" 결과는 프레임워크가 사용자의 브라우저를 securityerror.jsp 뷰 페이지로 리다이렉트 하도록 합니다.



### 액션 별 예외 처리

어떤 액션에 대해 특정 방식으로 예외를 처리해야하는 경우 액션 노드 내에서 `exception-mapping` 노드를 사용할 수 있습니다.

```xml
<action name="actionspecificexception" class="org.apache.struts.register.action.Register" method="throwSecurityException">
   <exception-mapping exception="org.apache.struts.register.exceptions.SecurityBreachException" result="login" />
   <result>/WEB-INF/views/register.jsp</result>
   <result name="login">/WEB-INF/views/login.jsp</result>
</action>
```

예제 애플리케이션의 struts.xml 파일의 위 액션 노드는 `throwSecurityException` 메서드가  `SecurityBreachException` 유형의 잡히지 않은 예외를 던지는 경우 Struts 2 프레임워크가 login 결과를 반환해야한다고 지정합니다. login 결과는 사용자의 브라우저가 login.jsp로 리다이렉트 되도록 합니다.

동일한 예외가 전역적으로 매핑되어있어도, 액션 별 매핑이 우선 적용됨을 알 수 있습니다.



### 예외 로깅

잡히지 않은 예외를 로깅하도록 Struts 2 프레임 워크를 설정할 수 있습니다. Struts 2 프레임워크에서 처리되는 예외의 로깅을 활성화 하려면 struts.xml에서 일부 파라미터 값을 지정해야합니다. [ExceptionMappingInterceptor 클래스 API](https://struts.apache.org/maven/struts2-core/apidocs/com/opensymphony/xwork2/interceptor/ExceptionMappingInterceptor.html)를 살펴보면 3가지 파라미터 값으로 로깅을 활성화하기 위한 설정(logEnabled), 사용할 로그 레벨(logLevel), 로그메시지에 지정할 로그 범주(logCategory)가 있습니다.

패키지에서 특정 인터셉터 스택을 사용하는 모든 액션에 대해 이러한 파라미터 값을  설정하려면 struts.xml의 package 노드를 여는 바로 아래에다 다음 내용을 포함합니다.

```xml
<interceptors>
    <interceptor-stack name="appDefaultStack">
        <interceptor-ref name="defaultStack">
            <param name="exception.logEnabled">true</param>
            <param name="exception.logLevel">ERROR</param>
        </interceptor-ref>
    </interceptor-stack>
</interceptors>

<default-interceptor-ref name="appDefaultStack" />
```

위의 인터셉터 노드는 appDefaultStack이라는 Struts 2 인터셉터의 새 스택을 설정합니다. 이 인터셉터 스택은 defaultStack을 기반으로 합니다. (defaultStack: Struts2 프레임워크에서 액션 클래스 메서드가 호출 될 때마다 기본적으로 실행되는 Struts 2 인터셉터) 

ExceptionMappingInterceptor는 기본 스택의 일부인 Struts 2 인터셉터 중 하나입니다. Struts의 defaultStack 정의에서 ExceptionMappingInterceptor는 `exception`의 이름이 주어집니다. 이름이 `exception.logEnabled`이고 값이 `true`인 pram 노드를 지정하여 ExceptionMappingInterceptor클래스의 logEnabled 매개변수(클래스 맴버변수)를 true로 설정합니다. 

이제 애플리케이션이 잡지 않은 예외를 던지면 Struts 2 프레임워크가 이를 처리하고 스택 트레이스를 포함하는 항목을 로그에 기록합니다. 위의 예에서는 이러한 예외를 ERROR 로깅 레벨로 설정했습니다. 

예제 애플리케이션에서 로깅은 서블릿 컨테이너의 콘솔에만 적용됩니다. (로그 설정은 `log4j2.xml` 파일 참조)



### 브라우저에 예외 정보  표시

`s:property` 태그를 `exception` 및 `exceptionStack` 값과 함께 사용하여 원하는 경우 브라우저에서 예외에 대한 정보를 표시할 수 있습니다. 예를 들어 error.jsp에는 아래 마크업이 있습니다.

```jsp
<h4>The application has malfunctioned.</h4>

<p>Please contact technical support with the following information:</p> 

<h4>Exception Name: <s:property value="exception" /> </h4>

<h4>Exception Details: <s:property value="exceptionStack" /></h4> 
```

예외 인터셉터가 트리거되면 예외 메시지와 예외의 스택 트레이스를 표시할 수 있는 필드에 추가됩니다.



### 요약

Struts 2는 잡히지 않은 예외를 처리하고 사용자를 적잘한 보기 페이지로 리다이렉트 하기 위한 사용하기 쉬운 설정을 제공합니다. 모든 액션에 대해 전역적으로 또는 특정 액션에 대해서만 예외 처리를 구성할 수 있습니다. 또한 Struts 2 프레임워크를 사용하여 잡히지 않은 예외를 로깅할 수 있습니다.




### >  [메시지 리소스 파일](../message-resource-files)로 돌아가기 또는 [Struts 디버깅](https://struts.apache.org/getting-started/debugging-struts.html)로 이동



---

## 예외 처리 예제 진행...

* 변경사항
  * 프로젝트명: [exception-handling-struts](exception-handling-struts)
  * 예제 프로젝트를 보니 **Struts 디버깅**에서 나올 내용들이 무의미하게 먼저 나온 부분들이 있는데 그런 부분은 코드상에서 제거했다.
  * Register의 validate() 메서드 제거... 예외 테스트할 때, 폼 입력이 없을 때, 이 메서드가 실행되어 NPE가 발생한다.
  * 내부코드를 보니 Log4j2의 클래스를 사용하고 있다.
  
* [x] 소개

* [x] 전역 예외 처리

* [x] 액션별 예외 처리

* [x] 예외 로깅

  * struts2-core-2.5.26의 struts-default.xml

    ```xml
    ...
    <interceptor name="exception" class="com.opensymphony.xwork2.interceptor.ExceptionMappingInterceptor"/>
    ...
    <interceptor-stack name="defaultStack">
        <interceptor-ref name="exception"/>
        <interceptor-ref name="alias"/>
        <interceptor-ref name="servletConfig"/>
        <interceptor-ref name="i18n"/>
        <interceptor-ref name="prepare"/>
        <interceptor-ref name="chain"/>
        <interceptor-ref name="scopedModelDriven"/>
        <interceptor-ref name="modelDriven"/>
        <interceptor-ref name="fileUpload"/>
        <interceptor-ref name="checkbox"/>
        <interceptor-ref name="datetime"/>
        <interceptor-ref name="multiselect"/>
        <interceptor-ref name="staticParams"/>
        <interceptor-ref name="actionMappingParams"/>
        <interceptor-ref name="params"/>
        <interceptor-ref name="conversionError"/>
        <interceptor-ref name="validation">
            <param name="excludeMethods">input,back,cancel,browse</param>
        </interceptor-ref>
        <interceptor-ref name="workflow">
            <param name="excludeMethods">input,back,cancel,browse</param>
        </interceptor-ref>
        <interceptor-ref name="debugging"/>
    </interceptor-stack>
    
    ```

    * xwork-default.xml 에도 defaultStack이 있는데, 여기는 exception 인터셉터가 포함되지 않음.

      ```xml
      <interceptor-stack name="defaultStack">
      	<interceptor-ref name="staticParams"/>
      	<interceptor-ref name="params"/>
      	<interceptor-ref name="conversionError"/>
      </interceptor-stack>
      ```

    * ExceptionMappingInterceptor 일부

      ```java
      public class ExceptionMappingInterceptor extends AbstractInterceptor {
          private static final Logger LOG = LogManager.getLogger(ExceptionMappingInterceptor.class);
      
          protected Logger categoryLogger;
          protected boolean logEnabled = false;
          protected String logCategory;
          protected String logLevel;
          ...
      ```

  * log4j2.xml에서 com.opensymphony.xwork2, org.apache.struts2 에 대해 debug 수준으로 로깅함. 그외 프로젝트 패키지 로그도 debug로 설정

* [x] 브라우저에 예외 정보 표시

* [x] 요약

