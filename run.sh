#!/bin/bash

export COMPOSE_FILE_PATH="${PWD}/target/docker/docker-compose.yml"

if [ -z "${M2_HOME}" ]; then
  export MVN_EXEC="${PWD}/mvnw"
else
  export MVN_EXEC="${M2_HOME}/bin/mvn"
fi

test() {
  $MVN_EXEC clean test
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
  *)
    echo "Usage: $0 {test|build|build_start|start|stop|reset|purge|tail|tail_db|tail_server|tail_all|cli_db|cli_server}"
esac
