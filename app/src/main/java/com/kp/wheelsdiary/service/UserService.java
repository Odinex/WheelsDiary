package com.kp.wheelsdiary.service;

import android.content.Context;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.google.gson.Gson;
import com.kp.wheelsdiary.R;
import com.kp.wheelsdiary.http.Constants;
import com.kp.wheelsdiary.http.LoginCallBack;
import com.kp.wheelsdiary.data.model.User;
import com.kp.wheelsdiary.http.VolleySingleton;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.concurrent.ExecutionException;

/**
 * Class that requests authentication and user information from the remote data source and
 * maintains an in-memory cache of login status and user credentials information.
 */
public class UserService {

    private static UserService instance;

    Gson gson = new Gson();

    // If user credentials will be cached in local storage, it is recommended it be encrypted
    // @see https://developer.android.com/training/articles/keystore
    private User user = null;

    // private constructor : singleton access
    private UserService() {

    }

    public static UserService getInstance() {
        if (instance == null) {
            instance = new UserService();
        }
        return instance;
    }

    public boolean isLoggedIn() {
        return user != null;
    }

    public void logout() {
        user = null;
        // TODO
    }

    private void setLoggedInUser(User user) {
        this.user = user;
        // If user credentials will be cached in local storage, it is recommended it be encrypted
        // @see https://developer.android.com/training/articles/keystore
    }

    public void login(String username, String password, final LoginCallBack callBack, Context context) throws ExecutionException, InterruptedException {
// Request a string response from the provided URL.
        WheelService.setCurrentUser(null);
        System.out.println(username + password);
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("username", username);
            jsonObject.put("password", password);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest
                (Request.Method.POST, Constants.LOGIN, jsonObject, new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
                        System.out.println(response.toString());
                        User user = gson.fromJson(response.toString(), User.class);
                        if(user == null || user.getId() == null) {
                            callBack.onFailure(R.string.login_failed);
                        } else {
                            WheelService.setCurrentUser(user);
                            callBack.onSuccess();
                        }
                    }
                }, new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error) {
                        error.printStackTrace();
                        callBack.onFailure( R.string.login_failed);
                    }
                });
        VolleySingleton instance = VolleySingleton.getInstance(context);
        instance.addToRequestQueue(jsonObjectRequest);
    }

    public void register(final String username, final String password, Context context, final LoginCallBack callBack) throws ExecutionException, InterruptedException {
        WheelService.setCurrentUser(null);
        System.out.println(username + password);
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("username", username);
            jsonObject.put("password", password);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest
                (Request.Method.POST, Constants.REGISTER, jsonObject, new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
                        System.out.println(response.toString());
                        User user = gson.fromJson(response.toString(), User.class);
                        if(user == null || user.getId() == null) {
                            callBack.onFailure( R.string.register_failed);
                        } else {
                            WheelService.setCurrentUser(user);
                            callBack.onSuccess();
                        }
                    }
                }, new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error) {
                        error.printStackTrace();
                        callBack.onFailure(R.string.register_failed);
                    }
                });
        VolleySingleton instance = VolleySingleton.getInstance(context);
        instance.addToRequestQueue(jsonObjectRequest);


    }
}
