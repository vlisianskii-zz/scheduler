package org.ton.configuration.properties;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.ton.model.TaskRequest;

import java.util.Collection;

import static com.google.common.collect.Lists.newArrayList;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class TaskProperty {
    private String key;
    private String cron;
    private TaskRequest request;
    @Builder.Default
    private Collection<String> dependencies = newArrayList();
}
