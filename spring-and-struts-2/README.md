# 스프링과 Struts 2

> 원문 : https://struts.apache.org/getting-started/spring.html

* 서문

* Struts 2 스프링 플러그인

* 스프링 설정 파일

* 대안 - ActionSupport 클래스 생성을 스프링으로 관리 

* 요약


이 튜토리얼의 예제 코드인 **spring-struts**는 [struts-examples](https://github.com/apache/struts-examples)에서 체크아웃할 수 있습니다.



## 서문

Struts 2 ActionSupport 클래스의 많은 실행 메서드에는 객체를 생성한 다음 해당 객체가 필요한 작업을 실행하는 문장이 있습니다. 한 클래스가 다른 클래스의 객체를 만들 때마다 두 클래스 간의 의존성이 발생합니다. 스프링 프레임워크를 사용하면 애플리케이션 개발자가 이러한 의존성을 더 쉽게 관리할 수 있으며 애플리케이션을 보다 유연하게하고 유지 관리할 수 있도록 합니다. 이 튜토리얼에서는 Struts 2와 스프링을 함께 사용하여 ActionSupport 클래스와 어플리케이션의 다른 클래스 간의 의존성을 관리하는 방법을 보여줍니다.

>이 튜토리얼에서는 스프링 프레임워크를 사용하여 클래스 간의 의존성을 관리하는 방법을 이해하고 있다고 가정합니다. 스프링의 자세한 내용은 https://spring.io/docs에서 확인할 수 있습니다.

Struts 2 [사용자 메일링](http://struts.apache.org/mail.html) 리스트는 도움을 받을 수 있는 훌륭한 장소입니다. 튜토리얼 예제 애플리케이션을 작동시키는데 문제가 있는 경우 Struts 2 메일리스트를 검색하세요. 문제에 대한 답을 찾지 못한 경우 메일링 리스트에 질문을 게시하세요.

[Struts 2 Themes](https://struts.apache.org/getting-started/themes.html) 튜토리얼의 예제 애플리케이션을 확인하면 `EditAction` 클래스에서 이 코드를 볼 수 있습니다.

### EditAction 클래스  - 하드 코딩된 의존성

```java
private EditService editService = new EditServiceInMemory();
```

위의 문장에서 `EditAction` 클래스와 `EditServiceInMemory` 클래스 간의 의존성이 하드 코딩되어 있습니다. 이것은 두가지 이유로 좋지 않은 디자인입니다.

1. `EditServiceInMemory`를 `EditService` 인터페이스를 구현하는 다른 클래스로 교체해야 하는 경우 하드 코딩한 모든 문장을 찾아 의존성을 교체해야 합니다.
2. `EditServiceInMemory` 클래스를 사용하지 않고 `EditAction`을 테스트할 수 없습니다. `EditServiceInMemory` 사용 부분이 하드 코딩되어 있기 때문에 테스트 케이스를 작성할 때 `EditService`의 스텁 구현을 사용하여 `EditAction`을 분리할 수 없습니다.

스프링은 런타임에 종속성을 주입하여 종속성을 관리하는 메커니즘을 제공합니다. 다른 Java 클래스와 마찬가지로 Struts 2 ActionSupport 클래스는 스프링 프레임워크에 의해 의존 객체와 함께 주입될 수 있습니다. 따라서 위의 코드를 사용하는 대신 `EditAction`에 이 문장을 사용합니다.

### EditAction 클래스 -  하드 코딩된 의존성  없음

```java
private EditService editService;
```

스프링 프레임워크는 런타임에 EditService 인터페이스를 구현하는 클래스의 객체를 제공합니다. 



## Struts 2 스프링 플러그인

Struts 2는 스프링 설정 파일에서 지정한 의존 객체를 ActionSupport 클래스에 주입할 수 있도록 하는 플러그인을 제공합니다. 플러그인 작동 방식에 대한 자세한 내용은 [스프링 플러그인 문서](../plugins/spring-plugin/)를 참조하세요.

Maven 애플리케이션의 경우 struts2-spring-plugin jar 의존성을 추가해야 합니다(이 튜토리얼의 Maven 예제 애플리케이션 참조). 플러그인의 pom.xml에는 스프링 jar 파일(beans, core, context, web)에 대한 전이적 의존성이 포함되어 있습니다.

>Struts 2 스프링 플러그인의 현재 버전(`2.5.10.1`)은 스프링 `4.1.6.RELEASE` 버전에 대한 전이 종속성을 가지고 있습니다. 최신 버전의 스프링을 사용하려면   pom.xml에서 Struts 2 스프링 플러그인에 대한 전이 의존성을 제외하고 현재 버전의 스프링 jar 파일의 대한 의존성 노드를 선언해야 합니다. Ant를 사용하고 애플리케이션에 jar 파일을 명시적으로 포함하는 경우 최신 버전의 스프링 jar 파일만 포함합니다.

`ActionSupport` 클래스에는 표준 Java 빈 명명 규칙을 따르는 의존 객체에 대한 set 메소드가 있어야 합니다. 이 튜토리얼의 애플리케이션에서 EditAction 클래스를 확인하면 아래 set 메서드를 볼 수 있습니다.

```java
public void setEditService(EditService editService) {
    this.editService = editService;
}
```

스프링은 런타임에 `EditAction` 클래스에 `EditService` 타입의 객체를 제공하기 위해 해당 set 메소드를 사용할 것입니다.

### web.xml에서의 스프링 리스너

```xml
<listener>
    <listener-class>org.springframework.web.context.ContextLoaderListener</listener-class>
</listener>
```

위의 코드는 애플리케이션이 서블릿 컨테이너에 의해 시작될 때 스프링 프레임워크를 활성화합니다. 기본적으로 스프링은 WEB-INF에서 설정 파일 이름으로 applicationContext.xml을 찾습니다. (스프링이 설정파일을 찾는 위치와 구성 설정 이름을 변경하는 방법은 스프링 문서를 참조하십시오)



## 스프링 설정 파일

스프링이 인스턴스를 생성하고 ActionSupport 클래스에 주입하기를 원하는 객체에 대한 빈 노드를 스프링 설정 파일에 추가합니다. 예제 애플리케이션에서 아래 applicationContext.xml이 있습니다.

### 스프링 설정 파일

```xml
<?xml version="1.0" encoding="UTF-8"?>
<beans xmlns="http://www.springframework.org/schema/beans"
       xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
       xsi:schemaLocation="
            http://www.springframework.org/schema/beans
            http://www.springframework.org/schema/beans/spring-beans.xsd">

    <bean id="editService" class="org.apache.struts.edit.service.EditServiceInMemory" />

</beans>
```

위의 id 값에 유의하세요. 기본적으로 스프링 플러그인은 스프링과 함께 작동하여 `name`으로 액션 클래스의 의존성을 autowire 합니다. 스프링은 EditServiceMemory 클래스의 객체를 생성하고 EditService 타입의 인수를 가진 setEditService 메서드가 있는 모든 ActionSupport 클래스에 해당 객체를 제공합니다. 기본 autowire 방법을 변경하는 방법은 [스프링 플러그인](../plugins/spring-plugin/) 문서를 참조하세요.

스프링에 의해 생성된 editService 빈은 default 스코프이기 때문에 singleton 스코프를 가질 것입니다. 다른 스코프(예: request 또는 session)를 사용하도록 빈 정의를 구성하는 방법에 대해서는 [스프링 문서](https://spring.io/docs)의 섹션 3.5를 참조하십시오.



## 대안 - ActionSupport 클래스 생성을 스프링으로 관리 

위의 방법론을 사용하여 Struts 2 프레임워크는 여전히 `ActionSupport` 클래스 생성을 관리합니다. 원하는 경우 Spring이 ActionSupport 클래스도 생성하도록 애플리케이션을 구성할 수 있습니다. 이 기술을 지원하려면 스프링 구성 파일에 ActionSupport 클래스에 대한 빈 노드를 추가해야 합니다.

### 스프링이 관리하는 ActionSupport 클래스의 스프링 설정

```xml
<?xml version="1.0" encoding="UTF-8"?>
<beans xmlns="http://www.springframework.org/schema/beans"
       xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
       xsi:schemaLocation="
            http://www.springframework.org/schema/beans
            http://www.springframework.org/schema/beans/spring-beans.xsd">
            
    <bean id="editService" class="org.apache.struts.edit.service.EditServiceInMemory" />

    <bean id="editAction" class="org.apache.struts.edit.action.EditAction" scope="prototype">
        <property name="editService" ref="editService" />
    </bean>

</beans>
```

위에서 `editAction` 빈이 있고 `editService` 속성이 `editService` 빈으로 설정된다는 점에 유의하세요. `EditAction` 클래스는 스프링이 관리하므로 스프링이 주입하기를 원하는 `EditAction`의 속성들을 지정해야 합니다. 액션은 각 요청에 대해 생성되어야 하며 `singletons`일 수 없음을 기억하세요. 이것이 기본 스코프이므로 `prototype`으로 변경해야 합니다.

`struts.xml `설정 파일에서 액션 노드의 클래스 속성에 대한 스프링 id 값을 지정해야 합니다. 이것은 Struts에게 스프링에서 Action Class에 대한 id 값을 가진 빈을 가져오라고 말합니다.

### 스프링으로 관리되는 ActionSupport 클래스를 위한 Struts 설정

```xml
<action name="edit" class="editAction" method="input">
    <result name="input">/edit.jsp</result>
</action>
```



## 요약

이 튜토리얼에서는 Struts 2 스프링 플러그인을 사용하여 스프링과 Struts를 통합하는 방법을 검토했습니다. Struts 2 스프링 플러그인을 사용하여 스프링이 ActionSupport 클래스의 의존성을 관리하도록 할 수 있습니다. 물론 스프링 프레임워크가 제공하는 다른 많은 이점(AOP, Spring JDBC)도 활용할 수 있습니다.



### >  [테마](https://struts.apache.org/getting-started/themes.html)로 돌아가기 또는 [어노테이션](https://struts.apache.org/getting-started/annotations.html)으로 이동

---



## 스텝 진행...

* 프로젝트 변경사항
  * 프로젝트명: spring-struts 로 동일하게 사용.
  * Spring 버전을 `5.2.18.RELEASE`로 올림 (spring-framework-bom 정의)
  * applicationContext.xml 위치를 src/main/resources로 변경
  * component-scan으로 서비스와 액션 클래스 스캔
  * 단순 JUnit 5 테스트 코드 추가
  * 한국어 메시지 프로퍼티 파일 추가
  
* [x] 서문

* [x] Struts 2 스프링 플러그인

  * Struts 2 스프링 플러그인의 현재 버전(`2.5.26`)은 스프링 `4.3.26.RELEASE` 버전에 대한 전이 의존성을 가지고 있음.

  * Setter를 안만들더라도 스프링 설정파일에서 component-scan으로 정의하고 의존 객체 필드에 @Autowired를 붙여주면 주입이 가능했다.

  * 생성자를 만들어줘도 가능, 그런데 생성자에 2개 이상 파라미터가 들어간다면 생성자에도 @Autowired를 붙여줘야할 듯.

  * 스프링 설정 파일 찾는 위치와 찾을 이름 변경

    * 아래는 `src/main/resources` 경로에 스프링 설정 파일을 두었을 때, 찾아 내도록  web.xml 설정에 아래 내용을 추가했다.

        ```xml
         <context-param>
            <param-name>contextConfigLocation</param-name>
            <param-value>classpath:applicationContext.xml</param-value>
          </context-param>
        ```

    

* [x] 스프링 설정 파일

* [x] 대안 - ActionSupport 클래스 생성을 스프링으로 관리

  * 스프링 설정파일에 component-scan을 설정하고 액션 클래스에 `@Component`, `@Scope("prototype")` 을 붙힌후, struts.xml에서도 빈 id로 설정했는데, 문제없이 잘된다.
  * 테스트 코드에서 확인시에도 프로토타입 동작(빈을 얻을 때마다 새로운 객체 생성)이 정상적인 것을 확인했다.

* [x] 요약

  
