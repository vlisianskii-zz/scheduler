package org.ton.execution;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.ton.listener.DefaultListener;
import org.ton.model.TaskRequest;
import org.ton.scheduler.TaskScheduler;
import org.ton.tasks.DefaultTask;

import java.util.Collection;

@Slf4j
@AllArgsConstructor
public class DefaultExecutor implements Runnable {
    private final TaskScheduler scheduler;
    private final TaskRequest request;
    private final DefaultTask task;
    private final Collection<String> dependencies;
    private final Collection<DefaultListener> listeners;

    @Override
    public void run() {
        try {
            listeners.forEach(listener -> listener.started(task.key(), request));

            log.debug("Applying task {}..", task);
            Object response = task.apply(request);

            listeners.forEach(listener -> listener.finished(task.key(), request, response));
            dependencies.forEach(dependency -> scheduler.trigger(dependency, request));
        } catch (Exception e) {
            listeners.forEach(listener -> listener.failed(task.key(), request, e));
        }
    }
}
