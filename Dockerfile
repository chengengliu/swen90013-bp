FROM maven:3-jdk-8-alpine as build

WORKDIR /app

COPY pom.xml ./

RUN mvn -B -T 1C dependency:go-offline

COPY src ./src

RUN mvn -B -T 1C package -DskipTests

FROM jetty:9-jre8-alpine 

COPY --from=build /app/target/preprocessing-plugin.war /var/lib/jetty/webapps/

EXPOSE 8080

CMD java -jar $JETTY_HOME/start.jar
