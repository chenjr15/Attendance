FROM gradle:6-jdk11-alpine as builder

WORKDIR /home/gradle/project

COPY . /home/gradle/project/
RUN gradle bootJar --info
EXPOSE 8080
CMD ["gradle","bootRun"]

FROM gradle:6-jdk11-alpine
COPY --from=builder  /home/gradle/project/build/libs/*.jar attendance.jar
CMD ["java","-jar","attendance.jar"]