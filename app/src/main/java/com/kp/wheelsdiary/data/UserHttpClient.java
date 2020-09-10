package com.kp.wheelsdiary.data;

import com.kp.wheelsdiary.data.model.User;

import java.io.IOException;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Class that handles authentication w/ login credentials and retrieves user information.
 */
public class UserHttpClient {

    public static final MediaType JSON
            = MediaType.get("application/json; charset=utf-8");

    OkHttpClient client = new OkHttpClient();

    String post(String username, String password, String url) throws IOException {
        //RequestBody body = RequestBody.create(json, JSON);
        Request request = new Request.Builder()
                .url(url).addHeader("username", username)
                .addHeader("password", password).build();
        try (Response response = client.newCall(request).execute()) {
            return response.body().string();
        }
    }

    public Result<User> login(String username, String password) {

        try {
            String post = post(username, password, "http://localhost:8080/public/login");
            System.out.println(post);
            return new Result.Success(new User(Long.valueOf(post), username, password));
        } catch (Exception e) {
            return new Result.Error(new IOException("Error logging in", e));
        }
    }

    public Result<User> register(String username, String password) throws IOException {
        try {
            String post = post(username, password, "http://localhost:8080/public/register");
            System.out.println(post);
            return new Result.Success<>(new User(Long.valueOf(post), username, password));
        } catch (Exception e) {
            return new Result.Error(new IOException("Error registering" , e));
        }
    }

    public void logout() {
        // TODO: revoke authentication
    }
}
