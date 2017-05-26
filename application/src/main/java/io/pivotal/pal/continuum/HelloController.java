package io.pivotal.pal.continuum;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HelloController {

    private final String message;

    public HelloController(String message) {
        this.message = message;
    }

    @GetMapping("/hello")
    public String hello() {
        return message;
    }
}
