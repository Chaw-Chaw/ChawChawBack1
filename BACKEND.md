> ChawChaw : 언어교환 채팅앱

# ChawChaw Back-End

## 개발 환경

- Spring Boot 2.5.2
- Mysql 8.0.23
- Java 11

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
- s3 파일 서버에 파일 저장, cloudFront를 이용한 이미지 캐싱 적용

## Entity

![entity](https://user-images.githubusercontent.com/50051656/140650766-1a65d3a1-4a21-4635-87da-8cd9c91a0a78.png)