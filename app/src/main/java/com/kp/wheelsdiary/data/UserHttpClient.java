package com.kp.wheelsdiary.data;

import android.os.AsyncTask;

import com.google.gson.Gson;
import com.kp.wheelsdiary.data.model.User;
import com.kp.wheelsdiary.service.WheelService;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.FormBody;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * Class that handles authentication w/ login credentials and retrieves user information.
 */
public class UserHttpClient {

    public static final MediaType JSON
            = MediaType.get("application/json; charset=utf-8");
    Gson gson = new Gson();
    OkHttpClient client = new OkHttpClient();

    User post(String username, String password, String url) {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("username", username);
            jsonObject.put("password", password);
        } catch (JSONException e) {
            e.printStackTrace();
        }


        RequestBody body = RequestBody.create(jsonObject.toString(),JSON);

        Request request = new Request.Builder().url(url).post(body).build();
        System.out.println(request.toString());
        System.out.println(body.toString());
        Call call = client.newCall(request);
        try (Response response = call.execute()) {
            String responseString = response.body().string();
            return gson.fromJson(responseString, User.class);
        }catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public Result login(String username, String password) {

        try {
            User post = post(username, password, "http://10.0.2.2:8080/public/users/login");
            System.out.println(post);


            return new Result.Success<User>(post);
        } catch (Exception e) {
            e.printStackTrace();
            return new Result.Error(new IOException("Error logging in", e));
        }
    }

    public Result register(String username, String password) throws IOException {
        try {
            User post = post(username, password, "http://10.0.2.2:8080/public/users/register");
            System.out.println(post);

            return new Result.Success<>(post);
        } catch (Exception e) {
            e.printStackTrace();
            return new Result.Error(new IOException("Error registering" , e));
        }
    }

    public void logout() {
        // TODO: revoke authentication
    }


}
