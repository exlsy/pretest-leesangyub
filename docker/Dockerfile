FROM openjdk:21-slim

RUN apt-get update && apt-get install -y vim

MAINTAINER LEESANGYUB

RUN mkdir -p /app/pretest/target
RUN mkdir -p /app/pretest/config
RUN mkdir -p /app/pretest/logs
ENV APP_PATH=/app/pretest

ARG version
ENV version_name=$version

ENV CONFIG_PATH=/app/pretest/config
ENV CONFIG_OPTS="-Dsecret=easytask -Dspring.config.location=${CONFIG_PATH}/application.yml,${CONFIG_PATH}/application-local.yml  --add-exports=java.base/sun.security.util=ALL-UNNAMED "

COPY ./pretest-leesangyub-${version_name}.jar ${APP_PATH}/target/

WORKDIR ${APP_PATH}

EXPOSE 9090
ENTRYPOINT  java -jar ${CONFIG_OPTS}  ${APP_PATH}/target/pretest-leesangyub-${version_name}.jar


