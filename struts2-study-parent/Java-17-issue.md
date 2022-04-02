# Java 17 대응 이슈

Java 17로 런타임을 바꾸고 나서 몇몇 예제 프로젝트에서 다음과 같은 오류가 발생했다.

리소스 번들을 리로딩 할 수 없다고 하는데, 원인은 ...

AbstractLocalizedTextProvider 클래스의 private static final  java.util.concurrent.ConcurrentMap 타입의 java.util.ResourceBundle.cacheList 멤버 필드에 리플렉션 접근하려 했지만 Java 9부터 처음 생긴 강력한 캡슐화 제한 기능이 16버전에서는 기븐값으로  적용하는 바람에, 이것을 풀어주는 옵션을 JVM에 지정해야했던 것 같다.

* https://openjdk.java.net/jeps/396
* https://openjdk.java.net/jeps/403

```
2022-04-02 07:03:37,749 ERROR [qtp12006451-43] util.AbstractLocalizedTextProvider (AbstractLocalizedTextProvider.java:299) - Could not reload resource bundles
java.lang.reflect.InaccessibleObjectException: Unable to make field private static final java.util.concurrent.ConcurrentMap java.util.ResourceBundle.cacheList accessible: module java.base does not "opens java.util" to unnamed module @463045fb
        at java.lang.reflect.AccessibleObject.checkCanSetAccessible(AccessibleObject.java:354) ~[?:?]
        at java.lang.reflect.AccessibleObject.checkCanSetAccessible(AccessibleObject.java:297) ~[?:?]
        at java.lang.reflect.Field.checkCanSetAccessible(Field.java:178) ~[?:?]
        at java.lang.reflect.Field.setAccessible(Field.java:172) ~[?:?]
        at com.opensymphony.xwork2.util.AbstractLocalizedTextProvider.clearMap(AbstractLocalizedTextProvider.java:333) ~[struts2-core-2.5.29.jar:2.5.29]
        at com.opensymphony.xwork2.util.AbstractLocalizedTextProvider.reloadBundles(AbstractLocalizedTextProvider.java:283) ~[struts2-core-2.5.29.jar:2.5.29]
        at com.opensymphony.xwork2.util.AbstractLocalizedTextProvider.reloadBundles(AbstractLocalizedTextProvider.java:268) ~[struts2-core-2.5.29.jar:2.5.29]
        at com.opensymphony.xwork2.util.AbstractLocalizedTextProvider.findDefaultText(AbstractLocalizedTextProvider.java:130) ~[struts2-core-2.5.29.jar:2.5.29]
        at com.opensymphony.xwork2.util.StrutsLocalizedTextProvider.findDefaultText(StrutsLocalizedTextProvider.java:39) ~[struts2-core-2.5.29.jar:2.5.29]
        at com.opensymphony.xwork2.util.AbstractLocalizedTextProvider.getDefaultMessage(AbstractLocalizedTextProvider.java:475) ~[struts2-core-2.5.29.jar:2.5.29]
        at com.opensymphony.xwork2.util.StrutsLocalizedTextProvider.findText(StrutsLocalizedTextProvider.java:347) ~[struts2-core-2.5.29.jar:2.5.29]
        at com.opensymphony.xwork2.util.StrutsLocalizedTextProvider.findText(StrutsLocalizedTextProvider.java:166) ~[struts2-core-2.5.29.jar:2.5.29]
        at com.opensymphony.xwork2.TextProviderSupport.getText(TextProviderSupport.java:233) ~[struts2-core-2.5.29.jar:2.5.29]
        at com.opensymphony.xwork2.TextProviderSupport.getText(TextProviderSupport.java:193) ~[struts2-core-2.5.29.jar:2.5.29]
        at org.apache.struts2.interceptor.FileUploadInterceptor.getTextMessage(FileUploadInterceptor.java:450) ~[struts2-core-2.5.29.jar:2.5.29]
        at org.apache.struts2.interceptor.FileUploadInterceptor.getTextMessage(FileUploadInterceptor.java:443) ~[struts2-core-2.5.29.jar:2.5.29]
        at org.apache.struts2.interceptor.FileUploadInterceptor.intercept(FileUploadInterceptor.java:240) ~[struts2-core-2.5.29.jar:2.5.29]
        at com.opensymphony.xwork2.DefaultActionInvocation.invoke(DefaultActionInvocation.java:249) ~[struts2-core-2.5.29.jar:2.5.29]
        at com.opensymphony.xwork2.interceptor.ModelDrivenInterceptor.intercept(ModelDrivenInterceptor.java:101) ~[struts2-core-2.5.29.jar:2.5.29]
        at com.opensymphony.xwork2.DefaultActionInvocation.invoke(DefaultActionInvocation.java:249) ~[struts2-core-2.5.29.jar:2.5.29]
        at com.opensymphony.xwork2.interceptor.ScopedModelDrivenInterceptor.intercept(ScopedModelDrivenInterceptor.java:142) ~[struts2-core-2.5.29.jar:2.5.29]
        at com.opensymphony.xwork2.DefaultActionInvocation.invoke(DefaultActionInvocation.java:249) ~[struts2-core-2.5.29.jar:2.5.29]
        at com.opensymphony.xwork2.interceptor.ChainingInterceptor.intercept(ChainingInterceptor.java:160) ~[struts2-core-2.5.29.jar:2.5.29]
        at com.opensymphony.xwork2.DefaultActionInvocation.invoke(DefaultActionInvocation.java:249) ~[struts2-core-2.5.29.jar:2.5.29]
        at com.opensymphony.xwork2.interceptor.PrepareInterceptor.doIntercept(PrepareInterceptor.java:175) ~[struts2-core-2.5.29.jar:2.5.29]
        at com.opensymphony.xwork2.interceptor.MethodFilterInterceptor.intercept(MethodFilterInterceptor.java:99) ~[struts2-core-2.5.29.jar:2.5.29]
        at com.opensymphony.xwork2.DefaultActionInvocation.invoke(DefaultActionInvocation.java:249) ~[struts2-core-2.5.29.jar:2.5.29]
        at org.apache.struts2.interceptor.I18nInterceptor.intercept(I18nInterceptor.java:121) ~[struts2-core-2.5.29.jar:2.5.29]
        at com.opensymphony.xwork2.DefaultActionInvocation.invoke(DefaultActionInvocation.java:249) ~[struts2-core-2.5.29.jar:2.5.29]
        at org.apache.struts2.interceptor.ServletConfigInterceptor.intercept(ServletConfigInterceptor.java:167) ~[struts2-core-2.5.29.jar:2.5.29]
        at com.opensymphony.xwork2.DefaultActionInvocation.invoke(DefaultActionInvocation.java:249) ~[struts2-core-2.5.29.jar:2.5.29]
        at com.opensymphony.xwork2.interceptor.AliasInterceptor.intercept(AliasInterceptor.java:207) ~[struts2-core-2.5.29.jar:2.5.29]
        at com.opensymphony.xwork2.DefaultActionInvocation.invoke(DefaultActionInvocation.java:249) ~[struts2-core-2.5.29.jar:2.5.29]
        at com.opensymphony.xwork2.interceptor.ExceptionMappingInterceptor.intercept(ExceptionMappingInterceptor.java:196) ~[struts2-core-2.5.29.jar:2.5.29]
        at com.opensymphony.xwork2.DefaultActionInvocation.invoke(DefaultActionInvocation.java:249) ~[struts2-core-2.5.29.jar:2.5.29]
        at org.apache.struts2.factory.StrutsActionProxy.execute(StrutsActionProxy.java:48) ~[struts2-core-2.5.29.jar:2.5.29]
        at org.apache.struts2.dispatcher.Dispatcher.serviceAction(Dispatcher.java:574) ~[struts2-core-2.5.29.jar:2.5.29]
        at org.apache.struts2.dispatcher.ExecuteOperations.executeAction(ExecuteOperations.java:79) ~[struts2-core-2.5.29.jar:2.5.29]
        at org.apache.struts2.dispatcher.filter.StrutsPrepareAndExecuteFilter.doFilter(StrutsPrepareAndExecuteFilter.java:141) ~[struts2-core-2.5.29.jar:2.5.29]
        at org.eclipse.jetty.servlet.FilterHolder.doFilter(FilterHolder.java:210) ~[?:?]
        at org.eclipse.jetty.servlet.ServletHandler$Chain.doFilter(ServletHandler.java:1594) ~[?:?]
        at org.eclipse.jetty.websocket.servlet.WebSocketUpgradeFilter.doFilter(WebSocketUpgradeFilter.java:170) ~[websocket-servlet-10.0.8.jar:10.0.8]
        at org.eclipse.jetty.servlet.FilterHolder.doFilter(FilterHolder.java:202) ~[?:?]
        at org.eclipse.jetty.servlet.ServletHandler$Chain.doFilter(ServletHandler.java:1594) ~[?:?]
        at org.eclipse.jetty.servlet.ServletHandler.doHandle(ServletHandler.java:506) ~[?:?]
        at org.eclipse.jetty.server.handler.ScopedHandler.handle(ScopedHandler.java:131) ~[?:?]
        at org.eclipse.jetty.security.SecurityHandler.handle(SecurityHandler.java:578) ~[?:?]
        at org.eclipse.jetty.server.handler.HandlerWrapper.handle(HandlerWrapper.java:122) ~[?:?]
        at org.eclipse.jetty.server.handler.ScopedHandler.nextHandle(ScopedHandler.java:223) ~[?:?]
        at org.eclipse.jetty.server.session.SessionHandler.doHandle(SessionHandler.java:1571) ~[?:?]
        at org.eclipse.jetty.server.handler.ScopedHandler.nextHandle(ScopedHandler.java:221) ~[?:?]
        at org.eclipse.jetty.server.handler.ContextHandler.doHandle(ContextHandler.java:1378) ~[?:?]
        at org.eclipse.jetty.server.handler.ScopedHandler.nextScope(ScopedHandler.java:176) ~[?:?]
        at org.eclipse.jetty.servlet.ServletHandler.doScope(ServletHandler.java:463) ~[?:?]
        at org.eclipse.jetty.server.session.SessionHandler.doScope(SessionHandler.java:1544) ~[?:?]
        at org.eclipse.jetty.server.handler.ScopedHandler.nextScope(ScopedHandler.java:174) ~[?:?]
        at org.eclipse.jetty.server.handler.ContextHandler.doScope(ContextHandler.java:1300) ~[?:?]
        at org.eclipse.jetty.server.handler.ScopedHandler.handle(ScopedHandler.java:129) ~[?:?]
        at org.eclipse.jetty.server.handler.ContextHandlerCollection.handle(ContextHandlerCollection.java:149) ~[?:?]
        at org.eclipse.jetty.server.handler.HandlerList.handle(HandlerList.java:51) ~[?:?]
        at org.eclipse.jetty.server.handler.HandlerWrapper.handle(HandlerWrapper.java:122) ~[?:?]
        at org.eclipse.jetty.server.Server.handle(Server.java:562) ~[?:?]
        at org.eclipse.jetty.server.HttpChannel.lambda$handle$0(HttpChannel.java:505) ~[?:?]
        at org.eclipse.jetty.server.HttpChannel.dispatch(HttpChannel.java:762) ~[?:?]
        at org.eclipse.jetty.server.HttpChannel.handle(HttpChannel.java:497) ~[?:?]
        at org.eclipse.jetty.server.HttpConnection.onFillable(HttpConnection.java:282) ~[?:?]
        at org.eclipse.jetty.io.AbstractConnection$ReadCallback.succeeded(AbstractConnection.java:319) ~[?:?]
        at org.eclipse.jetty.io.FillInterest.fillable(FillInterest.java:100) ~[?:?]
        at org.eclipse.jetty.io.SelectableChannelEndPoint$1.run(SelectableChannelEndPoint.java:53) ~[?:?]
        at org.eclipse.jetty.util.thread.strategy.AdaptiveExecutionStrategy.runTask(AdaptiveExecutionStrategy.java:412) ~[?:?]
        at org.eclipse.jetty.util.thread.strategy.AdaptiveExecutionStrategy.consumeTask(AdaptiveExecutionStrategy.java:381) ~[?:?]
        at org.eclipse.jetty.util.thread.strategy.AdaptiveExecutionStrategy.tryProduce(AdaptiveExecutionStrategy.java:268) ~[?:?]
        at org.eclipse.jetty.util.thread.strategy.AdaptiveExecutionStrategy.produce(AdaptiveExecutionStrategy.java:190) ~[?:?]
        at org.eclipse.jetty.util.thread.QueuedThreadPool.runJob(QueuedThreadPool.java:894) ~[?:?]
        at org.eclipse.jetty.util.thread.QueuedThreadPool$Runner.run(QueuedThreadPool.java:1038) ~[?:?]
        at java.lang.Thread.run(Thread.java:833) ~[?:?]
```





