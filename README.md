# STRUTS 2 스터티



## 개요

이전 회사에서 근무할 때, Legacy 프로젝트의 대부분은 Struts 2 와 비슷한 Lucy 프레임워크를 사용했었다.<br>Struts를 알아야할 일이 생겨서,  공식 홈페이지의 Getting Started 부터 하나씩 따라해보자.

### 예제 실행하기

* [struts2-study-parent](struts2-study-parent) 디렉토리 경로가 계층구조로 있지않고 상대적인 경로로 정의했습니다.
* 실행방법은 두가지입니다.
  * 개별 하위 프로젝트에서 들어가서 프로젝트를 실행 (예: [Struts 2 웹 어플리케이션을 만드는 방법](how-to-create-a-struts-2-web-application)의 예제실행)

    ```
    cd how-to-create-a-struts-2-web-application
    cd basic-struts
    mvnw clean jetty:run
    ```
    
  * struts2-study-parent 디렉토리에서 실행  (예: [메시지 리소스 파일](message-resource-files)의 예제실행)

    ```
    cd struts2-study-parent
    mvnw clean jetty:run -pl ../message-resource-files/message-resource-struts -am
    ```

  * 모든 하위 프로젝트 테스트

    ```
    cd struts2-study-parent
    mvnw clean test
    ```

     

## 시작하기

### 예제

* [Struts 2 웹 어플리케이션을 만드는 방법](how-to-create-a-struts-2-web-application)
* [Struts 2를 사용한 Hello World](hello-world-using-struts-2)
* [태그 사용하기](using-tags)
* [액션 코딩하기](coding-actions)
* [폼 처리](processing-forms)
* [폼 유효성 검사](form-validation)
* [메시지 리소스 파일](message-resource-files)
* 예외 처리
* Struts 디버깅
* 폼 테그
* XML을 사용하여 폼 검증
* 제어 테그
* 와일드카드 메서드 선택
* 테마
* 스프링과 Struts 2
* 어노테이션
* 인터셉터 소개
* 단위 테스트
* HTTP 세션
* Preparable 인터페이스
* 파라미터 제외

### 추가 튜토리얼
* JasperReports 튜토리얼



## 기타

### 예제 실행

* maven wrapper를 적용해서 JAVA_HOME 환경변수가 설정된 명령 프롬프트에서 mvnw로 실행이 가능함
  * 서버실행:  `mvnw jetty:run`
  * 서버 종료: `Ctrl + C` 
  * `jetty:run`으로 실행시 main, test 소스 컴파일 까지 하고(테스트 실행은 하지 않음 test-compile 페이즈까지 실행)까지 하고 target/classes 경로를 바라보고 서블릿 컨테이너를 띄우므로 실행시 일부러 `package` 페이즈를 실행시킬 필요는 없다.
    * `mvnw test jetty:run` 로 진행할 경우...
      * test 페이즈까지 진행되서 테스트까지 실행되고, 또 다시 리소스 복사, main/test 컴파일까지만 되고 Jetty가 실행된다.
  
* Tomcat과는 다르게 Jetty에 JSTL 라이브러리가 포함되어 실행시 JSTL관련 클래스가 중복되서 나타난다고 경고가 나타나서 scope를 provided 로 변경, (그런데 중복이 있더라도 크게 문제가 되는 부분은 아닌 것 같다.)

  ```xml
  <dependency>
      <groupId>javax.servlet</groupId>
      <artifactId>jstl</artifactId>
      <version>1.2</version>
      <scope>provided</scope> 
  </dependency>
  ```

  * Tomcat은 jstl라이브러리가 기본으로 포함되지 않기 때문에 실행시 오류가 날 수있음, 그런데, Jetty로만 확인 할 것이여서 이대로 둬도 문제는 없어보이기는한데 신경이 쓰임.
  * jetty-maven-plugin 이 apache-jstl (taglibs-standard-impl)를 기본 디펜던시해서 문제가 생기는 듯 보임
    * apache-jstl을 플러그인 종속성에서 제거해주면 좋은데, 직접 종속성이여서 제거가 안될 것으로 보임.
      * https://issues.apache.org/jira/browse/MNG-6222
      
    * 메인 디펜던시에서 taglibs-standard-impl을 runtime으로 디펜던시를 설정하고 Jetty 플러그인 디펜던시에는 apache-jstl에서 taglibs-standard-impl 를 제외했을 때, 클래스 중복경고가 뜨지 않고 Tomcat에서도 정상 동작함.
    
      ```xml
       <!-- jetty가 사용하는 apache-jstl을 통해 사용하는 taglibs-standard-impl 모듈이 Tomcat 8.5에서도 잘 동작한다. -->
      <dependencies>
          ...
          <!-- https://mvnrepository.com/artifact/org.apache.taglibs/taglibs-standard-impl -->
          <dependency>
              <groupId>org.apache.taglibs</groupId>
              <artifactId>taglibs-standard-impl</artifactId>
              <version>1.2.5</version>
              <scope>runtime</scope>
          </dependency>
          ...
      <dependencies>
          
      ...
          
      <build>
      ...
          <plugins>
          ...
          <plugin>
              <groupId>org.eclipse.jetty</groupId>
              <artifactId>jetty-maven-plugin</artifactId>
              ...
              <dependencies>
                  <dependency>
                      <groupId>org.eclipse.jetty</groupId>
                      <artifactId>apache-jstl</artifactId>
                      <version>${jetty.version}</version>
                      <exclusions>
                          <exclusion>
                              <groupId>org.apache.taglibs</groupId>
                              <artifactId>taglibs-standard-impl</artifactId>
                          </exclusion>
                      </exclusions>
                  </dependency>
              </dependencies>
          </plugin>    
          ...
          </plugins>
      ...
      </build>    
      ```
      
      

* Maven을 실행하는 JAVA 환경은 11이어야함, toolchain을 적용하여, mvnw의 Java 실행환경이 JDK 8이라도 JDK 11을 통해 빌드가 되지만, Jetty는 toolchain의 설정으로 처리되지 않고, mvnw의 JAVA 실행환경을 따르므로 Unsupported major.minor version 예외가 발생할 수 있음.

### Jetty의 변경 감지 자동 반영-재시작에 문제가 있음

* 변경사항 자동감지(`<scan>` )에 의한 Jetty 자동반영-재시작시 web.xml에 정의된 `StrutsPrepareAndExecuteFilter` 를 제대로 못읽음

    ```
    java.lang.ClassNotFoundException: org.apache.struts2.dispatcher.filter.StrutsPrepareAndExecuteFilter
    ...
    javax.servlet.UnavailableException: Class loading error for holder struts2==org.apache.struts2.dispatcher.filter.StrutsPrepareAndExecuteFilter@1ddaa7db{inst=false,async=false,src=DESCRIPTOR:file:///C:/프로젝트_디렉토리/message-resource-struts/src/main/webapp/WEB-INF/web.xml}
    ```
* Spring 프로젝트에 Jetty 플러그인을 적용했을 때도 동일한 패턴의 문제가 있어서 자동 반영-재시작이 일어나지 않도록 `<scan>` 값을 -1로 정했었다.
  * 자동반영-재시작시 ContextLoaderListener 를 정상적으로 못 읽음.
  
* 이 스터디 프로젝트도 `<scan>`값을 -1로 하자. 	



## 참조

* Getting Started
  * https://struts.apache.org/getting-started/

