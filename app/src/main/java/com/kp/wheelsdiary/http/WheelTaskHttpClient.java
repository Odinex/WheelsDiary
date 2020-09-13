package com.kp.wheelsdiary.http;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.kp.wheelsdiary.dto.WheelTask;

import okhttp3.Call;
import okhttp3.FormBody;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * Class that handles authentication w/ login credentials and retrieves user information.
 */
public class WheelTaskHttpClient {

    public static final MediaType JSON
            = MediaType.get("application/json; charset=utf-8");

    Gson gson = new GsonBuilder()
   .setDateFormat("yyyy-MM-dd").create();
    OkHttpClient client = new OkHttpClient();

      public String getWheelTasksByUserId(String userId) {
        String url = Constants.WHEELTASKS_BY_USER_ID;
        Request request = new Request.Builder().addHeader("userId",userId).url(url).build();
        System.out.println(request.toString());
        Call call = client.newCall(request);
        try (Response response = call.execute()) {
            String responseString = response.body().string();
            // TODO check response code and add condition
            return responseString;
        }catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public String saveWheelTask(WheelTask wheelTask) {
        String url = Constants.SAVE_UPDATE_DELETE_WHEELTASK;
        RequestBody body = RequestBody.create(gson.toJson(wheelTask), JSON);
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

    public String updateWheelTask(WheelTask wheelTask) {
        String url = Constants.SAVE_UPDATE_DELETE_WHEELTASK;
        RequestBody body = RequestBody.create(gson.toJson(wheelTask), JSON);
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

    public String getWheelTasksByUserIdAndWheelId(String userId, String wheelId) {
        String url = Constants.WHEELTASKS_BY_USER_ID_AND_WHEEL_ID;
        Request request = new Request.Builder().addHeader("userId",userId)
                .addHeader("wheelId",wheelId).url(url).build();
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

    public String deleteWheelTask(Long wheelTaskId) {
        String url = Constants.SAVE_UPDATE_DELETE_WHEELTASK;

        Request request = new Request.Builder().addHeader("id", String.valueOf(wheelTaskId)).url(url).delete().build();
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
