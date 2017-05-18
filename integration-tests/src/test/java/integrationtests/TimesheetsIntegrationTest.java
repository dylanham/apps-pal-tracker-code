package integrationtests;

import integrationtests.HttpClient.Response;

import static com.jayway.jsonpath.JsonPath.parse;
import static integrationtests.MapBuilder.jsonMapBuilder;
import static org.assertj.core.api.Assertions.assertThat;

public class TimesheetsIntegrationTest {

    private final HttpClient httpClient;

    public TimesheetsIntegrationTest(HttpClient httpClient) {
        this.httpClient = httpClient;
    }


    public void run() {
        testShow();
        int createdId = testCreate();
        testList();
        testUpdate(createdId);
        testDelete(createdId);
    }

    private void testShow() {
        Response response = httpClient.get("http://localhost:8080/time-entries/1");

        assertThat(response.status).isEqualTo(200);

        JsonPathAssert.assertThat(parse(response.body))
            .hasInt("$.id", 1)
            .hasInt("$.projectId", 10)
            .hasInt("$.userId", 20)
            .hasString("$.date", "2017-01-30")
            .hasInt("$.hours", 8);
    }

    private int testCreate() {
        Response response = httpClient.post("http://localhost:8080/time-entries", jsonMapBuilder()
            .put("projectId", 110)
            .put("userId", 201)
            .put("date", "2017-05-20")
            .put("hours", 6)
            .build()
        );

        assertThat(response.status).isEqualTo(201);
        JsonPathAssert.assertThat(parse(response.body))
            .hasInt("$.id")
            .hasInt("$.projectId", 110)
            .hasInt("$.userId", 201)
            .hasString("$.date", "2017-05-20")
            .hasInt("$.hours", 6);

        return parse(response.body).read("$.id", Integer.class);
    }

    private void testList() {
        Response response = httpClient.get("http://localhost:8080/time-entries");

        assertThat(response.status).isEqualTo(200);
        JsonPathAssert.assertThat(parse(response.body))
            .hasInt("$.timeEntries.length()", 2);
    }

    private void testUpdate(int entryId) {
        Response response = httpClient.put("http://localhost:8080/time-entries/" + entryId, jsonMapBuilder()
            .put("projectId", 111)
            .put("userId", 211)
            .put("date", "2017-05-21")
            .put("hours", 8)
            .build()
        );

        assertThat(response.status).isEqualTo(200);
        JsonPathAssert.assertThat(parse(response.body))
            .hasInt("$.id", entryId)
            .hasInt("$.projectId", 111)
            .hasInt("$.userId", 211)
            .hasString("$.date", "2017-05-21")
            .hasInt("$.hours", 8);
    }

    private void testDelete(int entryId) {
        Response deleteResponse = httpClient.delete("http://localhost:8080/time-entries/" + entryId);
        assertThat(deleteResponse.status).isEqualTo(200);

        Response findResponse = httpClient.get("http://localhost:8080/time-entries/" + entryId);
        assertThat(findResponse.status).isEqualTo(404);
    }
}
