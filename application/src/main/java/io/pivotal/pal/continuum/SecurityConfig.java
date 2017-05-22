package io.pivotal.pal.continuum;

import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

import static java.lang.System.getenv;

public class SecurityConfig extends WebSecurityConfigurerAdapter {

    @Override
    public void configure(HttpSecurity http) throws Exception {
        boolean forceHttps = "true".equalsIgnoreCase(getenv("SECURITY_FORCE_HTTPS"));

        http.csrf().disable();

        if (forceHttps) {
            http.requiresChannel().anyRequest().requiresSecure();
        }
    }
}
