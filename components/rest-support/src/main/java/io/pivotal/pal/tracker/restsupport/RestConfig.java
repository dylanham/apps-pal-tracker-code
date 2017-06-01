package io.pivotal.pal.tracker.restsupport;

import com.netflix.discovery.EurekaClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

import java.util.Optional;


@Configuration
public class RestConfig {

    @Autowired(required = false)
    private EurekaClient eurekaClient;

    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }

    @Bean
    public RestClient restClient(RestTemplate restTemplate) {
        return new RestClient(restTemplate, Optional.ofNullable(eurekaClient));
    }
}
