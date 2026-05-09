package com.onlineexam.repository;

import com.onlineexam.model.User;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class UserRepository {
    private final Map<String, User> users = new HashMap<>();

    public UserRepository() {
        add(new User("student", "student123", "Demo Student"));
        add(new User("mahesh", "exam123", "Mahesh"));
        add(new User("admin", "admin123", "Exam Coordinator"));
    }

    private void add(User user) {
        users.put(user.getUsername().toLowerCase(), user);
    }

    public Optional<User> findByUsername(String username) {
        if (username == null) {
            return Optional.empty();
        }
        return Optional.ofNullable(users.get(username.toLowerCase()));
    }
}
