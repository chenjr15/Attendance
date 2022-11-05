# Attendance Project - Backend Code

Attendance Project 后端源代码

## 项目依赖与运行工具

1. Java JDK 1.8+
2. MySQL 8.0+
3. Redis 6.2.1
4. Gradle 6.8.3
5. Git 2.0+

## 运行指南

1. 下载源码

```shell
git clone https://github.com/chenjr15/Attendance.git
cd Attendance
```

接下去依据你的环境来看

### [Plan A] 通过Docker Compose 一键运行

> 该方法需要Docker 环境，无需设置复杂的MySQL和Redis，推荐在Linux环境做。
>

0. 安装Docker

推荐使用国内源的教程: [Docker Community Edition 镜像使用帮助](https://mirrors.tuna.tsinghua.edu.cn/help/docker-ce/)

官方安装教程在这: [Docker Engine installation overview](https://docs.docker.com/engine/install/)

1. 安装Docker Compose Plugin

参考官方教程[Install the Compose plugin](https://docs.docker.com/compose/install/linux/)

注意，这里不是python版本的Docker Compose，这是用Go实现的v2版本。

```shell
apt update && apt install  -y docker-compose-plugin
```

2. 生成环境变量配置文件

生成一个随机的密码配置文件，目前包含MySQL密码、Redis密码、Token密钥

```shell
bash gen_env.sh .env
```

3. 启动！

```shell
docker compose up -d 
```

你可能看不到启动的日志，通过`docker compose logs ` 可以看到日志，或者去掉`-d`参数不做后台启动。

然后你就可以通过[http://ip:8080/swagger-ui.html](http://ip:8080/swagger-ui.html) 访问API文档，通过
[http://ip:8081](http://ip:8081) 访问phpmyadmin提供的web端数据库管理工具

### [Plan B] 直接起本地开发环境

> 该方法需要本地安装好Java 环境和Gradle 环境，并手动设置好MySQL和Redis

修改配置文件在这里，注意外面的`.env`文件会覆盖这个配置
`src\main\resources\application.properties`

1. 作为开发服务器运行

```
./gradlew bootRun
```

然后打开[http://localhost:8080/swagger-ui.html](http://localhost:8080/swagger-ui.html) 即可看到接口文档信息

2. 编译打包

```
./gradlew bootjar
```

得到的`jar`包在`build/libs/`目录下

该 jar 包内置`Tomcat`，通过`java -jar build/libs/attendance-0.0.1-SNAPSHOT.jar`命令行即可执行

然后打开[http://localhost:8080/swagger-ui.html](http://localhost:8080/swagger-ui.html) 即可看到接口文档信息
