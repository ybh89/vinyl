TODO
* 소셜 로그인 구현
* redis 캐시 적용 (소식 조회, Refresh token)
* 지금 밸류컬렉션 이거 조회되는거 distinct 로 막았는데 다른 방법 찾아볼 것
* 로그인 api 문서 수동 작성, oauth 로그인 문서도 같이 만들기
* 회원가입시 디폴트 권한으로 ROLE_USER 등록하기.
* 디폴트 권한 목록으로 SUPER, ADMIN, MANAGER, USER, ANONYMOUS 정의하고 해당 권한은 수정, 삭제 불가능으로 만들기
* 회원 가입시 디폴트 권한으로 ROLE_USER가 등록됨으로 권한을 따로 받지 않기.(api 변경)