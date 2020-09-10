package com.kp.wheelsdiary.service;

import com.google.gson.Gson;
import com.kp.wheelsdiary.data.UserHttpClient;
import com.kp.wheelsdiary.data.Result;
import com.kp.wheelsdiary.data.model.User;

/**
 * Class that requests authentication and user information from the remote data source and
 * maintains an in-memory cache of login status and user credentials information.
 */
public class UserService {

    private static UserService instance;
    private UserHttpClient dataSource;
    Gson gson=new Gson();

    // If user credentials will be cached in local storage, it is recommended it be encrypted
    // @see https://developer.android.com/training/articles/keystore
    private User user = null;

    // private constructor : singleton access
    private UserService(UserHttpClient dataSource) {
        this.dataSource = dataSource;
    }

    public static UserService getInstance(UserHttpClient userHttpClient) {
        if (instance == null) {
            instance = new UserService(userHttpClient);
        }
        return instance;
    }

    public boolean isLoggedIn() {
        return user != null;
    }

    public void logout() {
        user = null;
        dataSource.logout();
    }

    private void setLoggedInUser(User user) {
        this.user = user;
        // If user credentials will be cached in local storage, it is recommended it be encrypted
        // @see https://developer.android.com/training/articles/keystore
    }

    public Result<User> login(String username, String password) {
// Request a string response from the provided URL.

        Result<User> result = dataSource.login(username, password);
        if (result instanceof Result.Success) {
            setLoggedInUser(((Result.Success<User>) result).getData());
        }
        return result;
    }

    public Result<User> register(final String username, final String password) {


        return null;
    }
}
