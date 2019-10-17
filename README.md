<img src="http://mybatis.github.io/images/mybatis-logo.png" align="right" />

# Mybatis 通用 mapper 插件 
[![mybatis-mapper](https://img.shields.io/badge/plugin-mybatis--mapper-green)](https://github.com/tangxbai/mybatis-mappe) ![size](https://img.shields.io/badge/size-327kB-green) ![version](https://img.shields.io/badge/release-1.1.0-blue) [![maven central](https://img.shields.io/badge/maven%20central-1.1.0-brightgreen)](https://maven-badges.herokuapp.com/maven-central/org.mybatis/mybatis) [![license](https://img.shields.io/badge/license-Apache%202-blue)](http://www.apache.org/licenses/LICENSE-2.0.html)



## 项目简介

Mybatis通用Mapper插件，用于解决大多数基本操作，简化sql语法并提高动态执行效率。

可能有的人会说现在市面上已经有各种通用mapper插件了，而且也能满足日常开发需求，你为什么还要重复再造一个轮子？

对于此，我想说，一个东西的出现，必定是为了解决另一个问题。而且我也不喜欢重复这个词语，什么叫重复？两个一模一样毫无差异的东西才叫重复，而有差异的两个东西出现时，我管它叫 **选择**。当你需要一个东西而它的选择又很少时，你几乎就没得选择了，只能用它，本项目的出现就是为了带给你们更多的选择，你可以因为种种原因不选择这个项目，但你要知道，你的选择从不局限于任何一个固定的片面。

不知道你们对于现有的通用mapper什么看法，是完美？是还行？还是不得不用？可能你也发现了其他的通用mapper有不便的地方，或者缺少自己想要的功能，但是没办法，选择少啊，而且它确实能在某种程度上减少我们的工作量，以达到通用的目的，但如果你们去阅读过这些项目源代码，那你肯定会发现其中的设计或者是功能不是那么的完善，那怎么办呢？要么漫长的等待作者更新，要么给作者提改进，或者有能力的另外开发一款更全面一点的插件，在我看来，前二者都太耗费时间和精力，而且很难让别人完全弄懂你的一些想法，涉及到更改核心程序代码的话，那就意味着让别人重构整个架构了，所以，这几乎是不可行的，于是乎在前人的这些思想上加入了一些自己的想法，至此诞生出了 `mybatis-mapper`。

*注意：此项目并不代表组织或个人，是一款完全开源的项目，您可以在任何适用的场景使用它，商用或者学习都可以，如果您在其中找不到您想选择它的理由也没关系，那咱们共同学习学习也是可以的，如果您有任何项目上的疑问，可以在issue上提任何问题，我会在第一时间回复您，如果您觉得它对您有些许帮助，请让身边的更多人知道它了解它，谢谢。*

此项目遵照 [Apache 2.0 License]( http://www.apache.org/licenses/LICENSE-2.0.txt ) 开源许可



## 核心功能

- [支持模板语法](#支持模板语法)
- [支持支持SQL注释](#支持支持SQL注释)
- [支持各种主键生成策略](#支持各种主键生成策略)
- [支持多主键场景](#支持多主键场景)
- [更方便快捷的条件查询](#更方便快捷的条件查询)
- [支持聚合函数统计](#支持聚合函数统计)
- [支持数据库乐观锁](#支持数据库乐观锁)
- [支持各种逻辑删除](#支持各种逻辑删除)
- [支持逻辑删除数据的恢复](#支持逻辑删除数据的恢复)
- [支持查询自定义返回Bean类型](#支持查询自定义返回Bean类型)
- [支持零MapperXML配置文件](#支持零MapperXML配置文件)
- [扩展插件Api](#扩展插件Api)
- [提供各种场景的日志打印](#提供各种场景的日志打印)
- 提供更丰富的API



## 快速开始

Maven方式（**推荐**）

```xml
<dependency>
	<groupId>com.viiyue.plugins</groupId>
	<artifactId>mybatis-mapper</artifactId>
	<version>${latest.version}</version>
</dependency>
```

Springboot的话请移步到：https://github.com/tangxbai/mybatis-mapper-spring-boot-starter

如果你没有使用Maven构建工具，那么可以通过以下途径下载相关jar包，并导入到你的编辑器。

[点击跳转下载页面](https://search.maven.org/search?q=g:com.viiyue.plugins%20AND%20a:mybatis-mapper&core=gav)

![如何下载](https://user-gold-cdn.xitu.io/2019/10/16/16dd24a506f37022?w=995&h=126&f=png&s=14645)

如何获取最新版本？[点击这里获取最新版本](https://search.maven.org/search?q=g:com.viiyue.plugins)



## 如何使用

> 在Java项目中的使用方式

```java
// 注意：使用 MyBatisMapperFactoryBuilder 创建 SqlSessionFactory
SqlSessionFactory factory = new MyBatisMapperFactoryBuilder().build( Resources.getResourceAsStream("your-mybatis.xml"));
SqlSession session = factory.openSession();
YourMapper mapper = session.getMapper(YourMapper.class);

// 调用插件提供的各种Api
mapper.xxx(...);
session.commit();
```

请注意：**MyBatisMapperFactoryBuilder** 是插件提供的一个 **SqlSessionFactory** 工厂构造器，除了需要替换mybatis原始的 **SqlSessionFactoryBuilder** 以外，其他没有任何使用上的区别。



> 在Spring项目中的使用方式

1、首先需要配置 `spring.xml`

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
     </bean>
     
     <!-- 扫描Mapper文件 -->
     <bean class="org.mybatis.spring.mapper.MapperScannerConfigurer">
         <property name="..." value="..." />
         <property name="sqlSessionFactoryBeanName" value="sqlSessionFactory"/>
     </bean>
 </beans>
```

2、然后使用Spring自动注入功能获得Mapper接口

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

*这里几乎没有破坏任何的原始使用方式，只是加入了一些Spring Bean的附加属性配置*

3、配置你的数据库模型Bean，它可以是这样：

```java
@Table( prefix = "t_" ) // 数据库表名配置
@NamingRule( NameStyle.UNDERLINE ) // 字段命名转换规则
@ValueRule( ValueStyle.SHORT ) // 值生成规则
@ExpressionRule( ExpressionStyle.SHORT ) // 表达式生成规则
@DefaultOrderBy( "#pk" ) // 默认排序主键，#pk占位符指向默认主键，也可以直接写字段名，不影响
public class User implements Serializable {
    
    // 指示这个字段是主键，如果没有配置 primar 属性，那么第一个就是默认主键
    @Id
    // 排序注解，用于更改字段出现在SQL中的顺序，默认按照字段位置排序
	@Index( 1 )
    // 主键生成策略，只能出现一次，且必须配合@Id注解一起使用，否则此注解将被自动忽略
	@GeneratedKey( useGeneratedKeys = true )
	private Long id;
    
    // 插件支持多主键，但是默认只会使用一个主键，你如果需要使用这个主键需要特殊指明
    // 比如：mapper.selectByPrimaryKeyIndex( 1, 100 );
    // 如果你需要让这个主键作为默认主键的话可以这样配置：@Id(primary = true)
    @Id
    private Long id2;
    
    @Index( 2 )
    // 字段详细配置
	@Column( jdcbType = Type.CHAR, typeHandler = BooleanTypeHandler.class )
    // 逻辑删除注解，只能配置一次
	@LogicallyDelete( selectValue = "Y", deletedValue = "N" )
	private Boolean display;

	@Index( 3 )
    @Column( updateable = false )
	private Date createTime;

	@Index( 4 )
    @Column( insertable = false )
	private Date modifyTime;
	
    // 乐观锁注解，只能配置一次
	@Version
	@Index( 5 )
	private Long version;
    
}
```



## 偏好配置

> 属性说明

<table>
    <colgroup>
    	<col width="20%"/>
        <col width="65%"/>
        <col width="15%"/>
    </colgroup>
    <thead>
    	<tr>
            <th>属性</th>
            <th>描述</th>
            <th>值</th>
        </tr>
    </thead>
    <tbody>
    	<tr>
            <td>enableLogger</td>
            <td>启用日志</td>
            <td>true/false</td>
        </tr>
        <tr>
            <td>enableRuntimeLog</td>
            <td>开启实时日志</td>
            <td>true/false</td>
        </tr>
        <tr>
            <td>enableCompilationLog</td>
            <td>开启编译日志</td>
            <td>true/false</td>
        </tr>
        <tr>
            <td>enableKeywordsToUppercase</td>
            <td>关键字转换为全大写</td>
            <td>true/false</td>
        </tr>
        <tr>
            <td>databaseColumnStyle</td>
            <td>数据库列样式</td>
            <td>#</td>
        </tr>
    </tbody>
</table>

*特别说明*：`#` 代表据数据库中的列，比如mysql中默认列使用 &#x60;column&#x60;，那么你可以这样配置 &#x60;#&#x60;，默认是没有任何样式修饰符的。

```sql
-- 默认无样式 - #
select id, name, age, weight from ... where ...
-- Mysql样式 - `#`
select `id`, `name`, `age`, `weight` from ... where ...
-- Oracle - [#]
select [id], [name], [age], [weight] from ... where ...
-- 自定义样式 - L-#-R
select L-id-R, L-name-R, L-age-R, L-weight-R from ... where ...
```



> 在 mybatis.xml 中的配置方式

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
    
    <!-- 无论使用哪种方式，都极力推荐配置数据库Bean的别名配置 -->
    <typeAliases>
        <package name="..."/>
    </typeAliases>
</configuration>
```



> 在 spring.xml 中的配置方式

```xml
<?xml version="1.0" encoding="UTF-8"?>
<beans>
     <bean id="sqlSessionFactory" class="org.mybatis.spring.SqlSessionFactoryBean">
          <!-- 省略其他配置 -->
          <property name="..." value="..."/>
         
          <!-- 插件偏好配置 -->
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



## 支持模板语法

以下是已经支持的模板语法，你可以在自定义 `DynamicProvider` 中无条件的使用它们，也可以开启XML模板语法支持并使用它。

- `@{expression}` - 静态模板，会在程序启动过程中被解析成对应的文本
- `%{expression}` - 动态模板，会在SQL执行过程被解析成对应的文本，类似mybatis判断条件
- `[kewords]` - 关键字模板，会根据配置自动转换成大写或小写关键字
- `{{value.expression}}` - 取值表达式，可以获取执行方法的传入参数或程序上下文数据
- `<error>message</error>` - 错误信息，用于隐藏错误信息，不影响程序启动，在执行过程中抛出
- 可能你会常在模板使用看到关于 `this` 的一个关键字，这个关键字默认指向当前Mapper所使用的数据库Bean对应的 **解析对象**，如果你想在模板语法中使用其他Bean解析对象的话，请使用Mybatis提供的 **类对象别名** 调用，类似于 @{this.table} 或者 @{user.table}/@{role.table}/@{xxx.table}

> 特别说明

首先，插件分两部分，**默认是不支持XML模板语法解析的**，所以，可以根据个人喜好，选择开启或者不使用它，插件的运行和XML没太大关系，基础功能原理是基于Mybatis提供的 `@SelectProvider` `@UpdateProvider` `@InsertProvider` `@DeleteProvider` 提供运行需要的 `SqlSource`，所以可以做到零XML文件即可使用插件的任意Api方法，因为都是对单表的扩展Api，所以凡是涉及到任何复杂SQL的，请添加XML文件并书写自己的逻辑SQL脚本，

> 开启XML模板语法

如果你需要在XML也使用这些模板语法，请配置解析XML的扩展 `LanguageDriver`，插件提供的扩展Driver是 `MyBatisMapperLanguageDriver`，Mybatis的LanguageDriver接口无任何限制，任何人都可以对其进行自定义更改，属于Mybatis的一种扩展机制，具体使用的可以在单个XML语句节点上配置 `lang` 属性，也可以覆盖Mybatis的默认 `XMLLanguageDriver` 解析驱动，让全局都使用这个 `LanguageDriver`。

> DynamicProvider无条件支持任何已有模板语法

```java
// 必须继承自DynamicProvider，否则插件无法识别，Mybatis直接执行的话会报错
public final class YourCustomProvider extends DynamicProvider {
    
    // 实现一
    // 方法名必须和你接口中定义的方法名一致
	public String selectAll( MappedStatement ms ) {
		return "[select] @{this.columns} [from] @{this.table}";
	}
    
    // 实现二
    public String selectAll( MappedStatement ms, Class<?> yourModelBeanType ) {
		return "[select] @{this.columns} [from] @{this.table}";
	}
}
```

> 单个XML使用模板语法

```xml
<select id="xxx" lang="com.viiyue.plugins.mybatis.MyBatisMapperLanguageDriver">
    [select] @{this.columns} 
    [from] @{this.table} 
    [where] @{this.column.yourFieldName} = #{yourFieldName}
</select>
```

> 全局使用模板语法

```xml
<?xml version="1.0" encoding="UTF-8" ?>
<configuration>
    <settings>
		<setting name="..." value="..."/>
		<setting name="defaultScriptingLanguage" value="com.viiyue.plugins.mybatis.MyBatisMapperLanguageDriver"/>
	</settings>
</configuration>
```

接下来对每一种模板语法作一些详细说明，方便同学们更好的理解并熟练的使用它们。现在假设当前Mapper是这样的，那么 `this` 关键字就会指向 **User** 的解析对象，从其中获取各种元数据信息。

```java
public interface UserMapper extends Mapper<User, UserDTO, Long> { 
}
```

> @{expression} - 静态模板

```sql
-- 关于表
-- @see com.viiyue.plugins.mybatis.template.builder.TableBuilder
@{this.table} → t_user
@{this.table.prefix("xxx")} → xxx.t_user
@{this.table.alias("t")} → t_user as 't'
@{this.table.prefix("xxx").alias("t")} → xxx.t_user as 't'

-- 关于单列
-- @see com.viiyue.plugins.mybatis.template.builder.ColumnBuilder
@{this.column.yourFieldName} → your_field_name
@{this.column.yourFieldName.prefix("a")} → a.your_field_name
@{this.column.yourFieldName.suffix("_suffix")} → your_field_name_suffix
@{this.column.yourFieldName.javaType} → Integer/Long/YourFieldType
@{this.column.yourFieldName.jdbcType} → INT/BIGINT/OTHERS
@{this.column.yourFieldName.property} → yourFieldName
@{this.column.yourFieldName.prefix("a").suffix("_suffix")} → a.your_field_name_suffix

-- 关于所有列
-- @see com.viiyue.plugins.mybatis.template.builder.ColumnsBuilder
@{this.columns} → id, name, age
@{this.columns.include("id,name")} → id, name
@{this.columns.exclude("id,age")} → name
@{this.columns.prefix("t")} → t.id, t.name, t.age
@{this.columns.prefix("t").include("id,name").exclude("id")} → t.name

-- 关于值
-- @see com.viiyue.plugins.mybatis.template.builder.ValuesBuilder
@{this.values} → #{id}, #{name}, #{age}
@{this.values.alias("user")} → #{user.id}, #{user.name}, #{user.age}
@{this.values.include("id,name")} → #{id}, #{name}
@{this.values.exclude("id,age")} → #{name}
@{this.values.alias("user").include("name,age")} → #{user.name}, #{user.age}

-- 关于默认排序（需要在类上配置@DefaultOrderBy）
-- 没有配置相关注解的话，表达式返回一段空白文本("")
-- @see com.viiyue.plugins.mybatis.template.builder.LogicallyDeleteBuilder
@{this.defaultOrderBy} → order by sorter desc
@{this.defaultOrderBy.desc} → order by sorter desc
@{this.defaultOrderBy.asc} → order by sorter asc
@{this.defaultOrderBy.prefix("t")} → order by t.sorter desc

-- 关于逻辑删除（需要在字段上配置@LogicallyDelete）
-- 没有配置相关注解的话，表达式返回一段空白文本("")
-- @see com.viiyue.plugins.mybatis.template.builder.LogicallyDeleteBuilder
@{this.tryLogicallyDelete} → deleted = 'N'
@{this.tryLogicallyDelete.prefix("t")} → t.deleted = 'N'
@{this.tryLogicallyDelete.useWhereQuery} → where deleted = 'N'
@{this.tryLogicallyDelete.useAndQuery} → and deleted = 'N'
@{this.tryLogicallyDelete.useOrQuery} → or deleted = 'N'
@{this.tryLogicallyDelete.useQueryValue} → or deleted = 'Y'
@{this.tryLogicallyDelete.useDeletedValue} → or deleted = 'N'

-- 关于乐观锁（需要在类上配置@Version）
-- 没有配置相关注解的话，表达式返回一段空白文本("")
-- @see com.viiyue.plugins.mybatis.template.builder.OptimisticLockBuilder
@{this.tryOptimisticLock} → version = #{version}
@{this.tryOptimisticLock.prefix("t")} → t.version = #{version}
@{this.tryOptimisticLock.useWhere} → where version = #{version}
@{this.tryOptimisticLock.useAnd} → and version = #{version}
@{this.tryOptimisticLock.useOr} → or version = #{version}
```

> %{expression} - 动态模板

```sql
-- 关于列
-- $ 是当前传入的参数，如果单个参数，可以直接写 $ 符号，多参数使用 $.param 的形式。
-- 会筛选出Bean中所有non-null的属性，值得注意的是 @Column(nullable=true/false)
-- @see com.viiyue.plugins.mybatis.template.builder.ColumnBuilder
%{this.columns.dynamic($)} → id, name
%{this.columns.dynamic($).include("id,name")} → id, name
%{this.columns.dynamic($).exclude("id,age")} → name
%{this.columns.dynamic($).prefix("t")} → t.id, t.name, t.age
%{this.columns.dynamic($).prefix("t").include("id,name").exclude("id")} → t.name

-- 关于修改
-- 注意：如果开启了乐观锁的话，版本字段的取值不再和字段名一样，类似：next(?)Value
-- @see com.viiyue.plugins.mybatis.template.builder.UpdateSetBuilder
%{this.set} → id = #{id}, name = #{name}, version = #{nextVersionValue}
%{this.set.prefix("t")} → t.id = #{id}, t.name = #{name}, t.age = #{age}
%{this.set.alias("user")} → id = #{user.id}, name = #{user.name}, age = #{user.age}
%{this.set.include("id,name")} → id = #{id}, name = #{name}
%{this.set.exclude("id,name")} → age = #{age}
%{this.set.dynamic($)} → name = #{name}
%{this.set.dynamic($.user).prefix("t").alias("user")} → t.name = #{user.name}

-- 关于动态值
-- 构造器会自动筛选出所有non-null的属性，这里只是一个示例效果。
-- @see com.viiyue.plugins.mybatis.template.builder.ValuesBuilder
%{this.values.dynamic($)} → #{id}, #{name}, #{age}
%{this.values.dynamic($).alias("user")} → #{user.id}, #{user.name}, #{user.age}
%{this.values.dynamic($).include("id,name")} → #{id}, #{name}
%{this.values.dynamic($).exclude("id,age")} → #{name}
%{this.values.dynamic($).alias("u").include("name,age")} → #{u.name}, #{u.age}

-- 关于Where条件
-- 构造器会自动筛选出所有non-null的属性，这里只是一个示例效果。
-- @see com.viiyue.plugins.mybatis.template.builder.WhereBuilder
%{this.where($)} → where name = #{name} and age = #{age}
%{this.where($).prefix("t")} → where t.name = #{name} and t.age = #{age}
%{this.where($).include("name")} → where name = #{name}
%{this.where($).exclude("name")} → where age = #{age}
%{this.where($).tryLogicallyDeleteQuery} → where deleted = 'Y' and name = #{name}
%{this.where($).tryLogicallyDeleteQuery.useQueryValue} → where deleted = 'Y' and name = #{name}
%{this.where($).tryLogicallyDeleteQuery.useDeletedValue} → where deleted = 'N' and name = #{name}
```

> [keywords] - 关键字转换

需要说明一下，如果不做偏好配置的话，插件默认全部转换为小写关键字，如果想使用大写关键字，请更改相关配置。

```sql
[select] * [from] t_user [where] name = #{name}
-- enableKeywordsToUppercase = true
SELECT * FROM t_user WHERE name = #{name}
-- enableKeywordsToUppercase = false / default
select * from t_user where name = #{name}
```

> {{expression}} - 取值表达式

这种表达式主要是 **Example** 查询的时候用的相对多一些，其他情况你要使用也是可以的。此表达式不输出任何修饰符，各位同学使用的话记得添加修饰符，比如：引号。

```sql
-- Example
select {{$.example.columns}} from @{this.table} {{$.example.where}}

-- 获取系统值，表达式具体的值在运行时生效
-- 目前{{system.xxx}}仅支持一下四个属性
update @{this.table} set modify_time = {{system.now}} where ... -- Date
update @{this.table} set millis = {{system.systime}} where ... -- CurrentTimeMillis
update @{this.table} set order_code = '{{system.uuid}}' where ... -- UUID
update @{this.table} set sort_value = {{system.rundom.nexInt(5)}} where ... -- Number

-- 获取环境变量值，表达式具体的值在运行时生效
-- 更多属性调用请参照以下常量类的ENV_PROP_NAMES属性值
-- @see com.viiyue.plugins.mybatis.Constants#ENV_PROP_NAMES
insert into @{this.table} (name, text) values ('{{env.osName}', '{{env.osVersion}}')
```

>  <error>message</error>  - 异常表达式

这种情况不要轻易写在SQL脚本中，如果你写在了自己的SQL脚本中，执行方法时会把标签内部的文本信息以 `RuntimeException` 的形式抛出来，目前也只是插件内部用来判断一些特殊情况时才会使用到。



## 支持支持SQL注释

众所周知，XML文件中的SQL脚本是不支持注释的，但是本插件可以帮你实现在XML添加脚本注释，让你不再忘记当初写下那段复杂的脚本痛苦经历，不要惊慌，多余的注释会在你程序启动过程中被移除掉，不会有任何效率上的影响，所以放心拥抱SQL注释吧。

```xml
<select id="xxx" resultType="xxx" resultMap="xxx">
    // 单行注释
    // 支持大小写关键字转换，凡是包裹在“[]”中的任何文本，都会被转换成大写或小写文本。
    [select]

    -- SQL注释
    -- 输出表的所有列
    @{this.columns}

    [from]

    /* 单行文本注释 */
    /* 输出表明 */
    @{this.table} /* [where] */

    /**
     * 多行文本注释多行文本注释
     * 多行文本注释多行文本注释
     * 多行文本注释多行文本注释
     * 多行文本注释多行文本注释
     */

    // 所有程序通用的单行注释也是支持的
    // 内容可以直接写表达式，也可以结合mybatis的逻辑标签，没有任何限制。
    // @{this.column.loginName} = #{loginName}

    // 可以结合各种条件标签使用模板语法
    // 但是，节点属性不支持模板语法，仅限文本节点。
    <trim prefix="[where]" prefixOverrides="and|AND">
        <if test="loginName != null">[and] @{this.column.xxx} = #{xxx}</if>
    </trim>

    <!-- 不影响原有XML注释，该怎么使用还是怎么使用 -->

    // 你也可以加入逻辑删除表达式
    @{this.tryLogicallyDelete.useAndQuery}
    
    // 或者乐观锁表达式
    @{this.tryOptimisticLock.useAnd}
</select>
```



## 支持各种主键生成策略

目前插件主要支持以下三种主键生成策略，使用 `@Id` 标识主键，然后使用 `@GeneratedKey` 对主键生成策略进行配置。

```java
@Table( prefix = "t_" )
@NamingRule( NameStyle.UNDERLINE )
@ValueRule( ValueStyle.SHORT )
@ExpressionRule( ExpressionStyle.SHORT )
@DefaultOrderBy( "#pk" )
public class YouBean implements Serializable {
    
    @Id
	@Index( Integer.MIN_VALUE )
    // 1、使用JDBC的自增主键获取方式
	@GeneratedKey( useGeneratedKeys = true )
    
    // 2、也可以自己生成主键值，插件提供两种默认主键值生成器（雪花ID主键/UUID主键）
    // 如果需要雪花Id的同学可以照下面这样配置
	//@GeneratedKey( valueProvider = SnowFlakeIdValueProvider.class )
    //@GeneratedKey( valueProvider = UUIDValuePrivoder.class )
    
    // 3、对于不支持JDBC获取自增主键值的数据库来说，可以像下面这样使用：
    // 可以参照com.viiyue.plugins.mybatis.enums.AutoIncrement里面的枚举值，
    // 里面预置了部分获取自增主键的SQL，可以直接写枚举名字占位，没有的话可以自己提供。
	//@GeneratedKey( statement = "MYSQL" ) // MYSQL是枚举名
	//@GeneratedKey( statement = "SELECT T_USER.YYYY()" )
    
    // 如果枚举里面没有你需要的，可以通过statementProvider来提供你自己的SQL主键查询
	//@GeneratedKey( statementProvider = OracleAutoIncrementStatementProvider.class )
	private Long id;
    
}

// 自定义自增主键SQL脚本
public class OracleAutoIncrementStatementProvider implements AutoIncrementStatementProvider {
     @Override
     public String getAutoIncrementSqlStatement( GeneratedKeyInfo info ) {
         return "SELECT " + info.getTableName() + ".NEXTVAL FROM DUAL";
     }
}

```



## 支持多主键场景

> Bean的定义：

```java
// 可以对多个主键字段进行@Id标注，没有特殊指明的话，默认会使用对象中第一个出现的主键
// 否则默认使用@Id(primary=true)的那一个主键字段
@Table( prefix = "t_" )
@NamingRule( NameStyle.UNDERLINE )
@ValueRule( ValueStyle.SHORT )
@ExpressionRule( ExpressionStyle.SHORT )
@DefaultOrderBy( "#pk" )
public class YouBean implements Serializable {
    @Id
	private Long id;
    
    @Id(primary = true)
    private Long id2;
}
```

> 使用上的区别

```java
// 使用方式
mapper.selectByPrimaryKey(PK); // 使用默认主键
mapper.selectByPrimaryKeyIndex(Index, PK); // 使用指定下标的主键，多主键顺序由实体Bean决定
mapper.selectByPrimaryKeyGroup(Pk...); // 使用默认主键
mapper.selectByPrimaryKeyGroup(Index, PK...); // 使用指定下标的主键，多主键顺序由实体Bean决定

// 其他支持多主键的Api方法使用方法类似
mapper.xxx();
```



## 更方便快捷的条件查询

```java
Example example = null;

// query
// 这个方法后面可以直接跟各种Where条件
example = Example.query(User.class).equal("id", 1L).lt("age", 60);

// select
// 该方法可以对列进行操作，条件筛选的话使用when()来桥接
example = Example.select(User.class).includes( "id", "loginName", "password" );
example.when().equal("id", 1L).lt("age", 60);

// update
// 执行字段进行修改
example = Example.update(User.class).set(xx, yy, zz).values(XX, YY, ZZ);
example.when().equal("id", 1L).lt("age", 60);

// update
// 修改的话还可以绑定某个实体对象来操作
User user = null;
Example.update( User.class ).bind(user).when().equal("id", 1L);

// 条件查询
mapper.selectByExample(example);
```





## 支持聚合函数统计

```java
Example example = null;

// 支持多字段统计
example = Example.count(User.class, "*"); // 统计行数
example = Example.summation(User.class, "price", "num", ...); // 求和
example = Example.maximum(User.class, "price", "num", ...); // 求最大值
example = Example.minimum(User.class, "price", "num", ...); // 求最小值
example = Example.average(User.class, "price", "num", ...); // 求平均值

// 条件筛选
example.when().equal("id", 1L).lt("age", 60).xxx();

// 统计单个值，为了兼容不同的数据类型，这里统一使用BigDecimal接口，各位可自行转换
BigDecimal result = mapper.selectStatisticByAggregateFunction(example);

// 统计多列，这时候返回的是对应的实体对象，统计的字段值会自动封装到对应的同名字段中
List<DTO> results = mapper.selectStatisticListByAggregateFunction(example);
```



## 支持数据库乐观锁



## 支持各种逻辑删除



## 支持罗删除数据的恢复



## 支持查询自定义返回Bean类型



## 支持零MapperXML配置文件




## 扩展插件Api



## 提供各种场景的日志打印



## 关于作者

