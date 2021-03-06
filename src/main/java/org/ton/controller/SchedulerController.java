package org.ton.controller;

import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.ton.model.TaskRequest;
import org.ton.scheduler.TaskScheduler;

import java.util.Map;

@RestController
@AllArgsConstructor
public class SchedulerController extends AbstractController {
    private final TaskScheduler scheduler;

    @GetMapping("tasks")
    public ResponseEntity<Map<String, String>> tasks() {
        return responseEntity(scheduler::getScheduledTasks);
    }

    @PutMapping("cancel")
    public ResponseEntity<Object> cancel(@RequestParam String key, @RequestParam(defaultValue = "true") boolean mayInterruptIfRunning) {
        return responseEntity(() -> scheduler.cancel(key, mayInterruptIfRunning));
    }

    @PostMapping("schedule")
    public ResponseEntity<Object> schedule(@RequestParam String key) {
        return responseEntity(() -> scheduler.schedule(key));
    }

    @PostMapping("trigger")
    public ResponseEntity<Object> trigger(@RequestParam String key, @RequestBody(required = false) TaskRequest request) {
        return responseEntity(() -> scheduler.trigger(key, request));
    }
}
