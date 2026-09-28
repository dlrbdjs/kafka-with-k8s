FROM eclipse-temurin:21-jre

RUN ln -snf /usr/share/zoneinfo/Asia/Seoul /etc/localtime

RUN addgroup --system producer \
    && adduser --system --ingroup producer producer

WORKDIR /app

COPY --chown=producer:producer build/libs/*SNAPSHOT.jar app.jar

USER producer:producer

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "app.jar"]