package com.kp.wheelsdiary.service;

import com.kp.wheelsdiary.data.model.User;
import com.kp.wheelsdiary.dto.Wheel;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class WheelService {
    private static Map<String,Wheel> wheels = new HashMap<>();


    private static User currentUser = null;
    public static Collection<Wheel> getWheels() {
        fillWheels();
        return wheels.values();
    }

    private static void fillWheels() {
        if(wheels.isEmpty()) {
            for(int i = 0;i <4 ; i++) {
                Wheel wheel = new Wheel("make" +i, "model" + i, "name" + i, "variant" + i);
                saveWheel(wheel);
            }
        }
    }

    public static Set<String> getWheelNameSet() {
        fillWheels();
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

    public static User getCurrentUser() {
        return currentUser;
    }
    public static void setCurrentUser(User currentUser) {
        WheelService.currentUser = currentUser;
    }

}
