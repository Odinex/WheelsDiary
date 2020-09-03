package com.kp.wheelsdiary.service;

import com.kp.wheelsdiary.dto.Task;
import com.kp.wheelsdiary.dto.Wheel;
import com.kp.wheelsdiary.enums.TaskTypeEnum;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class TaskService {
    private static List<Task> tasks = new ArrayList<>();


    public static List<Task> getTasks() {
        if(tasks.isEmpty()) {
            for(Wheel wheel : WheelService.getWheels()) {
                for (int i = 0; i < 4; i++) {
                    TaskTypeEnum value = TaskTypeEnum.values()[i];
                    Task task;
                    if (value != TaskTypeEnum.OTHER) {
                        task = new Task(sheduleDateAfterMonths(i), value, "task for " + wheel.getName(), wheel.getName());
                    } else {
                        task = new Task(sheduleDateAfterMonths(i), value, "otherType","task for " + wheel.getName(), wheel.getName());
                    }
                    saveTask(task);
                }
            }
        }
        return tasks;
    }

    private static Date sheduleDateAfterMonths(int i) {
        Calendar instance = Calendar.getInstance();
        instance.setTime(new Date());
        instance.add(Calendar.MONTH, i);
        return instance.getTime();
    }

    public static void saveTask(Task task) {
        tasks.add(task);
    }

    public static List<Task> getTasksForWheel(final String wheelName) {
        List<Task> filtered = new ArrayList<>();
        for(Task task : tasks) {
            if(task.getWheelName().equals(wheelName)) {
                filtered.add(task);
            }
        }
        return filtered;

    }
}
