FROM arm64v8/alpine:latest

RUN apk add --no-cache openjdk17

ENV JAVA_HOME=/usr/lib/jvm/java-17-openjdk

ENV PATH="${JAVA_HOME}/bin:${PATH}"

EXPOSE 8085

COPY target/DiplomCloud-0.0.1-SNAPSHOT.jar app.jar

CMD ["java", "-jar", "app.jar"]


#docker build -t appdiplomcloud:latest .
#docker run -itd --name appDiplomCloud -p 8085:8085 appdiplomcloud:latest