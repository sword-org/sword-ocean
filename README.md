
  sword-ocean是由sword-org打造的最简单的数据服务中间件，适用于数据展示，数据接口服务等系统。可以分分钟创建一个数据服务系统。

# 1 适用需求
   
## 1.1 数据服务类
如果你的需求主要是数据服务类应用，目的是把数据库中存储的数据快速的提供给前台的应用，系统的主要目的就是数据展示，那么本框架非常适用。

## 1.2 可视化系统服务
如果你的服务或者系统是提供可视化服务的，那么本的系统在可视化数据提供方面也是非常合适的。当然你可以结合[sword-chart](https://github.com/sword-org/sword-chart)  的前台可视化控件来非常方便的提供可视化系统。


## 1.3 数据查询密集型系统
   如果你的系统是一款数据查询密集型系统，主要提供数据的查询服务，那么本框架也是非常适合你的应用的。


# 2 依赖环境

   java/maven/spring/springmvc


# 3 使用方法
要使用本框架，你可以是从一个空白的项目开始，这样建议你直接下载示例项目，在此基础之上进行你的项目开发。如果你已经有项目了，需要继承进去，那么你就需要参考手动创建的步骤。

## 3.1直接下载示例项目
   示例项目[ ] 

## 3.2手动创建
   手动创建主要是需要引入sword-ocean包及创建其运行的环境，同时在spring的配置文件中进行配置使得sword-ocean生效。
### 3.2.1 创建基于spring的web的maven项目
   首先需要创建一个maven的web项目，然后引入spring框架（3.0及以上）
### 3.2.2 引入sword-ocean的依赖
   引入sword-ocean的依赖，在pom文件中添加如下代码
```xml
	<repositories>
		<repository>
			<id>sword-maven-repository</id>
			<name>sword-maven-repository</name>
			<url>https://raw.github.com/sword-org/sword-repo/master/</url>
		</repository>
	</repositories>
```
   在dependencies节点下添加如下依赖
  ```xml
  	<dependency>
			<groupId>com.sword.ocean</groupId>
			<artifactId>sword-ocean</artifactId>
			<version>1.0.1</version>
		</dependency>
  ```
### 3.2.3 进行sword-ocean的服务配置
创建sword-ocean.properties文件,***的地方内容换成你自己的环境
```properties
######################################################  common   config  start###########################################
#JBDC about
sword.jdbc.driver=com.mysql.jdbc.Driver
sword.jdbc.url=jdbc:mysql://*****:3306/***?useUnicode=true&characterEncoding=UTF-8&autoReconnect=true&failOverReadOnly=false&useOldAliasMetadataBehavior=true
sword.jdbc.username=***
sword.jdbc.password=***

#is print jdbc sql?
show_sql=false
#client appid
client.appid=APP1,APP2,APP3
#ping timeout second
ping.timeout.second=60

```
spring-config.xml中引入sword-ocean
```xml
	<context:annotation-config />
	<!-- Enables sword-ocean -->
	<context:component-scan base-package="com.sword.ocean" />
```
spring-mvc.xml中引入sword-ocean
```xml
	<mvc:annotation-driven />
	<!--  add sword-oecan properties  -->
    <context:property-placeholder location="classpath:sword-ocean.properties"/>
    
 	<!-- Enables sword-ocean -->
 	<context:component-scan base-package="com.sword.ocean.web.controller" /> 
```

### 3.2.4 运行SQL脚本
   进入sword-ocean.properties文件中配置的数据库中运行[doc/sword-ocean.sql](https://github.com/chengn/sword-ocean/blob/master/doc/sword-ocean.sql)  文件
   
# 4 验证
  配置完成之后，启动web应用（tomcat等），运行 http://localhost:8080/***/sword/test 返回结果之后表示集成成功。
  
  
# 5 服务协议
## 5.1 格式
  sword-ocean提供的服务协议是json格式的数据内容。
## 5.2 内容
  协议内容包括三大类，分别是result（响应结果，包括success和failed），msg（结果说明），data（数据）
```json
{
    "data":[ {"id":1,"name":"test"}, {"id":2,"name":"test2"}],
    "result":"success",
    "msg":null
}
```
  
# 6 服务开发
在集成sword-ocean成功之后，需要开发自己的数据服务接口的时候，可以选择自己手动配置，也可以选择部署sword-ocean-admin服务之后，进行可视化开发。
## 6.1 sword服务开发工具
### 6.1.1 部署sword-ocean-admin管理系统

### 6.1.2 创建数据服务

## 6.2 手动配置
### 6.2.1 SQL脚本

### 6.2.2 存储过程

  

