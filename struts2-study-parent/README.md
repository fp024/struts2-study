# Struts 2 스터디 parent  pom 모듈

> 참조: https://github.com/apache/struts-examples/blob/master/pom.xml

예제 진행 프로젝트가 늘어감에 따라 버전이나 플러그인 옵션등을 변경해야할 때, 모든 pom.xml의 내용을 고쳐줘야하는 문제가 있는데, 이부분을 parent pom 모듈에 정의하고 그것을 가져다 사용하도록 하자.

apache의 struts-exampe은 부모 pom 하위에 모듈 프로젝트 디렉토리를 두었는데, 나는 따로 parent pom 모듈을 로컬 레파지토리에 install 해서 사용하는 식으로 하려했는데, parent 모듈의 버전업이나 동작변경 후, 하위모듈의 테스트를 한번에 하려면 문제가 있었다.

그래서 하위 모듈에서 상대적으로 부모를 참조하도록 `<relativePath>` 값을 넣어 부모 프로젝트를 참조하게 했다. 이렇게 하면 parent를 로컬 레파지토리에 설치할 필요도 없다.



#### 개별 스터디 프로젝트에서 parent 모듈과 중복되는 내용은 지우고 상위에 preant 노드만 추가해준다.

```xml
<?xml version="1.0" encoding="UTF-8"?>

<project xmlns="http://maven.apache.org/POM/4.0.0"
    xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
    xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 http://maven.apache.org/xsd/maven-4.0.0.xsd">
    <modelVersion>4.0.0</modelVersion>

    <!-- parent pom 지정 -->
    <parent> 
        <groupId>org.fp024.struts2.study</groupId>
        <artifactId>struts2-study-parent</artifactId>
        <version>1.0-SNAPSHOT</version>
        <relativePath>../../struts2-study-parent/pom.xml</relativePath>
    </parent>

    <artifactId>form-validation-struts</artifactId>
    <version>1.0-SNAPSHOT</version>
    <packaging>war</packaging>

    <name>${project.artifactId} : Struts 2 Study Maven Webapp</name>
    <url>https://github.com/fp024/struts2-study/tree/master/form-validation</url>

</project>

```



#### 개별 프로젝트로 경로를 옮겨서 실행하거나 `-pl` 옵션(`--projects`)으로 프로젝트 목록을 지정해서 parent 경로에서 실행할 수도 있다. 

  * 개별 하위 프로젝트에서 들어가서 프로젝트를 실행 (예: [Struts 2 웹 어플리케이션을 만드는 방법](../how-to-create-a-struts-2-web-application)의 예제실행)

    ```bash
    cd ..
    cd how-to-create-a-struts-2-web-application
    cd basic-struts
    mvnw clean jetty:run
    ```
    
  * struts2-study-parent 디렉토리에서 실행  (예: [메시지 리소스 파일](../message-resource-files)의 예제실행)

      * `-am`옵션(`--also-make`)은 해당 프로젝트를 빌드하는데 필요한 프로젝트까지 같이 빌드하는 옵션인데, 여기서는 struts2-study-parent를 같이 빌드하게 됨

    ```bash
    mvnw clean jetty:run -pl ../message-resource-files/message-resource-struts -am
    ```

  * 모든 하위 프로젝트 일괄 테스트

    ```bash
    mvnw clean test
    ```

 

