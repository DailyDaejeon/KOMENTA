# Komenta

[TOC]



## 팀원 소개

- 테크리더 : 배상웅

- 기획 : 정혜림

- QA : 김채린, 박수아

- Git Master : 고재석



## 주제

- 프로젝트명 : Komenta

  > 독일어 'Kommentar'에서 파생. (Commentary)

- VOD 영상 플랫폼에서 영상 재생 시간별로 댓글과 연동하여 사용자에게 더 큰 재미를 주는 서비스를 제공한다.



## 기능

1. 회원 관리

2. 로그인 관리

3. 어드민 기능

4. VOD 관리

5. 화면 구성



## 개발문서

https://docs.google.com/spreadsheets/d/1f44yO4aeAmSMDSMzdjwmA0yPJ5i3DKCw0QpL_Lu572M/edit#gid=0



## Gantt Chart

```mermaid
gantt
    dateFormat  MM-DD
    title       Komenta 진행상황
    %% excludes    weekends
    %% (`excludes` accepts specific dates in YYYY-MM-DD format, days of the week ("sunday") or "weekends", but not the word "weekdays".)

    section 프로젝트 준비
    주제 선정				  :done, 01-11, 01-14
    기능 목록 상세 도출			:done, 01-14, 3d
    화면 기획				  :done, 01-14, 3d
    개발 환경 구성             :done, 01-12, 2d
    ERD                     : 01-18, 2d

    section Auth server
    MariaDB 구축 및 백업구현     	 :01-19, 2d
    User CRUD 구현 		   :01-22, 3d
    User SNS Login API          :01-25, 2d
    User Session(JWT 인증)       :01-26, 1-27
    User PW, 이메일 인증        :01,28, 2d

    %%section 기타
```

## Stack

<img src="https://img.shields.io/badge/Spring-success?style=flat-square&logo=Spring&logoColor=white"/></a>
<img src="https://img.shields.io/badge/IntelliJ-9cf?style=flat-square&logo=intelliJ IDEA&logoColor=white"/></a>
<img src="https://img.shields.io/badge/Gradle-inactive?style=flat-square&logo=Gradle&logoColor=white"/></a>

<img src="https://img.shields.io/badge/Vue.js-green?style=flat-square&logo=Vue.js&logoColor=white"/></a>
<img src="https://img.shields.io/badge/VSCode-informational?style=flat-square&logo=Visual Studio Code&logoColor=white"/></a>

<img src="https://img.shields.io/badge/MariaDB-informational?style=flat-square&logo=MariaDB&logoColor=white"/></a>
<img src="https://img.shields.io/badge/Docker-informational?style=flat-square&logo=Docker&logoColor=white"/></a>



## Code Style



## Git Rule

### branch

`master -> develop -> feature/FE/{기능이름}-{상세기능}(김채린, 박수아, 정혜림) or feature/BE/{기능이름}-{상세기능}(고재석, 배상웅)`

### merge

``

### commit

``



## Prototype

### 1-1. About-page(1)

![image-20210120205153376](README.assets/image-20210120205153376.png)



### 1-2.About-page(2)

![image-20210120205257024](README.assets/image-20210120205257024.png)



### 1-3. About-page(3)

![image-20210120205321076](README.assets/image-20210120205321076.png)





### 2-1.Main-page(1) 

![image-20210120205407289](README.assets/image-20210120205407289.png)



### 2-2.Main-page(2)

![image-20210120205744348](README.assets/image-20210120205744348.png)



### 3.My Streaming List

![image-20210120205837584](README.assets/image-20210120205837584.png)



### 4.My Streaming List 상세 페이지

![image-20210120205858350](README.assets/image-20210120205858350.png)



### 5.VOD

![image-20210120205930506](README.assets/image-20210120205930506.png)