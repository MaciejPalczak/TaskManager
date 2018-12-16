package com.github.maciejpalczak.springtodolist.model;

import com.github.maciejpalczak.springtodolist.exceptions.NoFreeIdsLeftException;
import com.github.maciejpalczak.springtodolist.services.TasksManager;

import java.util.Objects;

public class Task {
    private final Integer taskId;
    private String task;
    private Integer priority;
    private String dueDate;

    private Task(Integer taskId, String task, Integer priority, String dueDate) {
        this.taskId = taskId;
        this.task = task;
        this.priority = priority;
        this.dueDate = dueDate;
    }

    public static Task ofPayload(TaskPayload payload) throws NoFreeIdsLeftException {
        return new Task(TasksManager.assignIdForNewTask(),
                payload.getTask(), payload.getPriority(), payload.getDueDate());
    }

    public static Task empty(){
        return new Task(null, null, null, null);
    }

    public Integer getTaskId() {
        return taskId;
    }

    public String getTask() {
        return task;
    }

    public Integer getPriority() {
        return priority;
    }

    public String getDueDate() {
        return dueDate;
    }

    public void setTask(String task) {
        this.task = task;
    }

    public void setPriority(Integer priority) {
        this.priority = priority;
    }

    public void setDueDate(String dueDate) {
        this.dueDate = dueDate;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Task task = (Task) o;
        return Objects.equals(taskId, task.taskId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(taskId);
    }
}
