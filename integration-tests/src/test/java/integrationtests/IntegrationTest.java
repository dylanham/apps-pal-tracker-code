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
        String response = httpClient.get("http://localhost:8080/hello");

        assertThat(response, containsString("Hello from the integration test!"));
    }
}
