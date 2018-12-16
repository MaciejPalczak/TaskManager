package com.github.maciejpalczak.springtodolist.model;

import com.github.maciejpalczak.springtodolist.controllers.MainController;
import com.github.maciejpalczak.springtodolist.services.DateValidator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Objects;

public class TaskPayload {

    private String task;
    private Integer priority;
    private String dueDate;
    private static final Logger log = LoggerFactory.getLogger(MainController.class);

    public TaskPayload(String task, Integer priority, String dueDate) {
        this.priority = priority;
        this.task = task;
        if (DateValidator.isValid(dueDate)) {
            this.dueDate = dueDate;
        } else {
            log.warn("Date type not valid");
            this.dueDate = new SimpleDateFormat("dd/MM/yyyy").format(new Date());
        }
    }

    public String getTask() {
        return task;
    }

    public void setTask(String task) {
        this.task = task;
    }

    public Integer getPriority() {
        return priority;
    }

    public void setPriority(Integer priority) {
        this.priority = priority;
    }

    public String getDueDate() {
        return dueDate;
    }


    public boolean setDueDate(String date) {
        if (DateValidator.isValid(date)) {
            this.dueDate = date;
            return true;
        }
        log.warn("Date type not valid");
        return false;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        TaskPayload that = (TaskPayload) o;
        return Objects.equals(task, that.task) &&
                Objects.equals(priority, that.priority) &&
                Objects.equals(dueDate, that.dueDate);
    }

    @Override
    public int hashCode() {

        return Objects.hash(task, priority, dueDate);
    }
}
