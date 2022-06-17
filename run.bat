@ECHO OFF

SET COMPOSE_FILE_PATH=%CD%\target\docker\docker-compose.yml

IF [%M2_HOME%]==[] (
    SET MVN_EXEC=%CD%\mvnw.cmd
)
IF NOT [%M2_HOME%]==[] (
    SET MVN_EXEC=%M2_HOME%\bin\mvn
)

IF [%1]==[] (
    echo "Usage: %0 {test|build|build_start|start|stop|reset|purge|tail|tail_db|tail_server|tail_all|cli_db|cli_server}"
    GOTO END
)

IF %1==test (
    CALL :test
    GOTO END
)
IF %1==build (
    CALL :build
    GOTO END
)
IF %1==build_start (
    CALL :down
    CALL :build
    CALL :start
    CALL :tail
    GOTO END
)
IF %1==start (
    CALL :start
    CALL :tail
    GOTO END
)
IF %1==stop (
    CALL :down
    GOTO END
)
IF %1==reset (
    CALL :down
    CALL :purge
    CALL :build
    CALL :start
    CALL :tail
    GOTO END
)
IF %1==purge (
    CALL:down
    CALL:purge
    GOTO END
)
IF %1==tail (
    CALL :tail
    GOTO END
)
IF %1==tail_db (
    CALL :build_share
    CALL :start_share
    CALL :tail
    GOTO END
)
IF %1==tail_server (
    CALL :build_acs
    CALL :start_acs
    CALL :tail
    GOTO END
)
IF %1==tail_all (
    CALL :tail_all
    GOTO END
)
IF %1==cli_db (
    CALL :cli_db
    GOTO END
)
IF %1==cli_server (
    CALL :cli_server
    GOTO END
)
echo "Usage: %0 {test|build|build_start|start|stop|reset|purge|tail|tail_db|tail_server|tail_all|cli_db|cli_server}"
:END
EXIT /B %ERRORLEVEL%

:test
  call %MVN_EXEC% clean test
EXIT /B 0

:start
    docker volume create esy_db_volume
    docker-compose -f "%COMPOSE_FILE_PATH%" up --build -d
EXIT /B 0

:down
    if exist "%COMPOSE_FILE_PATH%" (
        docker-compose -f "%COMPOSE_FILE_PATH%" down
    )
EXIT /B 0

:purge
    docker-compose -f "%COMPOSE_FILE_PATH%" kill
    docker-compose -f "%COMPOSE_FILE_PATH%" rm -f
    docker volume rm -f esy_db_volume
EXIT /B 0

:build
	call %MVN_EXEC% clean package -DskipTests
EXIT /B 0

:tail
    docker-compose -f "%COMPOSE_FILE_PATH%" logs -f
EXIT /B 0

:tail_db
    docker-compose -f "%COMPOSE_FILE_PATH%" logs -f esy_db
EXIT /B 0

:tail_server
    docker-compose -f "%COMPOSE_FILE_PATH%" logs -f esy_server
EXIT /B 0

:tail_all
    docker-compose -f "%COMPOSE_FILE_PATH%" logs --tail="all"
EXIT /B 0

:cli_db
    docker exec -it esy-db /bin/sh
EXIT /B 0

:dli_server
    docker exec -it esy-server /bin/sh
EXIT /B 0
