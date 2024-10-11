# SPRING PLUS
  ## 처음에 작성한 코드


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


## 두번째 코드
    @Transactional
    public void saveSignup(List<User> signupRequestList) {
        String sql = "INSERT INTO USERS (email, password, nick_name, user_role) " +
                " VALUES (?, ?, ?, ?)";

        final int batchSize = 1000;

        for (int i = 0; i < signupRequestList.size(); i += batchSize) {
            int end = Math.min(i + batchSize, signupRequestList.size());
            List<User> batchList = signupRequestList.subList(i, end);

            jdbcTemplate.batchUpdate(sql, batchList, batchList.size(), (PreparedStatement ps, User user) -> {
                ps.setString(1, user.getEmail());
                ps.setString(2, user.getPassword());
                ps.setString(3, user.getNickName());
                ps.setString(4, String.valueOf(user.getUserRole()));
            });
        }
    }

    @Test
    void signup_100만건_유저_생성하기() {
        List<User> userList = new ArrayList<>();
        for (int i = 0; i < 10_000; i++) {
            // given
            SignupRequest signupRequest = new SignupRequest("user" + i + "@naver.com", "Aa88888888!", "USER", "자동" + i);

            String encodedPassword = passwordEncoder.encode(signupRequest.getPassword());

            UserRole userRole = UserRole.signup(signupRequest.getUserRole());

            userList.add(new User(
                    signupRequest.getEmail(),
                    encodedPassword,
                    signupRequest.getNickName(),
                    userRole
            ));
        }

        // when
        saveSignup(userList);
    }

성능
10,000개 생성시   
![image](https://github.com/user-attachments/assets/4ce29ca9-2ce9-448b-addb-cbf28826d13e)

## 마지막 코드

    @Transactional
    public void saveSignup(List<User> signupRequestList){
        String sql = "INSERT INTO USERS (email, password, nick_name, user_role) " +
                " VALUES (?, ?, ?, ?)";

        jdbcTemplate.batchUpdate(sql, signupRequestList, signupRequestList.size(), (PreparedStatement ps, User user) -> {
            ps.setString(1, user.getEmail());
            ps.setString(2, user.getPassword());
            ps.setString(3, user.getNickName());
            ps.setString(4, String.valueOf(user.getUserRole()));
        });

    }
    @Test
    void signup_100만건_유저_생성하기() {
        List<User> userList = new ArrayList<>();
        for (int i = 0; i < 10_000; i++) {
            // given
            SignupRequest signupRequest = new SignupRequest("user" + i + "@naver.com", "Aa88888888!", "USER", "자동" + i);

            String encodedPassword = passwordEncoder.encode(signupRequest.getPassword());

            UserRole userRole = UserRole.signup(signupRequest.getUserRole());

            userList.add(new User(
                    signupRequest.getEmail(),
                    encodedPassword,
                    signupRequest.getNickName(),
                    userRole
            ));
        }

        // when
        saveSignup(userList);
    }

성능
10,000개 생성시   
![image](https://github.com/user-attachments/assets/28ad04df-7307-4913-92a0-1289f6b9e121)

## 100만건 데이터를 넣었을 때 시간
![image](https://github.com/user-attachments/assets/0ce1b3df-eda8-4205-b80f-e3878af4fc20)



