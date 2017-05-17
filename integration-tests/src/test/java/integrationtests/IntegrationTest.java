package integrationtests;

import integrationtests.HttpClient.Response;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

import static integrationtests.MapBuilder.jsonMapBuilder;
import static org.hamcrest.CoreMatchers.containsString;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.Assert.assertThat;

public class IntegrationTest {

    private HttpClient httpClient = new HttpClient();
    private ApplicationServer server = new ApplicationServer();
    private Map<String, String> integrationEnv = new HashMap<String, String>() {{
        put("APPLICATION_MESSAGE", "Hello from the integration test!");
    }};


    @Before
    public void setup() throws Exception {
        server.start(integrationEnv);
    }

    @After
    public void teardown() {
        server.stop();
    }

    @Test
    public void test() throws Exception {
        Response helloResponse = httpClient.get("http://localhost:8080/hello");
        assertThat(helloResponse.status, equalTo(200));
        assertThat(helloResponse.body, containsString("Hello from the integration test!"));


        Response timeEntryResponse = httpClient.get("http://localhost:8080/time-entries/1");
        assertThat(timeEntryResponse.status, equalTo(200));
        assertThat(timeEntryResponse.body, containsString("\"id\":1"));
        assertThat(timeEntryResponse.body, containsString("\"projectId\":10"));
        assertThat(timeEntryResponse.body, containsString("\"userId\":20"));
        assertThat(timeEntryResponse.body, containsString("\"date\":\"2017-01-30\""));
        assertThat(timeEntryResponse.body, containsString("\"hours\":8"));


        Response creationResponse = httpClient.post("http://localhost:8080/time-entries", jsonMapBuilder()
            .put("projectId", 110)
            .put("userId", 201)
            .put("date", "2017-05-20")
            .put("hours", 6)
            .build()
        );
        assertThat(creationResponse.status, equalTo(201));
        assertThat(creationResponse.body, containsString("\"id\":"));
        assertThat(creationResponse.body, containsString("\"projectId\":110"));
        assertThat(creationResponse.body, containsString("\"userId\":201"));
        assertThat(creationResponse.body, containsString("\"date\":\"2017-05-20\""));
        assertThat(creationResponse.body, containsString("\"hours\":6"));
    }
}
