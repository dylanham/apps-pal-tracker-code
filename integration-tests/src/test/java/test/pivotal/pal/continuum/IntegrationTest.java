package test.pivotal.pal.continuum;

import org.junit.After;
import org.junit.Before;
import test.pivotal.pal.continuum.support.ApplicationServer;
import test.pivotal.pal.continuum.support.HttpClient;

import static test.pivotal.pal.continuum.support.MapBuilder.envMapBuilder;

public abstract class IntegrationTest {

    protected HttpClient httpClient = new HttpClient();
    protected ApplicationServer server = new ApplicationServer(httpClient);

    @Before
    public void setup() throws Exception {
        server.start(envMapBuilder()
            .put("APPLICATION_MESSAGE", "Hello from the integration test!")
            .build()
        );
    }

    @After
    public void teardown() {
        server.stop();
    }
}
