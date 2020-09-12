package com.kp.wheelsdiary.dto;

import com.kp.wheelsdiary.data.model.User;

public class Wheel {
    private final Long id;
    private final String make;
    private final String model;
    private final String name;
    private final String variant;
    private final User user;

    public Wheel(Long id, String make, String model, String name, String variant, User user) {
        this.id = id;

        this.make = make;
        this.model = model;
        this.name = name;
        this.variant = variant;
        this.user = user;
    }

    public Long getId() {
        return id;
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
