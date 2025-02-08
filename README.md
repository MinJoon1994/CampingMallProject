# CAMPING MALL PROJECT

캠핑 용품을 판매하는 온라인 쇼핑몰 프로젝트입니다.

# 기술 스택

프론트엔드: HTML, CSS, JavaScript
백엔드: Java (Spring Framework)
데이터베이스: Maria DB

# 설치 및 실행 방법

1. 레포지토리 클론
  -  git clone https://github.com/MinJoon1994/CampingMallProject.git
     cd CampingMallProject

2. 의존성 설치
  - ./gradlew build

3. DB 설정
  - Maria DB에 campingmall DB생성
  - src/main/resources/application.properties 파일에서 데이터베이스 연결 설정을 수정
  - cd 백업DB파일 있는곳 주소
  - mysql -uroot -p1111 [데이터베이스 이름] < [백업데이터베이스 이름]

5. 이미지 업로드 공간 세팅
  - C:\upload\campingmall 이미지 업로드하는 폴더 만들기
  - 폴더안에 이미지 백업 폴더 내용 넣어주기

4. 애플리케이션 실행:
  - ./gradlew bootrun 실행

5. 접속
  - 브라우저에서 http://localhost:8080 으로 접속합니다.

#문의
질문이나 문의 사항이 있으시면 [aladin1994@naver.com]로 연락주세요.



