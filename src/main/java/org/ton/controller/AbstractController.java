package org.ton.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.ton.exception.SchedulerException;

import java.util.function.Supplier;

public abstract class AbstractController {
    protected ResponseEntity<Object> responseEntity(Runnable runnable) {
        return responseEntity(() -> {
            runnable.run();
            return HttpStatus.OK;
        });
    }

    protected <T> ResponseEntity<T> responseEntity(Supplier<T> supplier) {
        try {
            T response = supplier.get();
            return response == null ? ResponseEntity.noContent().build() : ResponseEntity.ok(response);
        } catch (SchedulerException e) {
            return ResponseEntity.badRequest().build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
}
