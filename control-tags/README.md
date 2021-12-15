# 제어 태그

> 원문 : https://struts.apache.org/getting-started/control-tags.html

* 서문
* Struts 2 if 태그
* Struts iterator 태그
* 추가 Iterator 속성

이 튜토리얼의 예제 코드인 **control-tags**는 Struts 2 깃허브의 [struts-example](https://github.com/apache/struts-examples) 레파지토리에서 체크아웃 할 수 있습니다.





## 서문

Struts 2에는 뷰에서 사용할 수 있는 여러 제어 태그가 있습니다. 이 튜토리얼에서는 Struts 2 if 및 iterator 태그를 사용하는 방법의 예를 설명하고 보여줍니다. 이러한 태그 및 기타 제어 태그에 대한 자세한 내용은 [태그 레퍼런스](http://cwiki.apache.org/confluence/display/WW/Generic+Tag+Reference)를 참조하세요. 

Struts 2 [사용자 메일링](http://struts.apache.org/mail.html) 리스트는 도움을 받을 수 있는 훌륭한 장소입니다. 튜토리얼 예제 애플리케이션을 작동시키는데 문제가 있는 경우 Struts 2 메일리스트를 검색하세요. 문제에 대한 답을 찾지 못한 경우 메일링 리스트에 질문을 게시해보세요. 





## Struts 2 if 태그

예제 애플리케이션의 Thankyou.jsp는 이 마크업입니다. 

### thankyou.jsp - Struts if 태그

```jsp
<s:if test="personBean.over21">
    <p>You are old enough to vote!</p>
</s:if>
<s:else>
   <p>You are NOT old enough to vote.</p>
</s:else>
```

Struts if 태그에는 test 속성이 있습니다. test 속성의 값은 true 또는 false로 평가되어야 합니다. true이면 여는 `s:if` 태그와 닫는 `s:if`  태그 사이의 문장이 실행됩니다. false인 경우 여는 `s:else` 태그와 닫는 `s:else`  태그 사이의 문장이 실행됩니다. `s:else` 태그는 닫는 `s:if` 태그 뒤에 오고 `s:else` 태그는 필수는 아닙니다. 

위의 예에서 Struts 프레임워크는 액션 클래스(EditAction.java)에 의해 노출된 getPersonBean 메서드를 호출합니다. 해당 메서드에서 반환된 Person 객체를 사용하여 프레임워크는 Person 클래스의 isOver21 메서드를 호출합니다. 이 메서드는 test가 참인지 거짓인지 결정하는 데 사용되는 부울값을 반환합니다. 

test 속성의 값은 true 또는 false로 평가되는 표현식이어야 하지만 부울을 반환하는 메서드 호출일 필요는 없습니다. 예를 들어, Thankyou.jsp에 있는 이 s:if 태그는 더 복잡한 표현을 가지고 있습니다. 

```jsp
<s:if test="personBean.carModels.length > 1">
    <p>Car models
</s:if>
<s:else>
   <p>Car model
</s:else>
```

위 마크업의 목적은 사용자가 편집 페이지에서 선택한 자동차 모델 수에 따라 "Car model" 또는 "Car models"을 사용하는 것입니다. 따라서 이 반복자 태그의 test 속성 값은 carModels String 배열의 길이를 가져와 1과 비교합니다. 1보다 크면 올바른 문법은 "Car models"이고 그렇지 않을 때 올바른 문법은 "Car model"입니다. 





## Struts iterator 태그

Struts iterator 태그는 컬렉션의 각 항목을 반복하는 루프를 생성하는 데 사용됩니다. Thankyou.jsp에 다음 마크업이 있습니다. 

```jsp
<table style="margin-left:15px">
    <s:iterator value="personBean.carModels">
        <tr><td><s:property /></td></tr>
    </s:iterator>
</table>
```

이 코드의 목표는 편집 페이지에서 사용자가 선택한 자동차 모델을 표시하는 행이 있는 HTML 테이블을 만드는 것입니다. 사용자가 편집 페이지에서 선택한 자동차 모델은 (Person 클래스의) personBean 객체의 carModels 필드(문자열 배열)에 저장됩니다. 

iterator 태그에는 컬렉션(배열, 목록, 맵)으로 평가되어야 하는 값 속성이 있습니다. 

iterator 태그 안에 중첩된 `s:property` 태그는 iterator가 컬렉션의 요소를 반복할 때마다 컬렉션의 특정 값을 표시하는 데 사용됩니다. 컬렉션은 String 객체의 배열이므로 `s:property` 태그는 value 속성을 지정할 필요가 없습니다. 기본적으로 `s:property` 태그는 컬렉션의 해당 요소에 대한 단일 문자열을 표시합니다. 

컬렉션에 여러 필드가 있는 객체가 포함된 경우 s:property 태그의 value 속성을 사용하여 표시할 필드를 결정해야 합니다. 예를 들면: 

```jsp
<table style="margin-left:15px">
    <s:iterator value="states" >	
        <tr><td><s:property value="stateAbbr" /></td> <td><s:property value="stateName" /></tr>
    </s:iterator>
</table>
```

iterator 태그의 값은 Struts 2 프레임워크가 액션 클래스(EditAction.java)의 getStates 메서드를 호출하도록 하는 상태입니다. getStates 메서드는 State 객체의 목록을 반환합니다. State 클래스에는 두 개의 필드 stateAbbr 및 stateName이 있으며 둘 다 적절한 get 메소드가 있습니다. iterator는 컬렉션에 저장된 각 State 개체를 반복합니다. 루프를 통과할 때마다 Struts 2 프레임워크는 현재 State 객체에 대한 참조를 가지며 현재 State 객체에 대해 getStateAbbr 및 getStateName 메서드를 호출합니다. 





## 추가 Iterator 속성

Struts 2 iterator 태그에는 iterator 태그가 컬렉션의 일부에 대해서만 루프하도록 지정하기 위해 시작 및 끝 값을 제어하는 데 사용할 수 있는 추가 속성이 있습니다.  자세한 내용은 [iterator 태그 레퍼런스](https://cwiki.apache.org/confluence/display/WW/iterator)를 참조하세요. 




### >  [XML을 사용한 폼 유효성 검증](../form-validation-using-xml)로 돌아가기 또는 [와일드카드 메서드 선택](../wildcard-method-selection)으로 이동

---

## 제어 태그 예제 진행...

* 변경사항
  * 프로젝트명: [control-tags-struts](control-tags-struts)
  * Hancrest 라이브러리 추가한 김에 단정문을 assertThat으로 바꿔봤는데, 뭔가 읽기 편해진 것 같다. 😁
* [x] 서문
* [x] Struts 2 if 태그
* [x] Struts iterator 태그
* [x] 추가 Iterator 속성
* [x] 요약

