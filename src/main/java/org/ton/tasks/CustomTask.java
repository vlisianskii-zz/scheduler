package org.ton.tasks;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.ton.model.TaskRequest;

@Slf4j
@Component
public class CustomTask extends DefaultTask {
    @Override
    public Object apply(TaskRequest request) {
        int x = request.getValue("x");
        return x / 2.3;
    }
}
