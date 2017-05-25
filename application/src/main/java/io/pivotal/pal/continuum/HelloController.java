package io.pivotal.pal.continuum;

import org.springframework.boot.actuate.metrics.CounterService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HelloController {

    private final String message;
    private final CounterService counterService;

    public HelloController(String message, CounterService counterService) {
        this.message = message;
        this.counterService = counterService;
    }

    @GetMapping("/hello")
    public String sayHello() {
        counterService.increment("continuum.counter.service.hello");

        return message;
    }
}
