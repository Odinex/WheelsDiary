package com.kp.wheelsdiary.service;

import com.google.gson.Gson;
import com.kp.wheelsdiary.data.UserHttpClient;
import com.kp.wheelsdiary.data.Result;
import com.kp.wheelsdiary.data.model.User;
import com.kp.wheelsdiary.tasks.UserLoginTask;
import com.kp.wheelsdiary.tasks.UserRegisterTask;

import java.util.concurrent.ExecutionException;

/**
 * Class that requests authentication and user information from the remote data source and
 * maintains an in-memory cache of login status and user credentials information.
 */
public class UserService {

    private static UserService instance;
    private UserHttpClient httpClient;
    private UserLoginTask userLoginTask;

    private UserRegisterTask userRegisterTask;
    Gson gson=new Gson();

    // If user credentials will be cached in local storage, it is recommended it be encrypted
    // @see https://developer.android.com/training/articles/keystore
    private User user = null;

    // private constructor : singleton access
    private UserService(UserHttpClient dataSource) {
        this.httpClient = dataSource;
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
        httpClient.logout();
    }

    private void setLoggedInUser(User user) {
        this.user = user;
        // If user credentials will be cached in local storage, it is recommended it be encrypted
        // @see https://developer.android.com/training/articles/keystore
    }

    public Result.Success<User> login(String username, String password) throws ExecutionException, InterruptedException {
// Request a string response from the provided URL.
        WheelService.setCurrentUser(null);
        System.out.println(username+password) ;
        userLoginTask = new UserLoginTask(username,password,httpClient);
        String s = userLoginTask.execute().get();
        System.out.println("Login result " + s);
        while(WheelService.getCurrentUser() == null) {
             try {
                 Thread.sleep(2000);
             } catch (InterruptedException e) {
                 e.printStackTrace();
             }
         }
        return new Result.Success<>(WheelService.getCurrentUser());
    }

    public Result<User> register(final String username, final String password) throws ExecutionException, InterruptedException {
        WheelService.setCurrentUser(null);
        System.out.println(username+password) ;
        userRegisterTask = new UserRegisterTask(username,password,httpClient);
        String s = userRegisterTask.execute().get();
        System.out.println("register result " + s);
        return new Result.Success<>(WheelService.getCurrentUser());
    }
}
