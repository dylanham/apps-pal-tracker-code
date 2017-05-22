package io.pivotal.pal.continuum;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;

import java.util.TimeZone;

@SpringBootApplication
@EnableResourceServer
@EnableWebSecurity
public class Application {

    public static void main(String[] args) {
        TimeZone.setDefault(TimeZone.getTimeZone("UTC"));
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
