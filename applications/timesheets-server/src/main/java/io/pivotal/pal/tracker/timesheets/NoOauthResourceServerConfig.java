package io.pivotal.pal.tracker.timesheets;

import com.netflix.discovery.EurekaClient;
import io.pivotal.pal.tracker.restsupport.RestClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.oauth2.config.annotation.web.configuration.ResourceServerConfigurerAdapter;
import org.springframework.web.client.RestTemplate;

import java.util.Optional;

@Configuration
@ConditionalOnProperty(value = "application.oauth-enabled", havingValue = "false")
public class NoOauthResourceServerConfig extends ResourceServerConfigurerAdapter {

    @Override
    public void configure(HttpSecurity http) throws Exception {
        http.authorizeRequests().anyRequest().permitAll();
    }

    @Autowired(required = false) EurekaClient eurekaClient;

    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }

    @Bean
    public RestClient restClient(RestTemplate template) {
        return new RestClient(template, Optional.ofNullable(eurekaClient));
    }
}
