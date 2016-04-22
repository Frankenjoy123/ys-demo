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
import java.util.stream.StreamSupport;

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
        return StreamSupport.stream(taskRepository.findByEnvironment(config.getEnvironment()).spliterator(), false)
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
        taskEntity.setStartRunDatetime(now);
        taskEntity.setTimeoutDateTime(timeout <= 0 ? null : now.plus(timeout));
        taskEntity = taskRepository.save(taskEntity);
        return new Task(taskEntity);
    }

    @ProcessorTransactional
    public void saveFinished(String code) {
        TaskEntity taskEntity = taskRepository.findOne(code);
        DateTime now = DateTime.now();
        taskEntity.setNextRunDatetime(calculateNextRunDatetime(taskEntity.getNextRunDatetime(), taskEntity.getInterval()));
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
        taskEntity.setLockedBy(null);
        taskEntity.setTimeoutDateTime(null);
        taskEntity.setLastRunBy(config.getName());
        taskEntity.setFailedCount(taskEntity.getFailedCount() == null ? 1 : taskEntity.getFailedCount() + 1);
        taskEntity.setLastFailedDateTime(now);
        if (failedReason != null && failedReason.length() > 4000) {
            failedReason = failedReason.substring(0, 4000);
        }
        taskEntity.setLastFailedReason(failedReason);
        taskRepository.save(taskEntity);
    }

    public List<Task> getRunnable() {
        return StreamSupport.stream(taskRepository.findByEnvironmentAndEnabledTrue(config.getEnvironment()).spliterator(), false)
                .filter(this::isRunnable)
                .map(Task::new)
                .collect(Collectors.toList());
    }

    private boolean isRunnable(TaskEntity task) {
        DateTime now = DateTime.now();
        return task.getExecutor().length() > 0
                && task.getNextRunDatetime() != null
                && task.getNextRunDatetime().isBefore(now)
                && (task.getLockedBy() == null || (task.getTimeoutDateTime() != null && task.getTimeoutDateTime().isBefore(now)));
    }

    private DateTime calculateNextRunDatetime(DateTime currentNextRunDatetime, String interval) {
        DateTime nextRunDatetime = IntervalUtils.plusInterval(currentNextRunDatetime, interval);
        if (nextRunDatetime != null) {
            DateTime now = DateTime.now();
            while (nextRunDatetime.isBefore(now)) {
                nextRunDatetime = IntervalUtils.plusInterval(nextRunDatetime, interval);
            }
        }
        return nextRunDatetime;
    }
}
