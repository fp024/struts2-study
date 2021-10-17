# Struts 2 스터디 parent  pom 모듈

> 참조: https://github.com/apache/struts-examples/blob/master/pom.xml

예제 진행 프로젝트가 늘어감에 따라 버전이나 플러그인 옵션등을 변경해야할 때, 모든 pom.xml의 내용을 고쳐줘야하는 문제가 있는데, 이부분을 parent pom 모듈에 정의하고 그것을 가져다 사용하도록 하자.

apache의 struts-exampe은 부모 pom 하위에 모듈 프로젝트 디렉토리를 두었는데, 나는 따로 parent pom 모듈을 로컬 레파지토리에 install 해서 사용하는 식으로 해보자.

### parent pom을 우선 로컬 레파지토리에 설치할 필요가 있다.

```
mvnw install
```



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
    </parent>

    <artifactId>form-validation-struts</artifactId>
    <version>1.0-SNAPSHOT</version>
    <packaging>war</packaging>

    <name>${project.artifactId} : Struts 2 Study Maven Webapp</name>
    <url>https://github.com/fp024/struts2-study/tree/master/form-validation</url>

</project>

```

이후 개별 스터디 프로젝트에서 빌드를 해보면 잘된다.

