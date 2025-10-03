FROM maven:3.9.0-eclipse-temurin-17-alpine AS builder
WORKDIR /app
COPY pom.xml .
COPY src ./src
RUN mvn clean package -DskipTests -Dmaven.javadoc.skip=true

FROM eclipse-temurin:17-jre-alpine
WORKDIR /app

# Install dumb-init for proper signal handling
RUN apk add --no-cache dumb-init

# Create non-root user
RUN addgroup -g 1001 -S appgroup && \
    adduser -u 1001 -S appuser -G appgroup

COPY --from=builder /app/target/*.jar app.jar
RUN chown appuser:appgroup app.jar

# Switch to non-root user
USER appuser

# Production environment variables - Optimized for low memory usage
ENV JAVA_OPTS="-Xms128m -Xmx384m -XX:+UseG1GC -XX:+UseStringDeduplication -XX:MaxGCPauseMillis=100 -XX:+DisableExplicitGC -XX:+UseCompressedOops -Djava.awt.headless=true"
ENV DATABASE_USERNAME="sistemajass"
ENV DATABASE_PASSWORD="ZC7O1Ok40SwkfEje"
ENV PORT=8087
ENV SWAGGER_ENABLED=false

EXPOSE 8087

ENTRYPOINT ["dumb-init", "sh", "-c", "java $JAVA_OPTS -jar app.jar"]