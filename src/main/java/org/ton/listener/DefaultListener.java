package org.ton.listener;

import org.ton.model.TaskRequest;

public class DefaultListener {
    public void started(String key, TaskRequest request) {

    }

    public void finished(String key, TaskRequest request, Object response) {

    }

    public void failed(String key, TaskRequest request, Exception exception) {

    }
}
