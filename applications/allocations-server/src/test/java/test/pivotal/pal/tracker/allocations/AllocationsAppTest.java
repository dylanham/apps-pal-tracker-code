package test.pivotal.pal.tracker.allocations;

import io.pivotal.pal.tracker.allocations.App;
import io.pivotal.pal.tracker.restsupport.RestClient;
import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class AllocationsAppTest {

    @Test
    public void embedded() {
        App.main(new String[]{});

        String response = new RestClient().get("http://localhost:8181/allocations?projectId=0");

        assertThat(response).isEqualTo("[]");
    }
}
