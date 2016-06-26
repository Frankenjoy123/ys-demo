package com.yunsoo.processor.controller;

import com.yunsoo.common.data.object.processor.TaskObject;
import com.yunsoo.processor.task.Task;
import com.yunsoo.processor.task.TaskService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by:   Lijian
 * Created on:   2016-04-22
 * Descriptions:
 */
@RequestMapping("/task")
@RestController
public class TaskController {

    @Autowired
    private TaskService taskService;

    @RequestMapping(value = "", method = RequestMethod.GET)
    public List<TaskObject> list() {
        return taskService.getAll().stream().map(this::toTaskObject).collect(Collectors.toList());
    }

    private TaskObject toTaskObject(Task task) {
        if (task == null) {
            return null;
        }
        TaskObject taskObject = new TaskObject();
        taskObject.setCode(task.getCode());
        taskObject.setName(task.getName());
        taskObject.setDescription(task.getDescription());
        taskObject.setExecutor(task.getExecutor());
        taskObject.setEnabled(task.isEnabled());
        taskObject.setInterval(task.getInterval());
        taskObject.setNextRunDateTime(task.getNextRunDateTime());
        taskObject.setLockedBy(task.getLockedBy());
        taskObject.setStartRunDateTime(task.getStartRunDateTime());
        taskObject.setTimeoutDateTime(task.getTimeoutDateTime());
        taskObject.setLastRunBy(task.getLastRunBy());
        taskObject.setLastFinishedDateTime(task.getLastFinishedDateTime());
        taskObject.setFailedCount(task.getFailedCount());
        taskObject.setLastFailedDateTime(task.getLastFailedDateTime());
        taskObject.setLastFailedReason(task.getLastFailedReason());
        return taskObject;
    }
}