### AbstractLocalizedTextProvider 의 clearMap() 333번째 줄

```java
 private void clearMap(Class cl, Object obj, String name)
            throws NoSuchFieldException, IllegalAccessException, NoSuchMethodException, InvocationTargetException {

        Field field = cl.getDeclaredField(name);
        field.setAccessible(true);   // <---- 필드를 접근가능하게 하려했을 때.. 예외발생

        Object cache = field.get(obj);

        synchronized (cache) {
            Class ccl = cache.getClass();
            Method clearMethod = ccl.getMethod("clear");
            clearMethod.invoke(cache);
        }
    }
```

### AbstractLocalizedTextProvider 의 reloadBundles() 283번째 줄

```java
    protected void reloadBundles(Map<String, Object> context) {
        if (reloadBundles) {
            try {
                Boolean reloaded;
                if (context != null) {
                    reloaded = (Boolean) ObjectUtils.defaultIfNull(context.get(RELOADED), Boolean.FALSE);
                } else {
                    reloaded = Boolean.FALSE;
                }
                if (!reloaded) {
                    bundlesMap.clear();
                    try {
                        clearMap(ResourceBundle.class, null, "cacheList"); // <--- 코드 진행 방향
                    } catch (NoSuchFieldException e) {
                        // happens in IBM JVM, that has a different ResourceBundle impl
                        // it has a 'cache' member
                        clearMap(ResourceBundle.class, null, "cache");
                    }

                    // now, for the true and utter hack, if we're running in tomcat, clear
                    // it's class loader resource cache as well.
                    clearTomcatCache();
                    if (context != null) {
                        context.put(RELOADED, true);
                    }
                    LOG.debug("Resource bundles reloaded");
                }
            } catch (Exception e) {
                LOG.error("Could not reload resource bundles", e);
            }
        }
    }
```



