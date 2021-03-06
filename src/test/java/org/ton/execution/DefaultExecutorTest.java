package org.ton.execution;

import org.junit.Before;
import org.junit.Test;
import org.ton.listener.DefaultListener;
import org.ton.model.TaskRequest;
import org.ton.scheduler.TaskScheduler;
import org.ton.tasks.DefaultTask;

import static com.google.common.collect.Lists.newArrayList;
import static org.mockito.Mockito.*;

public class DefaultExecutorTest {
    private static final String DEPENDENCY = "DEPENDENCY";

    private TaskScheduler scheduler;
    private TaskRequest request;
    private DefaultTask task;
    private DefaultListener listener;
    private DefaultExecutor executor;

    @Before
    public void before() {
        scheduler = mock(TaskScheduler.class);
        request = new TaskRequest();
        task = mock(DefaultTask.class);
        listener = mock(DefaultListener.class);
        executor = new DefaultExecutor(scheduler, request, task, newArrayList(DEPENDENCY), newArrayList(listener));
    }

    @Test
    public void verify_successful_executing_runnable_task() {
        // setup
        String key = "key";
        when(task.key()).thenReturn(key);
        Object taskResponse = "OK";
        when(task.apply(request)).thenReturn(taskResponse);
        // action
        executor.run();
        // verify
        verify(listener, times(1)).started(key, request);
        verify(listener, times(1)).finished(key, request, taskResponse);
        verify(scheduler, times(1)).trigger(DEPENDENCY, request);
    }

    @Test
    public void verify_failed_executing_runnable_task() {
        // setup
        String key = "key";
        when(task.key()).thenReturn(key);
        Exception exception = new RuntimeException("errorMessage");
        when(task.apply(request)).thenThrow(exception);
        // action
        executor.run();
        // verify
        verify(listener, times(1)).started(key, request);
        verify(listener, times(1)).failed(key, request, exception);
        verify(scheduler, never()).trigger(any(), any());
    }
}
