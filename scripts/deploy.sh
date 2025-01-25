#!/bin/bash
# SSM에서 환경변수 가져오기
echo "> SSM에서 환경변수 로드 중..." >> $DEPLOY_LOG_PATH

RDS_URL=$(aws ssm get-parameter --name "/config/forif/RDS_URL" --query "Parameter.Value" --output text)
RDS_USERNAME=$(aws ssm get-parameter --name "/config/forif/RDS_USERNAME" --query "Parameter.Value" --output text)
RDS_PASSWORD=$(aws ssm get-parameter --name "/config/forif/RDS_PASSWORD" --with-decryption --query "Parameter.Value" --output text)
JWT_SECRET=$(aws ssm get-parameter --name "/config/forif/JWT_SECRET" --query "Parameter.Value" --output text)

# 환경변수 유효성 검증
if [ -z "$RDS_URL" ] || [ -z "$RDS_USERNAME" ] || [ -z "$RDS_PASSWORD" ]; then
  echo "> ERROR: SSM 파라미터를 가져오지 못했습니다." >> $DEPLOY_ERR_LOG_PATH
  exit 1
fi

echo "> RDS_URL: $RDS_URL" >> $DEPLOY_LOG_PATH
echo "> RDS_USERNAME: $RDS_USERNAME" >> $DEPLOY_LOG_PATH
# RDS_PASSWORD는 보안을 위해 출력하지 않음

PROJECT_NAME="github_action"
JAR_PATH="/home/ubuntu/github_action/build/libs/*.jar"
DEPLOY_PATH=/home/ubuntu/$PROJECT_NAME/
DEPLOY_LOG_PATH="/home/ubuntu/$PROJECT_NAME/deploy.log"
DEPLOY_ERR_LOG_PATH="/home/ubuntu/$PROJECT_NAME/deploy_err.log"
APPLICATION_LOG_PATH="/home/ubuntu/$PROJECT_NAME/application.log"
BUILD_JAR=$(ls $JAR_PATH)
JAR_NAME=$(basename $BUILD_JAR)

echo "===== 배포 시작 : $(date +%c) =====" >> $DEPLOY_LOG_PATH

if [ -z "$BUILD_JAR" ]; then
  echo "> ERROR: build 파일을 찾을 수 없습니다." >> $DEPLOY_LOG_PATH
  exit 1
fi

echo "> build 파일명: $JAR_NAME" >> $DEPLOY_LOG_PATH
echo "> build 파일 복사" >> $DEPLOY_LOG_PATH
cp $BUILD_JAR $DEPLOY_PATH

echo "> 현재 동작중인 어플리케이션 pid 체크" >> $DEPLOY_LOG_PATH
CURRENT_PID=$(pgrep -f $JAR_NAME)

if [ -z "$CURRENT_PID" ]; then
  echo "> 현재 동작중인 어플리케이션 존재 X" >> $DEPLOY_LOG_PATH
else
  echo "> 현재 동작중인 어플리케이션 존재 O" >> $DEPLOY_LOG_PATH
  echo "> 현재 동작중인 어플리케이션 강제 종료 진행" >> $DEPLOY_LOG_PATH
  echo "> kill -9 $CURRENT_PID" >> $DEPLOY_LOG_PATH
  kill -9 $CURRENT_PID
fi

DEPLOY_JAR=$DEPLOY_PATH$JAR_NAME
echo "> DEPLOY_JAR 배포" >> $DEPLOY_LOG_PATH

# 수정된 java -jar 실행 명령
nohup java -jar \
  -Dspring.profiles.active=local \
  -Dspring.datasource.url=$RDS_URL \
  -Dspring.datasource.username=$RDS_USERNAME \
  -Dspring.datasource.password=$RDS_PASSWORD \
  -Dspring.jwt.secret=$JWT_SECRET \
  $DEPLOY_JAR --server.port=8080 >> $APPLICATION_LOG_PATH 2>> $DEPLOY_ERR_LOG_PATH &

# 백그라운드 실행이 완료될 때까지 잠시 대기
sleep 3

# 프로세스가 실행되었는지 확인
NEW_PID=$(pgrep -f $JAR_NAME)

if [ -z "$NEW_PID" ]; then
  echo "> ERROR: 어플리케이션이 시작되지 않았습니다." >> $DEPLOY_LOG_PATH
else
  echo "> 어플리케이션이 성공적으로 시작되었습니다. PID: $NEW_PID" >> $DEPLOY_LOG_PATH
fi
echo "> 배포 종료 : $(date +%c)" >> $DEPLOY_LOG_PATH
