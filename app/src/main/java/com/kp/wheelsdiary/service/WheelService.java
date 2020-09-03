package com.kp.wheelsdiary.service;

import com.kp.wheelsdiary.dto.Wheel;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public class WheelService {
    private static Map<String,Wheel> wheels = new HashMap<>();


    public static Collection<Wheel> getWheels() {
        if(wheels.isEmpty()) {
            for(int i = 0;i <4 ; i++) {
                Wheel wheel = new Wheel("make" +i, "model" + i, "name" + i, "variant" + i);
                saveWheel(wheel);
            }
        }
        return wheels.values();
    }

    public static void saveWheel(Wheel wheel) {
        wheels.put(wheel.getName(),wheel);
    }

    public static Wheel getWheelByName(String tabName) {
        return wheels.get(tabName);
    }
}
