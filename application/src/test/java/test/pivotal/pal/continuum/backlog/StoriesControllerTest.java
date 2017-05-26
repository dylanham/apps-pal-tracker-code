package test.pivotal.pal.continuum.backlog;

import io.pivotal.pal.continuum.backlog.StoriesController;
import io.pivotal.pal.continuum.backlog.StoryForm;
import io.pivotal.pal.continuum.backlog.StoryInfo;
import io.pivotal.pal.continuum.backlog.StoryInfoList;
import io.pivotal.pal.continuum.backlog.data.StoryRecord;
import io.pivotal.pal.continuum.backlog.data.StoryRepository;
import org.junit.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.List;

import static java.util.Arrays.asList;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;
import static test.pivotal.pal.continuum.TestBuilders.*;

public class StoriesControllerTest {

    private StoryRepository repository = mock(StoryRepository.class);
    private StoriesController controller = new StoriesController(repository);


    @Test
    public void testList() {
        List<StoryRecord> records = asList(
            testStoryRecordBuilder()
                .id(112L)
                .name("Story #1")
                .build(),
            testStoryRecordBuilder()
                .id(113L)
                .name("Story #2")
                .build()
        );
        doReturn(records).when(repository).findAll();


        ResponseEntity<StoryInfoList> entity = controller.list();


        assertThat(entity.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(entity.getBody()).isEqualTo(new StoryInfoList(asList(
            testStoryInfoBuilder()
                .id(112L)
                .name("Story #1")
                .build(),
            testStoryInfoBuilder()
                .id(113L)
                .name("Story #2")
                .build()
        )));
    }

    @Test
    public void testShow() {
        StoryRecord record = testStoryRecordBuilder()
            .id(112L)
            .name("Story #1")
            .build();
        doReturn(record).when(repository).findOne(112L);


        ResponseEntity<StoryInfo> entity = controller.show(112L);


        assertThat(entity.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(entity.getBody()).isEqualTo(testStoryInfoBuilder()
            .id(112L)
            .name("Story #1")
            .build()
        );
    }

    @Test
    public void testShow_WhenNotFound() {
        doReturn(null).when(repository).findOne(115L);


        ResponseEntity<StoryInfo> entity = controller.show(115L);


        assertThat(entity.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
        assertThat(entity.getBody()).isNull();
    }

    @Test
    public void testCreate() {
        StoryForm form = testStoryFormBuilder()
            .name("Story #10")
            .build();
        StoryRecord recordToSave = testStoryRecordBuilder()
            .id(null)
            .name("Story #10")
            .build();
        StoryRecord createdRecord = testStoryRecordBuilder()
            .id(121L)
            .name("Story #10")
            .build();

        doReturn(createdRecord).when(repository).save(recordToSave);


        ResponseEntity<StoryInfo> entity = controller.create(form);


        assertThat(entity.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        assertThat(entity.getBody()).isEqualTo(testStoryInfoBuilder()
            .id(121L)
            .name("Story #10")
            .build()
        );
    }

    @Test
    public void testUpdate() {
        StoryForm form = testStoryFormBuilder()
            .name("Story #21")
            .build();
        StoryRecord recordToSave = testStoryRecordBuilder()
            .id(121L)
            .name("Story #21")
            .build();
        StoryRecord updatedRecord = testStoryRecordBuilder()
            .id(121L)
            .name("Story #21")
            .build();

        doReturn(updatedRecord).when(repository).save(recordToSave);


        ResponseEntity<StoryInfo> entity = controller.update(121L, form);


        assertThat(entity.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(entity.getBody()).isEqualTo(testStoryInfoBuilder()
            .id(121L)
            .name("Story #21")
            .build()
        );
    }

    @Test
    public void testDelete() {
        ResponseEntity entity = controller.delete(121L);

        verify(repository).delete(121L);

        assertThat(entity.getStatusCode()).isEqualTo(HttpStatus.OK);
    }
}
