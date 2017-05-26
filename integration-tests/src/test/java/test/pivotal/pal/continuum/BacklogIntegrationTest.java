package test.pivotal.pal.continuum;

import org.junit.Test;
import test.pivotal.pal.continuum.support.HttpClient;
import test.pivotal.pal.continuum.support.MapBuilder;

import static com.jayway.jsonpath.JsonPath.parse;
import static org.assertj.core.api.Assertions.assertThat;
import static test.pivotal.pal.continuum.support.JsonPathAssert.assertThat;

public class BacklogIntegrationTest extends IntegrationTest {

    @Test
    public void test() {
        HttpClient.Response response;

        template.execute("DELETE FROM story");
        template.execute("INSERT INTO story (id, project_id, name) VALUES (1, 10, 'Initialize Git Repo')");


        //show
        response = httpClient.get("http://localhost:8080/stories/1");

        assertThat(response.status).isEqualTo(200);
        assertThat(parse(response.body))
            .hasInt("$.id", 1)
            .hasInt("$.projectId", 10)
            .hasString("$.name", "Initialize Git Repo");


        //create
        response = httpClient.post("http://localhost:8080/stories", MapBuilder.jsonMapBuilder()
            .put("projectId", 110)
            .put("name", "First Commit")
            .build()
        );

        assertThat(response.status).isEqualTo(201);
        assertThat(parse(response.body))
            .hasInt("$.id")
            .hasInt("$.projectId", 110)
            .hasString("$.name", "First Commit");

        int createdId = parse(response.body).read("$.id", Integer.class);


        //list
        response = httpClient.get("http://localhost:8080/stories");

        assertThat(response.status).isEqualTo(200);
        assertThat(parse(response.body)).hasInt("$.stories.length()", 2);


        //update
        response = httpClient.put("http://localhost:8080/stories/" + createdId, MapBuilder.jsonMapBuilder()
            .put("projectId", 111)
            .put("name", "First Commit (edited)")
            .build()
        );

        assertThat(response.status).isEqualTo(200);
        assertThat(parse(response.body))
            .hasInt("$.id", createdId)
            .hasInt("$.projectId", 111)
            .hasString("$.name", "First Commit (edited)");


        //delete
        response = httpClient.delete("http://localhost:8080/stories/" + createdId);
        assertThat(response.status).isEqualTo(200);

        response = httpClient.get("http://localhost:8080/stories/" + createdId);
        assertThat(response.status).isEqualTo(404);
    }
}
