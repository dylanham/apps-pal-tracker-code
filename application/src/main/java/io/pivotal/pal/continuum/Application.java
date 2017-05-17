package io.pivotal.pal.continuum;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class Application {

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }

    @Bean
    public String message() {
        return getEnv("APPLICATION_MESSAGE");
    }


    private String getEnv(String name) {
        String value = System.getenv(name);

        if (value == null) {
            throw new IllegalStateException("Could not load environment variable " + name);
        }

        return value;
    }
}
