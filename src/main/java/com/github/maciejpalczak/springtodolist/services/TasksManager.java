package com.github.maciejpalczak.springtodolist.services;

import com.github.maciejpalczak.springtodolist.exceptions.NoFreeIdsLeftException;
import com.github.maciejpalczak.springtodolist.exceptions.NoTaskWithGivenIdException;
import com.github.maciejpalczak.springtodolist.model.Task;
import com.github.maciejpalczak.springtodolist.model.TaskPayload;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;
import java.util.TreeSet;

@Service
public class TasksManager {

    private TasksStorage tasksStorage = new TasksStorage();
    private static final Set<Integer> freeIds = new TreeSet<>();
    private final Integer maxTodosNumber;

    private static final Logger log = LoggerFactory.getLogger(TasksManager.class);

    public TasksManager(@Value("${max.todos.number}") Integer maxTodosNumber) {
        this.maxTodosNumber = maxTodosNumber;
    }

    @PostConstruct
    private void fillIdsSet() {
        for (Integer i = 1; i < maxTodosNumber; ++i) {
            freeIds.add(i);
        }
    }

    public Set<Task> findAll() {
        return tasksStorage.getTasks();
    }

    public Task findTaskById(Integer id) throws NoTaskWithGivenIdException {
        for (Task t : tasksStorage.getTasks()) {
            if (t.getTaskId().equals(id)) {
                return t;
            }
        }
        throw new NoTaskWithGivenIdException("There is no task with given ID!");
    }

    public boolean saveTask(TaskPayload requestItem) throws NoFreeIdsLeftException {
        return tasksStorage.saveTask(Task.ofPayload(requestItem));
    }

    public boolean deleteTask(Integer id) throws NoTaskWithGivenIdException {
        try {
            tasksStorage.deleteTask(id);
            return freeIdAfterDeletingTask(id);
        } catch (NoTaskWithGivenIdException e) {
            log.info("No task with given ID found thus ID {} was not added to IDs pool", id);
            throw e;
        }
    }

    public boolean updateTask(Integer id, TaskPayload taskPayload) throws NoTaskWithGivenIdException {
        return tasksStorage.updateTask(id, taskPayload);
    }

    public static Integer assignIdForNewTask() throws NoFreeIdsLeftException {
        Iterator<Integer> it = freeIds.iterator();
        if (it.hasNext()) {
            Integer id = it.next();
            freeIds.remove(id);
            return id;
        }
        throw new NoFreeIdsLeftException("No free task ID left in the pool, unable to save");
    }

    private boolean freeIdAfterDeletingTask(Integer id) {
        return freeIds.add(id);
    }

    private final class TasksStorage {

        private final Set<Task> tasks = new HashSet<>();

        private TasksStorage() {
        }

        private boolean saveTask(Task task) {
            log.info("Saving task {}", task.getTaskId());
            return tasks.add(task);
        }

        private boolean deleteTask(Integer taskId) throws NoTaskWithGivenIdException {
            for (Task t : tasks) {
                if (t.getTaskId().equals(taskId)) {
                    log.info("Deleting task {}", taskId);
                    return tasks.remove(t);
                }
            }
            throw new NoTaskWithGivenIdException("No task with given ID, unable to delete!");
        }

        private boolean updateTask(Integer taskId, TaskPayload taskPayload) throws NoTaskWithGivenIdException {
            boolean[] taskUpdated = {false};
            tasks.stream().filter(t -> t.getTaskId().equals(taskId)).findFirst().ifPresent(t -> {
                log.info("Updating task {}", taskId);
                t.setTask(taskPayload.getTask());
                t.setDueDate(taskPayload.getDueDate());
                t.setPriority(taskPayload.getPriority());
                taskUpdated[0] = true;
            });
            if (taskUpdated[0]) {
                return true;
            }
            throw new NoTaskWithGivenIdException("No task with given ID, unable to delete!");
        }

        private Set<Task> getTasks() {
            return tasks;
        }
    }
}
