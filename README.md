## UNIMEET?

**University Meetup**이라는 뜻으로, 대학생들이 다양한 인연을 맺고 편안한 만남을 즐길 수 있는 커뮤니티 모바일 웹 서비스입니다.

### [FRONTEND GITHUB](https://github.com/SKHU-Gevelopers/Frontend_Repo)

### [API docs](https://documenter.getpostman.com/view/16786212/2s93eZwWJv)

## 저희 서비스에 오시면...

- 마이페이지를 통한 개인정보 작성 
- 게시글을 올려 만남 신청 주고받기
- 댓글, 쪽지, 방명록 등으로 소통

## 사용 기술

- Spring Boot 2.7.11
- JDK 11
- MariaDB, Spring Data JPA

- 서비스 실행 환경은 AWS EC2(App Server), RDS(DB) 환경에서 가동 중입니다.

## directory tree (src/main/java/site/unimeet/unimeetbackend)

- /api: 프레젠테이션 레이어입니다. 사용자의 요청/응답과 직접적으로 관련된 컨트롤러와 DTO를 포함합니다. 하위 디렉토리는 일반적으로 컨트롤러, dto를 포함합니다. 컨트롤러는 요청을 처리하는 핸들러 메서드를 포함합니다. 
  -   /auth: 인증 관련 요청을 처리합니다.
  -   /student: 회원 혹은 회원간 소통에 관련한 요청을 처리합니다.
  -  /common: "/api" 디렉토리 내에서 공통적으로 사용되는 공통 HTTP 응답 템플릿이나 페이지네이션 정보들을 포함하고 있습니다.
  -  /post, /comment: 게시글과 만남신청, 댓글 관련 요청을 처리합니다.

-   /domain: 주로 비즈니스 레이어를 담당하며, 프로젝트를 구성하는 핵심 요소들을 포함하고 있습니다. 영속성 레이어 또한 포함하고 있습니다.
    -   /common: "/domain" 디렉토리 내에서 공통적으로 사용되는 요소들을 포함하고 있습니다.
    -   /meetup, /post, /comment: 게시글과 만남신청, 댓글 관련 요소를 포함합니다.
    -   /auth, /jwt: jwt 발행과 검증을 담당하는 유틸리티성 클래스와 해당 클래스를 호출하는 인증 관련 요소를 포함합니다.
    -   /student: 회원과 회원 간 소통에 관련한 요소를 포함합니다.

-   /global: 프로젝트 전체에 영향을 주는 기능들입니다
    -   /config: 클라우드, HTTP 응답 등 프로젝트에 전역적으로 영향을 미치는 요소. 
    -   /converter: 사용자 입력값 변환에 도움을 주는 Converter입니다. enum 객체 바인딩에 대소문자 모두를 허용하는 등의 역할을 합니다.
    -   /exception: 전역 예외 처리, 커스텀 예외 클래스, 일관된 에러 응답을 제공하기 위한 Error Response Template 등을 포함합니다.