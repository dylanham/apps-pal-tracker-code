package test.pivotal.pal.continuum;

import org.junit.Test;
import test.pivotal.pal.continuum.support.HttpClient;

import static com.jayway.jsonpath.JsonPath.parse;
import static org.assertj.core.api.Assertions.assertThat;
import static test.pivotal.pal.continuum.support.JsonPathAssert.assertThat;
import static test.pivotal.pal.continuum.support.MapBuilder.jsonMapBuilder;

public class TimesheetsIntegrationTest extends IntegrationTest {

    @Test
    public void test() {
        HttpClient.Response response;

        //show
        response = httpClient.get("http://localhost:8080/time-entries/1");

        assertThat(response.status).isEqualTo(200);

        assertThat(parse(response.body))
            .hasInt("$.id", 1)
            .hasInt("$.projectId", 10)
            .hasInt("$.userId", 20)
            .hasString("$.date", "2017-01-30")
            .hasInt("$.hours", 8);


        //create
        response = httpClient.post("http://localhost:8080/time-entries", jsonMapBuilder()
            .put("projectId", 110)
            .put("userId", 201)
            .put("date", "2017-05-20")
            .put("hours", 6)
            .build()
        );

        assertThat(response.status).isEqualTo(201);
        assertThat(parse(response.body))
            .hasInt("$.id")
            .hasInt("$.projectId", 110)
            .hasInt("$.userId", 201)
            .hasString("$.date", "2017-05-20")
            .hasInt("$.hours", 6);


        //list
        response = httpClient.get("http://localhost:8080/time-entries");

        assertThat(response.status).isEqualTo(200);
        assertThat(parse(response.body)).hasInt("$.timeEntries.length()", 2);
    }
}
