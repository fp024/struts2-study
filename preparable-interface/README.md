# Preparable 인터페이스

> 원문 : https://struts.apache.org/getting-started/preperable-interface.html

* 소개
* Preparable 인터페이스
* 예제 애플리케이션
* 요약


이 튜토리얼의 예제 코드인 **preperable-interface**는 [struts-examples](https://github.com/apache/struts-examples)에서 체크아웃할 수 있습니다.



## 소개

종종 폼 컨트롤을 채우는 데 사용되는 데이터는 데이터베이스에서 동적으로 생성됩니다. 사용자가 폼을 제출하면 Struts 2 유효성 검사 인터셉터가 사용자의 폼 입력 유효성을 검사하려고 시도합니다. 유효성 검사가 실패하면 Struts 2 프레임워크는 값 `input`을 반환하지만 `input` 액션은 다시 실행되지 않습니다. 오히려 `input` 결과와 관련된 뷰가 사용자에게 렌더링됩니다. 일반적으로 이 뷰는 원래 폼을 표시한 페이지입니다.

이 워크플로는 표시된 폼 필드 또는 기타 데이터 중 하나 이상이 액션 클래스의 input 메서드에서 수행되는 동적 조회에 의존하는 경우 문제를 일으킬 수 있습니다. 액션 클래스의 input 메서드는 유효성 검사 실패 시 재실행되지 않기 때문에 뷰 페이지에서 더 이상 폼이나 기타 표시 정보를 생성하기 위한 올바른 정보에 액세스하지 못할 수 있습니다.

Struts 2 [사용자 메일링](http://struts.apache.org/mail.html) 리스트는 도움을 받을 수 있는 훌륭한 장소입니다. 튜토리얼 예제 애플리케이션을 작동시키는데 문제가 있는 경우 Struts 2 메일리스트를 검색하세요. 문제에 대한 답을 찾지 못한 경우 메일링 리스트에 질문을 게시하세요.





## Preparable 인터페이스

Struts 2는 이러한 문제를 극복하기 위해 [Preparable 인터페이스](https://struts.apache.org/maven/struts2-core/apidocs/com/opensymphony/xwork2/Preparable.html)를 제공합니다. 이 인터페이스를 구현하는 액션 클래스는 prepare 메서드를 재정의해야 합니다. `prepare` 메서드는 액션 클래스에 대해 메서드가 호출될 때마다 그리고 뷰가 렌더링되기 전에 유효성 검사가 실패할 때 항상 Struts 2 프레임워크의 [Prepare 인터셉터](https://struts.apache.org/core-developers/prepare-interceptor.html)에 의해 호출됩니다.

다른 액션 클래스 메서드가 호출되더라도 실행되어야 하는 명령문과 유효성 검사가 실패할 경우 실행해야 하는 명령문을 prepare 메서드에 넣어야 합니다. 일반적으로 prepare 메서드의 명령문은 폼 컨트롤을 채우는 데 사용할 액션 클래스 인스턴스 필드의 값을 설정하고 초기 폼 필드 값을 설정하는 데 사용할 값을 가져옵니다.

`prepare` 메서드를 자동으로 실행하는 것 외에도 prepare 인터셉터는 `prepare<ActionMethodName>`이라는 메서드도 호출합니다. 예를 들어, `Preparable`을 구현하는 액션 클래스에서 `prepare` 메서드와 `prepareInput` 메서드를 정의해보세요. Struts 2 프레임워크가 input 메서드를 호출할 때 prepare 인터셉터는 input 메서드를 호출하기 전에 prepareInput 및 prepare 메서드를 호출합니다.





## 예제 애플리케이션

예제 애플리케이션에서 EditAction 클래스를 살펴보면(위 참조) `Preparable` 인터페이스를 구현하고 있음을 알 수 있습니다. prepare 메서드에는 다음 코드가 있습니다:

### EditAction.java - prepare 메서드

```java
carModelsAvailable = carModelsService.getCarModels();

setPersonBean(editService.getPerson());
```

위의 명령문은 폼에 표시된 자동차 모델 확인란을 채우는 데 사용되는 자동차 모델 값을 가져오고 편집 중인 Person 객체에 대한 정보도 가져옵니다.

예제 애플리케이션을 실행할 때 입력 및 execute 메서드와 관련하여 prepare 메서드가 호출되는 시점을 로그에서 확인해보세요. 예제 애플리케이션을 실행하고 로그를 검사하면 `Preparable` 인터페이스와 prepare 메서드 구현의 영향을 이해하는 데 도움이 됩니다.





## 요약

액션 클래스의 어떤 메서드가 호출되든 또는 유효성 검사가 실패하더라도 애플리케이션에서 특정 명령문을 실행해야 하는 경우 Preparable 인터페이스를 구현하고 prepare 메서드를 재정의해야 합니다.



### >  [HTTP 세션](../http-session)으로 돌아가기 또는 [파라미터 제외하기](../exclude-parameters)로 이동

---



## 스텝 진행...

* 프로젝트 변경사항
  * 프로젝트명: [preparable-interface-struts](preparable-interface-struts) 
  
* [x] 소개

* [x] Preparable 인터페이스

* [x] 예제 애플리케이션

* [x] 요약

  

## 진행중 이슈 사항

### `<s:submit key="submit"/>` 태그 사용시 에러로그가 남는 문제

```
2021-12-17 14:03:46,647 ERROR [qtp2137205195-42] interceptor.ParametersInterceptor (ParametersInterceptor.java:238) - Developer Notification (set struts.devMode to false to disable this message):
Unexpected Exception caught setting 'submit' on 'class org.fp024.struts2.study.edit.action.EditAction: Error setting expression 'submit' with value ['Save Changes', ]
```

다국어 지원을 위해 key="번들 키 이름"을 사용했을 때, 내부적으로 키로 지정한 submit 에대한 setter(setSubmit)를 액션에서 찾으려해서 문제가 생기는 것 같다. 아래  StackOverflow 링크의 마지막 답변대로 인 것 같다.

* `<s:submit key="submit" name=""/>` 처럼 빈문자열을 갖는 name 속성까지 추가해주면 없어진다.
  * https://stackoverflow.com/questions/25529765/error-setting-expression-submit-with-value-submit-in-struts2



## prepare()에 폼의 기본 값을 제공하는 로직이 있을 때, validate() 테스트시 request 파라미터를 아예 안보내는 것이 아니라 빈값으로 설정해서 테스트 할 것!!

* 테스트하고 실제환경하고 다른 건가? 하고 해깔렸었다. 😒
* 파라미터 자체를 아예 안보낼 경우 기본 값으로 설정되어, 검증이 항상 통과해버린다.
  * EditActionTest 의 getActionProxy_MissingEmail() 메서드
