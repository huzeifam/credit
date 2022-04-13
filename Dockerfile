FROM openjdk:11
EXPOSE 8090
ADD build/libs/kredit-0.0.1-SNAPSHOT.jar kredit
ENTRYPOINT ["java", "-jar", "kredit"]