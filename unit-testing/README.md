# 단위 테스트

> 원문 : https://struts.apache.org/getting-started/unit-testing.html

* 소개
* 설정
* 단위 테스트 작성
* 검증 통과 테스트
* 검증 실패 테스트
* 요약


이 튜토리얼의 예제 코드인 **unit-testing**는 [struts-examples](https://github.com/apache/struts-examples)에서 체크아웃할 수 있습니다.



## 소개

Struts 2는 [Struts 2 JUnit 플러그인](https://struts.apache.org/plugins/junit/)을 사용하여 Struts Action 클래스의 메서드에 대한 단위 테스트 실행을 지원합니다. JUnit 플러그인을 사용하면 Struts 2 프레임워크 내에서 Action 클래스의 메서드를 테스트할 수 있습니다. Struts Servlet -filter 및 인터셉터는 애플리케이션이 Servlet 컨테이너에서 실행 중인 것처럼 실행됩니다.

Struts 2 [사용자 메일링](http://struts.apache.org/mail.html) 리스트는 도움을 받을 수 있는 훌륭한 장소입니다. 튜토리얼 예제 애플리케이션을 작동시키는데 문제가 있는 경우 Struts 2 메일리스트를 검색하세요. 문제에 대한 답을 찾지 못한 경우 메일링 리스트에 질문을 게시하세요.



## 설정



## 단위 테스트 작성



## 검증 통과 테스트



## 검증 실패 테스트





## 요약





### >  [인터셉터 소개](https://struts.apache.org/getting-started/introducing-interceptors.html)로 돌아가기 또는 [HTTP 세션](https://struts.apache.org/getting-started/http-session.html)으로 이동

---

## 스텝 진행...

공식 [struts2-junit-plugin](https://github.com/apache/struts/tree/master/plugins/junit)이 JUnit 5를 지원하지 않아서, JUnit 5를 지원하도록 플러그인을 수정해서 커스텀 프로젝트를 만들었다. 

* [struts2-junit5-user-custom-plugin](../struts2-junit5-user-custom-plugin) 프로젝트

  * Maven 레파지토리에 올리지않고 로컬에서 테스트로만 사용하므로 Maven 로컬 레파지토리에 설치하고 예제를 돌리도록 하자!

    ```bash
    cd ../struts2-junit5-user-custom-plugin
    mvnw clean install
    ```

* 프로젝트 변경사항

  * 프로젝트명: [unit-testing-struts](unit-testing-struts)

  * 커스텀 플러그인 사용을 위해 pom.xml에는 아래 디펜던시를 추가한다.

    ```xml
    <dependency>
      <groupId>org.fp024.struts2.study</groupId>
      <artifactId>struts2-junit5-user-custom-plugin</artifactId>
      <version>1.0.0-SNAPSHOT</version>
      <scope>test</scope>
    </dependency>
    ```

- [x] 설정
- [x] 단위 테스트 작성
- [x] 검증이 통과하는 경우의 테스트
- [x] 검증이 실패하는 경우의 테스트
- [x] 요약

### Action을 스프링 빈으로 관리되게 했을 때의 테스트

* 현재 프로젝트는 Spring 통합은 되어있지 않기 때문에, 통합이 되어있는 [spring-struts](../spring-and-struts-2/spring-struts) 프로젝트에 단위 테스트를 추가해서 확인을 했음
  * EditAcionTest 클래스를 알맞게 수정함.



