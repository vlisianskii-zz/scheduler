package org.ton.tasks;

import org.junit.Before;
import org.junit.Test;
import org.ton.model.TaskRequest;

import static org.springframework.test.util.AssertionErrors.assertEquals;

public class CustomTaskTest {
    private DefaultTask task;

    @Before
    public void before() {
        task = new CustomTask();
    }

    @Test
    public void verify_calculated_value() {
        // setup
        TaskRequest request = new TaskRequest();
        request.put("x", 1);
        // action
        Object response = task.apply(request);
        // verify
        assertEquals("Calculated values mismatch", 0.4347826086956522, response);
    }
}
