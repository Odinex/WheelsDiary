package com.kp.wheelsdiary.dto;

public class Wheel {
    private final String make;
    private final String model;
    private final String name;
    private final String variant;

    public Wheel(String make, String model, String name, String variant) {

        this.make = make;
        this.model = model;
        this.name = name;
        this.variant = variant;
    }

    public String getMake() {
        return make;
    }

    public String getModel() {
        return model;
    }

    public String getName() {
        return name;
    }

    public String getVariant() {
        return variant;
    }
}
