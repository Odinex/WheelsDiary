package com.kp.wheelsdiary.http;

public final class Constants {
    private static final String LOCALHOST = "http://10.0.2.2:8080";
    public static final String REGISTER = LOCALHOST + "/public/users/register";
    public static final String LOGIN = LOCALHOST + "/public/users/login";
    public static final String WHEELTASKS_BY_USER_ID = LOCALHOST +"/tasks/userId";
    public static final String WHEELTASKS_BY_USER_ID_AND_WHEEL_ID = LOCALHOST +"/tasks/ids";
    public static final String WHEELTASK_BY_ID = LOCALHOST +"/tasks/id";
    public static final String SAVE_UPDATE_DELETE_WHEELTASK = LOCALHOST +"/tasks";
    public static final String WHEELS_BY_USER_ID = LOCALHOST +"/wheels/userId";
    public static final String SAVE_UPDATE_WHEEL = LOCALHOST +"/wheels";

}
