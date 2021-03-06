package org.ton.scheduler;

import lombok.AllArgsConstructor;
import org.springframework.scheduling.Trigger;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;
import org.springframework.scheduling.support.CronTrigger;
import org.ton.model.DefaultDescription;
import org.ton.exception.SchedulerException;
import org.ton.execution.DefaultExecutor;
import org.ton.model.TaskRequest;
import org.ton.listener.DefaultListener;
import org.ton.tasks.DefaultTask;

import java.util.Collection;
import java.util.Map;
import java.util.concurrent.ScheduledFuture;

import static com.google.common.collect.Lists.newArrayList;
import static com.google.common.collect.Maps.newHashMap;

@AllArgsConstructor
public class SpringScheduler implements TaskScheduler {
    private final Map<String, DefaultTask> taskCache = newHashMap();
    private final Map<String, Collection<String>> dependencyCache = newHashMap();
    private final Map<String, DefaultDescription> descriptionCache = newHashMap();
    private final Map<String, ScheduledFuture<?>> futureCache = newHashMap();

    private final ThreadPoolTaskScheduler scheduler;
    private final Collection<DefaultListener> listeners;

    @Override
    public void register(DefaultTask task) {
        if (taskCache.containsKey(task.key())) {
            throw new SchedulerException("Task has already registered: " + task.key());
        }
        taskCache.put(task.key(), task);
    }

    @Override
    public void register(String key, Collection<String> dependencies) {
        dependencyCache.put(key, dependencies);
    }

    @Override
    public void schedule(String key) {
        if (futureCache.containsKey(key)) {
            throw new SchedulerException("Task has already scheduled: " + key);
        }
        DefaultDescription description = getDescriptionOrThrow(key);
        scheduleTask(key, description.getCron(), description.getRequest());
    }

    @Override
    public void schedule(String key, TaskRequest request, String cron, Collection<String> dependencies) {
        DefaultDescription description = DefaultDescription.builder()
                .key(key)
                .request(request)
                .cron(cron)
                .build();
        descriptionCache.put(key, description);
        scheduleTask(key, cron, request);
    }

    @Override
    public void trigger(String key) {
        DefaultDescription description = getDescriptionOrThrow(key);
        triggerTask(key, description.getRequest());
    }

    @Override
    public void trigger(String key, TaskRequest request) {
        triggerTask(key, request);
    }

    @Override
    public void cancel(String key) {
        cancel(key, true);
    }

    @Override
    public void cancel(String key, boolean mayInterruptIfRunning) {
        if (!futureCache.containsKey(key)) {
            throw new SchedulerException("Task has not been scheduled: " + key);
        }
        ScheduledFuture<?> future = futureCache.get(key);
        future.cancel(mayInterruptIfRunning);
        futureCache.remove(key);
    }

    @Override
    public Map<String, String> getScheduledTasks() {
        Map<String, String> response = newHashMap();
        for (String key: taskCache.keySet()) {
            if (futureCache.containsKey(key)) {
                String cron = getDescriptionOrThrow(key).getCron();
                response.put(key, cron);
            } else {
                response.put(key, null);
            }
        }
        return response;
    }

    @Override
    public boolean isRegistered(String key) {
        return taskCache.containsKey(key);
    }

    private void scheduleTask(String key, String cron, TaskRequest request) {
        if (cron != null) {
            DefaultTask task = getTaskOrThrow(key);
            Collection<String> dependencies = getDependenciesOrThrow(key);
            Trigger trigger = new CronTrigger(cron);
            DefaultExecutor executor = new DefaultExecutor(this, request, task, dependencies, listeners);
            ScheduledFuture<?> future = scheduler.schedule(executor, trigger);
            futureCache.put(task.key(), future);
        }
    }

    private void triggerTask(String key, TaskRequest request) {
        DefaultTask task = getTaskOrThrow(key);
        Collection<String> dependencies = getDependenciesOrThrow(key);
        DefaultExecutor executor = new DefaultExecutor(this, request, task, dependencies, listeners);
        scheduler.submit(executor);
    }

    private DefaultTask getTaskOrThrow(String key) {
        return getOrThrow(key, taskCache, "Task");
    }

    private DefaultDescription getDescriptionOrThrow(String key) {
        return getOrThrow(key, descriptionCache, "Task Description");
    }

    private Collection<String> getDependenciesOrThrow(String key) {
        return dependencyCache.getOrDefault(key, newArrayList());
    }

    private <T> T getOrThrow(String key, Map<String, T> map, String entity) {
        if (!map.containsKey(key)) {
            throw new SchedulerException("Unable to find " + entity + ": " + key);
        }
        return map.get(key);
    }
}
