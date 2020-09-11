package com.kp.wheelsdiary.tasks;

import android.os.AsyncTask;

import com.google.gson.Gson;
import com.kp.wheelsdiary.data.CallBack;
import com.kp.wheelsdiary.data.Result;
import com.kp.wheelsdiary.data.UserHttpClient;
import com.kp.wheelsdiary.data.model.User;
import com.kp.wheelsdiary.service.WheelService;

public class UserLoginTask extends AsyncTask<Void, Void, String> {
    String username;
    String passwrod;
    UserHttpClient userHttpClient;
    Gson gson = new Gson();

    public UserLoginTask(String username, String passwrod, UserHttpClient userHttpClient) {
        this.username = username;
        this.passwrod = passwrod;
        this.userHttpClient = userHttpClient;
    }

    @Override
    protected String doInBackground(Void... voids) {
        Result login = userHttpClient.login(username, passwrod);
        if (login instanceof Result.Success) {
            Object object = ((Result.Success) login).getData();
            if(object instanceof  User) {
                User data = (User) object;
                System.out.println("do in background set wheelService");
                WheelService.setCurrentUser(data);
                return gson.toJson(data);
            }
        }
        return null;
    }

    @Override
    protected void onPostExecute(String s) {
        super.onPostExecute(s);
        System.out.println(s + "postExecute");
        if(WheelService.getCurrentUser() == null) {
            User user = gson.fromJson(s, User.class);

            System.out.println("onPostExecute set wheelService");
            WheelService.setCurrentUser(user);
        }
    }
}