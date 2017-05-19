package io.pivotal.pal.continuum;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

import static java.lang.String.format;
import static java.lang.System.getenv;

@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    public void configureUser(AuthenticationManagerBuilder auth) throws Exception {
        String user = getRequiredEnv("SECURITY_USER");
        String password = getRequiredEnv("SECURITY_PASSWORD");

        auth
            .inMemoryAuthentication()
            .withUser(user)
            .password(password)
            .roles("ADMIN");
    }

    @Override
    public void configure(HttpSecurity http) throws Exception {
        boolean forceHttps = "true".equalsIgnoreCase(getenv("SECURITY_FORCE_HTTPS"));

        http
            .authorizeRequests()
            .anyRequest().authenticated()
            .and().httpBasic()
            .and().csrf().disable();

        if (forceHttps) {
            http.requiresChannel().anyRequest().requiresSecure();
        }
    }


    private static String getRequiredEnv(String name) {
        String value = getenv(name);

        if (value == null) {
            throw new IllegalStateException(format("Missing %s env variable", name));
        }

        return value;
    }
}
