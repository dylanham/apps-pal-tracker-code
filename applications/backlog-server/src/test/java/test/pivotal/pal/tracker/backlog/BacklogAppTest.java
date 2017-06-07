package test.pivotal.pal.tracker.backlog;

import io.pivotal.pal.tracker.backlog.App;
import io.pivotal.pal.tracker.restsupport.RestClient;
import org.junit.Test;
import org.springframework.web.client.RestTemplate;

import static org.assertj.core.api.Assertions.assertThat;

public class BacklogAppTest {

    @Test
    public void embedded() {
        App.main(new String[]{});

        String response = new RestClient(new RestTemplate()).get("http://localhost:8181/stories?projectId=0");

        assertThat(response).isEqualTo("[]");
    }
}
