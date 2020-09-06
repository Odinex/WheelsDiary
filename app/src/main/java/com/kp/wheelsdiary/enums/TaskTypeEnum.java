package com.kp.wheelsdiary.enums;

import java.util.ArrayList;
import java.util.List;

public enum TaskTypeEnum {
    INSURANCE, ENGINE_OIL_CHANGE, TRANSMISSION_OIL_CHANGE, OTHER;

    public static String [] getTaskTypeArray () {
        List<String> taskTypeList = new ArrayList<>();
        taskTypeList.add("Select a model...");
        for(TaskTypeEnum taskType : TaskTypeEnum.values()) {
            taskTypeList.add(taskType.name());
        }
        return taskTypeList.toArray(new String[0]);
    }
}