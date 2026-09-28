FROM eclipse-temurin:21-jre

RUN ln -snf /usr/share/zoneinfo/Asia/Seoul /etc/localtime

RUN addgroup --system consumer \
    && adduser --system --ingroup consumer consumer

WORKDIR /app

COPY --chown=consumer:consumer build/libs/*SNAPSHOT.jar app.jar

USER consumer:consumer

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "app.jar"]