# pretest-leesangyub
프로젝트 : 이상엽 프리테스트 

## 1. 사용한 외부 라이브러리 

### (1) jasypt 
주요정보(패스워드) 암호화
implementation 'com.github.ulisesbocchio:jasypt-spring-boot-starter:3.0.5'

### (2) jwt
jwt 토큰
implementation 'io.jsonwebtoken:jjwt-api:0.11.5'
runtimeOnly 'io.jsonwebtoken:jjwt-impl:0.11.5'
runtimeOnly 'io.jsonwebtoken:jjwt-jackson:0.11.5'

### (3) swagger
REST API 연동 IF명세 및 테스트 
implementation 'org.springdoc:springdoc-openapi-starter-webmvc-ui:2.2.0'

### (4) queryDSL | p6spy
QueryDSL 및 SQL 쿼리 확인 
implementation 'com.github.gavlyukovskiy:p6spy-spring-boot-starter:1.9.1'
implementation 'com.querydsl:querydsl-jpa:5.0.0:jakarta'
annotationProcessor "com.querydsl:querydsl-apt:5.0.0:jakarta"
annotationProcessor "jakarta.annotation:jakarta.annotation-api"
annotationProcessor "jakarta.persistence:jakarta.persistence-api"

### (5) vavr 
자바 함수형 프로그래밍 | tuple 자료형 
implementation 'io.vavr:vavr:0.10.4'

### (6) redis
레디스 연동 
implementation 'org.springframework.boot:spring-boot-starter-data-redis'
implementation 'redis.clients:jedis:4.4.8'

### (7) 기타 
lombok, mariadb-java-client 등 


## 2. 도커 실행 방법 
github에서 다운로드 후 docker 디렉토리로 이동 

### (1) 사전 조건 
로컬에 docker가 설치되어 있어야 합니다.

### (2) 포트의 사용가능을 확인
레디스 : 포트 6397 ( 패스워드 redis1357! )
MariaDB : 포트 3306 (DB명 dev 계정 user/ user1357! )
어플리케이션 리슨 포트 : 9090

### (3) docker compose 실행
* docker 디렉토리로 이동 
  - cd C:\pretest-leesangyub-main\docker

* docker compose 실행
  - docker compose -f ./docker-compose.yml up 

### (4) swagger 를 통한 IF 확인 
* http://localhost:9090/swagger-ui/index.html





