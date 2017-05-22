package integrationtests;

import integrationtests.HttpClient.Response;
import integrationtests.oauth.MockOauthServer;
import integrationtests.oauth.OauthServerInfo;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.springframework.jdbc.core.JdbcTemplate;

import java.util.Map;

import static integrationtests.MapBuilder.envMapBuilder;
import static org.assertj.core.api.Assertions.assertThat;

public class IntegrationTest {

    private String bearerToken = "integration-bearer-token";
    private HttpClient httpClient = new HttpClient(bearerToken);
    private JdbcTemplate template = new JdbcTemplate(TestDataSourceFactory.create());
    private ApplicationServer server = new ApplicationServer(httpClient);

    @Before
    public void setup() throws Exception {
        OauthServerInfo oauthInfo = new MockOauthServer(bearerToken).start();

        Map<String, String> integrationEnv = envMapBuilder()
            .put("APPLICATION_MESSAGE", "Hello from the integration test!")
            .put("SPRING_JPA_HIBERNATE_NAMING-STRATEGY", "org.hibernate.cfg.ImprovedNamingStrategy")
            .put("SPRING_JPA_PROPERTIES_HIBERNATE_DIALECT", "org.hibernate.dialect.MySQL5Dialect")
            .put("SECURITY_OAUTH2_CLIENT_CLIENT_ID", oauthInfo.clientId)
            .put("SECURITY_OAUTH2_CLIENT_CLIENT_SECRET", oauthInfo.clientSecret)
            .put("SECURITY_OAUTH2_RESOURCE_TOKEN_INFO_URI", oauthInfo.tokenInfoUri)
            .build();

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
