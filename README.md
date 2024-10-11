# SPRING PLUS
처음에 작성한 코드
  @Test
    void signup_100만건_유저_생성하기_처음() {
        for (int i = 0; i < 10_000; i++) {
            SignupRequest signupRequest = new SignupRequest("user" + i + "@naver.com", "Aa88888888!", "USER", "자동" + i);

            authService.signup(signupRequest);
        }
    }
성능
10,000개 생성시
![image](https://github.com/user-attachments/assets/60d95a10-8cfb-4289-9694-2d1208f47138)
