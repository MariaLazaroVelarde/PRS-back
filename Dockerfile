FROM maven:3.9.0-eclipse-temurin-17-alpine AS builder
WORKDIR /app
COPY pom.xml .
COPY src ./src
RUN mvn clean package -DskipTests

FROM eclipse-temurin:17-jre-alpine
WORKDIR /app
COPY --from=builder /app/target/*.jar app.jar

# Variables de entorno básicas para la conexión a la DB y puerto
ENV DATABASE_URL="mongodb+srv://sistemajass:<db_password>@sistemajass.jn6cpoz.mongodb.net/?retryWrites=true&w=majority&appName=SistemaJass"
ENV DATABASE_USERNAME="sistemajass"
ENV DATABASE_PASSWORD="ZC7O1Ok40SwkfEje"
ENV PORT=8080

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "app.jar"]