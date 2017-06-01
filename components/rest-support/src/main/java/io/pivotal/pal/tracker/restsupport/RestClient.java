package io.pivotal.pal.tracker.restsupport;

import com.netflix.discovery.EurekaClient;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import java.net.URI;
import java.util.Optional;

public class RestClient {

    private final RestTemplate template;
    private final Optional<EurekaClient> maybeEurekaClient;

    public RestClient(RestTemplate template, Optional<EurekaClient> maybeEurekaClient) {
        this.template = template;
        this.maybeEurekaClient = maybeEurekaClient;
    }


    public String get(String url) {
        String resolvedUrl = resolveUrl(url);

        HttpHeaders headers = new HttpHeaders();
        headers.set("Content-type", "application/json");
        HttpEntity entity = new HttpEntity<String>(headers);
        ResponseEntity<String> response = template.exchange(resolvedUrl, HttpMethod.GET, entity, String.class);

        return response.getBody() == null ? "" : response.getBody();
    }


    private String resolveUrl(String url) {
        return maybeEurekaClient
            .map(eurekaClient -> {
                URI uri = URI.create(url);

                String serverName = uri.getHost();
                String serverUrl = String.format("%s://%s/", uri.getScheme(), serverName);
                boolean isSecure = uri.getScheme().equalsIgnoreCase("https");

                String resolvedServerUrl = eurekaClient.getNextServerFromEureka(serverName, isSecure).getHomePageUrl();
                return url.replace(serverUrl, resolvedServerUrl);
            })
            .orElse(url);
    }
}
