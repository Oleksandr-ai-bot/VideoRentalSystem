package com.videorental.repository;

import com.videorental.model.user.User;

public class UserRepository extends InMemoryRepository<User> {
    private static UserRepository instance;

    private UserRepository() {}

    public static UserRepository getInstance() {
        if (instance == null) {
            instance = new UserRepository();
        }
        return instance;
    }
}
