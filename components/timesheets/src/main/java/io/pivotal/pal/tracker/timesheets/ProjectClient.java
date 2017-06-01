package io.pivotal.pal.tracker.timesheets;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import io.pivotal.pal.tracker.restsupport.RestClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class ProjectClient {

    private final Logger logger = LoggerFactory.getLogger(getClass());
    private final Map<Long, ProjectInfo> projectsCache = new ConcurrentHashMap<>();
    private final ObjectMapper mapper = buildObjectMapper();
    private final RestClient restClient;
    private final String registrationServerEndpoint;

    public ProjectClient(RestClient restClient, String registrationServerEndpoint) {
        this.restClient = restClient;
        this.registrationServerEndpoint = registrationServerEndpoint;
    }

    @HystrixCommand(fallbackMethod = "getProjectFromCache")
    public ProjectInfo getProject(long projectId) {
        String response = restClient.get(registrationServerEndpoint + "/projects/" + projectId);

        try {
            ProjectInfo project = mapper.readValue(response, ProjectInfo.class);
            projectsCache.put(projectId, project);
            return project;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public ProjectInfo getProjectFromCache(long projectId) {
        logger.info("Getting project from cache, with id {}", projectId);
        return projectsCache.get(projectId);
    }


    private static ObjectMapper buildObjectMapper() {
        ObjectMapper mapper = new ObjectMapper();
        mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        return mapper;
    }
}
