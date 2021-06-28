# AttendanceBackend

工程实践 105 组 后端源代码

## 项目依赖与运行工具

1. Java JDK 1.8+
2. MySQL 8.0+
3. Redis 6.2.1
4. Gradle 6.8.3
5. Git 2.0+

## 运行指南

1. 下载源码

```
git clone https://github.com/chenjr15/AttendanceBackend.git
```

2. 修改配置文件

`src\main\resources\application.properties`

下面的选项需要配置修改

```properties
# Token密钥
token.secret=mDBMsQKL6mESaUbhyBKOZ4JREJX4/LGmPWJZhFvbKOUWc/lk9YS0hNrKtSZhFkdKwZwZrd+upWtOcs8erTQ8G/nPlY3QLvUP+Ed7z0Tmp==

# 阿里云短信配置
aliyun.sms.accessKeyId=accessKeyId
aliyun.sms.accessKeySecret=accessKeySecret
aliyun.sms.templateCode=templateCode
aliyun.sms.signName=signName

# redis 配置
spring.redis.host=localhost
spring.redis.port=6379
spring.redis.password=

#  文件上传后保存的位置，windows配置示例：E:\\avatar\\
storage.file.storage-path=/path/to/avatar/directory/
# 文件访问的url前缀，务必带上头尾斜杠
storage.file.url-prefix=/avatar/

# 服务器监听地址
server.address=0.0.0.0
# 服务监听端口
server.port=8080
```

3. 作为开发服务器运行

```
./gradlew bootRun
```

然后打开[http://localhost:8080/swagger-ui.html](http://localhost:8080/swagger-ui.html) 即可看到接口文档信息

4. 编译打包

```
./gradlew bootjar
```

得到的`jar`包在`build/libs/`目录下

该 jar 包内置`Tomcat`，通过`java -jar build/libs/attendance-0.0.1-SNAPSHOT.jar`命令行即可执行

然后打开[http://localhost:8080/swagger-ui.html](http://localhost:8080/swagger-ui.html) 即可看到接口文档信息
