package org.ton.listener;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.ton.model.TaskRequest;

@Slf4j
@Component
public class LogListener extends DefaultListener {
    @Override
    public void started(String key, TaskRequest request) {
        log.info("[STARTED] {} with request: {}", key, request);
    }

    @Override
    public void finished(String key, TaskRequest request, Object result) {
        log.info("[FINISHED] {} with response: {}", key, result);
    }

    @Override
    public void failed(String key, TaskRequest request, Exception exception) {
        log.info("[FAILED] {} with response: {}", key, exception.getMessage());
    }
}
