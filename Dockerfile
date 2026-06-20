FROM eclipse-temurin:23-jdk

WORKDIR /app
COPY . .
RUN chmod +x gradlew

# `docker run <image>` -> `./gradlew test`; pass another task as an argument to override.
ENTRYPOINT ["./gradlew", "--no-daemon"]
CMD ["test"]
