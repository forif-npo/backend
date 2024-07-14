#!/bin/bash
PROJECT_NAME="github_action"
DEPLOY_PATH=/home/ubuntu/$PROJECT_NAME/
DEPLOY_LOG_PATH="/home/ubuntu/$PROJECT_NAME/deploy.log"
DEPLOY_ERR_LOG_PATH="/home/ubuntu/$PROJECT_NAME/deploy_err.log"
APPLICATION_LOG_PATH="/home/ubuntu/$PROJECT_NAME/application.log"
JAR_NAME=$(basename $(ls $DEPLOY_PATH/*.jar))

echo "> DEPLOY_JAR 배포" >> $DEPLOY_LOG_PATH
nohup java -jar -Dspring.profiles.active=local $DEPLOY_PATH$JAR_NAME --server.port=8080 >> $APPLICATION_LOG_PATH 2> $DEPLOY_ERR_LOG_PATH &

sleep 3

echo "> 배포 종료 : $(date +%c)" >> $DEPLOY_LOG_PATH
