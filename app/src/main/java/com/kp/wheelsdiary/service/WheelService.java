package com.kp.wheelsdiary.service;

import com.kp.wheelsdiary.dto.Wheel;

import java.util.ArrayList;
import java.util.List;

public class WheelService {
    public List<Wheel> wheels;

    public WheelService() {
        wheels = new ArrayList<>();
    }

    public List<Wheel> getWheels() {
        return wheels;
    }

    public void saveWheel(Wheel wheel) {
        wheels.add(wheel);
    }
}
