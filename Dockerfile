FROM gradle:8-jdk8
WORKDIR /app
COPY . /app
RUN ./gradlew build -x test
COPY build/libs/URL-Shortener-0.0.1-SNAPSHOT.jar /app/app.jar
CMD ["java", "-jar", "app.jar"]