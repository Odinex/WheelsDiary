package com.kp.wheelsdiary.dto;

import com.kp.wheelsdiary.enums.TaskTypeEnum;

import java.util.Date;

public class Task {
    private Date dateCreated;
    private Date dateScheduled;
    private TaskTypeEnum taskType;
    private String otherTaskType;
    private String details;
    private Wheel wheel;
    private long id;

    public Task(Date dateCreated, Date dateScheduled, TaskTypeEnum taskType, String otherTaskType, String details, Wheel wheel, long id) {
        this.dateCreated = dateCreated;
        this.dateScheduled = dateScheduled;
        this.taskType = taskType;
        this.otherTaskType = otherTaskType;
        this.details = details;
        this.wheel = wheel;
        this.id = id;
    }

    public Task(Date dateScheduled, TaskTypeEnum taskType, String otherTaskType, String details,  Wheel wheel, long id) {
        this.dateScheduled = dateScheduled;
        this.taskType = taskType;
        this.otherTaskType = otherTaskType;
        this.details = details;
        this.wheel = wheel;
        this.id = id;
    }

    public Task(Date dateScheduled, TaskTypeEnum taskType, String details,  Wheel wheel, long id) {
        this.dateScheduled = dateScheduled;
        this.taskType = taskType;
        this.details = details;
        this.wheel = wheel;
        this.id = id;
    }

    public Task() {
    }

    public Wheel getWheel() {
        return wheel;
    }

    public void setWheel(Wheel wheel) {
        this.wheel = wheel;
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

    @Override
    public String toString() {
        return "Task{" +
                "dateCreated=" + dateCreated +
                ", dateScheduled=" + dateScheduled +
                ", taskType=" + taskType +
                ", otherTaskType='" + otherTaskType + '\'' +
                ", details='" + details + '\'' +
                ", wheel=" + wheel +
                ", id=" + id +
                '}';
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }
}
