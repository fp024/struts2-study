# Jetty 12 EE8 버전 업그레이드

> https://github.com/apache/struts-examples Repo의 코드들도 Jetty 12로 버전업을 시도해보았다.
>
> * https://github.com/fp024/struts-examples/tree/dev-2023-12-16
>
> 그런데... 대부분 잘되지만, 레거시화 되서 관리되지 않는 프로젝트는 어떻게 하기가 좀 힘든면이 있긴하다.
>
> Jetty 12 때문에 문제가 생긴 것이아니라, Struts가 버전이 올라가서 생긴 문제나, 
>
> 그냥 원래 부터 오류가 있던 것들...
>
> * 관련 커밋
>   * https://github.com/fp024/struts-examples/commit/6f30a9cf3ca21bbe414dc83e6c6fbe10557a8278







## groupId, artifactId 변경

|                | Jetty 9.4          | Jetty 12               |
| -------------- | ------------------ | ---------------------- |
| **groupId**    | org.eclipse.jetty  | org.eclipse.jetty.ee8  |
| **artifactId** | jetty-maven-plugin | jetty-ee8-maven-plugin |



## 플러그인 설정 속성 변경

* `<scanInternals>` **>** `<scan>`



## 디펜던시 변경

`jetty:run` 실행시 다음 오류가 발생함.

```
Caused by: java.lang.ClassNotFoundException: org.apache.xml.serializer.OutputPropertiesFactory
    at org.codehaus.plexus.classworlds.strategy.SelfFirstStrategy.loadClass (SelfFirstStrategy.java:50)
```

플러그인의 디펜던시에 `xalan:serializer` 라이브러리 추가

```xml
    <build>
        <plugins>
            <plugin>
                <groupId>org.eclipse.jetty.ee8</groupId>
                <artifactId>jetty-ee8-maven-plugin</artifactId>
                <version>${jetty12-plugin.version}</version>
                <configuration>
                    <webApp>
                        <contextPath>/${project.artifactId}</contextPath>
                    </webApp>
                    <stopKey>CTRL+C</stopKey>
                    <stopPort>8999</stopPort>
                    <scan>10</scan>
                    <scanTargetPatterns>
                        <scanTargetPattern>
                          <directory>src/main/webapp/WEB-INF</directory>
                          <includes>
                            <include>web.xml</include>
                          </includes>
                        </scanTargetPattern>
                    </scanTargetPatterns>
                </configuration>
                <dependencies>
                    <dependency>
                        <groupId>xalan</groupId>
                        <artifactId>serializer</artifactId>
                        <version>2.7.3</version>
                    </dependency>
                </dependencies>
            </plugin>
        </plugins>
    </build>
```





### web.xml  네임 스페이스 변경

**From:**

```xml
<web-app id="WebApp_ID" version="2.4" xmlns="http://java.sun.com/xml/ns/j2ee" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance" xsi:schemaLocation="http://java.sun.com/xml/ns/j2ee http://java.sun.com/xml/ns/j2ee/web-app_2_4.xsd">
```

**To:**

```xml
<web-app id="WebApp_ID" version="4.0"
         xmlns="http://xmlns.jcp.org/xml/ns/javaee"
         xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
         xsi:schemaLocation="http://xmlns.jcp.org/xml/ns/javaee
                        http://xmlns.jcp.org/xml/ns/javaee/web-app_4_0.xsd">
```





# Jetty 12 버전업그레이드와 관계 없이 발생했던 오류

> * Jetty 9.4인 상태에서도 발생했던 기존의 오류를 가능한 범위내에 수정

## annotations

#### 발생 오류 

```
Caused by: java.lang.ClassNotFoundException: org.apache.commons.compress.compressors.CompressorException
```

#### 수정

annotations 프로젝트에 아래의 디펜던시 추가

```xml
<dependency>
    <groupId>org.apache.commons</groupId>
    <artifactId>commons-compress</artifactId>
    <version>1.25.0</version>
</dependency>
```

* 동일 문제 프로젝트
  * **text-provider**
  * **unknown-handler**

## debugging-struts 

#### 발생오류

