#!/bin/sh
# 베이스 이미지에 포함된 http-server를 실행합니다.
# /opt/www 디렉토리의 파일들을 8080 포트로 서비스합니다.
# -s 플래그를 추가하여 Single Page Application(SPA) 라우팅을 지원합니다.
# (페이지 새로고침 시 404 오류가 발생하는 것을 방지)
exec http-server /opt/www -p 8080 -s