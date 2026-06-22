FROM eclipse-temurin:23-jdk

WORKDIR /app
COPY . .
RUN chmod +x gradlew

ENTRYPOINT ["./gradlew", "--no-daemon"]
CMD ["test"]
