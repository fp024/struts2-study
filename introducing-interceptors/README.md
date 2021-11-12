# 인터셉터 입문

> 원문 : https://struts.apache.org/getting-started/introducing-interceptors.html

* 서문

* 인터셉터 입문

* 예제 실행

* 나만의 인터셉터 만들기

* 요약


이 튜토리얼의 예제 코드인 **interceptors**는 [struts-examples](https://github.com/apache/struts-examples)에서 체크아웃할 수 있습니다.



## 서문



Struts 2 [사용자 메일링](http://struts.apache.org/mail.html) 리스트는 도움을 받을 수 있는 훌륭한 장소입니다. 튜토리얼 예제 애플리케이션을 작동시키는데 문제가 있는 경우 Struts 2 메일리스트를 검색하세요. 문제에 대한 답을 찾지 못한 경우 메일링 리스트에 질문을 게시하세요.





## 인터셉터 입문





## 예제 실행





## 나만의 인터셉터 만들기





## 요약





### >  [어노테이션](https://struts.apache.org/getting-started/annotations.html)으로 돌아가기 또는 [단위 테스트](../unit-testing)로 이동

---



## 스텝 진행...

* 프로젝트 변경사항
  * 프로젝트명: [interceptors-struts](interceptors-struts) 로 변경.
  * Spring 5 + JPA + Hibernate + HSQLDB 로 동작하도록 프로젝트를 구성.
  * 액션, 서비스, 레파지토리코드에 대한 테스트코드를 추가.
  * 나만의 인터셉터 만들기를 별도 가이드 링크만 해주시길레 그 가이드 보고 추가해보았다.
  * [SimpleInterceptor.java](./interceptors-struts/src/main/java/org/fp024/struts2/study/register/interceptor/SimpleInterceptor.java)
  * mockito-inline 사용해보았는데.. 훌륭한 모듈 같다. CALLS_REAL_METHODS 옵션을 지정하면 스텁으로 정의하지 않은 메서드는 실제 메서드를 호출하는 부분이 정말 마음에 들었다!
  
* [ ] 서문

* [ ] 인터셉터 입문

* [ ] 예제 실행

* [ ] 나만의 인터셉터 만들기

* [ ] 요약

  
