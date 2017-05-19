package integrationtests;

import integrationtests.HttpClient.Response;
import org.springframework.jdbc.core.JdbcTemplate;

import static com.jayway.jsonpath.JsonPath.parse;
import static integrationtests.JsonPathAssert.assertThat;
import static integrationtests.MapBuilder.jsonMapBuilder;
import static org.assertj.core.api.Assertions.assertThat;

public class BacklogIntegrationTest {

    private final JdbcTemplate template;
    private final HttpClient httpClient;

    public BacklogIntegrationTest(JdbcTemplate template, HttpClient httpClient) {
        this.template = template;
        this.httpClient = httpClient;
    }


    public void run() {
        template.execute("DELETE FROM story");
        template.execute("INSERT INTO story (id, project_id, name) VALUES " +
            "(1, 10, 'Initialize Git Repo')");

        testShow();
        int createdId = testCreate();
        testList();
        testUpdate(createdId);
        testDelete(createdId);
    }

    private void testShow() {
        Response response = httpClient.get("http://localhost:8080/stories/1");

        assertThat(response.status).isEqualTo(200);

        assertThat(parse(response.body))
            .hasInt("$.id", 1)
            .hasInt("$.projectId", 10)
            .hasString("$.name", "Initialize Git Repo");
    }

    private int testCreate() {
        Response response = httpClient.post("http://localhost:8080/stories", jsonMapBuilder()
            .put("projectId", 110)
            .put("name", "First Commit")
            .build()
        );

        assertThat(response.status).isEqualTo(201);
        assertThat(parse(response.body))
            .hasInt("$.id")
            .hasInt("$.projectId", 110)
            .hasString("$.name", "First Commit");

        return parse(response.body).read("$.id", Integer.class);
    }

    private void testList() {
        Response response = httpClient.get("http://localhost:8080/stories");

        assertThat(response.status).isEqualTo(200);
        assertThat(parse(response.body))
            .hasInt("$.stories.length()", 2);
    }

    private void testUpdate(int entryId) {
        Response response = httpClient.put("http://localhost:8080/stories/" + entryId, jsonMapBuilder()
            .put("projectId", 111)
            .put("name", "First Commit (edited)")
            .build()
        );

        assertThat(response.status).isEqualTo(200);
        assertThat(parse(response.body))
            .hasInt("$.id", entryId)
            .hasInt("$.projectId", 111)
            .hasString("$.name", "First Commit (edited)");
    }

    private void testDelete(int entryId) {
        Response deleteResponse = httpClient.delete("http://localhost:8080/stories/" + entryId);
        assertThat(deleteResponse.status).isEqualTo(200);

        Response findResponse = httpClient.get("http://localhost:8080/stories/" + entryId);
        assertThat(findResponse.status).isEqualTo(404);
    }
}
