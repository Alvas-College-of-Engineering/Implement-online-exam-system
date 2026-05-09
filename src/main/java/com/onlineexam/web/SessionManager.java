package com.onlineexam.web;

import com.onlineexam.model.ExamResult;
import com.onlineexam.model.User;
import java.security.SecureRandom;
import java.util.Base64;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

public class SessionManager {
    private final SecureRandom secureRandom = new SecureRandom();
    private final Map<String, User> usersByToken = new ConcurrentHashMap<>();
    private final Map<String, ExamResult> resultsByToken = new ConcurrentHashMap<>();

    public String create(User user) {
        byte[] bytes = new byte[32];
        secureRandom.nextBytes(bytes);
        String token = Base64.getUrlEncoder().withoutPadding().encodeToString(bytes);
        usersByToken.put(token, user);
        return token;
    }

    public Optional<User> findUser(String token) {
        if (token == null) {
            return Optional.empty();
        }
        return Optional.ofNullable(usersByToken.get(token));
    }

    public void saveResult(String token, ExamResult result) {
        if (token != null) {
            resultsByToken.put(token, result);
        }
    }

    public Optional<ExamResult> findResult(String token) {
        if (token == null) {
            return Optional.empty();
        }
        return Optional.ofNullable(resultsByToken.get(token));
    }

    public void logout(String token) {
        if (token != null) {
            usersByToken.remove(token);
            resultsByToken.remove(token);
        }
    }
}
