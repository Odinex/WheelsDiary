package com.kp.wheelsdiary.dto;

import com.kp.wheelsdiary.enums.TaskTypeEnum;

import java.util.Date;

public class Task {
    private Date dateCreated;
    private Date dateScheduled;
    private TaskTypeEnum taskType;
    private String otherTaskType;
    private String details;
    private String wheelName;
    private long id;

    public Task(Date dateCreated, Date dateScheduled, TaskTypeEnum taskType, String otherTaskType, String details, String wheelName, long id) {
        this.dateCreated = dateCreated;
        this.dateScheduled = dateScheduled;
        this.taskType = taskType;
        this.otherTaskType = otherTaskType;
        this.details = details;
        this.wheelName = wheelName;
        this.id = id;
    }

    public Task(Date dateScheduled, TaskTypeEnum taskType, String otherTaskType, String details, String wheelName, long id) {
        this.dateScheduled = dateScheduled;
        this.taskType = taskType;
        this.otherTaskType = otherTaskType;
        this.details = details;
        this.wheelName = wheelName;
        this.id = id;
    }

    public Task(Date dateScheduled, TaskTypeEnum taskType, String details, String wheelName, long id) {
        this.dateScheduled = dateScheduled;
        this.taskType = taskType;
        this.details = details;
        this.wheelName = wheelName;
        this.id = id;
    }

    public Date getDateCreated() {
        return dateCreated;
    }

    public void setDateCreated(Date dateCreated) {
        this.dateCreated = dateCreated;
    }

    public Date getDateScheduled() {
        return dateScheduled;
    }

    public void setDateScheduled(Date dateScheduled) {
        this.dateScheduled = dateScheduled;
    }

    public TaskTypeEnum getTaskType() {
        return taskType;
    }

    public void setTaskType(TaskTypeEnum taskType) {
        this.taskType = taskType;
    }

    public String getOtherTaskType() {
        return otherTaskType;
    }

    public void setOtherTaskType(String otherTaskType) {
        this.otherTaskType = otherTaskType;
    }

    public String getDetails() {
        return details;
    }

    public void setDetails(String details) {
        this.details = details;
    }

    public String getWheelName() {
        return wheelName;
    }

    public void setWheelName(String wheelName) {
        this.wheelName = wheelName;
    }

    @Override
    public String toString() {
        return "Task{" +
                "dateCreated=" + dateCreated +
                ", dateScheduled=" + dateScheduled +
                ", taskType=" + taskType +
                ", otherTaskType='" + otherTaskType + '\'' +
                ", details='" + details + '\'' +
                ", wheelName='" + wheelName + '\'' +
                '}';
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }
}
