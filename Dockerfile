# Maven Builder
FROM maven:3-jdk-8-alpine as build

WORKDIR /app

COPY pom.xml ./

RUN mvn -B -T 1C dependency:go-offline

COPY src ./src

RUN mvn -B -T 1C package -DskipTests

# Deploy Server
FROM jetty:9-jre8-alpine

COPY --from=build /app/target/preprocessing-plugin.war /var/lib/jetty/webapps/

# Change Volume permissions from root to jetty
RUN mkdir /var/lib/jetty/preprocess_data

RUN chown jetty:jetty /var/lib/jetty/preprocess_data

USER jetty

EXPOSE 8080

CMD java -jar $JETTY_HOME/start.jar
