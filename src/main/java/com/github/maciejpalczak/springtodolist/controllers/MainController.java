package com.github.maciejpalczak.springtodolist.controllers;

import com.github.maciejpalczak.springtodolist.exceptions.NoFreeIdsLeftException;
import com.github.maciejpalczak.springtodolist.exceptions.NoTaskWithGivenIdException;
import com.github.maciejpalczak.springtodolist.model.Task;
import com.github.maciejpalczak.springtodolist.model.TaskPayload;
import com.github.maciejpalczak.springtodolist.services.TasksManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;

import java.util.Set;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.locks.ReentrantReadWriteLock;

@RestController
public class MainController {

    private static AtomicInteger entrancesNumber = new AtomicInteger(0);
    private static final Logger log = LoggerFactory.getLogger(MainController.class);
    private final ReentrantReadWriteLock lock = new ReentrantReadWriteLock();
    private final ReentrantReadWriteLock.ReadLock readLock = lock.readLock();
    private final ReentrantReadWriteLock.WriteLock writeLock = lock.writeLock();
    private final TasksManager tasksManager;

    public MainController(TasksManager tasksManager) {
        this.tasksManager = tasksManager;
    }

    @RequestMapping("/")
    public String index() {
        log.info("Endpoint reached: homepage. Entrance number: {}", entrancesNumber.incrementAndGet());
        return "Welcome to Taskee App!";
    }

    @RequestMapping("/list")
    public Set<Task> displayTasks() {
        log.info("Request for tasks list");
        try {
            readLock.lock();
            return tasksManager.findAll();
        } finally {
            readLock.unlock();
        }
    }

    @RequestMapping("/list/{id}")
    public Task getTaskById(@PathVariable Integer id) {
        log.info("Request for a single task");
        try {
            readLock.lock();
            return tasksManager.findTaskById(id);
        } catch (NoTaskWithGivenIdException e) {
            log.error(e.getMessage());
            return Task.empty();
        } finally {
            readLock.unlock();
        }
    }

    @RequestMapping(method = RequestMethod.POST, value = "/list")
    public boolean addTask(@RequestBody TaskPayload requestItem) {
        log.info("POST request for adding another task");
        try {
            writeLock.lock();
            tasksManager.saveTask(requestItem);
            log.info("Item added successfully");
            return true;
        } catch (NoFreeIdsLeftException e) {
            log.error("Unable to save task: {}", e.getMessage());
            return false;
        } finally {
            writeLock.unlock();
        }
    }

    @RequestMapping(method = RequestMethod.DELETE, value = "/list/{id}")
    public boolean deleteTaskById(@PathVariable Integer id) {
        log.info("DELETE request for deleting task");
        try {
            writeLock.lock();
            tasksManager.deleteTask(id);
            log.info("Item deleted successfully");
            return true;
        } catch (NoTaskWithGivenIdException e) {
            log.error(e.getMessage());
            return false;
        } finally {
            writeLock.unlock();
        }
    }

    @RequestMapping(method = RequestMethod.PUT, value = "/list/{id}")
    public String updateTasks(@PathVariable Integer id, @RequestBody String task,
                              @RequestBody String dueDate, @RequestBody Integer priority) {
        log.info("POST request for deleting task");
        try {
            writeLock.lock();
            tasksManager.updateTask(id, new TaskPayload(task, priority, dueDate));
            return "Item updated successfully";
        } catch (NoTaskWithGivenIdException e) {
            log.error(e.getMessage());
            return "No task with given ID!";
        } finally {
            writeLock.unlock();
        }
    }
}