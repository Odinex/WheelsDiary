package com.kp.wheelsdiary.http;

import com.google.gson.Gson;
import com.kp.wheelsdiary.dto.Wheel;

import okhttp3.Call;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * Class that handles authentication w/ login credentials and retrieves user information.
 */
public class WheelHttpClient {

    public static final MediaType JSON
            = MediaType.get("application/json; charset=utf-8");

    Gson gson = new Gson();
    OkHttpClient client = new OkHttpClient();

      public String getWheelsByUserId(String userId) {
        String url = Constants.WHEELS_BY_USER_ID;
        Request request = new Request.Builder().addHeader("userId",userId).url(url).build();
        System.out.println(request.toString());
        Call call = client.newCall(request);
        try (Response response = call.execute()) {
            String responseString = response.body().string();
            return responseString;
        }catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public String saveWheel(Wheel wheel) {
        String url = Constants.SAVE_UPDATE_DELETE_WHEEL;
        RequestBody body = RequestBody.create(gson.toJson(wheel), JSON);
        Request request = new Request.Builder().url(url).post(body).build();
        System.out.println(request.toString());
        Call call = client.newCall(request);
        try (Response response = call.execute()) {
            if(response.code() ==  200) {
                return "OK";
            } else {
                return "ERROR";
            }
        }catch (Exception e) {
            e.printStackTrace();
        }
        return "ERROR";
    }

    public String updateWheel(Wheel wheel) {
        String url = Constants.SAVE_UPDATE_DELETE_WHEEL;
        RequestBody body = RequestBody.create(gson.toJson(wheel), JSON);
        Request request = new Request.Builder().url(url).put(body).build();
        System.out.println(request.toString());
        Call call = client.newCall(request);
        try (Response response = call.execute()) {
            if(response.code() ==  200) {
                return "OK";
            } else {
                return "ERROR";
            }
        }catch (Exception e) {
            e.printStackTrace();
        }
        return "ERROR";
    }


    public String deleteWheel(Long wheelId) {
        String url = Constants.SAVE_UPDATE_DELETE_WHEEL;

        Request request = new Request.Builder().addHeader("id", String.valueOf(wheelId)).url(url).delete().build();
        System.out.println(request.toString());
        Call call = client.newCall(request);
        try (Response response = call.execute()) {
            if(response.code() ==  200) {
                return "OK";
            } else {
                return "ERROR";
            }
        }catch (Exception e) {
            e.printStackTrace();
        }
        return "ERROR";
    }
}
