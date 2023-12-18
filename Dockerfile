FROM amazoncorretto:17.0.9 as build
WORKDIR /build
COPY . .
RUN ./gradlew bootJar -x test --no-daemon

FROM amazoncorretto:17.0.9 as final
RUN mkdir -p /config
COPY ./scripts/application.properties /config/app.properties
COPY --from=build /build/build/libs/fake-jira.jar /app.jar
ENTRYPOINT ["java", "-Xmx1024M", "-jar", "/app.jar", "--spring.config.location=/config/app.properties"]
