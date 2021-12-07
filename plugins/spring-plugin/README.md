# 스프링 플러그인

> 원문: https://struts.apache.org/plugins/spring/

* 설명

* 사용법

  

## 설명

[스프링](https://spring.io/)은 "의존성 주입"이라는 기술을 사용하여 애플리케이션 객체의 중앙 집중식 자동화 구성 및 와이어링(wiring)을 제공하는 경량 컨테이너입니다.

스프링 플러그인은 Struts ObjectFactory를 재정의(overriding)하여 코어 프레임워크 객체 생성을 향상시키는 방식으로 작동합니다. 객체가 생성될 때 Struts 구성(struts.xml)의 class 속성을 사용하여 스프링 구성의 id 속성에 대응합니다. 만약 Struts 구성에서 발견되지 않으면 평소와 같이 생성을 시도한 다음 스프링에 의해 자동 연결됩니다. 액션들의 경우 스프링 2의 [빈 스코프](https://docs.spring.io/spring-framework/docs/2.0.x/reference/beans.html#beans-factory-scopes) 기능을 사용하여 액션 인스턴스의 범위를 세션, 애플리케이션 또는 사용자 정의 스코프로 지정하여 기본 요청별 범위 지정보다 높은 고급 사용자 정의를 제공할 수 있습니다.

> *기억하세요!* :
>
> **스프링에 액션을 등록할 필요가 없습니다.** 필요한 경우 Spring 대안이 있지만 프레임워크는 액션 매핑에서 액션 객체를 자동으로 생성합니다. 그러나 스프링을 사용하여 액션를 주입하려는 경우 옵션이 있습니다.

#### 특징

* 스프링에서 생성되는 액션, 인터셉터 및 결과(Result) 들을 허용함.
* Struts가 생성하는 객체는 생성 후 Spring에 의해 자동 연결(autowired)될 수 있습니다.
* 스프링 ObjectFactory를 사용하지 않는 경우 액션을 자동 연결하는 두 개의 인터셉터를 제공합니다.





## 사용법

스프링 통합을 활성화하려면 애플리케이션에 struts2-spring-plugin-x-x-x.jar를 포함하기만 하면 됩니다. 

둘 이상의 오브젝트 팩토리를 사용하는 경우(예: 애플리케이션에 스프링 및 Plexus 플러그인을 모두 포함) Constant Configuration을 통해 struts.objectFactory 속성을 default.properties 또는 여러 XML 파일 중 하나로 설정해야 합니다:



**struts.properties**

```properties
struts.objectFactory = spring
```



**struts.xml**

```xml
<struts>
  <constant name="struts.objectFactory" value="spring" />
  ... 
</struts>
```



#### **Autowiring**

프레임워크는 기본적으로 "autowiring"을 활성화합니다. (Autowiring은 여러분의 객체 속성과 스프링에 정의된 이름이 같은 객체를 찾는 것을 의미합니다). 와이어링 모드를 변경하려면 `spring.autoWire` 속성을 수정하세요.

**와이어링 모드**

```properties
struts.objectFactory.spring.autoWire = type
```

`autoWire` 속성은 여러 옵션으로 설정할 수 있습니다.

| 옵션        | 설명                                                         |
| :---------- | :----------------------------------------------------------- |
| name        | 스프링의 빈 이름을 액션의 속성 이름과 일치시켜 자동 연결합니다. **이것은 기본값입니다 **. |
| type        | 스프링에 등록된 액션의 속성과 동일한 타입의 빈을 찾아 자동 연결합니다. 이를 위해서는 스프링에 등록된 이 타입의 빈이 하나만 있어야 합니다. |
| auto        | 스프링은 여러분의 액션을 자동으로 연결하는 가장 좋은 방법을 자동으로 감지하려고 시도할 것입니다. |
| constructor | 스프링은 빈의 생성자의 파라미터로 자동으로 연결할 것입니다.  |
| no          | 외부에서 정의된 오토 와이어링을 끕니다. 스프링 기반의 어노테이션 주도 주입과 Aware-인터페이스 기반 주입은 여전히 적용됩니다. |

기본적으로 프레임워크는 적어도 스프링을 사용하여 모든 객체를 생성하려고 시도합니다. 스프링에서 객체를 만들 수 없는 경우 프레임워크가 객체 자체를 만듭니다.

다른 애플리케이션 객체에 대해 Spring 통합을 활성화하는 것은 2단계 프로세스입니다.

1. Spring 리스너 구성

    **web.xml**

    ```xml
    <listener>
      <listener-class>org.springframework.web.context.ContextLoaderListener</listener-class>
    </listener>
    ```

2. Spring 구성을 통해 객체 등록

   **applicationContext.xml**

   ```xml
   <?xml version="1.0" encoding="UTF-8"?>
   <!DOCTYPE beans PUBLIC "-//SPRING//DTD BEAN//EN" "http://www.springframework.org/dtd/spring-beans.dtd">
   <beans default-autowire="autodetect">
       <bean id="personManager" class="com.acme.PersonManager" scope="prototype"/>
       ...
   </beans>
   ```

더 많은 applicationContext 구성 파일이 필요한가요?

Spring 통합은 표준 리스너를 사용하기 때문에 applicationContext.xml 이외의 구성 파일을 지원하도록 구성할 수 있습니다. web.xml에 다음을 추가하면 주어진 패턴과 일치하는 모든 파일에서 Spring의 ApplicationContext가 초기화됩니다.

```xml
<!-- Context Configuration locations for Spring XML files -->
 <context-param>
     <param-name>contextConfigLocation</param-name>
     <param-value>/WEB-INF/applicationContext-*.xml,classpath*:applicationContext-*.xml</param-value>
 </context-param>
```

이 파라미터에 대한 전체 설명은 스프링 문서를 참조해보세요.



#### **스프링으로 액션 초기화하기**

일반적으로 struts.xml에서 각 액션에 대한 클래스를 지정합니다. 기본 SpringObjectFactory를 사용할 때 프레임워크는 기본 auto-wire 동작에 지정된 대로 액션을 만들고 의존성을 연결하도록 스프링에 요청합니다.

우리는 스프링이 여러분의 액션에 무엇을 제공해야 하는지 알려주는 선언적인 방법을 찾을 것을 **강력히** 추천합니다. 이것은  스프링에 정의된 제공되어야할 빈과 동일한 이름을 액션의 의존 속성 이름으로 지정하여 빈이 자동 연결될 수 있도록 하는 것 (이름 기반 autowire을 허용하기 위해) 또는 autowire-by-type을 사용하고 필요한 타입 중 하나만 스프링에 등록하는 것을 포함합니다. 또한 스프링 구성에서 명시적으로 프록시를 설정하지 않고 트랜잭션 및 보안 요구사항을 선언하기 위해 JDK 5 어노테이션을 사용하는 것을 포함할 수 있습니다. 스프링 applicationContext.xml에서 명시적 구성이 필요 없이 액션을 위해 스프링이 수행해야 할 작업을 알리는 방법을 찾을 수 있는 경우, 두 위치에서 모두 이 구성을 유지 관리할 필요가 없습니다.

그러나 때로는 빈이 스프링에 의해 완전히 관리되기를 원할 수 있습니다. 이것은 예를 들어 Acegi와 같은 더 복잡한 AOP 또는 스프링 지원 기술을 빈에 적용하려는 경우에 유용합니다. 이렇게 하려면 스프링 `applicationContext.xml`에서 빈을 구성한 다음 `struts.xml`에서 액션의 class 속성에다 클래스 이름 대신 스프링에서 정의된 bean 이름을 사용하도록 변경하기만 하면 됩니다.

그러면 `struts.xml` 파일에서 액션 클래스 속성이 변경됩니다.

**struts.xml**

```xml
<!DOCTYPE struts PUBLIC
    "-//Apache Software Foundation//DTD Struts Configuration 2.0//EN"
    "http://struts.apache.org/dtds/struts-2.0.dtd">
<struts>
    <include file="struts-default.xml"/>

    <package name="default" extends="struts-default">
        <action name="foo" class="com.acme.Foo">
            <result>foo.ftl</result>
        </action>
    </package>

    <package name="secure" namespace="/secure" extends="default">
        <action name="bar" class="bar">
            <result>bar.ftl</result>
        </action>
    </package>
</struts>
```

"bar"라는 이름의 `applicationContext.xml`에 정의된 Spring 빈이 있는 곳.  참고로 `com.acme.Foo` 액션은 자동 연결될 수 있으므로 변경할 필요가 없습니다.

bar의 일반적인 스프링 구성은 다음과 같습니다.

**applicationContext.xml**

```xml
<?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE beans PUBLIC "-//SPRING//DTD BEAN//EN" "http://www.springframework.org/dtd/spring-beans.dtd">
<beans default-autowire="autodetect">
    <bean id="bar" class="com.my.BarClass" singleton="false"/>
    ...
</beans>
```

스프링과 Struts와 함께 세션 범위 구성 요소를 사용하려면 [스프링 세션 구성 요소 해결 방법 분석](https://struts.apache.org/plugins/spring/spring-session-components-workarounds.html)을 참조하십시오.



#### **클래스 리로딩**

파일 시스템에서 변경되는 클래스를 자동으로 다시 로드하도록 스프링 플러그인을 구성할 수 있습니다. 이 기능을 사용하면 웹 컨테이너를 다시 시작하지 않고도 코드 변경 사항을 "hot deployed"할 수 있습니다. 이 기능을 활성화하려면 다음 단계를 따르세요: 

1. "struts.devMode"를 "true"로 설정합니다.

2. "struts.class.reloading.watchList"를 쉼표로 구분된 디렉토리 목록 또는 jar 파일(절대 또는 상대 경로)로 설정합니다.

3. web.xml에 다음을 추가합니다.

   ```xml
      <context-param>
          <param-name>contextClass</param-name>
          <param-value>org.apache.struts2.spring.ClassReloadingXMLWebApplicationContext</param-value>
      </context-param> 
   ```

4. Apache Commons JCI FAM을 클래스 경로에 추가합니다. maven을 사용하는 경우 이것을 pom.xml에 추가하세요.

   ```xml
      <dependency>
          <groupId>org.apache.commons</groupId>
          <artifactId>commons-jci-fam</artifactId>
          <version>1.0</version>
      </dependency> 
   ```

리로딩 클래스 로더가 모든 클래스를 처리하게 하면 다른 클래스 로더에 의해 로드된 동일한 클래스의 인스턴스가 서로 할당될 수 없기 때문에 ClassCastException(s)이 발생할 수 있습니다. 이 문제를 방지하기 위해 `struts.class.reloading.acceptClasses`를 사용하여 리로딩 클래스 로더에 의해 로드된 클래스를 제한하여 액션만 처리되도록 하는 것을 제안합니다. 이 상수는 쉼표로 구분된 정규식 목록을 지원합니다.

```xml
<constant name="struts.class.reloading.acceptClasses" value="com.myproject.example.actions..*" />
```

>이 기능은 실험적이며 프로덕션 시스템에서 사용하면 안 됩니다.





#### **설정**

다음 설정을 사용자 지정할 수 있습니다. [개발자 가이드](https://struts.apache.org/core-developers/configuration-files.html)를 참조하세요.

| 설정                                                     | 설명                                                         | 기본값                   | 설정 가능한 값                                               |
| :------------------------------------------------------- | :----------------------------------------------------------- | :----------------------- | :----------------------------------------------------------- |
| struts.objectFactory.spring.autoWire                     | autowire 전략                                                | name                     | name, type, auto, 또는 constructor                           |
| struts.objectFactory.spring.autoWire.alwaysRespect       | autowire 전략을 항상 사용해야 하는지 아니면 프레임워크가 상황에 따라 최상의 전략을 추측해야 하는지 여부 | 하위 호환성을 위해 false | true 또는 false                                              |
| struts.objectFactory.spring.useClassCache                | 스프링이 클래스 캐시를 사용할지 여부                         | true                     | true 또는 false                                              |
| struts.class.reloading.watchList                         | 변경 사항을 감시할 jar 파일 또는 디렉토리 목록               | null                     | jar 또는 디렉토리의 절대 또는 상대 경로의 쉼표로 구분된 목록 |
| struts.class.reloading.acceptClasses                     | 허용되는 클래스 이름의 정규식 목록                           | null                     | 리로딩 클래스 로더에 의해 로드될 클래스의 쉼표로 구분된 정규식 목록(액션 클래스만 리로딩 클래스 로더에 의해 처리되도록 정규식을 추가하는 것이 좋습니다) |
| struts.class.reloading.reloadConfig                      | 감시된 디렉터리 중 하나에서 변경 사항이 감지되면 런타임 구성(액션 매핑, 결과 등)을 다시 로드합니다. | false                    | true 또는 false                                              |
| DEPRECATED: struts.objectFactory.spring.enableAopSupport | 다른 로직을 사용하여 AOP 지원을 허용하는 빈을 구성하고 이전 접근 방식을 사용하여 빈을 만들고 스프링 빈 및 AOP에 문제가 있는 경우 이 플래그를 전환합니다. | false                    | true 또는 false                                              |





#### **설치**

이 플러그인은 플러그인 jar를 애플리케이션의 `/WEB-INF/lib` 디렉토리에 복사하여 설치할 수 있습니다. 다른 파일을 복사하거나 만들 필요가 없습니다.





---



## 의견

* Lucy 프레임워크에서도 스프링을 사용하기위헤 플러그인 형태로 포함이 되어있던 것 같은데, 설정 명칭들을 보면 거의 비슷했던 것 같다.



## 진행

* 스프링 빈 스코프
  * 2.0.x
    * https://docs.spring.io/spring-framework/docs/2.0.x/reference/beans.html#beans-factory-scopes
  * 5.2.x
    * https://docs.spring.io/spring-framework/docs/5.2.x/spring-framework-reference/core.html#beans-factory-scopes



* **스프링에 액션을 등록할 필요는 없습니다! **와 관련해서...
  * 액션을 스프링을 통해 주입하려면, 액션을 빈 설정에 추가하고 struts.xml에서 class 속성에 빈 이름만 넣어서, 진행하는 예제를 [스프링과 Struts 2 튜토리얼](../../spring-and-struts-2)에서 진행했다. 
  * 튜토리얼에서는 스프링 설정 xml파일(applicationContext.xml)에 액션 클래스를 빈으로 등록하는 설정에 대한 예시가 나왔었는데, component-scan으로 설정하기 위해서 액션 클레스에서 어노테이션을 달았는데... `@Controller`로 달면 Spring MVC를 사용하는 착각이 들 것 같아서,  `@Component`와 `@Scope("prototype")`로 달긴 했는데, `@Controller`를 달았을 때, 어떻게 되는지 봐도 좋을 것 같다.



* **와이어링 모드**에서 액션의 속성이라고 언급하는 부분...
  * 액션의 속성이, 빈이름에 대응되는 액션의 setter메서드를 말하는 것 같다.

* 스프링이 여러분의 액션에 무엇을 제공해야 하는지 알려주는 선언적인 방법을 찾을 것을 **강력히** 추천과 관련해서 ...
  * `struts.objectFactory.spring.autoWire` 값으로 auto 를 사용하지 말고 name이나 type으로 명시하거나  `no`로하고 또는 어노테이션 방식으로 사용하는 길 강력히 추천한다는 말 같다.
* [스프링 세션 구성 요소 해결 방법 분석](https://struts.apache.org/plugins/spring/spring-session-components-workarounds.html)
  * 문서 내용이 비어있다.


