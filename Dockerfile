FROM azul/zulu-openjdk:11-latest AS BUILD_IMAGE
COPY . .
RUN ./gradlew bootJar

FROM azul/zulu-openjdk-alpine:11-latest
WORKDIR /app
COPY --from=BUILD_IMAGE /build/libs/app.jar app.jar
RUN chmod +x app.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar", "--spring.profiles.active=prod"]
