package test.pivotal.pal.continuum.support.oauth;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import okhttp3.mockwebserver.Dispatcher;
import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.MockWebServer;
import okhttp3.mockwebserver.RecordedRequest;

import java.io.IOException;
import java.nio.charset.Charset;

import static java.util.Arrays.asList;
import static test.pivotal.pal.continuum.support.MapBuilder.jsonMapBuilder;

public class MockOauthServer {

    private final MockWebServer mockWebServer;
    private final String bearerToken;
    private final ObjectMapper objectMapper;

    public MockOauthServer(String bearerToken) {
        this.bearerToken = bearerToken;
        this.mockWebServer = new MockWebServer();
        this.objectMapper = new ObjectMapper();
    }

    public OauthServerInfo start() {
        try {
            mockWebServer.setDispatcher(new Dispatcher() {
                @Override
                public MockResponse dispatch(RecordedRequest request) throws InterruptedException {
                    String reqBody = request.getBody().readString(Charset.forName("UTF-8"));
                    MockResponse mockResponse = new MockResponse();

                    if (reqBody.contains(bearerToken)) {
                        try {
                            String body = objectMapper.writeValueAsString(jsonMapBuilder()
                                .put("exp", 1495523056)
                                .put("user_name", "user")
                                .put("authorities", asList("ROLE_ADMIN", "ROLE_USER"))
                                .put("client_id", "164bf1a2-e5aa-4ca0-a03f-2344d159d2b6")
                                .put("scope", asList("read"))
                                .build());
                            mockResponse.setBody(body);
                            mockResponse.setHeader("Content-Type", "application/json");

                        } catch (JsonProcessingException e) {
                            throw new RuntimeException(e);
                        }
                    } else {
                        mockResponse.setResponseCode(401);
                    }

                    return mockResponse;
                }
            });
            mockWebServer.start();

            return new OauthServerInfo(
                "not-so-secret-client-id",
                "not-so-secret-client-secret",
                String.format("http://localhost:%d/oauth/check_token", mockWebServer.getPort())
            );

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
