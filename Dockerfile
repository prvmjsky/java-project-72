FROM eclipse-temurin:21-jdk

WORKDIR /app

COPY /app .

RUN ["./gradlew", "clean", "build"]

ENV JAVA_OPTS="-Xms1g -Xmx1g"

CMD ["./gradlew", "run", "-Dorg.gradle.jvmargs=-Xms1g -Xmx1g"]