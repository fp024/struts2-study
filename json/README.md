# 외부 JSON 라이브러리 사용하기

> * 원본 예제 프로젝트 레파지토리
>   * https://github.com/apache/struts-examples/tree/master/json-customize

## 개요

[JSON 플러그인](../plugins/json-plugin/) 문서에서 [Flexjson](http://flexjson.sourceforge.net/) 이란 라이브러리를 사용한 예제로 설명을 해주셨는데, 이 라이브러리가 2013년 이후로 버전업이 없고, 현 시점에 잘쓰이지 않는 라이브러리로 보여, `Jackson`또는 `Gson`을 사용하는 예제를 위에 언급한 [원본 예제 프로젝트](https://github.com/apache/struts-examples/tree/master/json-customize)를 진행 중인 struts2-study-parent 프로젝트에 맞게 수정하면서, 진행해보자. 



## Jackson 을 적용한 프로젝트 진행

* 프로젝트: [json-customize-jackson-struts](json-customize-jackson-struts)
  * Jackson 디펜던시
    
    ```xml
    <properties>
      <jackson.version>2.13.0</jackson.version>
    </properties>
    ...
    <!--
      flexjson 대신 jackson-databind를 사용해보자!
      https://github.com/FasterXML/jackson-databind
    -->
    <dependency>
      <groupId>com.fasterxml.jackson.core</groupId>
      <artifactId>jackson-databind</artifactId>
      <version>${jackson.version}</version>
    </dependency>
    
    <!--
      Java 8 date/time 처리를 위해서는 아래 모듈 추가 후 등록해야한다.
      JacksonJSONWriter 클래스 참조
    -->
    <dependency>
      <groupId>com.fasterxml.jackson.datatype</groupId>
      <artifactId>jackson-datatype-jsr310</artifactId>
      <version>${jackson.version}</version>
    </dependency>
    ```
    
    

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


출력

```json
{
  "name": "William Shakespeare",
  "birthday": "04/26/1564",
  "addresses": [
    {
      "name": "home",
      "street": "Henley",
      "city": "Stratford-upon-Avon",
      "zipcodes": [
        {
          "code": "CV37"
        }
      ]
    }
  ],
  "phoneNumbers": [
    {
      "name": "cell",
      "number": "555-123-4567"
    },
    {
      "name": "home",
      "number": "555-987-6543"
    },
    {
      "name": "work",
      "number": "555-678-3542"
    }
  ],
  "lastLogin": "11/20/2021",
  "password": "******",
  "username": "WillShak"
}
```





##### 진행간 특이사항

* Java 8 이상의 새로운 Date/Time 타입인 LocalDateTime 등으로 JSON으로 파싱할 때,  `jackson-datatype-jsr310` 라이브러리도 추가하고 모듈 등록을 해야 잘 동작하는 점.

* 원본 코드에서 날짜포멧적용시 변환 오류가 났던 부분

  * 로케일과 관련되는 포멧이여서, 내 환경에서는 한글로 월을 적어줘야되었다. 포멧을 MM으로 하고 값은 04로 바꾸자!

  ```java
      SimpleDateFormat formatter = new SimpleDateFormat("dd-MMM-yyyy");
  	// user.setBirthday(formatter.parse("26-Apr-1564")); 파싱 오류
      user.setBirthday(formatter.parse("26-4월-1564"));
  ```

  

#### 원본 예제 프로젝트의 요구사항 대로 진행이 안된 부분

##### 1. excludeProperties

제외 할 프로퍼티 이름 패턴을 전달해서 런타임시 제외 시키는 속성이 있는데, 이부분을 Jackson에서 어떻게 처리해야할지? 지금은 잘 모르겠다.

##### 2. includeProperties

포함할 할 프로퍼티 이름 패턴을 전달해서 런타임시 포함 시키는 속성이 있는데, 이부분을 Jackson에서 어떻게 처리해야할지? 지금은 잘 모르겠다. 기본적으로 포함이라서 유용할 수 있는 옵션인지 좀 해깔린다.



### 참조

##### Java 8 Date/Time 관련

* https://github.com/FasterXML/jackson-modules-java8
* https://github.com/FasterXML/jackson-modules-java8/tree/2.14/datetime

##### NULL 필드 제외 관련

* // https://www.baeldung.com/jackson-ignore-null-fields



---

## flexJSON을 적용한 프로젝트 진행

* 프로젝트: [json-customize-flexjson-struts](json-customize-flexjson-struts)

  * FlexJSON 디펜던시

    ```xml
    <properties>
      <flexjson.version>3.3</flexjson.version>
    </properties>
    ...
    <!-- 
      원본 예제 프로젝트 그대로 flexjson을 사용
      http://flexjson.sourceforge.net/
      https://mvnrepository.com/artifact/net.sf.flexjson/flexjson/3.3      
    -->
    <dependency>
      <groupId>net.sf.flexjson</groupId>
      <artifactId>flexjson</artifactId>
      <version>${flexjson.version}</version>
    </dependency>
    ```

    

### 예제 프로젝트의 문제점

원본 예제 프로젝트를 보고 내 환경에 맞게 적용해보았는데, 문제가 있는 부분이 있다.

변활할 도메인의 프로퍼티 값이 NULL일 경우 제외하기 위한 ExcludeTransformer 클래스가 구현 되어있는데, 이상태에서는 잘못된 JSON 을 만들어낸다.

```java
public class ExcludeTransformer extends AbstractTransformer {
    public void transform(Object o) {
        return;
    }
}
```

결과에 nickname 부분의 값이 설정되지 않은 것을 볼 수 있다. : `"nickname":,"password":"******"`

```
{"addresses":[{"city":"Stratford-upon-Avon","class":"org.fp024.struts2.study.demo.domain.Address","name":"home","street":"Henley"}],"birthday":"04/26/1564","class":"org.fp024.struts2.study.demo.domain.User","lastLogin":"11/20/2021","username":"WillShak","name":"William Shakespeare","nickname":,"password":"******"}
```

이 문제를 해결하기 위해서는 아래 처럼 isInline 메서드를 오버라이드 해서 true를 반환하게 설정해야한다.

```java
public class ExcludeTransformer extends AbstractTransformer {
  @Override
  public Boolean isInline() {
    return true;
  }

  @Override
  public void transform(Object o) {}
}

```



### 프로퍼티 제외/포함 설정

FlexJSON에서는 프로퍼티 이름을 통한 포함 제외설정이 적용하기 쉽게 되어있긴 한 것 같다.

struts.xml에서 아래와 같이 설정이 되어있어서, addresses 속성은 포함이고, addresses 이하의 state 속성은 제외인데, 예제에서 state 에 값을 설정하지 않았기 때문에, 테스트의 의미가 없는 상태이긴하다.

이값을 대신 addresses.name으로 바꿔보자. 

```xml
    <action name="produce" class="org.fp024.struts2.study.demo.action.ProduceAction">
      <result type="json">
        <!--
          flexJSON의 serializer.serialize(object) 는 
          도메인 안의 객체형 필드를 포함설정해주지 않으면 직렬화 대상에 포함하지 않는다.
          객체형 필드까지 모두 직렬화하려면 serializer.deepSerialize(object) 를 사용하자!
        -->
        <param name="includeProperties">
          addresses,
          addresses.zipcodes,
          phoneNumbers
        </param>
        <param name="excludeProperties"> <!-- 제외 설정을 addresses.name 으로 변경설정 -->
          addresses.name
        </param>
        <param name="excludeNullProperties">true</param>
        <param name="root">user</param>
      </result>
    </action>
```

그리고 도메인 모델의 class 속성과 그값으로 전체패키명.클래스명을 노출하던데... 

이부분의 경우 아래와 같이 설정하더라도 제대로 FlexJSONWriter로 전달이 안된다.

```xml
<param name="excludeProperties">
  addresses.name,
  *.class
</param> 
<!-- 
  *.class를 넣더라도 소용없음, flexJSONWriter 사용측으로 제대로 전달안됌 
  패턴에서 *가 맨앞에 있으면 처리가 잘 안되는 것 같음.
-->
```

이부분은 FlexJSONWriter 의 메서드 내에서 직접 넣어줘서 해결했다.

```java
JSONSerializer serializer = new JSONSerializer().exclude("*.class");
```

출력 결과

```json
{
  "addresses": [
    {
      "city": "Stratford-upon-Avon",
      "street": "Henley",
      "zipcodes": [
        {
          "code": "CV37"
        }
      ]
    }
  ],
  "birthday": "04/26/1564",
  "lastLogin": "11/20/2021",
  "username": "WillShak",
  "name": "William Shakespeare",
  "password": "******",
  "phoneNumbers": [
    {
      "name": "cell",
      "number": "555-123-4567"
    },
    {
      "name": "home",
      "number": "555-987-6543"
    },
    {
      "name": "work",
      "number": "555-678-3542"
    }
  ]
}
```



처음에 객체형 필드들이 안보여서 상속관계에 있는 것이 안보이나 했는데... 객체형 필드는 포함 규칙에 넣어줘야 직렬화 대상이 포함된 점이 정말 동작이 특이하단 느낌이 들었는데...

따로 메서드가 있었다.  serialize 메서드 대신에 deepSerialize 메서드를 쓰면 객채형 필드까지 직렬화 대상이 된다.

```java
try {
  //return serializer.serialize(object);
  return serializer.deepSerialize(object);
} catch (Exception exception) {
  throw new JSONException(exception);
}
```



### 참조

* FlexJSON에서 null 값 필드 제외 방법
  * https://stackoverflow.com/questions/7886709/how-to-exclude-null-value-fields-when-using-flexjson

