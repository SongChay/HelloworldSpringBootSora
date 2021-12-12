FROM openjdk:8-jdk-alpine
EXPOSE 8080
ADD build/libs/hello-world-0.0.1.jar hello-world-0.0.1
ENTRYPOINT ["java","-jar","hello-world-0.0.1"]