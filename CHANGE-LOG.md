### 1.0.0

------

初始版本，由于当时并没有想到正式发布，只是以本地Jar包的方式在自己的项目中使用，随着后来需求的急需变更不得不把项目上传到中央库上去，这样不仅能更方便的使用，也能在某种程度让更多的人使用这个插件，所以在此基础上对整个项目进行了大量重构，并决定规范化上传到 `Github` 和Maven中央库，所以这个项目并没有 `1.0.x` 版本，算是一个先导版本吧。



### 1.1.0

------

正式面世的版本，目前已经上传到 `Github` 和 `Maven` 中央库供大家使用，下面是版本的功能点：

- [支持模板语法](https://github.com/tangxbai/mybatis-mapper#支持模板语法)

- [支持支持SQL注释](https://github.com/tangxbai/mybatis-mapper#支持支持SQL注释)

- [支持动态解析](https://github.com/tangxbai/mybatis-mapper#支持动态解析)

- [支持各种主键生成策略](https://github.com/tangxbai/mybatis-mapper#支持各种主键生成策略)

- [支持多主键场景](https://github.com/tangxbai/mybatis-mapper#支持多主键场景)

- [支持聚合函数统计](https://github.com/tangxbai/mybatis-mapper#支持聚合函数统计)

- [支持数据库乐观锁](https://github.com/tangxbai/mybatis-mapper#支持数据库乐观锁)

- [支持各种逻辑删除](https://github.com/tangxbai/mybatis-mapper#支持各种逻辑删除)

- [支持逻辑删除数据的恢复](https://github.com/tangxbai/mybatis-mapper#支持逻辑删除数据的恢复)

- [支持查询自定义返回Bean类型](https://github.com/tangxbai/mybatis-mapper#支持查询自定义返回Bean类型)

- [支持零MapperXML配置文件](https://github.com/tangxbai/mybatis-mapper#支持零MapperXML配置文件)

- [更方便快捷的条件查询](https://github.com/tangxbai/mybatis-mapper#更方便快捷的条件查询)

- [提供各种场景的日志打印](https://github.com/tangxbai/mybatis-mapper#提供各种场景的日志打印)

- 提供更丰富的API



### 1.1.1

------

1. 优化：移除 `pom.xml` 中无用的插件引用
2. 修复：编译日志无法禁用的问题



### 1.1.2

------

1. 优化 OptimisticLockBuilder 条件桥接关键字不统一的问题
2. 