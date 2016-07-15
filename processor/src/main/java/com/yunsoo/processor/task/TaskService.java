package com.yunsoo.processor.task;

import com.yunsoo.processor.config.ProcessorConfigProperties;
import com.yunsoo.processor.dao.config.ProcessorTransactional;
import com.yunsoo.processor.dao.entity.TaskEntity;
import com.yunsoo.processor.dao.repository.TaskRepository;
import com.yunsoo.processor.util.IntervalUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by:   Lijian
 * Created on:   2016-04-22
 * Descriptions:
 */
@Service
public class TaskService {

    private Log log = LogFactory.getLog(getClass());

    @Autowired
    private TaskRepository taskRepository;

    @Autowired
    private ProcessorConfigProperties config;


    public List<Task> getAll() {
        return taskRepository.findByEnvironment(config.getEnvironment())
                .stream()
                .map(Task::new)
                .collect(Collectors.toList());
    }

    @ProcessorTransactional
    public Task lock(String code, long timeout) {
        TaskEntity taskEntity = taskRepository.findOne(code);
        if (!isRunnable(taskEntity)) {
            return null;
        }
        if (taskEntity.getLockedBy() != null) {
            log.warn("[processor: " + config.getName() + "] force lock task " + code + " from " + taskEntity.getLockedBy());
        }

        DateTime now = DateTime.now();
        taskEntity.setLockedBy(config.getName());
        taskEntity.setStartRunDateTime(now);
        taskEntity.setTimeoutDateTime(timeout <= 0 ? null : now.plus(timeout));
        taskEntity = taskRepository.save(taskEntity);
        return new Task(taskEntity);
    }

    @ProcessorTransactional
    public void saveFinished(String code) {
        TaskEntity taskEntity = taskRepository.findOne(code);
        DateTime now = DateTime.now();
        DateTime nextRunDateTime = calculateNextRunDateTime(taskEntity.getNextRunDateTime(), taskEntity.getInterval());

        taskEntity.setNextRunDateTime(nextRunDateTime);
        taskEntity.setLockedBy(null);
        taskEntity.setTimeoutDateTime(null);
        taskEntity.setLastRunBy(config.getName());
        taskEntity.setLastFinishedDateTime(now);
        taskEntity.setFailedCount(0);
        taskRepository.save(taskEntity);
    }

    @ProcessorTransactional
    public void saveFailed(String code, String failedReason) {
        TaskEntity taskEntity = taskRepository.findOne(code);
        DateTime now = DateTime.now();
        Integer failedCount = taskEntity.getFailedCount();
        if (failedCount == null || failedCount < 0) {
            failedCount = 0;
        }
        failedCount++;
        if (failedReason != null && failedReason.length() > 4000) {
            failedReason = failedReason.substring(0, 4000);
        }
        //retry on non-interval task limit to 3 times
        DateTime nextRunDateTime = calculateNextRunDateTime(taskEntity.getNextRunDateTime(), taskEntity.getInterval());
        if (nextRunDateTime == null && failedCount < 3) {
            nextRunDateTime = DateTime.now().plusSeconds(300);
        }

        taskEntity.setNextRunDateTime(nextRunDateTime);
        taskEntity.setLockedBy(null);
        taskEntity.setTimeoutDateTime(null);
        taskEntity.setLastRunBy(config.getName());
        taskEntity.setFailedCount(failedCount);
        taskEntity.setLastFailedDateTime(now);
        taskEntity.setLastFailedReason(failedReason);
        taskRepository.save(taskEntity);
    }

    public List<Task> getRunnable() {
        return taskRepository.findByEnvironmentAndEnabledTrue(config.getEnvironment())
                .stream()
                .filter(this::isRunnable)
                .map(Task::new)
                .collect(Collectors.toList());
    }

    private boolean isRunnable(TaskEntity task) {
        DateTime now = DateTime.now();
        return task.getExecutor().length() > 0
                && task.getNextRunDateTime() != null
                && task.getNextRunDateTime().isBefore(now)
                && (task.getLockedBy() == null || (task.getTimeoutDateTime() != null && task.getTimeoutDateTime().isBefore(now)));
    }

    private DateTime calculateNextRunDateTime(DateTime currentNextRunDateTime, String interval) {
        DateTime nextRunDateTime = IntervalUtils.plusInterval(currentNextRunDateTime, interval);
        if (nextRunDateTime != null) {
            DateTime now = DateTime.now();
            while (nextRunDateTime.isBefore(now)) {
                nextRunDateTime = IntervalUtils.plusInterval(nextRunDateTime, interval);
            }
        }
        return nextRunDateTime;
    }
}