### 🎇 리플렉션 접근하려던 ResourceBundle의 cacheList 필드

```java
    /**
     * The cache is a map from cache keys (with bundle base name, locale, and
     * class loader) to either a resource bundle or NONEXISTENT_BUNDLE wrapped by a
     * BundleReference.
     *
     * The cache is a ConcurrentMap, allowing the cache to be searched
     * concurrently by multiple threads.  This will also allow the cache keys
     * to be reclaimed along with the ClassLoaders they reference.
     *
     * This variable would be better named "cache", but we keep the old
     * name for compatibility with some workarounds for bug 4212439.
     */
    private static final ConcurrentMap<CacheKey, BundleReference> cacheList
        = new ConcurrentHashMap<>(INITIAL_CACHE_SIZE);
```





## 해결

실행시점에 JVM옵션을 넣어주는데, JUnit 을 실행할 때와 Jetty를 실행할 때 각각 전달이 잘되어야했다.

### 테스트 환경 설정

maven-surefire-plugin 의 설정으로 JVM 옵션이 전달되게 설정

```xml
      <!-- 
        java.lang.reflect.InaccessibleObjectException: 
        Unable to make field private static final java.util.concurrent.ConcurrentMap 
        java.util.ResourceBundle.cacheList accessible: 
        module java.base does not "opens java.util" to unnamed module 관련 처리
      -->      
      <plugin>
        <groupId>org.apache.maven.plugins</groupId>
        <artifactId>maven-surefire-plugin</artifactId>        
        <configuration>
          <argLine>
            --add-opens=java.base/java.util=ALL-UNNAMED
          </argLine>
        </configuration>        
      </plugin>
```



### Jettty 실행시점 설정

`MAVEN_OPTS` 환경변수를 통해 설정할 수도 있지만, Maven 3.3.1부터 프로젝트 루트의 .mvn 디렉토리에 jvm.config 파일을 만들어주면 쉽게 JVM옵션을 전달할 수 있었다.

* `${프로젝트_루트_디렉토리}/.mvn/jvm.config`

  ```
  --add-opens=java.base/java.util=ALL-UNNAMED
  ```

  * 참고 
    * https://maven.apache.org/docs/3.3.1/release-notes.html#jvm-and-command-line-options

Jetty 플러그인 자체에도 `<jvmArgs>`란 옵션으로 전달하는 부분이 있긴한데... Embedded모드에서는 Forked 모드로 해야하는 것 같은데 나는 Embedded 모드로 쓰고 있다.

* https://www.eclipse.org/jetty/documentation/jetty-10/programming-guide/index.html#pg-forked



## 의견

적용하고나서 `mvnw clean test > test.result.txt` 으로 전체 프로젝트 결과를 확인해보았는데, 관련 오류는 나타나지 않았다. 다른 프로젝트에서도 오류로그를 자세히 확인해봐야겠다. 😂😂😂

