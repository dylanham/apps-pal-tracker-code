package pivotal.pal.continuum.timesheets;

import io.pivotal.pal.continuum.timesheets.TimeEntriesController;
import io.pivotal.pal.continuum.timesheets.TimeEntryInfo;
import io.pivotal.pal.continuum.timesheets.data.TimeEntryRecord;
import io.pivotal.pal.continuum.timesheets.data.TimeEntryRepository;
import org.junit.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.time.LocalDate;
import java.util.Optional;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;
import static pivotal.pal.continuum.TestBuilders.testTimeEntryInfoBuilder;
import static pivotal.pal.continuum.TestBuilders.testTimeEntryRecordBuilder;

public class TimeEntriesControllerTest {

    TimeEntryRepository repository = mock(TimeEntryRepository.class);
    TimeEntriesController controller = new TimeEntriesController(repository);

    @Test
    public void testShow() {
        TimeEntryRecord record = testTimeEntryRecordBuilder()
            .id(112L)
            .date(LocalDate.parse("2016-12-31"))
            .build();
        doReturn(Optional.of(record)).when(repository).find(112L);


        ResponseEntity<TimeEntryInfo> entity = controller.show(112L);


        assertThat(entity.getStatusCode(), equalTo(HttpStatus.OK));
        assertThat(entity.getBody(), equalTo(testTimeEntryInfoBuilder()
            .id(112L)
            .date("2016-12-31")
            .build()
        ));
    }

    @Test
    public void testShow_WhenNotFound() {
        doReturn(Optional.empty()).when(repository).find(115L);


        ResponseEntity<TimeEntryInfo> entity = controller.show(115L);


        assertThat(entity.getStatusCode(), equalTo(HttpStatus.NOT_FOUND));
        assertThat(entity.getBody(), nullValue());
    }
}
