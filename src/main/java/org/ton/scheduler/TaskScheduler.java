package org.ton.scheduler;

import org.ton.model.TaskRequest;
import org.ton.tasks.DefaultTask;

import java.util.Collection;
import java.util.Map;

public interface TaskScheduler {
    void register(DefaultTask task);
    void register(String key, Collection<String> dependencies);
    void schedule(String key);
    void schedule(String key, TaskRequest request, String cron, Collection<String> dependencies);
    void trigger(String key);
    void trigger(String key, TaskRequest request);
    void cancel(String key);
    void cancel(String key, boolean mayInterruptIfRunning);
    Map<String, String> getScheduledTasks();
    boolean isRegistered(String key);
}
