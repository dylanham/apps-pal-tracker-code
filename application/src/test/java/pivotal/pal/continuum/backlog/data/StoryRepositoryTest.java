package pivotal.pal.continuum.backlog.data;

import io.pivotal.pal.continuum.Application;
import io.pivotal.pal.continuum.backlog.data.StoryRecord;
import io.pivotal.pal.continuum.backlog.data.StoryRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;

import static io.pivotal.pal.continuum.backlog.data.StoryRecord.storyRecordBuilder;
import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace.NONE;

@RunWith(SpringRunner.class)
@DataJpaTest
@ContextConfiguration(classes = Application.class)
@AutoConfigureTestDatabase(replace = NONE)
public class StoryRepositoryTest {

    @Autowired
    private StoryRepository repo;

    @Test
    public void testEntityConfiguration() {
        StoryRecord createdRecord = repo.save(
            storyRecordBuilder()
                .projectId(52L)
                .name("Setup basic application")
                .build()
        );

        assertThat(createdRecord.id).isNotNull();
        assertThat(createdRecord.projectId).isEqualTo(52L);
        assertThat(createdRecord.name).isEqualTo("Setup basic application");


        StoryRecord persistedRecord = repo.findOne(createdRecord.id);

        assertThat(persistedRecord).isEqualTo(createdRecord);
    }
}
