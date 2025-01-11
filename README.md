# Hrr_FE
UMC-7th Hrr(흐르르) 프로젝트의 Android 파트 Repository입니다.

<br>

## Branch Naming Convention
브랜치를 생성할 때는 다음과 같은 형식을 따릅니다.
```
feature/{issue-number}-{feature-name}
```
### 예시
feature/1-login

<br>

## Commit Message Convention
작업한 내용을 커밋할 때는 다음과 같은 형식을 따릅니다.
```
type: Subject #issue-number

body
```

### Type
| Type | 설명 |
|------|------|
| `Feat` | 새로운 기능 추가 |
| `Fix` | 버그 수정 |
| `Design` | 앱 디자인 시스템 설정, 사용자 UI 디자인 변경 |
| `Style` | 코드 스타일 변경 (코드 포맷팅, 세미콜론 누락, 코드 변경이 없는 경우 등) |
| `Refactor` | 코드 리팩토링 |
| `Build` | Gradle 설정, 외부 라이브러리 추가 등 빌드 시스템 변경 |
| `Docs` | 문서 수정 (README.md 등) |
| `Test` | 테스트 코드 작성 |
| `Chore` | 그 외 자잘한 수정이나 빌드 업데이트 |


### Subject
- 제목, 변경 내용에 대한 간단한 요약
- **이슈 번호를 반드시 포함** `#이슈번호`
- **50자 이내**로 작성
- 마침표나 특수문자는 사용하지 않음

### Body (선택 사항)
- 작업 내용이 복잡하거나 자세한 설명이 필요한 경우 작성
- 제목과 본문 사이에 한 줄의 공백 추가
- '어떻게'보다는 '무엇을', '왜' 변경했는지에 대해 설명
- 한 줄 당 **72자 이내**로 작성
- 여러 줄의 메시지를 작성할 땐 '-'로 구분 

### 예시
```
Feat: 로그인 화면 구현 #1

- 이메일, 비밀번호 입력 필드 추가
- 로그인 버튼 클릭 시 이벤트 처리
- 유효성 검사 로직 구현
```
<br>

## Code Convention
추후 업데이트 예정입니다.
