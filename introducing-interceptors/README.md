# 인터셉터 입문

> 원문 : https://struts.apache.org/getting-started/introducing-interceptors.html

* 서문

* 인터셉터 입문

* 예제 실행

* 나만의 인터셉터 만들기

* 요약


이 튜토리얼의 예제 코드인 **interceptors**는 [struts-examples](https://github.com/apache/struts-examples)에서 체크아웃할 수 있습니다.



## 서문

지금까지의 튜토리얼은 Struts 2 프레임워크의 내부 작동을 탐구하지 않았습니다. 그러나 이 튜토리얼에서는 액션이 실행될 때마다 대부분의 작업을 수행하기 위해 Struts 2 프레임워크가 의존하는 주요 클래스 세트를 소개합니다. 이 튜토리얼의 예제 프로젝트에는 Struts XML 구성 파일(struts.xml)에서 Register 클래스의 execute 메서드에 매핑되는 register 링크가 있습니다. execute 메서드가 호출되기 전에 Struts 2 프레임워크에 의해 뒷단에서 많은 작업이 수행됩니다. 예를 들면:

1. 생성된 모든 예외 처리
2. String 요청 파라미터를 이름 값이 일치하는 Register 클래스의 인스턴스 필드로 변환
3. validate 메서드 및/또는 외부 XML 유효성 검증 호출

execute 메서드가 완료된 후에는 더 많은 작업을 수행해야 합니다.

1. 생성된 모든 예외 처리
2. 뷰 페이지에 표시하기 위해 Register 클래스의 인스턴스 필드를 String 값으로 변환
3. execute 메서드에서 반환된 result 문자열에 따라 올바른 뷰 페이지로 포워딩

위의 작업 목록은 완료되지 않았습니다. 다른 여러 작업이 액션 실행 전후에 수행됩니다.

Struts 2 사용의 이점은 이 모든 작업이 자동으로 발생한다는 것입니다. 여러분은 컨트롤러(Struts 2 ActionSupport 클래스), 서비스 계층, 데이터 액세스 계층, 도메인 모델 등의 로직에 집중할 수 있습니다.

Struts 2 [사용자 메일링](http://struts.apache.org/mail.html) 리스트는 도움을 받을 수 있는 훌륭한 장소입니다. 튜토리얼 예제 애플리케이션을 작동시키는데 문제가 있는 경우 Struts 2 메일리스트를 검색하세요. 문제에 대한 답을 찾지 못한 경우 메일링 리스트에 질문을 게시하세요.



## 인터셉터 입문

액션이 실행되기 전과 후에 Struts 2 프레임워크에 의해 수행되는 작업은 Struts 2 인터셉터에 의해 수행됩니다. 인터셉터는 특정 순서로 실행되는 Struts 2 core jar에 포함된 표준 Java 클래스입니다.

예제 애플리케이션에는 struts.xml에 package 노드가 있습니다. package 노드에는 값이 "struts-default"인 extends 속성이 있습니다. "struts-default" 값은 해당 패키지의 액션 전후에 실행될 특정 인터셉터 스택을 프레임워크에 확인합니다. 

인터셉터의 내부 작동, Struts 기본 스택에 속하는 인터셉터, Struts 2에 포함된 모든 인터셉터에 대해 자세히 알아보려면 [인터셉터 이해](https://struts.apache.org/core-developers/interceptors.html)를 방문하세요.

때로는 Struts 2 default 인터셉터 스택이 특정 액션에 필요한 것과 정확히 일치하지 않을 수 있습니다. Struts 2 default 스택에 속하지 않는 인터셉터를 사용할 수 있습니다. 개별 액션 또는 전체 액션 패키지에 대해 액션 또는 패키지가 사용해야 하는 다른 인터셉터 스택을 지정할 수 있습니다.

아래는 register 액션이 default 스택에서 제공하는 인터셉터 외에 logger와 timer 인터셉터를 모두 사용하도록 지정하는 방법입니다.

### 액션에 대한 특정 인터셉터 지정

```xml
<action name="register" class="org.apache.struts.register.action.Register" method="execute">
    <interceptor-ref name="timer" />
    <interceptor-ref name="logger" />
    <interceptor-ref name="defaultStack">
        <param name="exception.logEnabled">true</param>
        <param name="exception.logLevel">ERROR</param>
    </interceptor-ref>
    <result name="success">thankyou.jsp</result>
    <result name="input">register.jsp</result>
</action>
```

logger 인터셉터는 액션 실행의 시작과 끝을 기록합니다. timer 인터셉터는 액션 실행의 수행 시간(밀리초)을 기록합니다. 함께 사용되는 이 두 인터셉터는 개발자에게 유용한 피드백을 제공할 수 있습니다.

위의 코드 예제에서 세 개의 interceptor-ref 노드에 주목하십시오. 각각에는 name 속성에 대한 값이 있습니다. register 액션의 경우 프레임워크에게 `timer`, `logger` 및 `defaultStack` 인터셉터를 사용하도록 지시합니다. `defaultStack`은 일반적으로 액션에 대해 실행되는 모든 인터셉터입니다.

name 속성에 timer 값을 사용하는 방법과 timer 인터셉터가 있다는 것을 어떻게 알았을까요? Struts 2 문서의 [인터셉터](https://struts.apache.org/core-developers/interceptors.html) 문서 웹 페이지에는 Struts 2 프레임워크와 함께 제공되는 인터셉터 목록과 각 인터셉터의 이름 값이 나와 있습니다.

timer 인터셉터가 인터셉터의 defaultStack에 이미 포함되어 있지 않다는 것을 어떻게 알았을까요? 위에 언급했던 [인터셉터](https://struts.apache.org/core-developers/interceptors.html) 문서 웹 페이지에는 `defaultStack`에 속하는 인터셉터 목록이 있습니다.

param 노드도 확인하세요. 이러한 노드는 [Exception 인터셉터](https://struts.apache.org/core-developers/exception-interceptor.html)의 setLogEnabled 및 setLogLevel 메소드에 값을 제공하는 데 사용됩니다. true 및 ERROR 값을 제공하면 Struts 2 프레임워크가 애플리케이션 코드에서 잡지 못한 모든 예외를 기록하고 ERROR 수준에서 해당 예외를 기록합니다.



## 예제 실행

예제 애플리케이션에서 README 지침에 따라 애플리케이션을 빌드, 배포 및 실행합니다. logger 및 timer 인터셉터에 의해 생성된 로그 메시지를 보려면 JVM 콘솔로 전송된 출력을 보십시오. 다음과 유사한 로그 메시지가 표시되어야 합니다.

```
INFO: Starting execution stack for action //register
Nov 20, 2010 9:55:48 AM com.opensymphony.xwork2.util.logging.jdk.JdkLogger info
INFO: Finishing execution stack for action //register
Nov 20, 2010 9:55:48 AM com.opensymphony.xwork2.util.logging.jdk.JdkLogger info
INFO: Executed action /register!execute took 177 ms.
```

패키지의 모든 액션에 대해 logger 및 timer인터셉터를 실행하려면 `struts.xml`에서 다음을 사용합니다.

```xml
<package name="basicstruts2" extends="struts-default" > 
    <interceptors> 
        <interceptor-stack name="appDefault"> 
            <interceptor-ref name="timer" /> 
            <interceptor-ref name="logger" /> 
            <interceptor-ref name="defaultStack" /> 
        </interceptor-stack> 
    </interceptors>          

    <default-interceptor-ref name="appDefault" /> 
    <!-- rest of package omitted --> 
</package> 
```

위의 코드에서 interceptors 노드를 사용하여 `timer`, `logger` 및 `defaultStack` 인터셉터를 포함하는 인터셉터들의 새 스택을 정의합니다. 이 새로운 인터셉터 스택에 appDefault라는 이름을 지정합니다. 그런 다음 `default-interceptor-ref` 노드를 사용하여 이 패키지 노드 내부에 정의된 모든 액션에 대해 인터셉터의 `appDefault` 스택이 사용되도록 지정합니다. 따라서 이 패키지의 각 액션에 대해 timer 및 logger 인터셉터가 실행됩니다.

두 예제에서 우리는 여전히 defaultStack을 `interceptor-ref` 노드 중 하나로 포함하여 다른 모든 인터셉터를 실행하고 있습니다. 액션 또는 패키지에 사용할 인터셉터를 지정하면 해당 인터셉터만 실행됩니다. 따라서 예제에서 `defaultStack`에 대한 `interceptor-ref`를 생략했다면 logger 및 timer 인터셉터만 실행했을 것입니다.



## 나만의 인터셉터 만들기

사용자 고유의 인터셉터 스택을 지정할 뿐만 아니라 사용자 자신의 새 인터셉터를 작성하여 실행되는 스택에 추가할 수도 있습니다. Struts [인터셉터 작성하기](writing-interceptors.md) 가이드는 이를 수행하는 방법을 설명합니다. 예를 들어 인증 및 권한 부여를 처리하기 위해 자체 인터셉터를 만들 수 있습니다.



## 요약

인터셉터는 Struts 2 프레임워크에 강력함과 유연성을 모두 제공합니다. 개발자는 액션 클래스가 호출될 때 실행되는 인터셉터 스택에 인터셉터(Struts 2에 의해 제공되거나 자신이 만든 인터셉터)를 추가할 수 있습니다.



### >  [어노테이션](../annotations)으로 돌아가기 또는 [단위 테스트](../unit-testing)로 이동

---



## 스텝 진행...

* 프로젝트 변경사항
  * 프로젝트명: [interceptors-struts](interceptors-struts) 로 변경.
  * Spring 5 + JPA + Hibernate + HSQLDB 로 동작하도록 프로젝트를 구성.
  * 액션, 서비스, 레파지토리코드에 대한 테스트코드를 추가.
  * 나만의 인터셉터 만들기를 별도 가이드 링크만 해주시길레 그 가이드 보고 추가해보았다.
  * [SimpleInterceptor.java](./interceptors-struts/src/main/java/org/fp024/struts2/study/register/interceptor/SimpleInterceptor.java)
  * mockito-inline 사용해보았는데.. 훌륭한 모듈 같다. CALLS_REAL_METHODS 옵션을 지정하면 스텁으로 정의하지 않은 메서드는 실제 메서드를 호출하는 부분이 정말 마음에 들었다!
  
* [x] 서문

* [x] 인터셉터 입문

  * Logging 설정을 info로 항상태에서 logger, timer 인터셉터의 관련 로그

    ```
    2021-11-13 19:06:49,930 INFO  [qtp1588330347-43] interceptor.LoggingInterceptor (LoggingInterceptor.java:86) - Starting execution stack for action //register
    2021-11-13 19:06:49,931 INFO  [qtp1588330347-43] interceptor.SimpleInterceptor (SimpleInterceptor.java:21) - class org.fp024.struts2.study.register.interceptor.SimpleInterceptor 인터셉터 접근: 2021-11-13 19:06:49
    Hibernate:
        select
            person0_.email as email1_0_0_,
            person0_.age as age2_0_0_,
            person0_.first_name as first_na3_0_0_,
            person0_.last_name as last_nam4_0_0_,
            person0_.register_date as register5_0_0_
        from
            person person0_
        where
            person0_.email=?
    2021-11-13 19:06:50,659 INFO  [qtp1588330347-43] interceptor.LoggingInterceptor (LoggingInterceptor.java:86) - Finishing execution stack for action //register
    2021-11-13 19:06:50,660 INFO  [qtp1588330347-43] interceptor.TimerInterceptor (TimerInterceptor.java:205) - Executed action [//register!execute] took 729 ms.
    ```

    * 액션 실행 전후로 로그를 출력하는 logger 인터셉터 동작과, 액션 실행 이후 액션의 수행결과를 보여주는 timer 인터셉터의 로그를볼 수 있다.
    * [SimpleInterceptor.java](./interceptors-struts/src/main/java/org/fp024/struts2/study/register/interceptor/SimpleInterceptor.java) 는 사용자 정의 인터셉트를 추가했고, register액션의 필드에 접근 시간(LocalDateTime.now())를 저장하는 기능을 하는 간단한 인터셉터이다.

* [x] 예제 실행

  * 나의 예제 환경에는 log4j2.xml에서 형식을 따로 지정해줘서 그런지 로그출력 형식이 다르긴하다.

* [x] 나만의 인터셉터 만들기

* [x] 요약

  
