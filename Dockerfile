FROM maven:3-jdk-8-alpine as build

WORKDIR /app

COPY pom.xml ./

RUN mvn dependency:go-offline

COPY src ./src

RUN mvn package -DskipTests

FROM jetty:9-jre8-alpine 

COPY --from=build /app/target/preprocessing-plugin.war /var/lib/jetty/webaps/

WORKDIR $JETTY_HOME

CMD ["java", "-jar", "start.jar"]
