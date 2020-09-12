package com.kp.wheelsdiary.tasks;

import android.os.AsyncTask;

import com.google.gson.Gson;
import com.kp.wheelsdiary.dto.WheelTask;
import com.kp.wheelsdiary.enums.WheelTaskRequests;
import com.kp.wheelsdiary.http.WheelTaskHttpClient;

public class WheelTasksAsyncTask extends AsyncTask<Void, Void, String> {
    WheelTaskRequests request;
    Long userId;
    Long wheelId;
    Long wheelTaskId;
    WheelTaskHttpClient wheelTaskHttpClient;

    public WheelTasksAsyncTask(WheelTaskRequests request, Long id, WheelTaskHttpClient wheelTaskHttpClient) {
        this.request = request;
        if(request == WheelTaskRequests.BY_ID) {
           this.wheelTaskId = id;
        } else if(request == WheelTaskRequests.BY_USER_ID){
            this.userId = id;
        }
        this.wheelTaskHttpClient = wheelTaskHttpClient;
    }

    public WheelTasksAsyncTask(WheelTaskRequests request, Long userId, Long wheelId, WheelTaskHttpClient wheelTaskHttpClient) {
        this.request = request;
        this.userId = userId;
        this.wheelId = wheelId;
        this.wheelTaskHttpClient = wheelTaskHttpClient;
    }

    @Override
    protected String doInBackground(Void... voids) {
        if(request == WheelTaskRequests.BY_USER_ID) {
            return wheelTaskHttpClient.getWheelTasksByUserId(userId.toString());


        } else if(request == WheelTaskRequests.BY_USER_ID_AND_WHEEL_ID) {
            return wheelTaskHttpClient.getWheelTasksByUserIdAndWheelId(userId.toString(), wheelId.toString());
        }
        return null;
    }

    @Override
    protected void onPostExecute(String s) {
        super.onPostExecute(s);
        System.out.println(s + "postExecute");

    }
}
