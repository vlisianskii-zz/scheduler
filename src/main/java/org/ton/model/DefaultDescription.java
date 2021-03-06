package org.ton.model;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class DefaultDescription {
    private final String key;
    private final TaskRequest request;
    private final String cron;
}
