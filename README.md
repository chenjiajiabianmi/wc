# 项目简介
这个项目是一个Java应用程序，专注于网络爬虫，使用MyBatis进行对象关系映射（ORM），并结构化为各种组件，用于配置、连接、爬虫、异常处理、持久化、映射和任务管理。

# 编程语言
Java
# 使用的框架
1. Maven
2. MyBatis
# 数据库
1. MySQL
# API类型
1. Swagger
# 项目结构
## 配置文件
1. .classpath: 定义Eclipse项目的Java构建路径。
2. .project: Eclipse项目配置文件，定义项目元数据。
3. .settings/: Eclipse工作区和开发工具的各种配置文件。
4. pom.xml: Java应用程序的Maven项目对象模型配置文件。
## 目录结构
1. src/config/mybatis_config.xml: 配置MyBatis设置用于数据访问和事务管理。
2. src/connection/: 连接相关的常量、核心逻辑和实体类。
3. src/crawler/: 爬虫功能的基类、实体类和特定组件。
4. src/engine/: 包含主执行线程和Zookeeper集成的相关类。
5. src/exception/: 自定义异常类。
6. src/log4j.properties: 应用程序的日志配置。
7. src/mapper/: MyBatis映射文件。
8. src/persistence/: 持久化层的工厂类、会话工厂设置和实体类。
9. src/task/: 任务管理的基本类、基本任务实现和特定任务实现。
10. src/test/: 持久化层和任务相关功能的单元测试。
## 依赖的库
1. MyBatis
2. C3P0
3. log4j
4. Apache POI
