# Maven Builder
FROM maven:3-jdk-8-alpine as build

WORKDIR /app

COPY pom.xml ./

RUN mvn -B -T 1C dependency:go-offline

COPY src ./src

RUN mvn -B -T 1C package -DskipTests

# Deploy Server
FROM tomcat:8.0-alpine

COPY --from=build /app/target/preprocessing-plugin.war /usr/local/tomcat/webapps/

EXPOSE 8080

CMD ["catalina.sh", "run"]