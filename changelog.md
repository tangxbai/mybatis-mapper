## v1.3.2 ( 2020/03/26 )

1. 修复Example动态条件中IN/NOT-IN的可扩展参数无效的问题；
2. 优化若干核心逻辑处理代码，使其更稳定更高效；



## v1.3.1 ( 2020/03 )

1. 修复在解析数据Bean实体对象中出现的`static`字段解析抛异常的问题；
2. 优化若干核心逻辑处理代码，使其更稳定更高效；
3. 升级父项目依赖版本号；



## v1.3.0 ( 2019/11 )

1. 优化pom配置信息，将发布Maven中央库的基本配置信息统一独立到 `plugin-release-parent` 中，使pom配置更为简洁、更专注化
2. 降低 `common-lang3` 的版本引用，之前使用最新版本的库导致新版本一些工具方法在其他插件中不兼容，于是版本降低为常用版本，不兼容方法改为内部实现
3. 替换License许可证头部注释生成插件，使其更为规范化
4. 删除一些之前遗留无用的文件
5. 优化一些处理逻辑代码
6. 完善READMD文档



## v1.2.0 ( 2019/10 )

1. 修复若干BUG
2. 修复 `UpdateExample` 缺失参数导致SQL异常的问题
2. 优化 `Example` 条件查询
3. 优化 `StringAppender` 的基础构造
4. 优化 `OptimisticLockBuilder` 条件桥接关键字不统一的问题
5. 优化 `MyBatisMapperFactoryBuilder` 逻辑代码
6. 进一步完善项目逻辑代码
8. 完善READMD文档



## v1.1.1 ( 2019/10 )

1. 修复编译日志无法禁用的问题
2. 移除 `pom.xml` 中无用的插件引用



## v1.1.0 ( 2019/10 )

正式面世的版本，目前已经上传到 `Github` 和 `Maven` 中央库供大家使用，下面是版本的功能点：

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



## v1.0.0 ( 2017 )

先导版本，此版本并未正式发布，随后在此基础上对整个项目进行了大量重构，发布之后从 `1.1.x` 开始。
