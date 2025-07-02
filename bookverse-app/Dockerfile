# 베이스 이미지로 nodejs-http-server를 사용합니다.
FROM webratio/nodejs-http-server

# 로컬에서 빌드된 'build' 폴더를 이미지 안의 웹 서버 루트(/opt/www)로 복사합니다.
ADD build /opt/www

# 컨테이너 실행 시 사용할 스크립트를 복사하고 실행 권한을 부여합니다.
ADD run.sh /opt/run.sh
RUN chmod +x /opt/run.sh

# 환경변수를 설정합니다. 이 변수는 컨테이너 실행 시점에 사용될 수 있습니다.
# (이미 빌드된 파일에는 영향을 주지 않습니다.)
ARG REACT_APP_API_HOST
ENV REACT_APP_API_HOST=$REACT_APP_API_HOST

# 타임존을 서울로 설정합니다.
ENV TZ=Asia/Seoul
RUN ln -snf /usr/share/zoneinfo/$TZ /etc/localtime && echo $TZ > /etc/timezone

# http-server가 사용할 포트를 노출합니다.
EXPOSE 8080

# 컨테이너가 시작될 때 실행할 명령어를 지정합니다.
ENTRYPOINT ["sh", "/opt/run.sh"]