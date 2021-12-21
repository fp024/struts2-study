# Bean Validation 플러그인

> 원문: https://struts.apache.org/plugins/bean-validation/

## Bean 유효성 검증

Bean Validation 플러그인은 Struts 액션에서 Bean 유효성 검사를 사용하는 브리지를 구현합니다.  Bean 유효성 검증은 JSR 303에 지정되었으며 JavaEE 플랫폼의 일부입니다. 본격적인 애플리케이션 서버는 이미 이 플러그인에서 활용할 수 있는 유효성 검사 공급자를 제공합니다. 플러그인은 다음과 같은 다른 Struts 기능과 통합됩니다. 

- i18n
- model driven
- AJAX Validation
- workflow



## 설정

Bean Validation 플러그인을 사용하려면 먼저 JAR 파일을 애플리케이션의 `WEB-INF/lib` 디렉토리에 추가하거나 프로젝트의 Maven POM 파일에 의존성을 포함해야 합니다. 

### pom.xml

```xml
<dependency>
    <groupId>org.apache.struts</groupId>
    <artifactId>struts2-bean-validation-plugin</artifactId>
    <version>X.X.X</version>
</dependency>
```



## 구성

이 샘플은 플러그인이 제공하는 구성 상수를 보여줍니다. 또한 플러그인과 함께 제공되는 `struts-bean-validation`에서 자신의 애플리케이션 패키지를 확장하여 bean-validation을 활성화하는 방법을 보여줍니다. 

### struts.xml

```xml
<struts>
    <constant name="struts.beanValidation.providerClass" value="org.hibernate.validator.HibernateValidator"/>
    <constant name="struts.beanValidation.ignoreXMLConfiguration" value="false"/>
    <constant name="struts.beanValidation.convertMessageToUtf" value="false"/>
    <constant name="struts.beanValidation.convertMessageFromEncoding" value="ISO-8859-1"/>
 
    <package name="my-bean-validation" extends="struts-bean-validation">
    </package>
</struts>
```

다음은 자신의 인터셉터 스택을 구성하여 bean-validation을 다른 플러그인과 결합하는 방법을 보여주는 또 다른 예입니다(참고: 이것은 매우 짧은 예일 뿐입니다. 실제 앱에서는 스택에 더 주의해야 합니다). 스택에 적절한 인터셉터를 넣거나 스택에서 제거하여 Bean 유효성 검사를 고전적인 Struts 유효성 검사와 결합하거나 둘 중 하나를 비활성화할 수 있습니다. 

### struts.xml

```xml
<package name="my-app-package" extends="struts-default">
    <interceptors>
        <interceptor 
            name="beanValidation"
            class="org.apache.struts.beanvalidation.validation.interceptor.BeanValidationInterceptor"></interceptor>
        <interceptor-stack name="appDefaultStack">
            <interceptor-ref name="beanValidation"/>
            <interceptor-ref name="defaultStack"/>
        </interceptor-stack>
    </interceptors>
</package>
```





## Bean 유효성검사 예

다음은 Bean 유효성 검사를 사용하는 액션의 예입니다. 유효성 검사 어노테이션 중 일부는 `javax` 패키지(JSR에 정의됨)에서 가져오고 다른 것은 유효성 검사 제공자(이 경우 `hibernate`)에서 가져옵니다. 

어노테이션의 `message` 속성에서 고유한 텍스트 key를 지정할 수 있습니다. 그렇게 하면 전체 Struts i18n 메커니즘이 시작되어 해당 텍스트 key를 해결합니다. 



### com.example.actions.BeanValidationAction

```java
package com.example.actions;


import com.opensymphony.xwork2.ActionSupport;
import org.apache.struts.beanvalidation.constraints.FieldMatch;
import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Namespace;
import org.apache.struts2.convention.annotation.ParentPackage;
import org.apache.struts2.convention.annotation.Result;
import org.apache.struts2.interceptor.validation.SkipValidation;
import org.hibernate.validator.constraints.Email;
import org.hibernate.validator.constraints.NotBlank;
import org.hibernate.validator.constraints.ScriptAssert;
import org.hibernate.validator.constraints.URL;
import javax.validation.constraints.*;
import java.util.Date;


@Namespace("/bean-validation")
@ParentPackage("my-bean-validation")
@Action(results = {
        @Result(name = "input", location = "bean-validation.jsp"),
        @Result(name = "success", location = "/WEB-INF/validation/successFieldValidatorsExample.jsp")
})
@FieldMatch(first = "fieldExpressionValidatorField", second = "requiredValidatorField", message = "requiredValidatorField and fieldExpressionValidatorField are not matching")
@ScriptAssert(lang = "javascript", script = "_this.dateValidatorField != null && _this.dateValidatorField.before(new java.util.Date())", message = "Date need to before now")
public class BeanValidationExampleAction extends ActionSupport {
    @NotNull
    private String requiredValidatorField = null;

    @NotBlank
    private String requiredStringValidatorField = null;

    @NotNull(message="your.text.key.here")
    @Min(1)
    @Max(10)
    private Integer integerValidatorField = null;

    @NotNull
    private Date dateValidatorField = null;

    @NotNull
    @Size(min = 4, max = 64)
    @Email

    private String emailValidatorField = null;

    @NotNull
    @Size(min = 4, max = 64)
    @URL
    private String urlValidatorField = null;

    @NotNull
    @Size(min = 2, max = 4)
    private String stringLengthValidatorField = null;

    @Pattern(regexp = "[^<>]+")
    private String regexValidatorField = null;

    private String fieldExpressionValidatorField = null;


 public Date getDateValidatorField() {

        return dateValidatorField;
    }
    public void setDateValidatorField(Date dateValidatorField) {
        this.dateValidatorField = dateValidatorField;
    }
    public String getEmailValidatorField() {
        return emailValidatorField;
    }
    public void setEmailValidatorField(String emailValidatorField) {
        this.emailValidatorField = emailValidatorField;
    }
    public Integer getIntegerValidatorField() {
        return integerValidatorField;
    }
    public void setIntegerValidatorField(Integer integerValidatorField) {
        this.integerValidatorField = integerValidatorField;
    }
    public String getRegexValidatorField() {
        return regexValidatorField;
    }
    public void setRegexValidatorField(String regexValidatorField) {
        this.regexValidatorField = regexValidatorField;
    }
    public String getRequiredStringValidatorField() {
        return requiredStringValidatorField;
    }
    public void setRequiredStringValidatorField(String requiredStringValidatorField) {
        this.requiredStringValidatorField = requiredStringValidatorField;
    }
    public String getRequiredValidatorField() {
        return requiredValidatorField;
    }
    public void setRequiredValidatorField(String requiredValidatorField) {
        this.requiredValidatorField = requiredValidatorField;
    }
    public String getStringLengthValidatorField() {
        return stringLengthValidatorField;
    }
    public void setStringLengthValidatorField(String stringLengthValidatorField) {
        this.stringLengthValidatorField = stringLengthValidatorField;
    }
    public String getFieldExpressionValidatorField() {
        return fieldExpressionValidatorField;
    }
    public void setFieldExpressionValidatorField(
            String fieldExpressionValidatorField) {
        this.fieldExpressionValidatorField = fieldExpressionValidatorField;
    }
    public String getUrlValidatorField() {
        return urlValidatorField;
    }
    public void setUrlValidatorField(String urlValidatorField) {
        this.urlValidatorField = urlValidatorField;
    }
}
```