```
HTTP ERROR 500 javax.servlet.ServletException: org.apache.jasper.JasperException: java.lang.NullPointerException: Cannot invoke "java.util.List.add(Object)" because "tags" is null
URI:	/debugging-struts/register.jsp
STATUS:	500
MESSAGE:	javax.servlet.ServletException: org.apache.jasper.JasperException: java.lang.NullPointerException: Cannot invoke "java.util.List.add(Object)" because "tags" is null
SERVLET:	jsp
CAUSED BY:	javax.servlet.ServletException: org.apache.jasper.JasperException: java.lang.NullPointerException: Cannot invoke "java.util.List.add(Object)" because "tags" is null
CAUSED BY:	org.apache.jasper.JasperException: java.lang.NullPointerException: Cannot invoke "java.util.List.add(Object)" because "tags" is null
CAUSED BY:	java.lang.NullPointerException: Cannot invoke "java.util.List.add(Object)" because "tags" is null
```

#### 수정

* Jetty 버전업과 관계없이 **register.jsp**에 접근하면 오류가 발생
  * 관련해서 수정하지 않음.
  * Struts 2 버전은 `2.5.33`으로 다운그레이드 하면 오류가 없어짐.
  * `6.3.0.2`에서는 Struts 테그가 포함된 JSP 페이지를 URL로 직접 접근시, 관련 테그 부분 처리가 안되는 것 같음. 
    * 반드시 액션을 통해 접근해야 스트럿츠 테그가 처리되는 것 같음.
* 동일한 문제가 있는 예제 프로젝트
  * **exception-handling**
  * **form-validation**
  * **unit-testing**




## dynamic-href

### 발생 오류

```
Caused by: java.lang.ClassNotFoundException: javax.servlet.Filter
```

### 수정

* Jetty 11.0.15 설정이 잘못들어가 있어 제거함. 
  * Jetty 11은 Servlet 5 (jakarta.* package)를 지원하여 Servlet  4 프로젝트에서는 사용할 수 없음.



## form-processing

### 발생 오류

```
2023-12-16 07:20:59,616 WARN  [qtp2073484941-48] ognl.OgnlValueStack (OgnlValueStack.java:399) - Caught an exception while evaluating expression 'options[1]' against value stack
java.lang.IndexOutOfBoundsException: Index 1 out of bounds for length 1
```

### 수정

* [ ] TODO: 수정이 가능한지 나중에 확인해보자.. 코드 수정이 필요한 것 같은데.. 지금은 모르겠음.



## interceptors

### 발생 오류

```
Caused by: com.opensymphony.xwork2.config.ConfigurationException: Unable to find interceptor class referenced by ref-name timer
```

### 수정

timer 인터셉터는 6.0.0부터는 제공되지 않음. = 주석으로 바꿈.

```xml
<!-- <interceptor-ref name="timer"/> --> <!-- As of Struts2 6.0.0, the timer interceptor has been removed. -->
```



## json-customize

### 발생오류

```
java.text.ParseException: Unparseable date: "26-Apr-1564"
```

### 수정

* Locale을 명시적으로 지정

  ```java
  SimpleDateFormat formatter = new SimpleDateFormat("dd-MMM-yyyy", Locale.ENGLISH);
  ```





## mailreader2

* 겉보기에 동작은 문제가 없어보이나, 테스트가 힘들다.





## restful2actionmapper

### 발생오류

* `http://localhost:8080/restful2actionmapper/movie/1` 이면 1이 move 액션의 메서드의 id로 전달 되야한다고 하는데.. 원래 부터 다음 오류가 난다.

  ```
  Method view for action movie/1 is not allowed! - [unknown location]
      com.opensymphony.xwork2.DefaultActionProxy.prepare(DefaultActionProxy.java:191)
  ```

### 수정

* 수정없음
* Jetty는 버전업 해두자!



## shiro-basic

### 발생오류

```
Caused by: java.lang.ClassNotFoundException: org.apache.commons.logging.LogFactory
```

* 이미 존재하는 오류

### 수정 

* 수정없음
* Jetty는 버전업 해둠.



## type-conversion

### 발생오류

```xml
<action name="Number" class="org.apache.struts.example.IndexAction">
    <result name="input">/WEB-INF/example/Number.jsp</result>
</action>
```

* 액션 클래스가 잘못 정의되어있음.  IndexAction 이란 것은 없음.

### 수정

```xml
<action name="Number" class="org.apache.struts.example.NumberAction">
  <result name="input">/WEB-INF/example/Number.jsp</result>
</action>
```



# 버전업하지 않은 프로젝트

## portlet

* 예전 환경에 맞춰져 있어서 버전업이 힘들어보인다.



## quarkus

* Jetty를 사용하지 않는 프로젝트.
* `mvn clean compile quarkus:dev` 로 실행하면 오류남.



## jasperreports

### 발생 오류

* ServletContext#getRealPath()의 반환 값이 null이 됨

### 수정

* Jetty 10.0.18 으로 바꿈.

