package com.kp.wheelsdiary.service;

import com.google.gson.Gson;
import com.kp.wheelsdiary.data.model.User;
import com.kp.wheelsdiary.dto.Wheel;
import com.kp.wheelsdiary.enums.WheelTaskRequests;
import com.kp.wheelsdiary.http.WheelHttpClient;
import com.kp.wheelsdiary.tasks.WheelsAsyncTask;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ExecutionException;

public class WheelService {
    private static Map<String,Wheel> wheels = new HashMap<>();
    private static Gson gson = new Gson();

    private static User currentUser = null;
    public static Collection<Wheel> getWheels() throws ExecutionException, InterruptedException {
        if(currentUser != null && (wheels == null || wheels.isEmpty())) {
            fillWheels();
        }
        return wheels.values();
    }

    private static void fillWheels() throws ExecutionException, InterruptedException {
        WheelsAsyncTask task = new WheelsAsyncTask(WheelTaskRequests.BY_USER_ID,currentUser.getId(),new WheelHttpClient());
        String s = task.execute().get();
        Wheel[] wheelArray = gson.fromJson(s, Wheel[].class);
        for(Wheel w : wheelArray) {
            saveWheel(w);
        }

//        if(wheels.isEmpty()) {
//            for(int i = 0;i <4 ; i++) {
//                Wheel wheel = new Wheel("make" +i, "model" + i, "name" + i, "variant" + i);
//                saveWheel(wheel);
//            }
//        }
    }

    public static Set<String> getWheelNameSet() {
        //fillWheels();
        return wheels.keySet();
    }
    private static String[] convert(Set<String> setOfString)
    {

        // Create String[] of size of setOfString
        String[] arrayOfString = new String[setOfString.size()+1];
        arrayOfString[0] = "Select a model...";
        // Copy elements from set to string array
        // using advanced for loop
        int index = 1;
        for (String str : setOfString)
            arrayOfString[index++] = str;

        // return the formed String[]
        return arrayOfString;
    }
    public static String[] getWheelNameArray() {
        return  convert(getWheelNameSet());
    }


    public static void saveWheel(Wheel wheel) {
        wheels.put(wheel.getName(),wheel);
    }

    public static Wheel getWheelByName(String tabName) {
        return wheels.get(tabName);
    }

    public static synchronized User getCurrentUser() {
        return currentUser;
    }
    public static synchronized void setCurrentUser(User currentUser) {
        WheelService.currentUser = currentUser;
        if(currentUser != null) {
            try {
                wheels.clear();
                fillWheels();
            } catch (ExecutionException e) {
                e.printStackTrace();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

}
