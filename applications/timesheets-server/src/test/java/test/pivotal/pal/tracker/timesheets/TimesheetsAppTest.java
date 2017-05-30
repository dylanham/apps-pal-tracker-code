package test.pivotal.pal.tracker.timesheets;

import io.pivotal.pal.tracker.restsupport.RestClient;
import io.pivotal.pal.tracker.timesheets.App;
import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class TimesheetsAppTest {

    @Test
    public void embedded() {
        App.main(new String[]{});

        String response = new RestClient().get("http://localhost:8181/time-entries?projectId=0");

        assertThat(response).isEqualTo("[]");
    }
}
