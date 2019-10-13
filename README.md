<img src="http://mybatis.github.io/images/mybatis-logo.png" align="right" />

# Mybatis 通用 mapper 插件 
[![mybatis-mapper](https://img.shields.io/badge/plugin-mybatis--mapper-green)](https://github.com/tangxbai/mybatis-mappe) ![size](https://img.shields.io/badge/size-327kB-green) ![version](https://img.shields.io/badge/release-1.1.0-blue) [![maven central](https://img.shields.io/badge/maven%20central-1.1.0-brightgreen)](https://maven-badges.herokuapp.com/maven-central/org.mybatis/mybatis) [![license](https://img.shields.io/badge/license-Apache%202-blue)](http://www.apache.org/licenses/LICENSE-2.0.html)



## 项目简介

Mybatis通用Mapper插件，用于解决大多数基本操作，简化sql语法并提高动态执行效率。

可能有的人会说现在市面上已经有各种通用mapper插件了，而且使用过程中也没有出现什么样的问题，你为什么还要再写一个？

对于此，我想说，一个东西的出现，必定是为了解决另一个问题。可能你们也发现了其他的通用mapper插件多多少少也有不便，或者缺少自己想要的功能，但是没办法啊，它确实能在某种程度上减少我们的工作量，以达到通用的目的，如果你们去阅读过这些框架源代码，那你肯定会发现其中的设计或者是功能不是那么的完善，那怎么办呢？要么漫长的等待作者更新，要么给作者提改进，或者另外开发一款更全面一点的插件，在我看来，前二者都太耗费时间和精力，而且你很难让别人完全弄懂你的一些想法，而且涉及到更改核心程序代码的话，那就意味着让别人重构整个架构了，所以，这几乎是不可能的，于是乎在前人的思想上加入了一些自己的想法，至此诞生出了`mybatis-mapper`。

*注意：此项目并不代表组织任何个人，是一款完全开源的项目，您可以用在任何适用的场景，如果您在其中找不到您想用的理由，那咱们共同学习学习也是好的，顺便提提issues，交流交流想法，我会在第一时间回复您的任何疑问，如果您觉得它对您有些许帮助，帮助宣传宣传让多一些人了解它*



## 核心功能

- [支持模板语法](#支持模板语法)
- [支持支持SQL注释](#支持支持SQL注释)
- [支持动态解析](#支持动态解析)
- [支持各种主键生成策略](#支持各种主键生成策略)
- [支持多主键场景](#支持多主键场景)
- [支持聚合函数统计](#支持聚合函数统计)
- [支持数据库乐观锁](#支持数据库乐观锁)
- [支持各种逻辑删除](#支持各种逻辑删除)
- [支持逻辑删除数据的恢复](#支持逻辑删除数据的恢复)
- [支持查询自定义返回Bean类型](#支持查询自定义返回Bean类型)
- [支持零Mapper XML配置文件](#支持零Mapper XML配置文件)
- [更方便快捷的条件查询](#更方便快捷的条件查询)
- [提供各种场景的日志打印](#提供各种场景的日志打印)
- 提供更丰富的API



## 快速开始

> Maven方式（推荐）

```xml
<!-- Java -->
<dependency>
	<groupId>com.viiyue.plugins</groupId>
	<artifactId>mybatis-mapper</artifactId>
	<version>${latest.version}</version>
</dependency>

<!-- Springboot -->
<dependency>
	<groupId>com.viiyue.plugins</groupId>
	<artifactId>mybatis-mapper-spring-boot-starter</artifactId>
	<version>${latest.version}</version>
</dependency>
```



> 如果你没有使用Maven构建工具，那么可以通过以下途径下载相关jar包，并导入到你的编辑器：

[点击跳转下载页面](https://mvnrepository.com/artifact/com.alibaba/druid-spring-boot-starter/1.1.20)

![下载操作](C:\Users\Ying\AppData\Roaming\Typora\typora-user-images\1570775582670.png)



> 最新版本版本是多少？

[点击这里获取最新版本](https://mvnrepository.com/artifact/com.alibaba/druid-spring-boot-starter)



## 如何使用

> 在普通Java项目中使用：

```java
// 注意：使用MyBatisMapperFactoryBuilder创建SqlSessionFactory
SqlSessionFactory factory = new MyBatisMapperFactoryBuilder().build( Resources.getResourceAsStream( "your-mybatis.xml" ) );

SqlSession session = factory.openSession();
YourMapper mapper = session.getMapper( YourMapper.class );
mapper.xxx(...);
session.commit();
```

*除了替换了mybatis原始的FactoryBuilder以外其他没有任何使用上的区别*



> 在Spring项目中使用：

1. 首先需要配置 `spring.xml`

```xml
<?xml version="1.0" encoding="UTF-8"?>
<beans>
     <!-- 定义SqlSessionFactory -->
     <bean id="sqlSessionFactory" class="org.mybatis.spring.SqlSessionFactoryBean">
          <!-- 其他配置，这里就不一一罗列出来了 -->
          <property name="..." value="..."/>
         
          <!-- 别名配置，推荐这样配置，这为模板语法解析提供了良好支持 -->
          <property name="typeAliasesPackage" value="model.bean.aliases.package"/>
  
          <!-- 默认不支持XML的模板语法，必须通过以下方式开启支持 -->
          <property name="configuration.defaultScriptingLanguage" value="com.viiyue.plugins.mybatis.MyBatisMapperLanguageDriver"/>
         
          <!-- 这个是必须要启用的配置，否则mybatis-mapper无法工作 -->
          <property name="sqlSessionFactoryBuilder" value="com.viiyue.plugins.mybatis.MyBatisMapperFactoryBuilder"/>
         
          <!-- Mybatis-mapper 配置参数 -->
          <property name="configurationProperties">
              <props>
                  <prop key="enableLogger">true</prop>
                  <prop key="enableRuntimeLog">true</prop>
                  <prop key="enableCompilationLog">true</prop>
                  <prop key="enableKeywordsToUppercase">true</prop>
                  <prop key="databaseColumnStyle">#</prop>
              </props>
         </property>
     </bean>
     
     <!-- 扫描Mapper文件 -->
     <bean class="org.mybatis.spring.mapper.MapperScannerConfigurer">
         <property name="..." value="..." />
         <property name="sqlSessionFactoryBeanName" value="sqlSessionFactory"/>
     </bean>
 </beans>
```

2. 使用Spring自动注入功能获得Mapper

```java
@Service
public YourServiceImpl extends YourService {
    
    @Autowired
    private YourMapper mapper;
    
    public Object getApiResult() {
        return mapper.xxx();
    }
     
 }
```

*这里几乎没有破坏任何原始使用方式，只是加入了一些Bean属性配置*



> 在SpringBoot中使用：

```xml
<dependency>
	<groupId>com.viiyue.plugins</groupId>
	<artifactId>mybatis-mapper-spring-boot-starter</artifactId>
	<version>${latest.version}</version>
</dependency>
```

*在Springboot中几乎不用配置任何东西就可以让插件工作起来，当然了，一些偏好设置需要在application.properties中进行配置*



## 偏好配置

> 属性说明

| 描述               | 属性                      | 值         |
| ------------------ | ------------------------- | ---------- |
| 启用日志           | enableLogger              | true/false |
| 开启实时日志       | enableRuntimeLog          | true/false |
| 开启编译日志       | enableCompilationLog      | true/false |
| 关键字转换为全大写 | enableKeywordsToUppercase | true/false |
| 数据库列样式       | databaseColumnStyle       | #          |

*特别说明*：`#` 代表据数据库中的列，比如mysql中默认列使用 &#x60;column&#x60;，那么你可以这样配置 &#x60;#&#x60;，默认是没有任何样式修饰符的。

```sql
-- 默认无样式 - #
select id, name, age, weight from ... where ...
-- 自定义样式 - `#`
select `id`, `name`, `age`, `weight` from ... where ...
-- 自定义样式 - [#]
select [id], [name], [age], [weight] from ... where ...
-- 自定义样式 - L-#-R
select L-id-R, L-name-R, L-age-R, L-weight-R from ... where ...
```



> 在mybatis.xml中

```xml
<?xml version="1.0" encoding="UTF-8" ?>
<configuration>
    <!-- JDBC和偏好配置 -->
    <properties resource="jdbc.properties">
    	<property name="enableLogger" value="true"/>
    	<property name="enableRuntimeLog" value="true"/>
    	<property name="enableCompilationLog" value="true"/>
    	<property name="enableKeywordsToUppercase" value="true"/>
    	<property name="databaseColumnStyle" value="`#`"/>
    </properties>
    <!-- 无论使用哪种方式，都极力推荐配置Bean类型别名配置 -->
    <typeAliases>
        <package name="..."/>
    </typeAliases>
</configuration>
```



> 在spring.xml中

```xml
<?xml version="1.0" encoding="UTF-8"?>
<beans>
     <bean id="sqlSessionFactory" class="org.mybatis.spring.SqlSessionFactoryBean">
          <!-- 其他配置，这里就不一一罗列出来了 -->
          <property name="..." value="..."/> 
          <!-- Mybatis-mapper 配置参数 -->
          <property name="configurationProperties">
              <props>
                  <prop key="enableLogger">true/false</prop>
                  <prop key="enableRuntimeLog">true/false</prop>
                  <prop key="enableCompilationLog">true/false</prop>
                  <prop key="enableKeywordsToUppercase">true/false</prop>
                  <prop key="databaseColumnStyle">#</prop>
              </props>
         </property>
     </bean>
 </beans>
```



> 在springboot中，properties/yaml类似

```properties
mybatis-mapper.setting.enableLogger = true/false
mybatis-mapper.setting.enableRuntimeLog = true/false
mybatis-mapper.setting.enableCompilationLog = true/false
mybatis-mapper.setting.enableKeywordsToUppercase = true/false
mybatis-mapper.setting.databaseColumnStyle = #
```




## 支持模板语法

- `@{expression}` - 静态模板，会在程序启动过程中被解析成对应的文本

- `%{expression}` - 动态模板，会在SQL执行过程被解析成对应的文本，类似mybatis判断条件

- `[kewords]` - 关键字模板，会根据配置自动转换成大写或小写关键字

- `{{value.expression}}` - 取值表达式，可以获取执行方法的传入参数或程序上下文数据

- `<error>exception message</error>` - 错误信息，用于隐藏错误信息，不影响程序启动，在执行过程中抛出



## 许可证

Mybatis-mapper 是根据 [Apache 2.0 license](https://github.com/alibaba/fastjson/blob/master/license.txt) 许可发布的

