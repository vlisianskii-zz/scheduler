package org.ton.scheduler;

import org.junit.Before;
import org.junit.Test;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;
import org.ton.exception.SchedulerException;
import org.ton.tasks.DefaultTask;
import org.ton.tasks.SumTask;

import java.util.Map;

import static com.google.common.collect.Lists.newArrayList;
import static org.mockito.Mockito.mock;
import static org.springframework.test.util.AssertionErrors.*;

public class SpringSchedulerTest {
    private ThreadPoolTaskScheduler threadPoolTaskScheduler;
    private TaskScheduler scheduler;

    @Before
    public void before() {
        threadPoolTaskScheduler = mock(ThreadPoolTaskScheduler.class);
        scheduler = new SpringScheduler(threadPoolTaskScheduler, newArrayList());
    }

    @Test
    public void verify_registering_task() {
        // setup
        DefaultTask task = new SumTask();
        // action
        scheduler.register(task);
        // verify
        Map<String, String> tasks = scheduler.getScheduledTasks();
        assertEquals("Wrong size of scheduled tasks", tasks.size(), 1);
        assertTrue("No scheduled task found", tasks.containsKey(task.getClass().getSimpleName()));
        assertNull("No scheduled task value found", tasks.get(task.getClass().getSimpleName()));
    }

    @Test(expected = SchedulerException.class)
    public void verify_throwing_error_when_registering_task_already_exists() {
        // setup
        DefaultTask task = new SumTask();
        scheduler.register(task);
        // action
        scheduler.register(task);
    }
}
