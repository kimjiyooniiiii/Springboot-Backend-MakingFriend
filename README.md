# RUDoori?
### Knu capstone project
## 캡스톤 프로젝트

<br>

## 규칙
- 1 작업 1 커밋
- 각각의 수정된 코드 주석 작성 및 수정 일자, 수정자 이름 작성
- 작업 후 스프레드 시트 수정 필수

## commit 메시지 참고사항
- FEAT : 새로운 기능의 추가
- FIX: 버그 수정
- DOCS: 문서 수정
- STYLE: 스타일 관련 기능(코드 포맷팅, 세미콜론 누락, 코드 자체의 변경이 없는 경우)
- REFACTOR: 코드 리펙토링
- TEST: 테스트 코트, 리펙토링 테스트 코드 추가
- CHORE: 빌드 업무 수정, 패키지 매니저 수정(ex .gitignore 수정 같은 경우)
  <br>

## :exclamation: 주의사항
- main branch push 금지
- 각자 branch 위치만 사용
- <b>작업 후 공용 스프레드 시트 수정</b>
```
https://docs.google.com/spreadsheets/d/16sM6Ys2sdX6XfJ24DwlpQ8nKQ-M5O-r3yLUhIxicAmE/edit#gid=0
```
- merge 절대 금지
- compile error 상태로 commit 금지
- .gitignore 설정
```
https://www.toptal.com/developers/gitignore
```

<br>

## :star: git 명령어

- <b>clone : github → local</b>
``` 
git clone https://github.com/star-moon-cloud-k/408project.git
```
- <b>branch 이름 확인
```
git branch
```
- <b>branch 이동
```
git branch -M <branch 이름>
```
- <b>동기화 : github → local</b>
```
git pull
```
- <b>push : local → github</b>
```
git add .
```
```
git commit -m "메시지"
```
```
git push
```
