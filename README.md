

![MybatisMapper](mybatis-mapper-logo.png)

[![mybatis-mapper](https://img.shields.io/badge/plugin-mybatis--mapper-green?style=flat-square)](https://github.com/tangxbai/mybatis-mappe) [![maven central](https://img.shields.io/badge/maven%20central-v1.3.0-brightgreen?style=flat-square)](https://maven-badges.herokuapp.com/maven-central/org.mybatis/mybatis) ![size](https://img.shields.io/badge/size-196kB-green?style=flat-square) [![license](https://img.shields.io/badge/license-Apache%202-blue?style=flat-square)](http://www.apache.org/licenses/LICENSE-2.0.html)



## 项目简介

Mybatis通用Mapper插件，用于解决大多数基础CRUD，简化sql语法并提高动态执行效率，拥有更丰富的Api。用最少的配置，提供一个健全的使用体系。

*注意：此项目是一款完全开源的项目，您可以在任何适用的场景使用它，商用或者学习都可以，如果您有任何项目上的疑问，可以在issue上提出您问题，我会在第一时间回复您，如果您觉得它对您有些许帮助，希望能留下一个您的星星（★），谢谢。*

------

此项目遵照 [Apache 2.0 License]( http://www.apache.org/licenses/LICENSE-2.0.txt ) 开源许可 

技术讨论QQ群：947460272



## 核心亮点

- **无侵入**：100%兼容mybatis，不与mybatis冲突，只添加功能，对现有程序无任何影响；
- **配置少**：所有配置均在原始mybatis的基础上读取，不增加额外配置消耗；
- **无捆绑关系**：与XML文件独立，有无独立XML均可；
- **效率高**：由Xml+OGNL模式转向Java+ExpressionEngine的模式，省去了XML的解析和OGNL解析的时间消耗；
- **灵活的规则制定**：灵活多元化的（表/列）规则制定，完美融合各种（名字/类型等）不统一的场景；
- **SQL模板语法**：模板语法简单易掌握，一次开发，终生受用，避免因数据库变化而大量更改SQL语句的场景。表达式会在程序完全启动之前编译成完整的SQL，仅留下需要动态解析的表达式，对效率的影响微乎甚微；
- **CRUD增强**：除了提供基础的CRUD外还提供一系列高级辅助方法（Example/ForUpdate/聚合函数/逻辑删除/回收站/乐观锁等）；
- **便捷的条件查询**：更简单便捷的Example条件查询，提供符合SQL语义化的链式条件函数调用；
- **支持多主键**：支持多主键场景，CRUD中可选择特定主键进行操作；
- **丰富的主键生成策略**：支持JDBC自增主键、自定义主键SQL查询、雪花ID、UUID、自定义主键生成策略等；
- **支持常量值**：灵活化的常量值生成策略，可针对不同场景做不同操作，均支持雪花ID/UUID等内置生成器；
- **可排序字段**：可对SQL字段排列顺序进行干扰，简直是强迫症重度患者的福音啊；
- **粘连性小**：可以独立使用，纯Java环境/spring/springboot都提供了单独的组件。
- **可扩展的开发模式**：继承Marker/Mapper/BaseMapper即可继续增强基础CRUD。



## 关联文档

关于整合spring，请移步到：https://github.com/tangxbai/mybatis-mapper-spring

关于整合springboot，请移步到：https://github.com/tangxbai/mybatis-mapper-spring-boot



## 项目演示

- java + mybatis-mapper - [点击获取](https://github.com/tangxbai/mybatis-mapper-demo)
- spring + mybatis-mapper - [点击获取](https://github.com/tangxbai/mybatis-mapper-spring-demo)
- springboot + mybatis-mapper - [点击获取](https://github.com/tangxbai/mybatis-mapper-spring-boot/tree/master/mybatis-mapper-spring-boot-samples)



## 功能列表

- [支持模板语法](#支持模板语法)
- [支持SQL注释](#支持SQL注释)
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
	<version>[VERSION]</version>
</dependency>
```

如果你没有使用Maven构建工具，那么可以通过以下途径下载相关jar包，并导入到你的编辑器。

[点击跳转下载页面](https://search.maven.org/search?q=g:com.viiyue.plugins%20AND%20a:mybatis-mapper&core=gav)

![如何下载](https://user-gold-cdn.xitu.io/2019/10/16/16dd24a506f37022?w=995&h=126&f=png&s=14645)

如何获取最新版本？[点击这里获取最新版本](https://search.maven.org/search?q=g:com.viiyue.plugins%20AND%20a:mybatis-mapper&core=gav)



## 如何使用

1、配置 mybatis.xml

这个文件具体如何配置不作过多说明，你可以拉取相关demo查看详细配置，在配置上也没有什么区别，需要注意的是 **typeAliases**（实体别名配置）一定要配置，不然插件可能无法正常工作。

2、配置数据库实体Bean

```java
@Table( prefix = "t_" ) // 表名生成规则，可以配置更多详细说明
@NamingRule( NameStyle.UNDERLINE ) // 字段和数据库列之间的转换规则
@ValueRule( ValueStyle.SHORT ) // 值的生成规则，类似于：#{id}
@ExpressionRule( ExpressionStyle.SHORT ) // 表达式生成规则，类似于: id = #{id}
@DefaultOrderBy( "#pk" ) // #默认排序字段，"pk"为主键占位符，指向当前生效的主键字段，也可以直接写"id"
public class YourModelBean {

    @Id // 主键可以配置多个，但是只会有一个生效，Api方法中如果想要使用其他主键请指明所在下标位置
    @Index( Integer.MIN_VALUE )
    @GeneratedKey( useGeneratedKeys = true ) // JDBC支持的自增主键获取方式
    // @GeneratedKey( valueProvider = SnowFlakeIdValueProvider.class ) // 雪花Id，插件提供的两种主键生成策略之一
    // @GeneratedKey( statement = "MYSQL" ) // 枚举引用
    // @GeneratedKey( statement = "SELECT LAST_INSERT_ID()" ) // 自增主键SQL查询语句
    // @GeneratedKey( statementProvider = YourCustomStatementProvider.class ) // 通过Provider提供SQL语句
    private Long id;

    @Index( Integer.MAX_VALUE - 4 )
    @Column( jdcbType = Type.CHAR ) // 对字段进行详细描述
    @LogicallyDelete( selectValue = "Y", deletedValue = "N" ) // 开启逻辑删除支持，只能配置一次
    private Boolean display;

    @Index( Integer.MAX_VALUE - 3 )
    private Date createTime;

    @Index( Integer.MAX_VALUE - 2 )
    private Date modifyTime;

    @Version // 开启乐观锁支持，只能配置一次
    @Index( Integer.MAX_VALUE - 1 )
    @Column( insertable = false )
    private Long version;
    
    // setter/getter...
    
    // ----------------------------------------------------------------
    // @Index主要对字段出现顺序进行干扰，对字段进行干扰以后，输出的顺序大概是这样：
    // => id, ..., display, create_time, modify_time, version
    // 如果您未使用@Index注解，那么字段的原始顺序是这样的：
    // => id, display, create_time, modify_time, version, ...
    // 默认输出会将父类的字段排在最前面
    // ----------------------------------------------------------------  

}
```

3、Mapper接口需要继承 `BaseMapper` 或者 `Mapper`

```java
// 继承Mapper
public interface YourMapper extends Mapper<YourModelBean, YourModelBeanDTO, Long> {
}
// 或者继承BaseMapper
public interface YourMapper extends BaseMapper<YourModelBean, YourModelBeanDTO, Long> {
}
```

4、使用方式

```java
SqlSessionFactory factory = new MyBatisMapperFactoryBuilder().build( Resources.getResourceAsStream("your-mybatis.xml"));
SqlSession session = factory.openSession();
YourMapper mapper = session.getMapper(YourMapper.class);
mapper.xxx(...);
session.commit();
```

请注意：**MyBatisMapperFactoryBuilder** 是插件提供的一个 **SqlSessionFactory** 工厂构造器，这里我们需要用插件提供的MyBatisMapperFactoryBuilder替换mybatis原始的 **SqlSessionFactoryBuilder** 以启用插件相关Api功能。



## 配置注解

<table>
    <thead>
    	<tr>
        	<th align="left">类型</th>
            <th align="left">注解</th>
            <th align="left">描述</th>
        </tr>
    </thead>
    <tbody>
    	<tr>
        	<td rowspan="4">类注解</td>
            <td>@Table</td>
            <td>配置表名生成规则</td>
        </tr>
        <tr>
        	<td>@ResultMap</td>
            <td>自定义ResultMap，默认使用<code>BaseResultMap</code></td>
        </tr>
        <tr>
        	<td>@Excludes</td>
            <td>排除不需要的字段属性，一般用于子类排除父类某字段的场景</td>
        </tr>
        <tr>
        	<td>@DefaultOrderBy</td>
            <td>默认排序字段，<code>#pk</code>内置占位符，隐式地指向当前生效的主键</td>
        </tr>
         <tr>
        	<td rowspan="3">规则注解</td>
            <td>@NamingRule</td>
            <td>配置字段名和数据库列的转换规则</td>
        </tr>
        <tr>
        	<td>@ValueRule</td>
            <td>配置值的生成规则，类似：<code>#{id, ...}</code></td>
        </tr>
        <tr>
        	<td>@ExpressionRule</td>
            <td>配置表达式的生成规则，类似：<code>id = #{id, ...}</code></td>
        </tr>
        <tr>
        	<td colspan="3">以上两种类型的注解都只能配置在类上，主要用于描述数据库实体Bean的一些基础信息，通常建议配置在父类上，进而避免大量重复代码的产生。</td>
        <tr>
        	<td rowspan="8">成员注解</td>
        	<td>@Id</td>
            <td>主键标识，默认使用第一个标注字段，否则使用<code>primary</code>为true的字段作为主键</td>
        </tr>
        <tr>
        	<td>@Index</td>
            <td>干扰字段的排列顺序，默认按照Bean定义的顺序从父类到子类排列</td>
        </tr>
        <tr>
        	<td>@Column</td>
            <td>显式地配置字段和数据库列的规则说明</td>
        </tr>
        <tr>
        	<td>@GeneratedKey</td>
            <td>主键生成策略，必须和<code>@Id</code>组合使用，否则无效，<b>且只能出现一次</b></td>
        </tr>
        <tr>
        	<td>@GeneratedValue</td>
            <td>生成常量值，插件提供 SnowFlakeId/UUID，可自行拓展</td>
        </tr>
        <tr>
        	<td>@Conditional</td>
            <td>条件表达式，默认使用 <code>=</code> 桥接前后条件，可更改条件规则</td>
        </tr>
        <tr>
        	<td>@LogicallyDelete</td>
            <td>启用逻辑删除，<b>只能出现一次</b></td>
        </tr>
        <tr>
        	<td>@Version</td>
            <td>启用乐观锁，<b>只能出现一次</b></td>
        </tr>
        <tr>
        	<td colspan="3">成员注解主要是对实体字段的一些描述</td>
        </tr>
        </tr>
        <tr>
        	<td rowspan="2">标识注解</td>
        	<td>@EnableResultMap</td>
            <td>标注在Mapper方法上，是否使用ResultMap结果映射</td>
        </tr>
        <tr>
        	<td>@Reference</td>
            <td>标注在Mapper方法上，指向@XXXProvider(type)的其他非同名方法</td>
        </tr>
        <tr>
        	<td colspan="3">标识注解主要用于扩展插件Api的时候用的场景多一些，你也可以在自己的Mapper上使用默认的ResultMap，申明一下即可。</td>
        </tr>
    </tbody>
</table>



## 偏好配置

> 属性说明

<table>
    <thead>
    	<tr>
            <th width="20%" align="left">属性</th>
            <th width="50%" align="left">描述</th>
            <th width="15%" align="left">类型</th>
            <th width="15%" align="left">默认</th>
        </tr>
    </thead>
    <tbody>
    	<tr>
            <td>enableLogger</td>
            <td>是否启用日志</td>
            <td>Boolean</td>
            <td>true</td>
        </tr>
        <tr>
            <td>enableMapperScanLog</td>
            <td>是否开启Mapper扫描日志</td>
            <td>Boolean</td>
            <td>true</td>
        </tr>
        <tr>
            <td>enableRuntimeLog</td>
            <td>是否开启实时日志</td>
            <td>Boolean</td>
            <td>true</td>
        </tr>
        <tr>
            <td>enableCompilationLog</td>
            <td>是否开启编译日志</td>
            <td>Boolean</td>
            <td>true</td>
        </tr>
        <tr>
            <td>enableKeywordsToUppercase</td>
            <td>关键字大小写转换</td>
            <td>Boolean</td>
            <td>false</td>
        </tr>
        <tr>
            <td>databaseColumnStyle</td>
            <td>数据库列样式</td>
            <td>String</td>
            <td>#</td>
        </tr>
    </tbody>
</table>

*特别说明*：`#` 是一个占位符，代表据数据库中的列名，比如mysql中默认列使用 &#x60;column&#x60;，那么你可以这样配置 &#x60;#&#x60;，默认是没有任何样式修饰符的。

```sql
-- 默认样式 - #
select id, name, age, weight from ... where ...
-- Mysql样式 - `#`
select `id`, `name`, `age`, `weight` from ... where ...
-- Oracle - [#]
select [id], [name], [age], [weight] from ... where ...
-- 自定义样式 - L-#-R
select L-id-R, L-name-R, L-age-R, L-weight-R from ... where ...
```

> 配置方式

```xml
<?xml version="1.0" encoding="UTF-8" ?>
<configuration>
    <properties resource="jdbc.properties">
        <property name="enableLogger" value="true"/>
        <property name="enableRuntimeLog" value="true"/>
        <property name="enableCompilationLog" value="true"/>
        <property name="enableMapperScanLog" value="true"/>
        <property name="enableKeywordsToUppercase" value="true"/>
        <property name="databaseColumnStyle" value="`#`"/>
    </properties>
</configuration>
```



## 支持模板语法

以下是目前已经支持的模板语法，你可以在自定义 `DynamicProvider` 中无条件的使用它们，也可以开启XML模板语法支持并使用它。

- `@{expression}` - 静态模板，会在程序启动过程中被解析成完整的文本。
- `%{expression}` - 动态模板，会在SQL执行过程被解析成完整的文本，类似mybatis判断条件。
- `[kewords]` - 关键字模板，会根据配置自动转换成大写或小写关键字。
- `{{value.expression}}` - 取值表达式，可以获取执行方法的传入参数或程序上下文数据。
- `<error>message</error>` - 错误信息，用于隐藏错误信息，不影响程序启动，但会在执行过程中抛出。

> 关于this关键字

可能你会在模板看到 `this` 字样的关键字，这个关键字默认指向当前Mapper对应的数据库Bean的 **解析对象**，如果你想在模板语法中使用其他Bean解析对象的话，请使用Mybatis提供的 **类对象别名** 进行调用，这将会类似于 @{this.table} 或者 @{user.table}/@{role.table}/@{xxx.table} 等。

```java
this = EntityParser.getEntity(Bean.class);
```

> 特别说明

首先，插件分两部分，**默认是不支持XML模板语法解析的**，所以可以根据个人喜好，选择开启或者不使用它，插件的运行和XML没太大关系，基础功能是基于Mybatis内部的 `@SelectProvider`、`@UpdateProvider`、`@InsertProvider`、`@DeleteProvider` 注解提供运行所需的 `SqlSource`，所以即使无任何XML文件也可正常工作，因为都是对单表的扩展Api，所以如果涉及一些任何复杂SQL，请添加XML文件并书写自己的SQL逻辑脚本。

> 开启XML模板语法

如果你需要在XML也使用这些模板语法，请配置解析XML的扩展 `LanguageDriver`，插件提供的扩展Driver是 `MyBatisMapperLanguageDriver`，Mybatis的LanguageDriver接口无任何限制，任何人都可以对其进行自定义更改，属于Mybatis的一种扩展机制，具体使用的话，可以在单个XML语句节点上配置 `lang` 属性来开启单个Statement支持，也可以覆盖Mybatis的默认 `XMLLanguageDriver` 解析驱动。

1、单个语句块，使用lang属性即可

```xml
<select id="xxx" lang="com.viiyue.plugins.mybatis.MyBatisMapperLanguageDriver">
    [select] @{this.columns} 
    [from] @{this.table} 
    [where] @{this.column.yourFieldName} = #{yourFieldName}
</select>
```

2、全局范围，需要覆盖mybatis默认语言驱动

```xml
<?xml version="1.0" encoding="UTF-8" ?>
<configuration>
    <settings>
        <setting name="defaultScriptingLanguage" value="com.viiyue.plugins.mybatis.MyBatisMapperLanguageDriver"/>
    </settings>
</configuration>
```

3、DynamicProvider无条件支持任何模板语法

```java
public final class YourCustomProvider extends DynamicProvider {
    // 实现一
    // 方法名必须和你接口中定义的方法名一致
    public String selectAll( MappedStatement ms ) {
        return "[select] @{this.columns} [from] @{this.table}";
    }
    
    // 实现二
    // 两个参数，第二个为当前对应的Mapper数据库实体Class类型
    public String selectAll( MappedStatement ms, Class<?> yourModelBeanType ) {
        System.out.println(yourModelBeanType);
        return "[select] @{this.columns} [from] @{this.table}";
    }
}
```

接下来对每一种模板语法作一些详细说明，方便大家能更好的理解并熟练的使用它们。现在假设当前Mapper是下面这样的，那么 `this` 关键字会指向 **User的解析对象**，将从其中获取各种元数据信息。

```java
// DO
// 省略各种注解配置
public class User {
    private Long id;
    private String name;
    private Integer age;
}

// Mapper
public interface UserMapper extends Mapper<User, UserDTO, Long> { 
}

// DO → User
// DTO → UserDTO
// PK → Long
// this → EntityParser.getEntity(User.class);
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

这种表达式主要是 **Example** 查询的时候用的相对多一些，其他情况你要使用也是可以的。此表达式不输出任何修饰符，大家使用的话记得添加修饰符，比如：引号。

```sql
-- Example
select {{$.example.columns}} from @{this.table} {{$.example.where}}

-- 获取系统值，表达式具体的值在运行时生效
-- 目前{{system.xxx}}仅支持下面四个属性
update @{this.table} set modify_time = {{system.now}} where ... -- Date
update @{this.table} set millis = {{system.systime}} where ... -- CurrentTimeMillis
update @{this.table} set order_code = '{{system.uuid}}' where ... -- UUID
update @{this.table} set sort_value = {{system.rundom.nexInt(5)}} where ... -- Number

-- 获取环境变量值，表达式具体的值在运行时生效
-- 更多属性调用请参照以下常量类的ENV_PROP_NAMES属性值
-- @see com.viiyue.plugins.mybatis.Constants#ENV_PROP_NAMES
insert into @{this.table} (name, text) values ('{{env.osName}', '{{env.osVersion}}')
```

>  &lt;error&gt;message&lt;/error&gt;  - 异常表达式

这种情况不要轻易写在SQL脚本中，如果你写在了自己的SQL脚本中，执行方法时会把标签内部的文本信息以 `RuntimeException` 的形式抛出来，目前也只是插件内部用来判断一些特殊情况时才会使用到。

[回到顶部](#功能列表)



## 支持SQL注释

众所周知，XML文件中的SQL脚本是不支持注释的，但是我们可以帮你实现在XML添加脚本注释，你可以直接从DB软件中整个复制过来，而不需要单独剔除你添加的注释，不要惊慌，多余的注释会在你程序启动过程中被移除掉，不会有任何效率上的影响，所以放心拥抱SQL注释吧。

```xml
<select id="xxx" resultMap="xxx">
    // 单行注释
    // 支持大小写关键字转换，凡是包裹在“[]”中的任何文本，都会被转换成全大写或全小写文本。
    [select]

    -- SQL注释
    -- 输出表的所有列
    -- 注意：注释不支持#注释，#会和mybatis的取值表达式冲突，所以不要使用#注释
    @{this.columns}
    [from]

    /* 单行文本注释 */
    /* 输出表名 */
    @{this.table} /* --- [where] --- */

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
    <trim prefix="[where]" prefixOverrides="and|AND">
        <if test="loginName != null">[and] @{this.column.xxx} = #{xxx}</if>
    </trim>

    <!-- 不影响原有XML注释 -->

    // 你也可以加入逻辑删除表达式
    @{this.tryLogicallyDelete.useAndQuery}
    
    // 或者使用乐观锁表达式
    @{this.tryOptimisticLock.useAndQuery}
</select>
```

[回到顶部](#功能列表)



## 支持各种主键生成策略

目前插件主要支持以下四种主键生成策略，使用 `@Id` 标识主键，然后使用 `@GeneratedKey` 对主键生成策略进行配置。

```java
// 省略各种注解配置
public class YouBean {
    
    @Id
    // 1、使用JDBC的自增主键获取方式
    @GeneratedKey( useGeneratedKeys = true )
    
    // 2、直接生成主键值，插件提供两种默认主键值生成器（SnowFlakeId/UUID）
    // @GeneratedKey( valueProvider = SnowFlakeIdValueProvider.class )
    // @GeneratedKey( valueProvider = UUIDValuePrivoder.class )
    
    // 3、对于不支持JDBC获取自增主键值的数据库来说，可以像下面这样使用：
    // 具体参照com.viiyue.plugins.mybatis.enums.AutoIncrement里面的枚举值，
    // 里面预置了部分获取自增主键的SQL，可以直接写枚举名字，没有的话也可以自己写SQL脚本。
    // @GeneratedKey( statement = "MYSQL" ) // MYSQL是枚举名，通过枚举找到对应SQL脚本
    // @GeneratedKey( statement = "SELECT T_USER.YYYY()" ) // 直接写SQL脚本
    
    // 4、自定义自增主键SQL提供者
    // 如果枚举里面没有你需要的，可以通过statementProvider来提供你自己的SQL主键查询
    // @GeneratedKey( statementProvider = OracleAutoIncrementStatementProvider.class )
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

[回到顶部](#功能列表)



## 支持多主键场景

> Bean的定义

可以对多个主键字段进行 `@Id` 标注，没有特殊指明的话，默认会使用对象中第一个标注的主键，否则将会使用 `@Id(primary=true)` 的那一个主键。

```java
// 省略各种注解配置
public class YouBean {

    @Id
    private Long id;

    @Id(primary = true)
    private Long id2;
    
}
```

> 使用上的区别

```java
mapper.selectByPrimaryKey(PK); // 使用默认主键
mapper.selectByPrimaryKeyIndex(Index, PK); // 使用指定下标的主键，多主键顺序由实体Bean决定
mapper.selectByPrimaryKeyGroup(Pk...); // 使用默认主键
mapper.selectByPrimaryKeyGroupIndex(Index, PK...); // 使用指定下标的主键，多主键顺序由实体Bean决定
```

[回到顶部](#功能列表)



## 更方便快捷的条件查询

```java
Example example = null;

// query
// 方法后面可以直接跟各种Where条件
example = Example.query(User.class).equal("id", 1L).lt("age", 60).xxx(...);

// select
// 该方法可以对列进行操作，条件筛选的话使用when()来桥接
example = Example.select(User.class).includes( "id", "loginName", "password" );
example.when().equal("id", 1L).lt("age", 60).xxx(...);

// update
// 使用set/values可以对单个字段进行设值，不支持乐观锁
example = Example.update(User.class).set(xx, yy, zz).values(XX, YY, ZZ);
example.when().equal("id", 1L).lt("age", 60).xxx(...);

// update
// 还可以绑定某个实体对象来修改
User user = null;
Example.update(User.class).bind(user).when().equal("id", 1L).xxx(...);

// 具体使用
mapper.selectByExample(example);
mapper.updateByExample(example);
mapper.deleteByExample(example);
```

[回到顶部](#功能列表)



## 支持聚合函数统计

```java
Example example = null;

// 支持多字段统计，* 仅能在count函数中使用，其他函数使用会出现异常
example = Example.count(User.class, "*", ...); // 统计行数
example = Example.summation(User.class, "price", "num", ...); // 求和
example = Example.maximum(User.class, "price", "num", ...); // 求最大值
example = Example.minimum(User.class, "price", "num", ...); // 求最小值
example = Example.average(User.class, "price", "num", ...); // 求平均值

// 条件筛选
example.when().equal("id", 1L).lt("age", 60).xxx();

// 统计单个值，为了兼容不同的数据类型，统一使用BigDecimal接口，大家可自行转换成需要的数据类型
// 这种情况只能统计单列，如果统计多个字段，会出现SQL异常，此时请使用下面这种方式
BigDecimal result = mapper.selectStatisticByAggregateFunction(example);

// 统计多列，返回的是对应的实体对象，统计的字段值会自动封装到对象同名字段中
List<DTO> results = mapper.selectStatisticListByAggregateFunction(example);
```

[回到顶部](#功能列表)



## 支持数据库乐观锁

只需要在你的字段上标注 `@Version` 即可，**乐观锁注解只能出现一次**，默认为版本自增实现，数据类型支持 `Short`、`Integer`、`Long`、`Timestamp`，还可以选择雪花版本值，甚至你可以自己实现版本值的获取，实现 `NextVersionProvider` 接口即可。

```java
// 省略各种注解配置
public class User {
    @Version
    // 默认：DefaultNextVersionProvider.class
    // @Version(nextVersion = SnowFlakeIdNextVersionProvider.class)
    private Long version;
}
```

[回到顶部](#功能列表)



## 支持各种逻辑删除

逻辑删除需要配合 `@LogicallyDelete` 注解一起使用，如无任何注解配置，方法执行将会抛出异常，**只可配置一次**。

```java
// 逻辑删除所有
mapper.logicallyDeleteAll();

// 根据特定条件逻辑删除部分
mapper.logicallyDelete(Object);

// 通过主键逻辑删除指定数据
mapper.logicallyDeleteByPrimaryKey(PK);

// 通过主键数组逻辑删除特定数据
mapper.logicallyDeleteByPrimaryKeyGroup(Pk...);

// 多主键情况下，通过主键下标和主键值逻辑删除特定数据
mapper.logicallyDeleteByPrimaryKeyIndex(Integer, Pk);

// 多主键情况下，通过主键下标和主键数组逻辑删除特定数据
mapper.logicallyDeleteByPrimaryKeyIndexGroup(Index, Pk...);

// 通过自定义条件筛选逻辑删除特定数据
mapper.logicallyDeleteByExample(example);
```

[回到顶部](#功能列表)



## 支持逻辑删除数据的恢复

数据恢复也需要配合 `@LogicallyDelete` 注解一起使用，如无任何注解配置，方法执行将会抛出异常。

```java
// 查询所有被逻辑删除过的数据
mapper.selectAllDeleted();

// 根据特定条件还原指定数据
mapper.restore(Object);

// 还原所有被逻辑删除过的数据
mapper.restoreAllDeleted();

// 通过主键还原指定Id的数据
mapper.restoreByPrimaryKey(Pk);

// 通过主键数组批量还原数据
mapper.restoreByPrimaryKeyGroup(PK...);

// 多主键的情况，通过指定主键下标和主键值还原特定数据
mapper.restoreByPrimaryKeyIndex(Integer, Pk);

// 多主键的情况，通过指定主键下标和主键数组还原特定数据
mapper.restoreByPrimaryKeyIndexGroup(Index, Pk...);

// 通过自定义条件筛选还原特定数据
mapper.restoreByExample(example);
```

[回到顶部](#功能列表)



## 支持查询自定义返回Bean类型

Mapper接口提供三个泛型参数，依次为<数据库实体类型，返回数据类型，主键类型>。对于返回数据类型你可以任意定义，但是 **ResultMap** 结果映射只会生成和 **数据库实体** 同名的匹配字段，其他不匹配的字段值将一直为  `null`。

```java
public class YourMapper extends Mapper<User, UserDTO, Long> {
    // 那么你的返回数据类型就是：UserDTO
}
```

[回到顶部](#功能列表)



## 支持零MapperXML配置文件

因为插件通过Mybatis的注解 `@SelectProvider`、`@UpdateProvider`、`@InsertProvider`、`@DeleteProvider` 来提供基础 `SqlSource` ，所以即使你没有配置任何XML也是可以正常工作的。

[回到顶部](#功能列表)




## 扩展插件Api

Mybatis提供的注解SQL功能本身就是一种扩展机制，所以扩展就很好理解了，你可以写自己的@xxxProvider，也可以在插件的基础上实现 `DynamicProvider` 进而扩展插件的Api，这样的话你就可以在通用的Mapper上调用你自己的Api了。

> 接口定义，需要继承Marker

```java
public interface Mapper<DO, DTO, PK extends Serializable> extends Marker<DO, DTO, PK> {
    @SelectProvider( type = YourProvider.class, method = DynamicProvider.dynamicSQL )
    List<DTO> selectCustomApi( @Param("param1") String param1, @Param("param2") String param2 );
}
```

> 具体实现，需要继承DynamicProvider

```java
public final class YourProvider extends DynamicProvider {
    public String selectCustomApi( MappedStatement ms ) {
        return "[select] @{this.columns} [from] @{this.table} [where] @{this.column.name} = #{param1} [and] @{this.column.xxx} = #{param2}";
    }
}
```

[回到顶部](#功能列表)



## 提供各种场景的日志打印

> 编译日志，需要开启 enableCompilationLog 配置

```
... --------------------------------------------------------------------------------
... ----- Target: YourMapper( BaseSelectProvider )
... -- Namespace: xxx.xxx.xxx.YourMapper.selectAll
... Template SQL: [select] @{this.columns} [from] @{this.table} @{this.tryLogicallyDelete.useWhereQuery} @{this.defaultOrderBy}
... Compiled SQL: SELECT `id`, `name`, `version` FROM `t_user` WHERE deleted = 'N' ORDER BY `id` DESC
... ------- Time: 1ms
... --------------------------------------------------------------------------------
```

> 实时动态日志，需要开启 enableRuntimeLog 配置

```
... --------------------------------------------------------------------------------
... ==> Compile runtime SQL ...
... --------------------------------------------------------------------------------
... ==> - Template: SELECT `id`, `name`, `version` FROM `t_user` %{this.where($).tryLogicallyDeleteQuery} ORDER BY `id` DESC
... ==> - Compiled: SELECT `id`, `name`, `version` FROM `t_user` WHERE deleted = 'N' AND `name` = #{name} ORDER BY `id` DESC
... ==> Parameters: xxx.xxx.xxx.User@145eaa29
... <== ----- Time: 7ms
... --------------------------------------------------------------------------------
... ==>  Preparing: SELECT `id`, `name`, `version` FROM `t_user` WHERE delete = 'N' AND `name` = ? ORDER BY `id` DESC 
... ==> Parameters: xxx(String)
... <==      Total: 3
... --------------------------------------------------------------------------------
```

[回到顶部](#功能列表)



## 关于作者

- 邮箱：tangxbai@hotmail.com
- 掘金： https://juejin.im/user/5da5621ce51d4524f007f35f
- 简书： https://www.jianshu.com/u/e62f4302c51f
- Issuse：https://github.com/tangxbai/mybatis-mapper/issues
