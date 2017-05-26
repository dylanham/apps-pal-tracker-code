package test.pivotal.pal.continuum.backlog.data;

import io.pivotal.pal.continuum.backlog.data.StoryFields;
import io.pivotal.pal.continuum.backlog.data.StoryRecord;
import io.pivotal.pal.continuum.backlog.data.StoryRepository;
import org.junit.Test;

import java.util.List;
import java.util.Optional;

import static io.pivotal.pal.continuum.backlog.data.StoryFields.storyFieldsBuilder;
import static io.pivotal.pal.continuum.backlog.data.StoryRecord.storyRecordBuilder;
import static org.assertj.core.api.Assertions.assertThat;

public class StoryRepositoryTest {

    private StoryRepository repo = new StoryRepository();

    @Test
    public void testFindAll() {
        StoryRecord createdRecord = repo.create(
            storyFieldsBuilder()
                .projectId(52L)
                .name("Setup basic application")
                .build()
        );


        List<StoryRecord> foundRecords = repo.findAll();


        assertThat(foundRecords).containsExactlyInAnyOrder(
            createdRecord,
            storyRecordBuilder()
                .id(1)
                .projectId(10)
                .name("Initialize Git Repo")
                .build()
        );
    }

    @Test
    public void testFind() {
        Optional<StoryRecord> maybeRecord = repo.find(1);

        assertThat(maybeRecord).hasValue(storyRecordBuilder()
            .id(1)
            .projectId(10)
            .name("Initialize Git Repo")
            .build());
    }

    @Test
    public void testFind_whenNotFound() {
        Optional<StoryRecord> maybeRecord = repo.find(10);

        assertThat(maybeRecord).isEmpty();
    }

    @Test
    public void testCreate() {
        StoryFields fields = storyFieldsBuilder()
            .projectId(52L)
            .name("Setup basic application")
            .build();


        StoryRecord created = repo.create(fields);


        assertThat(created.id).isNotEqualTo(0L);
        assertThat(created.projectId).isEqualTo(52L);
        assertThat(created.name).isEqualTo("Setup basic application");

        assertThat(repo.find(created.id)).hasValue(storyRecordBuilder()
            .id(created.id)
            .projectId(52)
            .name("Setup basic application")
            .build()
        );
    }

    @Test
    public void testUpdate() {
        StoryFields fields = storyFieldsBuilder()
            .projectId(3)
            .name("Setup basic application")
            .build();


        StoryRecord updated = repo.update(1, fields);


        assertThat(updated.id).isEqualTo(1L);
        assertThat(updated.projectId).isEqualTo(3L);
        assertThat(updated.name).isEqualTo("Setup basic application");

        assertThat(repo.find(1)).hasValue(storyRecordBuilder()
            .id(1)
            .projectId(3)
            .name("Setup basic application")
            .build()
        );
    }

    @Test
    public void testDelete() {
        assertThat(repo.find(1)).isPresent();

        repo.delete(1);

        assertThat(repo.find(1)).isNotPresent();
    }
}
