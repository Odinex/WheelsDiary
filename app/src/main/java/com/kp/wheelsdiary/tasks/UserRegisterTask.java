package com.kp.wheelsdiary.tasks;

import android.os.AsyncTask;

import com.google.gson.Gson;
import com.kp.wheelsdiary.data.Result;
import com.kp.wheelsdiary.http.UserHttpClient;
import com.kp.wheelsdiary.data.model.User;
import com.kp.wheelsdiary.service.WheelService;

import java.io.IOException;

public class UserRegisterTask extends AsyncTask<Void, Void, String> {
    String username;
    String passwrod;
    UserHttpClient userHttpClient;
    Gson gson = new Gson();

    public UserRegisterTask(String username, String passwrod, UserHttpClient userHttpClient) {
        this.username = username;
        this.passwrod = passwrod;
        this.userHttpClient = userHttpClient;
        System.out.println("UserRegister Task created");
    }

    @Override
    protected String doInBackground(Void... voids) {
        Result register;
        try {
            register = userHttpClient.register(username, passwrod);

        if (register instanceof Result.Success) {
            Object object = ((Result.Success) register).getData();
            if(object instanceof  User) {
                User data = (User) object;
                System.out.println("do in background set register");
                if(data.getId() == null) {
                    return null;
                }
                WheelService.setCurrentUser(data);
                return gson.toJson(data);
            }
        }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    protected void onPostExecute(String s) {
        super.onPostExecute(s);
        System.out.println(s + "postExecute");
        if(WheelService.getCurrentUser() == null) {
            User user = gson.fromJson(s, User.class);
            if(user.getId() == null) {
                System.out.println("null user id");
                return;
            }
            System.out.println("onPostExecute  setCurrentUser");
            WheelService.setCurrentUser(user);
        }
    }
}
