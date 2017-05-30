package io.pivotal.pal.tracker.backlog;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.pivotal.pal.tracker.restsupport.RestClient;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class ProjectClient {

    private final ObjectMapper mapper = buildObjectMapper();
    private final RestClient restClient;
    private final String endpoint;

    public ProjectClient(RestClient restClient, String registrationServerEndpoint) {
        this.restClient = restClient;
        this.endpoint = registrationServerEndpoint;
    }

    public ProjectInfo getProject(long projectId) {
        String response = restClient.get(endpoint + "/projects/" + projectId);

        try {
            return mapper.readValue(response, ProjectInfo.class);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private static ObjectMapper buildObjectMapper() {
        ObjectMapper mapper = new ObjectMapper();
        mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        return mapper;
    }
}