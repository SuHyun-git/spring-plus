package org.example.expert.domain.auth.service;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.example.expert.config.PasswordEncoder;
import org.example.expert.domain.auth.dto.request.SignupRequest;
import org.example.expert.domain.user.entity.User;
import org.example.expert.domain.user.enums.UserRole;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.servlet.view.ContentNegotiatingViewResolver;

import java.sql.PreparedStatement;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest
class AuthServiceTest {
    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @PersistenceContext
    private EntityManager entityManager;

    @Autowired
    private AuthService authService;


    @Autowired
    private ContentNegotiatingViewResolver viewResolver;

    // 10_000기준 8초 740
    // 100_000기준 1분 21초
    // 1_000_000기준 13분 -> 54초
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



    // 10000기준 8초 937
    // 100000기준 1분 22초
//    @Transactional
//    public void saveSignup(List<User> signupRequestList) {
//        String sql = "INSERT INTO USERS (email, password, nick_name, user_role) " +
//                " VALUES (?, ?, ?, ?)";
//
//        final int batchSize = 5000;  // 배치 사이즈를 5000으로 설정
//
//        for (int i = 0; i < signupRequestList.size(); i += batchSize) {
//            int end = Math.min(i + batchSize, signupRequestList.size());
//            List<User> batchList = signupRequestList.subList(i, end);
//
//            jdbcTemplate.batchUpdate(sql, batchList, batchList.size(), (PreparedStatement ps, User user) -> {
//                ps.setString(1, user.getEmail());
//                ps.setString(2, user.getPassword());
//                ps.setString(3, user.getNickName());
//                ps.setString(4, String.valueOf(user.getUserRole()));
//            });
//        }
//    }

    // 10000기준 9초 32
    // 100000기준 1분 23초
//    @Transactional
//    public void saveSignup(List<User> signupRequestList) {
//        String sql = "INSERT INTO USERS (email, password, nick_name, user_role) " +
//                " VALUES (?, ?, ?, ?)";
//
//        final int batchSize = 1000;
//
//        for (int i = 0; i < signupRequestList.size(); i += batchSize) {
//            int end = Math.min(i + batchSize, signupRequestList.size());
//            List<User> batchList = signupRequestList.subList(i, end);
//
//            jdbcTemplate.batchUpdate(sql, batchList, batchList.size(), (PreparedStatement ps, User user) -> {
//                ps.setString(1, user.getEmail());
//                ps.setString(2, user.getPassword());
//                ps.setString(3, user.getNickName());
//                ps.setString(4, String.valueOf(user.getUserRole()));
//            });
//        }
//    }

    @Test
    void signup_100만건_유저_생성하기() {
        List<User> userList = new ArrayList<>();

        String encodedPassword = passwordEncoder.encode("Aa88888888!");

        for (int i = 0; i < 1_000_000; i++) {
            userList.add(new User(
                    "user" + i + "@naver.com",
                    encodedPassword,
                    "자동" + i,
                    UserRole.ROLE_USER
            ));
        }

        // when
        saveSignup(userList);
    }

//    @Test
//    void signup_100만건_유저_생성하기_처음() {
//        for (int i = 0; i < 10_000; i++) {
//            SignupRequest signupRequest = new SignupRequest("user" + i + "@naver.com", "Aa88888888!", "USER", "자동" + i);
//
//            authService.signup(signupRequest);
//        }
//    }
}