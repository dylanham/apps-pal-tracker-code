package integrationtests;

import integrationtests.HttpClient.Response;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.Map;

import static integrationtests.MapBuilder.envMapBuilder;
import static integrationtests.MapBuilder.jsonMapBuilder;
import static org.assertj.core.api.Assertions.assertThat;

public class IntegrationTest {

    private HttpClient httpClient = new HttpClient();
    private ApplicationServer server = new ApplicationServer();
    private Map<String, String> integrationEnv = envMapBuilder()
        .put("APPLICATION_MESSAGE", "Hello from the integration test!")
        .build();


    @Before
    public void setup() throws Exception {
        server.start(integrationEnv);
    }

    @After
    public void teardown() {
        server.stop();
    }

    @Test
    public void test() {
        testHello();
        testShowTimeEntry();
        testCreateTimeEntry();
        testListTimeEntries();
    }

    private void testHello() {
        Response response = httpClient.get("http://localhost:8080/hello");

        assertThat(response.status).isEqualTo(200);
        assertThat(response.body).contains("Hello from the integration test!");
    }

    private void testShowTimeEntry() {
        Response response = httpClient.get("http://localhost:8080/time-entries/1");

        assertThat(response.status).isEqualTo(200);
        assertThat(response.body)
            .contains("\"id\":1")
            .contains("\"projectId\":10")
            .contains("\"userId\":20")
            .contains("\"date\":\"2017-01-30\"")
            .contains("\"hours\":8");
    }

    private void testCreateTimeEntry() {
        Response response = httpClient.post("http://localhost:8080/time-entries", jsonMapBuilder()
            .put("projectId", 110)
            .put("userId", 201)
            .put("date", "2017-05-20")
            .put("hours", 6)
            .build()
        );

        assertThat(response.status).isEqualTo(201);
        assertThat(response.body)
            .contains("\"id\":")
            .contains("\"projectId\":110")
            .contains("\"userId\":201")
            .contains("\"date\":\"2017-05-20\"")
            .contains("\"hours\":6");
    }

    private void testListTimeEntries() {
        Response response = httpClient.get("http://localhost:8080/time-entries");

        assertThat(response.status).isEqualTo(200);
        assertThat(response.body).contains("\"timeEntries\":[");
    }
}
