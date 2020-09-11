package com.kp.wheelsdiary.service;

import com.kp.wheelsdiary.dto.WheelTask;
import com.kp.wheelsdiary.dto.Wheel;
import com.kp.wheelsdiary.enums.TaskTypeEnum;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class WheelTaskService {
    private static List<WheelTask> wheelTasks = new ArrayList<>();

    private static int nextId = 1;

    public static int getNextId() {
        return nextId++;
    }
    public static List<WheelTask> getWheelTasks() {
        // Instantiate the RequestQueue.


        if(wheelTasks.isEmpty()) {
            for(Wheel wheel : WheelService.getWheels()) {
                for (int i = 0; i < 4; i++) {
                    TaskTypeEnum value = TaskTypeEnum.values()[i];
                    WheelTask wheelTask;
                    if (value != TaskTypeEnum.OTHER) {
                        wheelTask = new WheelTask(scheduleDateAfterMonths(i), value, "task for " + wheel.getName(), wheel,getNextId());
                    } else {
                        wheelTask = new WheelTask(scheduleDateAfterMonths(i), value, "otherType","task for " + wheel.getName(), wheel,getNextId());
                    }
                    saveTask(wheelTask);
                }
            }
        }
        return wheelTasks;
    }

    private static Date scheduleDateAfterMonths(int i) {
        Calendar instance = Calendar.getInstance();
        instance.setTime(new Date());
        instance.add(Calendar.MONTH, i);
        return instance.getTime();
    }

    public static void saveTask(WheelTask wheelTask) {
        wheelTasks.add(wheelTask);
    }

    public static List<WheelTask> getTasksForWheel(final String wheelName) {
        List<WheelTask> filtered = new ArrayList<>();
        for(WheelTask wheelTask : wheelTasks) {
            if(wheelTask.getWheel().getName().equals(wheelName)) {
                filtered.add(wheelTask);
            }
        }
        return filtered;

    }

    public static WheelTask getTaskById(Long taskId) throws Exception {
        for(WheelTask current : wheelTasks) {
            if(current.getId() == taskId) {
                return current;
            }
        }
        throw new Exception("Task not found for id " + taskId);
    }

    public static void updateTask(WheelTask currentWheelTask) {
        for(int i = 0; i < wheelTasks.size(); i++) {
            if(wheelTasks.get(i).getId() == currentWheelTask.getId()) {
                wheelTasks.set(i, currentWheelTask);
            }
        }
    }
}
