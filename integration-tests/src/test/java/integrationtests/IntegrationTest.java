package integrationtests;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

import static org.hamcrest.CoreMatchers.containsString;
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
        String helloResponse = httpClient.get("http://localhost:8080/hello");
        assertThat(helloResponse, containsString("Hello from the integration test!"));

        String timeEntryResponse = httpClient.get("http://localhost:8080/time-entries/1");
        assertThat(timeEntryResponse, containsString("\"id\":1"));
        assertThat(timeEntryResponse, containsString("\"projectId\":10"));
        assertThat(timeEntryResponse, containsString("\"userId\":20"));
        assertThat(timeEntryResponse, containsString("\"date\":\"2017-01-30\""));
        assertThat(timeEntryResponse, containsString("\"hours\":8"));
    }
}
