#!/bin/bash

export COMPOSE_FILE_PATH="${PWD}/target/docker/docker-compose.yml"

if [ -z "${M2_HOME}" ]; then
  export MVN_EXEC="${PWD}/mvnw"
else
  export MVN_EXEC="${M2_HOME}/bin/mvn"
fi

sonarqube() {
  SONAR_LAUNCH=$(docker ps --filter "name=sonarqube" | wc -l)
  SONAR_DOCKER="${PWD}/docker-compose-sonarqube.yml"
  # If sonar is launch stop else start
  if [ "$SONAR_LAUNCH" -eq 2 ]; then
    docker-compose -f "$SONAR_DOCKER" down
  else
    docker volume create sonarqube_data
    docker volume create sonarqube_logs
    docker volume create sonarqube_exts
    docker-compose -f "$SONAR_DOCKER" up -d
  fi
}

sonarpurge() {
  SONAR_DOCKER="${PWD}/docker-compose-sonarqube.yml"
  docker-compose -f "$SONAR_DOCKER" kill
  docker-compose -f "$SONAR_DOCKER" rm -f
  docker volume rm -f sonarqube_data
  docker volume rm -f sonarqube_logs
  docker volume rm -f sonarqube_exts
}


test() {
  # Launch tests
  $MVN_EXEC jacoco:prepare-agent
  export LOGGING_LEVEL="info"
  $MVN_EXEC clean test
  unset LOGGING_LEVEL
  $MVN_EXEC jacoco:report
  # If sonar is present sent the report
  SONAR_LAUNCH=$(docker ps --filter "name=sonarqube" | wc -l)
  if [ "$SONAR_LAUNCH" -eq 2 ]; then
    #$MVN_EXEC org.pitest:pitest-maven:mutationCoverage # Mutation testing with Pitest, disabled if not installed
    $MVN_EXEC org.sonarsource.scanner.maven:sonar-maven-plugin:sonar
  fi
}

start() {
    docker volume create esy_db_volume
    docker-compose -f "$COMPOSE_FILE_PATH" up -d
}

down() {
    if [ -f "$COMPOSE_FILE_PATH" ]; then
        docker-compose -f "$COMPOSE_FILE_PATH" down
    fi
}

purge() {
    docker-compose -f $COMPOSE_FILE_PATH kill
    docker-compose -f $COMPOSE_FILE_PATH rm -f
    docker volume rm -f esy_db_volume
}

build() {
    $MVN_EXEC clean package -DskipTests
}

tail() {
    docker-compose -f "$COMPOSE_FILE_PATH" logs -f
}

tail_db() {
    docker-compose -f $COMPOSE_FILE_PATH logs -f esy_db
}

tail_server() {
    docker-compose -f $COMPOSE_FILE_PATH logs -f esy_server
}

tail_all() {
    docker-compose -f "$COMPOSE_FILE_PATH" logs --tail="all"
}

cli_db() {
    docker exec -it esy-db /bin/sh
}

cli_server() {
    docker exec -it esy-server /bin/sh
}

embed() {
  java -Dspring.profiles.active=embeddb -jar target/*-SNAPSHOT.jar
}

case "$1" in
  test)
    test
    ;;
  build)
    build
    ;;
  build_start)
    down
    build
    start
    tail
    ;;
  start)
    start
    tail
    ;;
  stop)
    down
    ;;
  reset)
    down
    purge
    build
    start
    tail
    ;;
  purge)
    down
    purge
    ;;
  tail)
    tail
    ;;
  tail_db)
    tail_db
    ;;
  tail_server)
    tail_server
    ;;
  tail_all)
    tail_all
    ;;
  cli_db)
    cli_db
    ;;
  cli_server)
    cli_server
    ;;
  start_embed)
    embed
    ;;
  sonarqube)
    sonarqube
    ;;
  sonarpurge)
    sonarpurge
    ;;
  *)
    echo "Usage: $0 {test|build|build_start|start|stop|reset|purge|tail|tail_db|tail_server|tail_all|cli_db|cli_server|start_embed|sonarqube|sonarpurge}"
esac
