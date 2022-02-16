> ChawChaw : 언어교환 채팅앱

# ChawChaw Back-End

## 개발 환경

- Spring Boot 2.5.2
- Mysql 8.0.23
- Java 11

## 깃 전략

- main : 기준 브랜치이며, 제품을 배포
- develop : 개발 브랜치, 기능 단위로 개발 후 Merge
- feature : 기능 단위로 개발
- hotfix : 배포 브랜치에 문제 시 수정 후 Merge

## 명세서

- [API 명세서](https://www.notion.so/API-62e9756e88b5422682e06d773d51c0f2)
- [상태 코드 명세서](https://dog-particle-e37.notion.site/3aa3c172f4a94897b9fab6faa1af61ba)

## Architecture

![architecture](https://user-images.githubusercontent.com/50051656/140650764-0eb0ec31-1087-4e51-9f9d-adda561f6050.jpeg)

- EC2 프리 티어 환경
- RDB는 AWS RDS, Redis는 AWS Elasticache 이용
- 깃, 젠킨스를 이용하여 CI/CD 환경 구축
- Nginx 설정을 이용하여 Blue-Green Deployment 적용
- Slack과 연동하여 실시간 로그 확인
- S3 파일 서버에 파일 저장, CloudFront를 이용한 이미지 캐싱 적용

## Entity

![entity](https://user-images.githubusercontent.com/50051656/140650766-1a65d3a1-4a21-4635-87da-8cd9c91a0a78.png)

## 구현 내용

### 무중단 배포

- Blue-Green 배포 방식 이용

1. Jenkins에서 새로운 버전 빌드
2. 새로운 버전을 배포하려고 할 때 기존과 다른 포트를 가지는 애플리케이션 구동
3. 정상적으로 구동이 완료된 것을 확인한 후에 Nginx 설정 파일에서 포트 변경과 리로드 후에 이전 버전의 연결을 해제

```shell
serviceA=$(sudo netstat -nltp | grep 8080 | awk '{print $7}' | awk -F '/' '{print $1}')
serviceB=$(sudo netstat -nltp | grep 8082 | awk '{print $7}' | awk -F '/' '{print $1}')

if [ "$serviceA" = "" ]; then
        echo "8082 포트가 존재하여 8080 포트 실행"
        sudo nohup /usr/lib/jvm/java-11-openjdk-amd64/bin/java -jar -Dserver.port=8080 -Djasypt.encryptor.password="" chawchaw.jar --spring.profiles.active=prod &
else
        echo "8080 포트가 존재하여 8082 포트 실행"
        sudo nohup /usr/lib/jvm/java-11-openjdk-amd64/bin/java -jar -Dserver.port=8082 -Djasypt.encryptor.password="" chawchaw.jar --spring.profiles.active=prod &
fi

echo "서비스 구동 대기"

while [ 1 ]
do
        if [ "$serviceA" = "" ]; then
                if [ -n "$(sudo netstat -nltp | grep 8080)" ]; then
                        echo "8080 포트 서비스 ON"
                        break
                fi
        else
                if [ -n "$(sudo netstat -nltp | grep 8082)" ]; then
                        echo "8082 포트 서비스 ON"
                        break
                fi
        fi
        sleep 1s
done

echo "경로 설정"
if [ "$serviceA" = "" ]; then
        echo "8082 -> 8080 경로 변경"
        echo "set \$service_url http://127.0.0.1:8080;" | sudo tee /etc/nginx/conf.d/service-url.inc
else
        echo "8080 -> 8082 경로 변경"
        echo "set \$service_url http://127.0.0.1:8082;" | sudo tee /etc/nginx/conf.d/service-url.inc
fi

sudo service nginx reload

if [ "$serviceA" = "" ]; then
        echo "8082 포트 프로세스 죽이기"
        sudo kill -9 $serviceB
else
        echo "8080 포트 프로세스 죽이기"
        sudo kill -9 $serviceA
fi
```

### S3

- 사용자 프로필 이미지 저장을 위해 클라우드 파일 스토리지 사용

### CloudFront

- 사용자의 프로필이 자주 바뀌지 않을 것을 생각하여 캐시 적용
- 오리진이 아닌 엣지 서버까지와의 통신으로 인한 응답 속도 상승과 통신 비용 부담 ↓

### JWT

- 로그인 시에 30분 유효기간을 가지는 액세스 토큰과 일주일 유효기간을 가지는 리프레쉬 토큰 발급
- 액세스 토큰 만료 시에 리프레쉬 토큰으로 밸리데이션 후에 액세스 토큰 재발급
- 리프레쉬 토큰은 RDB에 저장하고 만약 탈취당한 사실을 알았을 시에 값을 변경하여 액세스 토큰 재발급을 막음으로써 보안 ↑

### 중복 로그인

- 다른 곳에서 로그인했을 시에 기존 로그인을 로그인 해제가 되도록 구현
- 로그인 시 RefreshToken 여부를 통해 동시 접속 판단
  - STOMP 양방향 통신으로 /ws/queue/login/{userId} 구독자에게 중복 로그인 전달 

### HTTP Status Code

- 기존 HTTP 상태 코드로는 각 상황에 맞는 상황 코드를 전달하기 어렵다고 생각하여 따로 상태 코드와 메시지를 담아서 보내기로 프론트와 맞춰 구현
- 기본적으로 응답 메시지 시작 줄 상태 코드는 200대는 성공, 400, 500대는 실패로 간주하도록 통일
- [상태 코드 명세서](https://dog-particle-e37.notion.site/3aa3c172f4a94897b9fab6faa1af61ba)

### Caching

- 자주 조회되고 실시간성을 크게 중요하지 않은 부분을 캐싱
  - 인기 검색 언어 등 순위
  - 사용자 프로필 카드

<img width="664" alt="cache data" src="https://user-images.githubusercontent.com/50051656/154266796-b28af5a4-fe57-40f9-b809-371f69c892b4.png">

### 조회 수 처리

- 매번 유저 카드를 조회할 때마다 update 쿼리가 발생되면 성능에 좋지 않을 거로 생각하였음
- 조회 수를 처음 조회할 때 select 쿼리를 날려 현재 값을 저장하고 redis에서 값을 최신화시킴
- 스케줄러를 이용해서 정해진 시간(02:00)에 RDB에 변경 값을 반경

```java
@Scheduled(cron = "0 0 2 * * *")
public void updateUserViews() {
    log.info("조회수 동기화 시작");
    userService.updateViews();
    log.info("조회수 동기화 종료");
}
```

### 채팅

- STOMP 양방향 네트워크 프로토콜 이용
- 메시지 전송 시 브로커(Broker)에 전달 후 구독 중인 유저에게 메시지 전달하는 방식
- 차단한 또는 차단된 유저, 읽음 로직 처리 후 전달
- pub(메시지 송신) : /ws/message
- sub(메시지 수신) : /ws/queue/chat/{userId}

### 알림

- 실시간 알림
  - STOMP 를 통해 실시간으로 데이터를 받음
  - sub : /ws/queue/chat/{userId}
  - sub : /ws/queue/like/{userId}
- 로그인 시 알림
  - API 요청으로 인해 전달받음

### 읽음처리

- 메시지에 isRead 변수를 통해서 읽었는지 아닌지 저장
- 소켓 연결 시 Redis에 ws::{userEmail} : {roomId} 저장하고 바라보는 방이 변경될 때마다 roomId 최신화
- 읽음처리 되는 경우
  - 상대방이 소켓에 연결되어있고 roomId가 일치한다면 (수신된 메시지는 바로 읽음 처리)
  - 내가 방을 옮길 때 (해당 방 메시지 읽음 처리)

