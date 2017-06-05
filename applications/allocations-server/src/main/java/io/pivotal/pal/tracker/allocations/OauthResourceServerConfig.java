package io.pivotal.pal.tracker.allocations;

import com.netflix.discovery.EurekaClient;
import io.pivotal.pal.tracker.restsupport.RestClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.oauth2.client.OAuth2ClientContext;
import org.springframework.security.oauth2.client.OAuth2RestTemplate;
import org.springframework.security.oauth2.client.resource.OAuth2ProtectedResourceDetails;
import org.springframework.security.oauth2.config.annotation.web.configuration.ResourceServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configurers.ResourceServerSecurityConfigurer;
import org.springframework.web.client.RestTemplate;

import java.util.Optional;

@Configuration
@ConditionalOnProperty(value = "application.oauth-enabled", matchIfMissing = true)
public class OauthResourceServerConfig extends ResourceServerConfigurerAdapter {

    @Override
    public void configure(HttpSecurity http) throws Exception {
        http.authorizeRequests().anyRequest().authenticated();
    }

    @Override
    public void configure(ResourceServerSecurityConfigurer resources) throws Exception {
        resources.resourceId(null);
    }

    @Autowired(required = false) EurekaClient eurekaClient;

    @Bean
    public RestTemplate restTemplate(OAuth2ProtectedResourceDetails resource, OAuth2ClientContext oauth2ClientContext) {
        return new OAuth2RestTemplate(resource, oauth2ClientContext);
    }

    @Bean
    public RestClient restClient(RestTemplate template) {
        return new RestClient(template, Optional.ofNullable(eurekaClient));
    }
}

