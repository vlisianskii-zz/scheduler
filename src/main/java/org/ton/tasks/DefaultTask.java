package org.ton.tasks;

import org.ton.model.TaskRequest;

public abstract class DefaultTask {
    public abstract Object apply(TaskRequest request);

    public String key() {
        return getClass().getSimpleName();
    }
}
