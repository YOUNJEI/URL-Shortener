FROM openjdk:8-jre-alpine
COPY build/libs/URL-Shortener-0.0.1-SNAPSHOT.jar app.jar
ARG SPRING_PROFILES_ACTIVE=prod
ENV SPRING_PROFILES_ACTIVE ${SPRING_PROFILES_ACTIVE}
CMD ["java", "-jar", "-Dspring.profiles.active=${SPRING_PROFILES_ACTIVE}", "app.jar"]