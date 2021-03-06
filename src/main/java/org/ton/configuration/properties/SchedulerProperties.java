package org.ton.configuration.properties;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.Collection;

import static com.google.common.collect.Lists.newArrayList;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ConfigurationProperties(prefix = "ton-scheduler")
public class SchedulerProperties {
    private int poolSize = 3;
    private String threadNamePrefix = "TON-SCHEDULER-";
    private Collection<TaskProperty> tasks = newArrayList();
}
