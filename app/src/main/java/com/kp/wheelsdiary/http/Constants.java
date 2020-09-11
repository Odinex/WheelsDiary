package com.kp.wheelsdiary.http;

final class Constants {
    private static final String LOCALHOST = "http://10.0.2.2:8080";
    static final String REGISTER = LOCALHOST + "/public/users/register";
    static final String LOGIN = LOCALHOST + "/public/users/login";
    static final String WHEELTASKS_BY_OWNER_ID = LOCALHOST +"/tasks/ownerId";
    static final String WHEELTASKS_BY_OWNER_ID_AND_WHEEL_ID = LOCALHOST +"/tasks/ids";
    static final String WHEELTASK_BY_ID = LOCALHOST +"/tasks/id";
    static final String SAVE_UPDATE_WHEELTASK = LOCALHOST +"/tasks";

}
