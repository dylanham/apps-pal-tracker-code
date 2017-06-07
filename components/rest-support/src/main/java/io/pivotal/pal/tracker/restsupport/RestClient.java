package io.pivotal.pal.tracker.restsupport;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

public class RestClient {

    private final RestTemplate template;

    public RestClient(RestTemplate template) {
        this.template = template;
    }


    public String get(String url) {
        HttpHeaders headers = new HttpHeaders();
        headers.set("Content-type", "application/json");
        HttpEntity entity = new HttpEntity<String>(headers);
        ResponseEntity<String> response = template.exchange(url, HttpMethod.GET, entity, String.class);

        return response.getBody() == null ? "" : response.getBody();
    }
}
