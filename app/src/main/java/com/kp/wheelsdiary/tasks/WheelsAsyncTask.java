package com.kp.wheelsdiary.tasks;

import android.os.AsyncTask;

import com.kp.wheelsdiary.enums.WheelTaskRequests;
import com.kp.wheelsdiary.http.WheelHttpClient;
import com.kp.wheelsdiary.http.WheelTaskHttpClient;

public class WheelsAsyncTask extends AsyncTask<Void, Void, String> {
    WheelTaskRequests request;
    Long userId;
    Long wheelId;
    WheelHttpClient httpClient;

    public WheelsAsyncTask(WheelTaskRequests request, Long id, WheelHttpClient httpClient) {
        this.request = request;
        if(request == WheelTaskRequests.BY_ID) {
           this.wheelId = id;
        } else if(request == WheelTaskRequests.BY_USER_ID){
            this.userId = id;
        }
        this.httpClient = httpClient;
    }



    @Override
    protected String doInBackground(Void... voids) {
        if(request == WheelTaskRequests.BY_USER_ID) {
            String wheelsByUserId = httpClient.getWheelsByUserId(userId.toString());
            return wheelsByUserId;


        } else if(request == WheelTaskRequests.BY_ID) {
            //return httpClient.ge(userId.toString(), wheelId.toString());
        }
        return null;
    }

    @Override
    protected void onPostExecute(String s) {
        super.onPostExecute(s);
        System.out.println(s + "postExecute");

    }
}
