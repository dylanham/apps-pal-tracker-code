package test.pivotal.pal.tracker.registration;

import io.pivotal.pal.tracker.registration.App;
import io.pivotal.pal.tracker.restsupport.RestClient;
import org.junit.Test;
import org.springframework.web.client.RestTemplate;

import static org.assertj.core.api.Assertions.assertThat;

public class RegistrationAppTest {

    @Test
    public void embedded() {
        App.main(new String[]{});


        RestClient restClient = new RestClient(new RestTemplate());

        assertThat(restClient.get("http://localhost:8181/accounts?ownerId=0")).isEqualTo("[]");
        assertThat(restClient.get("http://localhost:8181/projects?accountId=0")).isEqualTo("[]");
        assertThat(restClient.get("http://localhost:8181/projects/0")).isEqualTo("");
        assertThat(restClient.get("http://localhost:8181/users/0")).isEqualTo("");
    }
}
