package test.pivotal.pal.continuum;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import test.pivotal.pal.continuum.support.ApplicationServer;
import test.pivotal.pal.continuum.support.HttpClient;
import test.pivotal.pal.continuum.support.HttpClient.Response;

import static org.assertj.core.api.Assertions.assertThat;

public class HelloIntegrationTest {

    protected HttpClient httpClient = new HttpClient();
    protected ApplicationServer server = new ApplicationServer(httpClient);

    @Before
    public void setup() throws Exception {
        server.start();
    }

    @After
    public void teardown() {
        server.stop();
    }

    @Test
    public void testHello() {
        Response response = httpClient.get("http://localhost:8080/hello");

        assertThat(response.status).isEqualTo(200);
        assertThat(response.body).contains("Hello world!");
    }
}
