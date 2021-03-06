package org.ton;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Import;
import org.ton.configuration.SwaggerController;
import org.ton.configuration.SchedulerConfiguration;

@Import({
        SwaggerController.class,
        SchedulerConfiguration.class
})
@SpringBootApplication
public class Application {
    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }
}
