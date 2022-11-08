FROM adoptopenjdk/openjdk11:jdk-11.0.11_9 as build
WORKDIR /workspace/app

COPY mvnw .
COPY .mvn .mvn
COPY pom.xml .
COPY src src

RUN ./mvnw -s .mvn/settings.xml install -DskipTests
RUN mkdir -p target/dependency \
    && (cd target/dependency; jar -xf ../*.jar)


# Final smaller image
FROM adoptopenjdk/openjdk11:jre-11.0.11_9

VOLUME /tmp

ARG BUILD_WORKSPACE=/workspace/app
ARG DEPENDENCY=${BUILD_WORKSPACE}/target/dependency

ENV ENVIRONMENT=local

COPY --from=build ${DEPENDENCY}/BOOT-INF/lib /app/lib
COPY --from=build ${DEPENDENCY}/META-INF /app/META-INF
COPY --from=build ${DEPENDENCY}/BOOT-INF/classes /app

ENTRYPOINT ["sh", "-c", "java -Xms512m -Xmx2048m -cp app:app/lib/* -Dspring.profiles.active=${ENVIRONMENT} com.homedepot.mm.pc.merchantalerting"]
