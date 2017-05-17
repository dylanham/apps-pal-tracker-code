package integrationtests;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.hamcrest.CoreMatchers.containsString;
import static org.junit.Assert.assertThat;

public class IntegrationTest {

    private HttpClient httpClient = new HttpClient();
    private ApplicationServer server = new ApplicationServer();


    @Before
    public void setup() throws Exception {
        server.start();
    }

    @After
    public void teardown() {
        server.stop();
    }

    @Test
    public void test() throws Exception {
        String response = httpClient.get("http://localhost:8080/hello");

        assertThat(response, containsString("Hello world!"));
    }
}
