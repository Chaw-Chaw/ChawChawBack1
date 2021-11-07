> ChawChaw : 언어교환 채팅앱

## 🗣 프로젝트 소개

ChawChaw는 국내 대학교 유학생들 과 국내 재학생 간의 언어소통을 위한 웹 채팅 어플리케이션 입니다.

국내 웹메일을 지원하는 대학을 대상으로 웹메일을 통한 대학생 신분인증 서비스를 지원합니다.

각 대학교 마다 독립적입 커뮤니티를 통해 실질적으로 거리감이 가까운 사람들끼리 1대 1 채팅을 서비스하며 채팅안에서 이미지 전송 및 언어 번역을 통해 언어를 교환할 수 있습니다.

## ⏰ 제작 기간 & 참여 인원

- 2021년 7월 20일 ~ 2021년 9월 30일
- 팀 프로젝트 : 노두현, 하주현, 김진현

## 🚪 접속 방법

[사이트 접속 🗣](https://chawchaw.vercel.app/)

- 테스트 계정 [ 0 ~ 199 총 200개 ]
  - ID : test0@naver.com ~ test199@naver.com
  - PW : sssssssS1!

- 관리자 계정 [0 ~ 2 총 3개]
  - ID : admin0@chawchaw.kr  ~ admin2@chawchaw.kr
  - PW  : admin123!@#
  
## 🕹 주요기능

<details>
    <summary>테마변경</summary>

- 웹 애플리 케이션 전반에 걸쳐 다크모드, 라이트 모드 설정이 가능합니다.
</details>
<details>
    <summary>회원가입</summary>   

- 일반 회원가입
  * 로그인 페이지 → 일반 회원가입 버튼 → 웹메일 인증 페이지

    ![회원가입-일반1](https://user-images.githubusercontent.com/57996756/137507419-18929cbc-7247-4514-82ed-878616d0d4e5.gif)
  * 웹메일 주소 입력 → 발송하기 버튼 →  웹 메일 유효성 검사 → 웹 메일 인증 번호 발송

    ![회원가입-일반2](https://user-images.githubusercontent.com/57996756/137507433-57dc183a-4357-4363-97c1-e26018ffb974.gif)
  * 인증번호 입력 → 회원 정보 입력 버튼 → 웹 메일 인증 번호 유효성 검사 → 회원가입 정보 입력 페이지

    ![회원가입-일반3](https://user-images.githubusercontent.com/57996756/137507446-804af429-1db0-41ea-9a1e-28009e9451d3.gif)

    ![회원가입-일반4](https://user-images.githubusercontent.com/57996756/137507964-cb3766e7-d81e-4bfc-a609-70a357fa2e32.gif)
  * 이메일 유효성 검사 → 이메일 중복검사 → 비밀번호 유효성 검사 → 그외 정보 입력 → 회원가입버튼 →  회원가입 [GUEST]

    ![회원가입-일반5](https://user-images.githubusercontent.com/57996756/137507451-8ac5d0b3-df2a-45c6-b3de-433fe56ba873.gif)

    ![회원가입-일반6](https://user-images.githubusercontent.com/57996756/137507458-cc992554-58ca-4212-8983-d2323c8be85f.gif)

    ![회원가입-일반7](https://user-images.githubusercontent.com/57996756/137507461-20da9436-07f0-4050-9a2f-b3dfb4badcd1.gif)

  ❗️ [GUSET]는 프로필 작성을 하지 않은 회원으로 채팅기능에 제한이 있습니다.
- 소셜 회원가입
  * 로그인 페이지 → [카카오, 페이스북 버튼] → 소셜 로그인 인증 → 회원가입 여부 확인
  * 비가입 회원인경우 → 회원가입진행 → 웹메일 인증 페에지
  * 웹메일 주소 입력 → 발송하기 버튼 →  웹 메일 유효성 검사 → 웹 메일 인증 번호 발송
  * 회원가입 버튼 → 회원가입 [GUEST]
</details>
<details>
    <summary>로그인</summary>

- 일반 로그인
  * 로그인 페이지 → 이메일, 비밀번호 입력 → 로그인 버튼 → 로그인
  * 학교별  페이지

    ![로그인-일반1](https://user-images.githubusercontent.com/57996756/137508150-915d7d93-4fb4-4974-af82-ee042ffb6cba.gif)
- 소셜 로그인
  * 로그인 페이지 → [카카오, 페이스북 버튼] → 소셜 로그인 인증 → 로그인
  * 학교별 페이지
</details>
<details>
    <summary>헤더</summary>

- 프로필

  ![헤더-프로필창](https://user-images.githubusercontent.com/57996756/137508234-257fa8c4-3716-44c5-93da-af44211ddc2a.gif)
  * 프로필 수정 : 프로필 수정 페이지 이동
  * 채팅창 : 채팅 페이지 이동
  * 설정 : 회원탈퇴 , 소속대학확인, 차단 목록 관리
  * 로그아웃 : 로그아웃 → 로그인 페이지 이동

- 새로운 알림
  * 새로운 메세지
    + [ 프로필 사진, 이름, 보낸 메세지, 보낸 메세지 시간 ] 이 표시 됩니다.
    + 해당 메세지를 클릭하면 채팅방으로 이동합니다.

      ![새로운메세지 - 알림번호 변함](https://user-images.githubusercontent.com/57996756/137508394-7a69dc0e-79d5-4045-b6d4-4c390f269ef1.gif함.gif)

      ![헤더-새로운메세지2](https://user-images.githubusercontent.com/57996756/137508429-1c5b700f-0565-42c9-b0f7-9a1ce3aae75e.gif)

      ![헤더-새로운메세지4](https://user-images.githubusercontent.com/57996756/137508448-1cbbe794-c963-4e74-bea8-f0d721d2716a.gif)

  * 좋아요 메세지
    + [ 프로필 사진, 이름, 좋아요 , 좋아요취소 , 클릭 시간 ] 이 표시 됩니다.
</details>
<details>
    <summary>프로필 작성</summary>

![프로필1](https://user-images.githubusercontent.com/57996756/137509319-a15cd315-f743-453e-a9d4-ec24dbe53665.gif)

- 이미지 수정
  * 이미지 버튼 → 5MB 이하 이미지 선택 → 이미지 업로드 완료

    ![프로필2](https://user-images.githubusercontent.com/57996756/137508526-6761e07c-19e9-4a50-ae27-de5e55893a02.gif)
- 이미지 삭제
  * 이미지 삭제버튼 → 이미지 삭제 완료

    ![프로필3](https://user-images.githubusercontent.com/57996756/137508541-5112376d-0f8a-4745-984c-1624eeb7e4d4.gif)
- 자기소개 수정
  * 코멘트 수정 버튼 → 코멘트 수정 → 업데이트 버튼

    ![프로필4](https://user-images.githubusercontent.com/57996756/137508546-7dca0886-49af-4a24-9063-b30e13bdae71.gif)
- 국적, 할수있는 언어, 배우고 싶은언어 추가
  * '+' 버튼 → dropbox 버튼 → select 박스에서 원하는 메뉴 선택'+'
  * '-' 버튼 → 이전 선택 박스 삭제

  ❗️ 국적은 최대 2개, 언어는 최대 4개 선택가능 합니다

  ![프로필5](https://user-images.githubusercontent.com/57996756/137509741-ba5b5852-a49f-42ab-bfd2-b8d35ca72b94.gif)
- SNS 주소 수정
  1. [facebook, instagram] 주소 수정 버튼 → 수정 → 업데이트 버튼

     ![프로필6](https://user-images.githubusercontent.com/57996756/137508567-8fb6f173-fd6a-448b-b346-768df094b813.gif)
- 프로필 업로드 버튼 → 프로필

  ![프로필7](https://user-images.githubusercontent.com/57996756/137508584-e760b057-1853-4eb6-a398-1d915b5709be.gif)

  ❗️ 프로필 업로드시 이미지, SNS 주소를 제외하고 국적, 언어, 희망언어 모두 하나이상 선택해야 업로드가 가능합니다.

  ![프로필8](https://user-images.githubusercontent.com/57996756/137509838-31d82bf0-cac8-4ddd-aede-08d94043ef63.gif)
</details>
<details>
    <summary>포스팅 검색</summary>

프로필 업로드를 하면 학교별 포스팅 페이지에 포스팅이 올라갑니다. 자기 자신의 포스팅은 볼 수 없습니다. 그리고 자신의 학교에 속한 학생들의 포스팅만 볼 수 있습니다. 포스팅은 자신의 프로필을 기반으로 올라갑니다.
- 검색창에 검색하고 싶은 이름 입력 [이름에 해당 입력한 단어가 들어가면 해당 유저의 포스팅이 검색됩니다.]

  ![포스팅1](https://user-images.githubusercontent.com/57996756/137510332-4e125f68-7e06-4291-babb-5a9696689dcd.gif)
- 모국어, 할수 있는 언어, 국적 선택

  ![포스팅2](https://user-images.githubusercontent.com/57996756/137510861-c76e5830-e352-455f-b044-a7a151e6958f.gif)
- Search 버튼 → 포스팅 검색 화면

  ![포스팅3](https://user-images.githubusercontent.com/57996756/137510343-edc96fa2-ba6a-4fe7-bd92-dafb711eda47.gif)

  ❗️자신이 차단한 유저는 검색되지 않습니다.

  ![포스팅4](https://user-images.githubusercontent.com/57996756/137510347-991d8076-bd2a-41b3-83c8-3d35105cef84.gif)
</details>
<details>
    <summary>포스팅 카드</summary>

검색화면에서 보이는 포스팅 카드에는 해당유저의 [ 프로필 사진, 이름, 대표 국적, 대표 언어, 대표 희망 언어 , 간략한 자기소개, 포스팅 좋아요, 포스팅이 올라간 후 시간, 포스팅 카드 조회수 ] 가 표시됩니다.

![포스트모달1](https://user-images.githubusercontent.com/57996756/137511957-ddaa8dd7-b5ff-46d5-a24a-5bb3f48e311f.gif)

- 해당 유저의 포스팅 카드를 클릭하면 포스트 카드 모달이 뜨며 자세한 해당 유저의 자세한 프로필을 확인 할수 있습니다.

  ![포스트모달5](https://user-images.githubusercontent.com/57996756/137511455-8b9c30ef-7ffd-48f2-9971-dd0f82e9e8cd.gif)
- 포스트 카드 모달에는  [ 프로필 사진, 이름, 국적들, 할 수 있는 언어들, 희망 언어들 , 자기소개 전체, 포스팅 좋아요, 포스팅이 올라간 후 시간, 포스팅 카드 조회수, sns 주소, 좋아요 버튼, 차단 버튼, 채팅버튼 ]게시됩니다.

- 좋아요

  ![포스트모달2](https://user-images.githubusercontent.com/57996756/137511439-85dedcb5-b530-4b51-808f-a54f39d16711.gif)
  * 좋아요 버튼 :  해당 사용자가 좋아요 표시가 되며 해당 사용자의 좋아요 갯수가 1 올라갑니다. → 좋아요 취소 버튼 변환
  * 좋아요 취소 버튼 : 좋아요 표시가 취소되며 해당 사용자의 좋아요 갯수가 1 내려갑니다. → 좋아요 버튼 변환
- 차단

  ![포스트모달3](https://user-images.githubusercontent.com/57996756/137511446-0dac14cc-a6be-4be4-922b-2bfa1e354152.gif)

  ![포스트모달4](https://user-images.githubusercontent.com/57996756/137511452-684c6417-057b-4905-9bff-a7827f9b472c.gif)
  * 차단 버튼
    + 해당 사용자가 보내는 메세지가 차단
    + 사용자와 차단 대상 모두 서로에게 채팅을 걸수 없음
    + 차단 상태에서 좋아요 취소는 가능하나 좋아요는 할 수 없음
    + 설정 페이지에서 차단목록 관리 가능
  * 차단 해제 버튼
    + 차단 상태의 모든 기능이 다시 정상화
- TryChat 버튼

  ![포스트모달-tryChat](https://user-images.githubusercontent.com/57996756/137511429-0e740858-c8a7-42eb-87c7-5b5e8bf1f868.gif)
  * 채팅방 페이지로 이동
  * 해당 사용자와의 채팅방 생성 → 채팅방 목록 추가
  * 해당 사용자와의 채팅방이 메인채팅방 → 메세지 입력창 활성화
</details>
<details>
    <summary>채팅</summary>

- 메인 채팅방
  * 헤더
    + 홈버튼 : 학교별 포스트 검색 페이지 이동

    ![채팅-홈](https://user-images.githubusercontent.com/57996756/137512228-b26c74fd-7374-4235-bf82-9e107a677aa6.gif)
  * 채팅방목록  버튼 [모바일 화면] : 채팅방 목록

    ![채팅-채팅목록](https://user-images.githubusercontent.com/57996756/137512226-bd60a8d2-df61-4c45-b7bd-2ffbbeb81b1d.gif)

    + 나가기 버튼 : 채팅방에서 나가기

    ![채팅-나가기](https://user-images.githubusercontent.com/57996756/137512447-d917a73e-44ed-432f-8520-3ea3cdab537e.gif)
  * 메세지 입력
    + 일반 텍스트 : 일반 텍스트 입력후 'enter' 혹은 보내기 버튼을 누르면 메세지 전송 → 메인 채팅방에 메시지

      ![채팅1](https://user-images.githubusercontent.com/57996756/137512233-9ef4d11a-ff2a-429d-8763-320dfc90bfe8.gif)
    + 이미지 보내기 : 이미지 버튼 누르고 이미지 선택 [5MB 이하 파일] →  이미지 전송 → 메인 채팅방에 이미지 메시지 띄우기

      ![채팅2](https://user-images.githubusercontent.com/57996756/137512235-5e05b398-8f9d-4200-ab6f-daa2e872ae3d.gif)
    + 메세지 번역 : 헤더에서 번역이 될 언어를 선택 → 번역을 원하는 메시지 박스 클릭 → 번역 버튼 생성 → 번역 버튼 클릭 → 메세지 박스의 언어가 헤더에 선택한 언어로 번역

      ![채팅3](https://user-images.githubusercontent.com/57996756/137512238-50db2750-0206-476f-bb98-9f5b8fbbec69.gif)

      ![채팅4](https://user-images.githubusercontent.com/57996756/137512241-a39c0a1a-7fb0-43a2-a460-30b287630ac9.gif)
    + 프로필 카드 : 상대방의 프로필 사진 클릭 → 간단한 프로필 카드 모달 생성 → 차단하기 버튼

      ![채팅5](https://user-images.githubusercontent.com/57996756/137512245-4d98cebe-69d8-41bb-9da2-d68cdff712dd.gif)

      ![채팅6](https://user-images.githubusercontent.com/57996756/137512246-d3d2ddf3-6d7e-4c66-8c49-1b2414636f1f.gif)
- 채팅 목록

  ![채팅-채팅목록](https://user-images.githubusercontent.com/57996756/137512226-bd60a8d2-df61-4c45-b7bd-2ffbbeb81b1d.gif)
  [ 프로필 사진, 이름, 마지막으로 보낸 메세지, 최근 메세지 시간, 몇개의 새로운 메세지가 왔는지 ] 가 표시 됩니다.

  * 상대방에게 새로운 채팅이 올 경우 : 새로운 채팅목록이 생성됩니다.

    ![채팅-새로운채팅](https://user-images.githubusercontent.com/57996756/137512195-af182622-9841-432d-a37e-878108ecf91a.gif)
  * 채팅페이지 들어올 때 : 해당 채팅방에서 새로운 메시지의 갯수를 표시합니다.

    ![채팅-새로운채팅2](https://user-images.githubusercontent.com/57996756/137512219-8a1cc267-1bbc-44ea-b605-ed2abf45dc9a.gif)
  * 채팅방 입장시 : 채팅방에 들어가면 해당 채팅방의 메세지들은 읽음 표시가 되어 새로운 메세지에서 제외 됩니다.

    ![채팅-새로운채팅입장](https://user-images.githubusercontent.com/57996756/137512222-41c04661-7553-4e8e-b8cf-d229ad3d8b91.gif)
  * 채팅방에서 알림 표시 : 메세지 알림을 제외한 좋아요 알림만 울립니다.

    ![채팅-좋아요알림](https://user-images.githubusercontent.com/57996756/137512224-b5563fc3-b92e-41ab-bec7-ce22c7ace6b3.gif)
    ❗️ 채팅중 상대방이 차단을 할 경우

    + 채팅방은 여전히 존재합니다. 다만 서로 메세지를 보낼수 없습니다.
    + 차단 된 채팅방에서 메세지를 보내면  더이상 메세지가 보내지지 않습니다.
    + 차단 한 이후의 메세지는 기록에 남겨지지 않습니다.
</details>

<details>
    <summary>설정</summary>

- 회원탈퇴 : 버튼을 누르면 ChawChaw 회원탈퇴가 됩니다. →  로그인 페이지

  ![회원삭제](https://user-images.githubusercontent.com/57996756/137512936-0cdbe718-328c-4a4c-abfa-72d66e588b43.gif)
- 소속 대학교 : 웹메일을 인증한 대학교가 표시됩니다. [ 수정 불가 ]
- 차단 목록 관리 : 차단한 유저 리스트가 표시됩니다.
  * 차단 해제 버튼 : 차단한 유저의 차단이 해제됩니다. → 차단 버튼 으로 변경 : 차단을 해제한  유저는 새로고침시 보이지 않습니다.
  * 차단 버튼 : 해당 유저를 차단합니다.  →  차단 해제 버튼으로 변경


</details>

<details>
    <summary>관리자 페이지</summary>

- 회원 정보 검색
- 회원 정보 관리
- 통계
  + 인기 검색 언어 순위
  + 선택 언어 순위
  + 선택 희망 언어 순위

</details>

## 🧩 앞으로 개발할 기능

- [x]  관리자 페이지
- [ ]  다중 채팅방
- [ ]  다중 채팅방 만들기 페이지
- [ ]  다국어 지원 페이지 [ 영어, 중국어, 한국어 ]
- [ ]  음성메세지 기능
- [ ]  동영상 메세지 기능
- [ ]  자동 로그인 확인 기능

## 📋 백엔드 개발 내용

[보러가기](BACKEND.md)