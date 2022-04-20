FROM openjdk:11
EXPOSE 8090
ADD build/libs/credit-0.0.1-SNAPSHOT.jar credit
ENTRYPOINT ["java", "-jar", "credit"]