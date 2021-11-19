# JSON 플러그인

> 원문: https://struts.apache.org/plugins/json/

* 설명
* 설치
* 직렬화와 역직렬화의 커스터마이징
  * 속성 제외하기
  * 속성 포함하기
  * 루트 객체
  * 래핑(Wrapping)
  * 주석으로 감싸기
  * Prefix
  * Base 클래스
  * 열거형 (Enumerations)
  * 출력의 압축
  * 브라우저가 응답을 캐시하지 못하도록 방지
  * null 값인 속성 제외
  * 상태 및 에러 코드
  * JSONP
  * 컨텐츠 타입
  * 인코딩
  * 출력의 사용자 정의

* 예제
  * 액션 셋업
  * 액션에 대한 매핑 작성
  * JSON 예제 출력
  * JSON 수락하기

* JSON RPC
* 프록시 객체

## 설명

JSON 플러그인은 액션을 JSON으로 직렬화하는 `json` 결과 타입을 제공합니다.

1. `content-type`은 `application/json`이여야 합니다.
2. JSON 컨텐츠는 잘 구성되어야 합니다. 문법은 [json.org](https://www.json.org/json-ko.html)를 참조하세요.
3. 액션에는 입력해야하는 필드에 대해 public "setter" 메서드가 있어야합니다.
4. 입력에 지원되는 타입: 기본형 타입 (int,long…String), Date, List, Map, 기본형 타입 배열, 그외 클래스들 (나중에 상세 설명), 그외 클래스들의 배열
5. 리스트나 맵안에 채워질 JSON의 모든 객체는 Map 타입(속성에서 값으로 매핑됨), 모든 정수 타입은 Long 타입, 소수는 Double 타입,  모든 배열은 List 타입이 됩니다.



아래 JSON 문자열이 주어졌을 때:

```json
{
   "doubleValue": 10.10,
   "nestedBean": {
      "name": "Mr Bean"
   },
   "list": ["A", 10, 20.20, {
      "firstName": "El Zorro"
   }],
   "array": [10, 20] 
}
```

액션에는 `float` 또는 `double` 인수를 사용하는 `setDoubleValue` 메서드가 있어야 합니다(인터셉터는 값을 올바른 값으로 변환합니다).인수로 `String`을 취하는 `setName` 메서드를 가지고 있는 어떤 클래스를 인자로 받을 수 있는 `setNestedBean` 메서드가 있어야 합니다. `List`를 인수로 사용하는 `setList` 메서드가 있어야 하며, 해당 list에는 "A"(`String`), 10(`Long`), 20.20(`Double`), Map(`firstName` -> `El Zorro`)가 포함됩니다. `setArray` 메서드는 `List` 또는 모든 숫자 배열을 파라미터로 사용할 수 있습니다.

>javascript에서는 객체를 JSON으로 직렬화합니다. [json2](https://github.com/douglascrockford/JSON-js)를 참조하십시오.

`root` 속성은 JSON 배열을 처리할 때 `JSONInterceptor`에서 설정해야합니다.

이 플러그인은 [JSON Ajax 유효성 검사](https://struts.apache.org/plugins/json/json-ajax-validation)도 제공합니다.



## 설치

이 플러그인은 플러그인 jar를 애플리케이션의 `/WEB-INF/lib` 디렉토리에 복사하여 설치할 수 있습니다. 다른 파일을 복사하거나 만들 필요가 없습니다.

maven을 사용하면 다음을 pom에 추가하세요:

```xml
<dependencies>
   ...
   <dependency>
       <groupId>org.apache.struts</groupId>
       <artifactId>struts2-json-plugin</artifactId>
       <version>STRUTS_VERSION</version>
   </dependency>
   ...
</dependencies>
```



## 직렬화와 역직렬화의 커스터마이징

JSON 어노테이션을 사용하여 직렬화/역직렬화 프로세스를 사용자 정의하세요. 사용 가능한 JSON 어노테이션 필드:

| 이름        | 설명                                                         | 기본 값                 | 직렬화 | 역직렬화 |
| ----------- | ------------------------------------------------------------ | ----------------------- | ------ | -------- |
| name        | 사용자정의 필드 이름                                         | 비어있음                | 예     | 아니요   |
| serialize   | 직렬화에 포함                                                | true                    | 예     | 아니요   |
| deserialize | 역직렬화에 포함                                              | true                    | 아니요 | 예       |
| format      | Date 필드의 형식을 지정하거나 구문 분석하는 데 사용되는 형식 | “yyyy-MM-dd’T’HH:mm:ss” | 예     | 예       |



### 속성 제외하기

쉼표로 구분된 정규식 목록을 JSON 결과 및 인터셉터에 전달할 수 있습니다. 이러한 정규식과 일치하는 속성는 직렬화 프로세스에서 무시됩니다:

```xml
<!-- Result fragment -->
<result type="json">
  <param name="excludeProperties">
    login.password,
    studentList.*.sin
  </param>
</result>

<!-- Interceptor fragment -->
<interceptor-ref name="json">
  <param name="enableSMD">true</param>
  <param name="excludeProperties">
    login.password,
    studentList.*.sin
  </param>
</interceptor-ref>
```



### 속성 포함하기

쉼표로 구분된 정규식 목록을 JSON 결과에 전달하여 직렬화할 속성을 제한할 수 있습니다. 이러한 정규식과 일치하는 속성만 직렬화된 출력에 포함됩니다.

>제외 속성 표현식은 포함 속성 표현식보다 우선합니다. 즉, 동일한 결과에 대해 포함 및 제외 속성 표현식을 사용하는 경우 제외 속성 표현식이 속성과 먼저 일치하면 포함 속성 표현식이 적용되지 않습니다.

```xml
<!-- Result fragment -->
<result type="json">
  <param name="includeProperties">
    ^entries\[\d+\].clientNumber,
    ^entries\[\d+\].scheduleNumber,
    ^entries\[\d+\].createUserId
  </param>
</result>
```



### 루트 객체

`root`속성(OGNL 식)을 사용하여 직렬화할 루트 객체를 지정합니다.

```xml
<result type="json">
  <param name="root">
    person.job
  </param>
</result>
```

`root` 속성(OGNL 표현식)은 채워져야 하는 객체를 지정하기 위해 인터셉터에서 사용할 수도 있습니다. **이 객체가 null이 아닌지 확인하세요.**

```xml
<interceptor-ref name="json">
  <param name="root">bean1.bean2</param>
</interceptor-ref>
```



### 래핑(Wrapping)

주석으로 래핑, prefix 추가 또는 결과를 텍스트 영역에 래핑해야 하는 파일 업로드를 사용하는 등 여러 가지 이유로 JSON 출력을 일부 텍스트로 래핑할 수 있습니다. `wrapPrefix`를 사용하여 시작 부분에 내용을 추가하고 `wrapPostfix`를 사용하여 끝에 내용을 추가합니다. 이 설정은 0.34부터 더 이상 사용되지 않는 `wrapWithComments` 및 `prefix`보다 우선 적용됩니다. 예:

* 주석으로 래핑

  ```xml
  <result type="json">
    <param name="wrapPrefix">/*</param>
    <param name="wrapSuffix">*/</param>
  </result>
  ```

* prefix 추가

  ```xml
  <result type="json">
    <param name="wrapPrefix">{}&&</param>
  </result>
  ```

* 파일업로드를 위한 래핑

  ```xml
  <result type="json">
    <param name="wrapPrefix"><![CDATA[<html><body><textarea>]]></param>
    <param name="wrapSuffix"><![CDATA[</textarea></body></html>]]></param>
  </result>
  ```

  

### 주석으로 감싸기

`wrapWithComments`는 0.34에서 더 이상 사용되지 않습니다. 대신 `wrapPrefix` 및 `wrapSuffix`를 사용하세요.

>`wrapWithComments`는 안전한 JSON 텍스트를 위험한 텍스트로 바꿀 수 있습니다. 예: `"\*/ alert('XSS'); /\*"` 팁을 주신 Douglas Crockford에게 감사드립니다! `wrapWithComments` 대신에 **prefix**를 사용하는 것이 좋습니다.

직렬화된 JSON이 `{name: 'El Zorro'}`이라면 출력은 `{}&& ({name: 'El Zorro'})`가 됩니다.

`wrapWithComments`(기본값은 false) 속성이 true로 설정된 경우 생성된 JSON은 다음과 같은 주석으로 래핑됩니다.

```json
/* {
   "doubleVal": 10.10,
   "nestedBean": {
      "name": "Mr Bean"
   },
   "list": ["A", 10, 20.20, {
      "firstName": "El Zorro"
   }],
   "array": [10, 20] 
} */
```

이러한 주석을 제거하려면 다음처럼 사용하세요.

```javascript
var responseObject = eval("("+data.substring(data.indexOf("\/\*")+2, data.lastIndexOf("\*\/"))+")");
```



### Prefix

`prefix`는 0.34에서 더 이상 사용되지 않으며 대신 `wrapPrefix` 및 `wrapSuffix`를 사용하세요.

`prefix` 파라미터를 true로 설정하면 생성된 JSON에 `{}&&` 파라미터가 붙습니다. 이것은 하이재킹을 방지하는 데 도움이 됩니다. 자세한 내용은 이 [Dojo 티켓](http://trac.dojotoolkit.org/ticket/6380)을 참조하세요.

```xml
<result type="json">
  <param name="prefix">true</param>
</result>
```



### Base 클래스

기본적으로 루트 객체의 Base 클래스에 정의된 속성은 직렬화되지 않습니다. 모든 Base 클래스(Object까지)의 속성을 직렬화하려면 JSON 결과에서 `ignoreHierarchy`를 false로 설정합니다.

```xml
<result type="json">
  <param name="ignoreHierarchy">false</param>
</result>
```



### 열거형 (Enumerations)

기본적으로 Enum은 `value = name()`인 `name=value` 쌍으로 직렬화됩니다.

```java
  public enum AnEnum {
     ValueA,
     ValueB
  }
```

```json
 "myEnum":"ValueA"
```

`enumAsBean` 결과 파라미터를 사용하여 값이 `name()`인 특수 속성 `_name`이 있는 빈으로 Enum을 직렬화합니다. 열거형의 모든 속성도 직렬화됩니다.

```java
public enum AnEnum {
    ValueA("A"),
    ValueB("B");

    private String val;

    public AnEnum(val) {
        this.val = val;
    }
    public getVal() {
        return val;
    }
}
```

```json
 myEnum: { "_name": "ValueA", "val": "A" }
```

struts.xml을 통해 `enumAsBean` 파라미터를 활성화하세요:

```xml
<result type="json">
  <param name="enumAsBean">true</param>
</result>
```



### 출력의 압축

생성된 json 응답을 gzip으로 압축하려면 `enableGZIP` 속성을 true로 설정합니다. 이것이 작동하려면 요청(request)에서 `Accept-Encoding` 헤더에 gzip이 포함되어야 합니다.

```xml
<result type="json">
  <param name="enableGZIP">true</param>
</result>
```



### 브라우저가 응답을 캐시하지 못하도록 방지

응답에서 다음 헤더를 설정하려면 `noCache`를 true(기본값은 false)로 설정합니다.

* `Cache-Control: no-cache`
* `Expires: 0`
* `Pragma: No-cache`

```xml
<result type="json">
  <param name="noCache">true</param>
</result>
```



### null 값인 속성 제외

기본적으로 null 값이 있는 필드는 `{property_name: null}`과 같이 직렬화됩니다. 이 문제는 `excludeNullProperties`를 `true`로 설정하여 방지할 수 있습니다.

```xml
<result type="json">
  <param name="excludeNullProperties">true</param>
</result>
```



### 상태 및 에러코드

`statusCode`를 사용하여 응답 상태를 설정합니다.

```xml
<result type="json">
  <param name="statusCode">304</param>
</result>
```

오류를 보내기 위한 `errorCode` (서버가 클라이언트에 직렬화된 JSON이 아닌 무언가를 보낼 수 있음):

```xml
<result type="json">
  <param name="errorCode">404</param>
</result>
```



### JSONP

JSONP를 활성화하려면 JSON 결과 또는 인터셉터에서 `callbackParameter` 파라미터를 설정하세요. 해당 이름을 가진 파라미터가 요청에서 읽혀지고, 그 값은 JSONP 함수로 사용됩니다.

"callback"="exec" 파라미터로 요청이 이루어진다고 가정합니다.

```xml
<result type="json">
  <param name="callbackParameter">callback</param>
</result>
```

그리고 직렬화된 JSON은 `{name: 'El Zorro'}`입니다. 그러면 출력은 `exec({name: 'El Zorro'})`가 됩니다.



### 컨텐츠 타입

컨텐츠 타입은 SMD를 사용하는 경우 기본적으로 `application/json-rpc`로 설정되고 그렇지 않으면 `application/json`으로 설정됩니다. Dojo 및 YUI로 파일을 업로드할 때와 같이 컨텐츠 타입을 다른 것으로 설정해야 하는 경우가 있습니다. 이러한 경우에는 contentType 파라미터를 사용하세요. 

```xml
<result type="json">
  <param name="contentType">text/html</param>
</result>
```



### 인코딩

사용자는 `struts.i18n.encoding`에 할당된 기본값을 기반으로 또는 결과(Result)별로 인코딩을 정의할 수 있습니다. 주어진 결과에 대한 인코딩을 정의하려면 아래와 같이 인코딩 파라미터를 추가하세요.

```xml
<result type="json">
  <param name="encoding">UTF-8</param>
</result>
```



### 출력의 사용자 정의

Struts 2.5.14부터 `org.apache.struts2.json.JSONWriter` 인터페이스를 구현하여 생성된 json 응답을 사용자 정의할 수 있습니다. 구현은 다음과 같이 `struts.xml`에 정의되어야 합니다.

```xml
<struts>
    <bean type="org.apache.struts2.json.JSONWriter" name="myJSONWriter" class="com.mycompany.MyJSONWriter"
          scope="prototype"/>
    <constant name="struts.json.writer" value="myJSONWriter"/>
</struts>
```

[struts-examples/json-customize/FlexJSONWriter.java](https://github.com/apache/struts-examples/blob/master/json-customize/src/main/java/org/demo/FlexJSONWriter.java)에 예제가 있습니다. 아래와 같이 Struts 기본 json 직렬 변환기를 [Flexjson](http://flexjson.sourceforge.net/)으로 대체합니다.

```java
import flexjson.JSONSerializer;
import flexjson.transformer.DateTransformer;
import org.apache.struts2.json.JSONException;
import org.apache.struts2.json.JSONWriter;

public class FlexJSONWriter implements JSONWriter {
    private String dateFormatter;

    public String write(Object object) throws JSONException {
        return this.write(object, null, null, false);
    }

    public String write(Object object, Collection<Pattern> excludeProperties
                      , Collection<Pattern> includeProperties
                      , boolean excludeNullProperties) throws JSONException {

        JSONSerializer serializer = new JSONSerializer();
        if (excludeProperties != null) {
            for (Pattern p : excludeProperties) {
                serializer = serializer.exclude(p.pattern());
            }
        }
        if (includeProperties != null) {
            for (Pattern p : includeProperties) {
                serializer = serializer.include(p.pattern());
            }
        }
        if (excludeNullProperties) {
            serializer = serializer.transform(new ExcludeTransformer(), void.class);
        }
        if (dateFormatter != null) {
            serializer = serializer.transform(new DateTransformer(dateFormatter), Date.class);
        }
        return serializer.serialize(object);
    }
    //...
```

> Flexjson은 Java 객체를 JSON으로 직렬화 및 역직렬화하기 위한 경량 라이브러리입니다.



## 예제

### 액션 셋업

이 단순한 액션에는 다음과 같은 필드가 있습니다.

##### 예제:

```java
import java.util.HashMap;
import java.util.Map;

import com.opensymphony.xwork2.Action;

public class JSONExample {
    private String field1 = "str";
    private int[] ints = {10, 20};
    private Map map = new HashMap();
    private String customName = "custom";

    //'transient' fields are not serialized
    private transient String field2;

    //fields without getter method are not serialized
    private String field3;

    public String execute() {
        map.put("John", "Galt");
        return Action.SUCCESS;
    }

    public String getField1() {
        return field1;
    }

    public void setField1(String field1) {
        this.field1 = field1;
    }

    public int[] getInts() {
        return ints;
    }

    public void setInts(int[] ints) {
        this.ints = ints;
    }

    public Map getMap() {
        return map;
    }

    public void setMap(Map map) {
        this.map = map;
    }

    @JSON(name="newName")
    public String getCustomName() {
        return this.customName;
    }
}
```



### 액션에 대한 매핑 작성

1. `json-default`를 확장하는 패키지 내부에 맵 추가
2.  `json` 타입의 결과 추가

##### [Convention 플러그인](https://struts.apache.org/plugins/convention/) 구성의 예:  (Struts 2.1 이후 번들로 제공됨)

```java
import java.util.HashMap;
import java.util.Map;

import com.opensymphony.xwork2.ActionSupport;
import org.apache.struts2.convention.annotation.Result;

@Result(type = "json")
public class JSONExample extends ActionSupport {
// action code
}
```

##### XML 구성의 예:

```xml
<?xml version="1.0" encoding="UTF-8" ?>
<!DOCTYPE struts PUBLIC
    "-//Apache Software Foundation//DTD Struts Configuration 2.0//EN"
    "http://struts.apache.org/dtds/struts-2.0.dtd">

<struts>
  <package name="example"  extends="json-default">
     <action name="JSONExample" class="example.JSONExample">
        <result type="json"/>
     </action>
  </package>
</struts>
```



### JSON 예제 출력

```json
{  
   "field1" : "str", 
   "ints": [10, 20],
   "map": {
       "John":"Galt"
   },
   "newName": "custom"
}
```



### JSON 수락하기

액션을 `json` 인터셉터를 사용하는 패키지에 두거나  다음과 같이 참조를 추가하여 액션은 들어오는 JSON을  수락할 수 있습니다.

```java
@InterceptorRef(value="json")
```

기본적으로 `Content-Type` 이 `application/json` 이면 역직렬화에 사용되고 `application/json-rpc`는 SMD 처리를 실행하는 것으로 인식됩니다. `jsonContentType` 및 `jsonRpcContentType` 파라미터를 정의하여 이러한 설정을 재정의할 수 있습니다. 예:

```xml
<interceptor-ref name="json">
  <param name="jsonContentType">text/json</param>
  <param name="jsonRpcContentType">text/json-rpc</param>
</interceptor-ref>
```

이것들은 스택당 범위가 지정된 파라미터입니다. 즉, 일단 설정되면 이 스택 범위의 액션에서 사용됩니다. 



## JSON RPC

json 플러그인을 사용하여 javascript에서 액션 메서드를 실행하고 출력을 반환할 수 있습니다. 이 기능은 Dojo를 염두에 두고 개발되었으므로 [Simple Method Definition](http://manual.dojotoolkit.org/WikiHome/DojoDotBook/Book9)을 사용하여 원격 서비스를 알립니다. 예제로 해결해 보겠습니다(대부분의 예제에서는 무의미함).

##### 먼저 액션을 작성하세요.

```java
package smd;

import com.googlecode.jsonplugin.annotations.SMDMethod;
import com.opensymphony.xwork2.Action;

public class SMDAction {
    public String smd() {
        return Action.SUCCESS;
    }
    
    @SMDMethod
    public Bean doSomething(Bean bean, int quantity) {
        bean.setPrice(quantity * 10);
        return bean;
    }
}
```

원격으로 호출되는 메서드는 보안상의 이유로 **`@SMDMethod` 어노테이션을 달아야 합니다.** 이 메서드는 빈 객체를 가져와서 가격을 수정하고 반환합니다.

액션에 `SMD` 어노테이션을 추가하여 생성된 SMD를 사용자 정의할 수 있으며(자세한 내용은 곧 설명) 파라미터에 `SMDMethodParameter` 어노테이션을 추가할 수 있습니다.  보시다시피 "dummy", `smd` 메서드가 있습니다. 이 메서드는 `json` 결과를 사용하여 Simple Method Definition(이 클래스에서 제공하는 모든 서비스의 정의)를 생성하는 데 사용됩니다.

##### 빈 클래스:

```java
package smd;

public class Bean {
    private String type;
    private int price;
    
    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }
}
```



##### 매핑:

```xml
<package name="RPC" namespace="/nodecorate" extends="json-default">
    <action name="SMDAction" class="smd.SMDAction" method="smd">
        <interceptor-ref name="json">
            <param name="enableSMD">true</param>
        </interceptor-ref>
        <result type="json">
             <param name="enableSMD">true</param>
        </result>
    </action>
</package>
```

인터셉터와 결과 **둘다** 액션에 적용되어야 하고 둘 다에 대해 `enableSMD`가 활성화되어야 한다는 점을 제외하고 여기에서는 특별한 것이 없습니다.



##### 자바스크립트 코드:

```javascript
<s:url id="smdUrl" namespace="/nodecorate" action="SMDAction" />
<script type="text/javascript">
    //dojo RPC 로드
    dojo.require("dojo.rpc.*");
    
    // SMD를 사용하여 서비스 객체(프록시) 생성(json 결과에 의해 생성됨)
    var service = new dojo.rpc.JsonService("${smdUrl}");
    
    // 원격 메서드가 반환될 때 호출되는 함수
    var callback = function(bean) {
        alert("Price for " + bean.type + " is " + bean.price);
    };
    
    // 파라미터
    var bean = {type: "Mocca"};
    
    // 원격 메소드 실행
    var defered = service.doSomething(bean, 5);

    // 지연된 객체에 콜백 연결
    defered.addCallback(callback);
</script>
```

Dojo의 JsonService는 SMD를 로드하기위해 액션에 요청을 하고, SMD는 사용 가능한 원격 메서드의 정의와 함께 JSON 객체를 반환하며, 이 정보를 사용하여 Dojo는 이러한 메서드에 대한 "프록시"를 생성합니다.

요청의 비동기적 특성 때문에 메서드가 실행될 때 콜백 함수를 연결할 수 있는 지연 객체가 반환됩니다. 콜백 함수는 액션에서 반환된 객체를 파라미터로 받습니다. 그것이 다입니다.



## 프록시 객체

Java에서는 어노테이션이 상속되지 않기 때문에 일부 사용자가 프록시 객체를 직렬화하는 동안 문제가 발생할 수 있습니다. (예를 들어 액션에 AOP 인터셉터를 연결했을 경우.)

이러한 상황에서 플러그인은 액션의 메서드들의 어노테이션을 감지하지 않습니다.

이 문제를 해결하려면 `ignoreInterfaces` 결과 파라미터를 false(기본값 true)로 설정하여 플러그인이 액션 메서드의 어노테이션을 감지하기 위해 모든 인터페이스와 액션의 슈퍼클래스를 검사하도록 요청합니다.

> 이 파라미터는 인터페이스를 통한 재귀로 인해 성능 비용이 발생하므로 액션이 프록시일 수 있는 경우에만 false로 설정해야 합니다.

```xml
<action name="contact" class="package.ContactAction" method="smd">
   <interceptor-ref name="json">
      <param name="enableSMD">true</param>
      <param name="ignoreSMDMethodInterfaces">false</param>
   </interceptor-ref>
   <result type="json">
      <param name="enableSMD">true</param>
      <param name="ignoreInterfaces">false</param>
   </result>
   <interceptor-ref name="default"/>
</action>
```



---

## 의견

* Lucy 프레임워크를 사용할 때, 특별히 JSON 플러그인 같은 것을 사용해진 않았던것 같다.
  * Jackson 을 사용해서 JSON 문자열로 바꿔서 response.write로 응답으로 보냈던 것 같다
  * 그런데 가끔 @JSON 어노테이션을 사용했던 부분이 보였던 것 같긴하다.

* 플러그인 기능을 최소화하고, 구체적인 기능은 Jackson이나 Gson으로 위임하는게 나을 것 같은데...

  * 문서 앞 부분과 json 플러그인 코드를 잠깐 봤을 때는, JSON을 직접 처리하고 있길레 위와 같은 생각을 했는데, 타사 JSON 직렬화/역직렬화 라이브러리를 연동해서 사용하는 방법이 나와 있다.
  * [Flexjson](http://flexjson.sourceforge.net/) 이란 라이브러리를 사용한 예제로 설명을 해주셨는데, 이 라이브러리가 2013년 이후로 버전업이 없고, 현 시점에 잘쓰이지 않는 라이브러리로 보여, Jackson이나, Gson을 사용하는 예제를 만들어봐야겠다.

* SMD?

  * [Simple Method Definition](http://manual.dojotoolkit.org/WikiHome/DojoDotBook/Book9)  (현재는 링크가 깨져있다.)

  * 그런데,, 아무래도 지금은 예전과 다르게 SMD의 의미가 바뀐 것인지...  [Service Mapping Description](https://dojotoolkit.org/reference-guide/1.10/dojox/rpc/smd.html#id1) 이다.

    

