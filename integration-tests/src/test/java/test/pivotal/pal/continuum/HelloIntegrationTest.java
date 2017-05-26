package test.pivotal.pal.continuum;

import org.junit.Test;
import test.pivotal.pal.continuum.support.HttpClient.Response;

import static org.assertj.core.api.Assertions.assertThat;

public class HelloIntegrationTest extends IntegrationTest {

    @Test
    public void testHello() {
        Response response = httpClient.get("http://localhost:8080/hello");

        assertThat(response.status).isEqualTo(200);
        assertThat(response.body).contains("Hello from the integration test!");
    }
}
