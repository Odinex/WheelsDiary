package com.kp.wheelsdiary.service;

import android.os.AsyncTask;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.google.gson.Gson;
import com.kp.wheelsdiary.http.UserHttpClient;
import com.kp.wheelsdiary.data.Result;
import com.kp.wheelsdiary.data.model.User;
import com.kp.wheelsdiary.tasks.UserLoginTask;
import com.kp.wheelsdiary.tasks.UserRegisterTask;

import org.json.JSONObject;

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

    public Result login(String username, String password) throws ExecutionException, InterruptedException {
// Request a string response from the provided URL.
        WheelService.setCurrentUser(null);
        System.out.println(username+password) ;
        userLoginTask = new UserLoginTask(username,password,httpClient);
        AsyncTask<Void, Void, String> execute = userLoginTask.execute();
        while(execute.getStatus() != AsyncTask.Status.FINISHED) {
            Thread.sleep(2000);
        }

        User currentUser = WheelService.getCurrentUser();
        if(currentUser == null || currentUser.getId() == null) {
            return new Result.Error(new Exception("Unsuccessful login"));
        }
        return new Result.Success<>(currentUser);
    }

    public Result register(final String username, final String password) throws ExecutionException, InterruptedException {
        WheelService.setCurrentUser(null);
        System.out.println(username+password) ;
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest
                (Request.Method.POST, url, null, new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
                        textView.setText("Response: " + response.toString());
                    }
                }, new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // TODO: Handle error

                    }
                });
        userRegisterTask = new UserRegisterTask(username,password,httpClient);
        AsyncTask<Void, Void, String> execute = userRegisterTask.execute();
        while(execute.getStatus() != AsyncTask.Status.FINISHED) {
            Thread.sleep(2000);
            System.out.println("Waiting "+execute.getStatus().name());
        }
        System.out.println("register result ");
        User currentUser = WheelService.getCurrentUser();
        if(currentUser == null || currentUser.getId() == null) {
            return new Result.Error(new Exception("Unsuccessful login"));
        }
        return new Result.Success<>(currentUser);
    }
}
