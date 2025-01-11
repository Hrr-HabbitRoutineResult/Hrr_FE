# Hrr_FE
UMC-7th Hrr(흐르르) 프로젝트의 Android 파트 Repository입니다.

<br>

## Git Branch Strategy
Hrr 프로젝트는 GitHub Flow를 기반으로 한 브랜치 전략을 사용합니다.
- `main`: 배포 가능한 안정적인 코드를 관리하는 브랜치
- `develop`: 개발 중인 코드를 관리하는 default 브랜치
- `feature`: 새로운 기능 개발을 위한 브랜치

### 작업 순서
1. **Issue 생성**
   - 새로운 기능이나 버그 수정을 위한 이슈 생성
   - 이슈 템플릿을 사용하여 작업할 내용에 대해 작성
     
2. **Branch 생성**
   - develop 브랜치의 최신 상태 가져오기
     ```
     git fetch origin
     git pull origin develop
     ```
   - 새로운 feature 브랜치 생성 및 이동
     ```
     git checkout -b feature/{issue-number}-{feature-name}
     ```
     
3. **Commit & Push**
   - 작은 단위로 나눠 commit
   - 아래의 Commit Message Convention을 준수

4. **Pull Request**
   - GitHub에서 PR 생성
     - develop ← feature/{issue-number}-{feature-name}
   - PR 템플릿에 따라 상세 내용 작성
   - 팀원들의 코드 리뷰와 피드백 진행

5. **Merge**
   - 최소 1명 이상의 리뷰어가 Approve(승인)한 경우에만 merge 가능
   - 작업 브랜치 삭제
   - 관련 이슈 close

6. **배포**
   - 개발이 완료되고 QA 테스트를 마친 코드는 main 브랜치로 merge하여 배포

## Branch Naming Convention
브랜치를 생성할 때는 다음과 같은 형식을 따릅니다.
```
feature/{issue-number}-{feature-name}
```
### 예시
feature/1-login  
feature/7-challenge-create

<br>

## Commit Message Convention
작업한 내용을 커밋할 때는 다음과 같은 형식을 따릅니다.
```
type: Subject #issue-number
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

### 예시
```
Feat: 로그인 화면 UI 구현 #1
Fix: 챌린지 유형 선택 시 버튼 활성화 오류 수정 #7
```
<br>

## Code Convention
추후 업데이트 예정입니다.
