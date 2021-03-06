package org.ton.controller;

import org.junit.Test;

import java.util.Map;

import static com.google.common.collect.Maps.newHashMap;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class SchedulerControllerTest extends AbstractControllerTest {
    @Test
    public void verify_get_tasks_endpoint() throws Exception {
        // setup
        Map<String, String> map = newHashMap();
        map.put("Task1", "cron-expression");
        map.put("Task2", null);
        when(scheduler.getScheduledTasks()).thenReturn(map);
        // action & verify
        assertGet("/tasks", status().isOk(), "{\"Task2\":null,\"Task1\":\"cron-expression\"}");
    }

    @Test
    public void verify_404_error_code_endpoint() throws Exception {
        // action & verify
        assertGet("/task/", status().isNotFound(), "");
    }

    @Test
    public void verify_cancel_endpoint() throws Exception {
        // action & verify
        assertPut("/cancel?key=KEY", status().isOk(), "\"OK\"");
    }

    @Test
    public void verify_400_error_code_endpoint() throws Exception {
        // action & verify
        assertPut("/cancel", status().isBadRequest(), "");
    }

    @Test
    public void verify_schedule_endpoint() throws Exception {
        // action & verify
        assertPost("/schedule?key=KEY", status().isOk(), "\"OK\"");
    }

    @Test
    public void verify_trigger_endpoint() throws Exception {
        // action & verify
        assertPost("/trigger?key=KEY", status().isOk(), "\"OK\"");
    }
}
