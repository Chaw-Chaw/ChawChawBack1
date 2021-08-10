# ChawChaw Back-End

## Architecture

![architecture](https://user-images.githubusercontent.com/50051656/128911410-553b779b-2834-407e-9f1a-a87a09d0408c.JPG)

- EC2 프리 티어 환경
- RDB는 AWS RDS, Redis는 OS에서 따로 설치
- 깃, 젠킨스를 이용하여 CI/CD 환경 구축
- Nginx 설정을 이용하여 Blue-Green Deployment 적용
- Slack과 연동하여 실시간 로그 확인

## Git Flow

![gitflow](https://user-images.githubusercontent.com/50051656/128914666-719f6643-2070-4d00-ab08-bee09f2c6d33.JPG)


- main
  - prod
- develop
  - release 전 테스팅
- feture-<service>
  - service 별로 기능 구현
- hotfix
  - 상품에 문제가 있는 치명적인 버그 문제 해결