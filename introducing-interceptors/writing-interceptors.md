# 인터셉터 작성하기

> 원문 : https://struts.apache.org/core-developers/writing-interceptors.html

인터셉터의 작동 방식에 대한 개요는 [인터셉터](https://struts.apache.org/core-developers/interceptors.html) 페이지를 참조하세요.



## Interceptor 인터페이스

인터셉터는 `com.opensymphony.xwork2.interceptor.Interceptor` 인터페이스를 구현해야 합니다.

### Interceptor.java

```java
public interface Interceptor extends Serializable {
    void destroy();
    void init();
    String intercept(ActionInvocation invocation) throws Exception;
}
```

`init` 메서드는 인터셉터가 인스턴스화되고 `intercept` 메서드를 호출하기 전에 호출됩니다. 인터셉터가 사용하는 리소스를 할당하는 곳입니다.

`intercept` 메서드는 인터셉터 코드가 작성되는 곳입니다. 액션 메소드와 마찬가지로 `intercept`는 Struts가 요청을 다른 웹 리소스로 전달하는 데 사용하는 결과를 반환합니다. `ActionInvocation` 타입의 파라미터에 대해 `invoke` 메서드를 호출하면 액션(스택의 마지막 인터셉터인 경우) 또는 다른 인터셉터가 실행됩니다.

>`invoke`는 결과가 호출된 후(예: JSP가 렌더링된 후) 반환되므로 *open-session-in-view* 패턴과 같은 작업에 적합합니다. 결과가 호출되기 전에 무언가를 하고 싶다면 `PreResultListener`를 구현해야 합니다.

애플리케이션 종료 시 리소스를 해제하려면 `destroy` 메서드를 오버라이드 합니다. 



## 스레드 안정성

**인터셉터는 스레드로부터 안전해야 합니다!**

>Struts 2 액션 인스턴스는 모든 요청에 대해 생성되며 스레드로부터 안전할 필요는 없습니다. 반대로 인터셉터는 요청 간에 공유되며 스레드로부터 안전해야 합니다. (만약 Spring 빈으로 만든다면 싱글톤 빈 스코프로 등록하되, 상태를 갖는 필드가 없도록 해야할 것 같다.)

## AbstractInterceptor

AbstractInterceptor 클래스는 `init` 및 `destroy`의 비어있는 구현을 제공하며 이러한 메서드가 구현되지 않을 경우 사용할 수 있습니다.



## 매핑

인터셉터는 `<interceptors/>` 요소 내부에 중첩된 `<interceptor/>` 요소를 사용하여 선언됩니다.

` struts-default.xml`의 예:

```xml
<struts>
   ...

   <package name="struts-default">
      <interceptors>
         <interceptor name="alias" class="com.opensymphony.xwork2.interceptor.AliasInterceptor"/>
         <interceptor name="autowiring" class="com.opensymphony.xwork2.spring.interceptor.ActionAutowiringInterceptor"/>
         ...
      </interceptors>
   </package>

   ...
</struts>
```

### 예제

setDate(Date) 메서드가 있는  "MyAction" 타입의 액션이 있다고 가정할 때, 아래의 `SimpleInterceptor`는 액션의 date를 현재 날짜로 설정합니다.

```java
import com.opensymphony.xwork2.ActionInvocation;
import com.opensymphony.xwork2.interceptor.AbstractInterceptor;

public class SimpleInterceptor extends AbstractInterceptor {

    public String intercept(ActionInvocation invocation) throws Exception {
       MyAction action = (MyAction)invocation.getAction();
       action.setDate(new Date());
       return invocation.invoke();
    }
}
```

