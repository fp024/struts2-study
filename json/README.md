# 외부 JSON 라이브러리 사용하기

> * 원본 예제 프로젝트 레파지토리
>   * https://github.com/apache/struts-examples/tree/master/json-customize

## 개요

[JSON 플러그인](../plugins/json-plugin/) 문서에서 [Flexjson](http://flexjson.sourceforge.net/) 이란 라이브러리를 사용한 예제로 설명을 해주셨는데, 이 라이브러리가 2013년 이후로 버전업이 없고, 현 시점에 잘쓰이지 않는 라이브러리로 보여, `Jackson`또는 `Gson`을 사용하는 예제를 위에 언급한 [원본 예제 프로젝트](https://github.com/apache/struts-examples/tree/master/json-customize)를 진행 중인 struts2-study-parent 프로젝트에 맞게 수정하면서, 진행해보자. 



## 진행

### Jackson 을 적용한 프로젝트

* 프로젝트명: [json-customize-jackson-struts](json-customize-jackson-struts)
  * Jacksn 디펜던시
    * `jackson-databind`

##### 원본 예제 프로젝트의 요구사항 대로 진행 된 부분

struts.xml에서 값 설정을 통해 매퍼 설정을 변경해야하므로, 런타임시에 설정을 바꿔서 매퍼를 생성할 수 있어야한다.

1. 값이 NULL 인 프로퍼티는 직렬화에 포함하지 않기

   ```java
   mapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
   ```

2. 날짜 포맷 설정

   * Date 타입

     ```java
     // 날짜 형식 지정, Date 타입에 대해서만 적용된다.
     DateFormat dateFormat = new SimpleDateFormat(dateFormatter);
     mapper.setDateFormat(dateFormat);
     ```

   * LocalDateTime 타입

     ```java
     // JavaTimeModule에 포멧을 설정해서 매퍼에 추가해야한다.
     ObjectMapper mapper = new ObjectMapper();
     JavaTimeModule javaTimeModule = new JavaTimeModule();
     ...
     DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern(dateFormatter);
     javaTimeModule.addSerializer(
         LocalDateTime.class, new LocalDateTimeSerializer(dateTimeFormatter));
     
     ```

3. 필드명을 다른 명칭으로, 특정 필드 무시, 직렬화시 별도 컨버터 사용

   ```java
   @Getter
   @Setter
   @ToString
   public class User extends Person {
     // 필드명을 다른이름으로 사용
     @JsonProperty("username")
     private String login;
   
     // 직렬화 대상에서 무시
     @JsonIgnore private String hashedPassword;
   
     private LocalDateTime lastLogin;
   
     // 직렬화할 때, 사용자 정의 컨버터 사용
     @JsonSerialize(converter = PasswordConverter.class)
     private String password;
   }
   
   ```

   ```java
   // password 필드를 ****** 로 대체해서 직렬화함.
   public class PasswordConverter extends StdConverter<String, String> {
     @Override
     public String convert(String s) {
       return "******";
     }
   }
   ```

   

##### 진행간 특이사항

* Java 8 이상의 새로운 Date/Time 타입인 LocalDateTime 등으로 JSON으로 파싱할 때,  `jackson-datatype-jsr310` 라이브러리도 추가하고 모듈 등록을 해야 잘 동작하는 점.



#### 원본 예제 프로젝트의 요구사항 대로 진행이 안된 부분

##### 1. excludeProperties

제외 할 프로퍼티 이름 패턴을 전달해서 런타임시 제외 시키는 속성이 있는데, 이부분을 Jackson에서 어떻게 처리해야할지? 지금은 잘 모르겠다.

##### 2. includeProperties

포함할 할 프로퍼티 이름 패턴을 전달해서 런타임시 제외 시키는 속성이 있는데, 이부분을 Jackson에서 어떻게 처리해야할지? 지금은 잘 모르겠다.



## 참조

##### Java 8 Date/Time 관련

* https://github.com/FasterXML/jackson-modules-java8
* https://github.com/FasterXML/jackson-modules-java8/tree/2.14/datetime

##### NULL 필드 제외 관련

* // https://www.baeldung.com/jackson-ignore-null-fields

## 

