package integrationtests;

import integrationtests.HttpClient.Response;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.springframework.jdbc.core.JdbcTemplate;

import java.util.Map;

import static integrationtests.MapBuilder.envMapBuilder;
import static org.assertj.core.api.Assertions.assertThat;

public class IntegrationTest {

    private HttpClient httpClient = new HttpClient();
    private JdbcTemplate template = new JdbcTemplate(TestDataSourceFactory.create());
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
        new TimesheetsIntegrationTest(template, httpClient).run();
        new BacklogIntegrationTest(template, httpClient).run();
    }

    private void testHello() {
        Response response = httpClient.get("http://localhost:8080/hello");

        assertThat(response.status).isEqualTo(200);
        assertThat(response.body).contains("Hello from the integration test!");
    }
}
