package test.pivotal.pal.continuum;

import org.junit.After;
import org.junit.Before;
import org.springframework.jdbc.core.JdbcTemplate;
import test.pivotal.pal.continuum.support.ApplicationServer;
import test.pivotal.pal.continuum.support.HttpClient;
import test.pivotal.pal.continuum.support.MapBuilder;
import test.pivotal.pal.continuum.support.TestDataSourceFactory;
import test.pivotal.pal.continuum.support.oauth.MockOauthServer;
import test.pivotal.pal.continuum.support.oauth.OauthServerInfo;

import java.util.Map;

public abstract class IntegrationTest {

    protected String bearerToken = "integration-bearer-token";
    protected HttpClient httpClient = new HttpClient(bearerToken);
    protected JdbcTemplate template = new JdbcTemplate(TestDataSourceFactory.create());
    protected ApplicationServer server = new ApplicationServer(httpClient);

    @Before
    public void setup() throws Exception {
        OauthServerInfo oauthInfo = new MockOauthServer(bearerToken).start();

        Map<String, String> integrationEnv = MapBuilder.envMapBuilder()
            .put("APPLICATION_MESSAGE", "Hello from the integration test!")
            .put("SECURITY_OAUTH2_CLIENT_CLIENT_ID", oauthInfo.clientId)
            .put("SECURITY_OAUTH2_CLIENT_CLIENT_SECRET", oauthInfo.clientSecret)
            .put("SECURITY_OAUTH2_RESOURCE_TOKEN_INFO_URI", oauthInfo.tokenInfoUri)
            .put("SPRING_DATASOURCE_URL", "jdbc:mysql://localhost:3306/continuum_test?useSSL=false&useTimezone=true&serverTimezone=UTC&useLegacyDatetimeCode=false")
            .build();

        server.start(integrationEnv);
    }

    @After
    public void teardown() {
        server.stop();
    }
}
