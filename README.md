# 방명록 백엔드 서버

이 프로젝트는 Next.js 포트폴리오 사이트의 방명록 기능을 위한 백엔드 서버입니다.

## 기술 스택

- Java 21
- Spring Boot 3.4.3
- Spring Security
- Spring Data JPA
- OAuth2 Client
- H2 Database (개발용)
- MySQL (운영용)

## 기능

- 소셜 로그인 (Google, Kakao, Naver)
- 방명록 CRUD 기능
- 사용자 별 방명록 관리

## 프로젝트 구조

```
src/main/java/com/community/communitybackend
├── CommunityBackendApplication.java
├── config
│   ├── SecurityConfig.java
│   └── auth
│       ├── CustomOAuth2UserService.java
│       └── OAuthAttributes.java
├── controller
│   ├── GuestbookController.java
│   └── UserController.java
├── domain
│   ├── guestbook
│   │   └── Guestbook.java
│   └── user
│       ├── Role.java
│       └── User.java
├── dto
│   ├── GuestbookRequestDto.java
│   ├── GuestbookResponseDto.java
│   └── UserResponseDto.java
├── repository
│   ├── GuestbookRepository.java
│   └── UserRepository.java
└── service
    ├── GuestbookService.java
    └── UserService.java
```

## API 엔드포인트

### 사용자 API

- `GET /api/user/me`: 현재 로그인한 사용자 정보 조회

### 방명록 API

- `GET /api/guestbook/all`: 모든 방명록 조회
- `GET /api/guestbook/me`: 내가 작성한 방명록 조회
- `POST /api/guestbook`: 방명록 작성
- `PUT /api/guestbook/{id}`: 방명록 수정
- `DELETE /api/guestbook/{id}`: 방명록 삭제

## 실행 방법

### 개발 환경

1. Java 21과 Gradle이 설치되어 있어야 합니다.
2. 소셜 로그인 설정을 위해 application.yml 파일을 수정합니다.
3. 다음 명령어로 애플리케이션을 실행합니다:

```bash
./gradlew bootRun
```

### 소셜 로그인 설정

카카오, 구글, 네이버 로그인을 사용하기 위해서는 각 플랫폼에서 애플리케이션을 등록하고, 발급받은 클라이언트 ID와 시크릿을 application.yml 파일에 설정해야 합니다.

#### 카카오 로그인 설정

1. [카카오 개발자 사이트](https://developers.kakao.com/)에서 애플리케이션 등록
2. Redirect URI 설정: `http://localhost:8080/login/oauth2/code/kakao`
3. 발급받은 클라이언트 ID와 시크릿을 application.yml에 설정

#### 구글 로그인 설정

1. [Google Cloud Console](https://console.cloud.google.com/)에서 OAuth 클라이언트 ID 생성
2. Redirect URI 설정: `http://localhost:8080/login/oauth2/code/google`
3. 발급받은 클라이언트 ID와 시크릿을 application.yml에 설정

#### 네이버 로그인 설정

1. [네이버 개발자 센터](https://developers.naver.com/)에서 애플리케이션 등록
2. Redirect URI 설정: `http://localhost:8080/login/oauth2/code/naver`
3. 발급받은 클라이언트 ID와 시크릿을 application.yml에 설정

## 프론트엔드 연동

프론트엔드에서는 다음 URL로 API 요청을 보내도록 설정해야 합니다:

```
http://localhost:8080/api/guestbook/all  # 방명록 목록 조회
http://localhost:8080/api/guestbook      # 방명록 작성
```

또한, 소셜 로그인 인증 정보가 쿠키에 저장되어 API 요청과 함께 전송될 수 있도록 fetch 요청 시 `credentials: 'include'` 옵션을 추가해야 합니다.
