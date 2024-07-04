FROM eclipse-temurin:17-jdk-focal
WORKDIR /app

COPY . /app/
RUN chmod +x ./mvnw
RUN ./mvnw clean install

CMD ["./mvnw", "spring-boot:run"]