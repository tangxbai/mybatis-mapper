## v1.3.6（2020/12/02）

1. 修复`PropertyFilter`绑定对象之后无法绑定自定义属性值的问题；
2. 优化`Example`使用链式调用相关问题；
3. 优化了一些其他业务逻辑；

## v1.3.5（2020/11/29）

1. 优化`@Column`注解无法指定更多JdbcType，移除自定义`Type`类型制定，更新为Mybatis自带的`JdbcType`；
2. `@Column`注解方法名`jdcbType`更新为`jdbcType`；

## v1.3.4（2020/11/24）

1. 优化`Example`条件查询，支持通过`get`获取泛型Example继续操作；
2. 优化了一些其他业务逻辑；
3. 修复了一些BUG；

## v1.3.3（2020/05/18）

1. 优化`UpdateExample`参数无法自由绑定的问题，现已在原始基础上扩展支持自由追加参数绑定功能；
2. 优化`PropertyFilter`的属性过滤规则，并新增支持动态追加过滤规则；
3. 优化`BeanUtil`关于获取参数返回值的问题，不再使用`ThreadLocal`来获取返回值，而是直接返回`Map`；
4. 优化了一些其他业务逻辑；


## v1.3.2（2020/03/26）

1. 修复Example动态条件中IN/NOT-IN的可扩展参数无效的问题；
2. 优化若干核心逻辑处理代码，使其更稳定更高效；



## v1.3.1（2020/03）

1. 修复在解析数据Bean实体对象中出现的`static`字段解析抛异常的问题；
2. 优化若干核心逻辑处理代码，使其更稳定更高效；
3. 升级父项目依赖版本号；



## v1.3.0（2019/11）

1. 优化pom配置信息，将发布Maven中央库的基本配置信息统一独立到 `plugin-release-parent` 中，使pom配置更为简洁、更专注化；
2. 降低 `common-lang3` 的版本引用，之前使用最新版本的库导致新版本一些工具方法在其他插件中不兼容，于是版本降低为常用版本，不兼容方法改为内部实现；
3. 替换License许可证头部注释生成插件，使其更为规范化；
4. 删除一些之前遗留无用的文件；
5. 优化一些处理逻辑代码；
6. 完善READMD文档；



## v1.2.0（2019/10）

1. 修复若干BUG；
2. 修复 `UpdateExample` 缺失参数导致SQL异常的问题；
2. 优化 `Example` 条件查询；
3. 优化 `StringAppender` 的基础构造；
4. 优化 `OptimisticLockBuilder` 条件桥接关键字不统一的问题；
5. 优化 `MyBatisMapperFactoryBuilder` 逻辑代码；
6. 进一步完善项目逻辑代码；
8. 完善READMD文档；



## v1.1.1（2019/10）

1. 修复编译日志无法禁用的问题；
2. 移除 `pom.xml` 中无用的插件引用；



## v1.1.0（2019/10）

正式发布的版本，目前已经上传到 `Github` 和 `Maven` 中央库供大家使用，下面是版本的功能点：

- [支持模板语法](https://github.com/tangxbai/mybatis-mapper#支持模板语法)
- [支持SQL注释](https://github.com/tangxbai/mybatis-mapper#支持SQL注释)
- [支持各种主键生成策略](https://github.com/tangxbai/mybatis-mapper#支持各种主键生成策略)
- [支持多主键场景](https://github.com/tangxbai/mybatis-mapper#支持多主键场景)
- [更方便快捷的条件查询](https://github.com/tangxbai/mybatis-mapper#更方便快捷的条件查询)
- [支持聚合函数统计](https://github.com/tangxbai/mybatis-mapper#支持聚合函数统计)
- [支持数据库乐观锁](https://github.com/tangxbai/mybatis-mapper#支持数据库乐观锁)
- [支持各种逻辑删除](https://github.com/tangxbai/mybatis-mapper#支持各种逻辑删除)
- [支持逻辑删除数据的恢复](https://github.com/tangxbai/mybatis-mapper#支持逻辑删除数据的恢复)
- [支持查询自定义返回Bean类型](https://github.com/tangxbai/mybatis-mapper#支持查询自定义返回Bean类型)
- [支持零MapperXML配置文件](https://github.com/tangxbai/mybatis-mapper#支持零MapperXML配置文件)
- [扩展插件Api](https://github.com/tangxbai/mybatis-mapper#扩展插件Api)
- [提供各种场景的日志打印](https://github.com/tangxbai/mybatis-mapper#提供各种场景的日志打印)
- 提供更丰富的API



## v1.0.0（2017）

先导版本，此版本并未正式发布，随后在此基础上对整个项目进行了大量重构，发布之后从 `1.1.x` 开始。
