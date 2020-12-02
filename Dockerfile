FROM openjdk:8-jdk-stretch AS build

ADD . /workspace
WORKDIR /workspace
RUN sh gradlew bootJar

FROM openjdk:8-jre-stretch

# DNS Cache TTL
RUN echo "networkaddress.cache.ttl=10" >> $JAVA_HOME/lib/security/java.security

WORKDIR /app
COPY --from=build /workspace/build/libs/*.jar ./server.jar

CMD ["java", "-Djava.awt.headless=true", "-Djava.security.egd=file:/dev/./urandom", "-jar", "/app/server.jar"]