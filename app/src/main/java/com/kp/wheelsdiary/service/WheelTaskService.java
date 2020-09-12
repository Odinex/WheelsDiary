package com.kp.wheelsdiary.service;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import com.kp.wheelsdiary.dto.WheelTask;
import com.kp.wheelsdiary.enums.WheelTaskRequests;
import com.kp.wheelsdiary.http.WheelTaskHttpClient;
import com.kp.wheelsdiary.tasks.WheelTasksAsyncTask;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.concurrent.ExecutionException;

public class WheelTaskService {
    private static List<WheelTask> wheelTasks = new ArrayList<>();

    private static int nextId = 1;
    // Creates the json object which will manage the information received


    public static int getNextId() {
        return nextId++;
    }

    public static List<WheelTask> getWheelTasks() throws ExecutionException, InterruptedException {
        // Instantiate the RequestQueue.
        if(wheelTasks.isEmpty()) {
            getWheelTasksByUserId();
        }

//        if(WheelTaskService.wheelTasks.isEmpty()) {
//            for(Wheel wheel : WheelService.getWheels()) {
//                for (int i = 0; i < 4; i++) {
//                    TaskTypeEnum value = TaskTypeEnum.values()[i];
//                    WheelTask wheelTask;
//                    if (value != TaskTypeEnum.OTHER) {
//                        wheelTask = new WheelTask(scheduleDateAfterMonths(i), value, "task for " + wheel.getName(), wheel,getNextId());
//                    } else {
//                        wheelTask = new WheelTask(scheduleDateAfterMonths(i), value, "otherType","task for " + wheel.getName(), wheel,getNextId());
//                    }
//                    saveTask(wheelTask);
//                }
//            }
//        }
        return WheelTaskService.wheelTasks;
    }

    private static void getWheelTasksByUserId() throws ExecutionException, InterruptedException {
        GsonBuilder builder = new GsonBuilder();

        // Register an adapter to manage the date types as long values
        builder.registerTypeAdapter(Date.class, new JsonDeserializer<Date>() {
            public Date deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws
                    JsonParseException {
                return new Date(json.getAsJsonPrimitive().getAsLong());
            }
        });

        Gson gson = builder.create();
        if (WheelService.getCurrentUser() != null) {
            WheelTasksAsyncTask wheelTasksAsyncTask = new WheelTasksAsyncTask(WheelTaskRequests.BY_USER_ID,
                    WheelService.getCurrentUser().getId(), new WheelTaskHttpClient());
            String json = wheelTasksAsyncTask.execute().get();
            WheelTask[] wheelTaskArray = gson.fromJson(json, WheelTask[].class);
            wheelTasks.addAll(Arrays.asList(wheelTaskArray));
        }
    }

    private static Date scheduleDateAfterMonths(int i) {
        Calendar instance = Calendar.getInstance();
        instance.setTime(new Date());
        instance.add(Calendar.MONTH, i);
        return instance.getTime();
    }

    public static void saveTask(WheelTask wheelTask) throws Exception {
        WheelTasksAsyncTask save = new WheelTasksAsyncTask(WheelTaskRequests.SAVE,wheelTask,new WheelTaskHttpClient());
        String s = save.execute().get();
        if(s.equals("ERROR")) {
            throw new Exception("Save task failed");
        } else {
            wheelTasks.add(wheelTask);
        }
    }
    public static List<WheelTask> getTasksForWheel(final String wheelName) throws ExecutionException, InterruptedException {
        if (wheelTasks == null || wheelTasks.isEmpty()) {
            getWheelTasksByUserId();
        }
        List<WheelTask> filtered = new ArrayList<>();
        for (WheelTask wheelTask : wheelTasks) {
            if (wheelTask.getWheel().getName().equals(wheelName)) {
                filtered.add(wheelTask);
            }
        }
        return filtered;

    }

    public static WheelTask getTaskById(Long taskId) throws Exception {
        for (WheelTask current : wheelTasks) {
            if (current.getId() == taskId) {
                return current;
            }
        }
        throw new Exception("Task not found for id " + taskId);
    }

    public static void updateTask(WheelTask currentWheelTask) {
        for (int i = 0; i < wheelTasks.size(); i++) {
            if (wheelTasks.get(i).getId() == currentWheelTask.getId()) {
                wheelTasks.set(i, currentWheelTask);
            }
        }
    }

    public static synchronized void clearTasks() {
        wheelTasks.clear();
    }
}
