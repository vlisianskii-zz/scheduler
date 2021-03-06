package org.ton.configuration;

import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;
import org.ton.scheduler.SpringScheduler;
import org.ton.scheduler.TaskScheduler;
import org.ton.configuration.properties.SchedulerProperties;
import org.ton.configuration.properties.TaskProperty;
import org.ton.tasks.DefaultTask;
import org.ton.listener.DefaultListener;

import java.util.Collection;
import java.util.List;
import java.util.Map;

import static com.google.common.collect.Lists.newArrayList;

@Configuration
@EnableConfigurationProperties(SchedulerProperties.class)
public class SchedulerConfiguration {
    @Bean
    public ThreadPoolTaskScheduler threadPoolTaskScheduler(SchedulerProperties properties) {
        ThreadPoolTaskScheduler scheduler = new ThreadPoolTaskScheduler();
        scheduler.setPoolSize(properties.getPoolSize());
        scheduler.setThreadNamePrefix(properties.getThreadNamePrefix());
        return scheduler;
    }

    @Bean
    public TaskScheduler scheduler(SchedulerProperties properties, ThreadPoolTaskScheduler threadPoolTaskScheduler, ApplicationContext context) {
        TaskScheduler scheduler = new SpringScheduler(threadPoolTaskScheduler, configureListeners(context));
        for (DefaultTask applicationTask : configureTasks(context)) {
            scheduler.register(applicationTask);
        }
        for (TaskProperty taskProperty : properties.getTasks()) {
            if (scheduler.isRegistered(taskProperty.getKey())) {
                scheduler.register(taskProperty.getKey(), taskProperty.getDependencies());
                scheduler.schedule(taskProperty.getKey(), taskProperty.getRequest(), taskProperty.getCron(), taskProperty.getDependencies());
            }
        }
        return scheduler;
    }

    private List<DefaultListener> configureListeners(ApplicationContext context) {
        Map<String, DefaultListener> beans = context.getBeansOfType(DefaultListener.class);
        return newArrayList(beans.values());
    }

    private Collection<DefaultTask> configureTasks(ApplicationContext context) {
        return context.getBeansOfType(DefaultTask.class).values();
    }
}
