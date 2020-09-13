package com.kp.wheelsdiary.dto;

public class Wheel {
    private Long id;
    private String make;
    private String model;
    private String name;
    private String variant;
    private User user;

    public Wheel(Long id, String make, String model, String name, String variant, User user) {
        this.id = id;

        this.make = make;
        this.model = model;
        this.name = name;
        this.variant = variant;
        this.user = user;
    }

    public Wheel() {
    }

    public User getUser() {
        return user;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setMake(String make) {
        this.make = make;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setVariant(String variant) {
        this.variant = variant;
    }

    public void setUser(User user) {
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
