FROM gradle:6-jdk11-alpine as builder
# 两阶段构建，发布用的容器无源码
WORKDIR /home/gradle/project

COPY . /home/gradle/project/
RUN gradle bootJar --info&& \
 cp build/libs/*.jar attendance-`git rev-parse --short HEAD`.jar&&\
 ls *.jar
EXPOSE 8080
CMD ["gradle","bootRun"]

FROM gradle:6-jdk11-alpine
COPY --from=builder  /home/gradle/project/*.jar .
COPY --from=builder /home/gradle/project/run.sh .
CMD ["sh","run.sh"]