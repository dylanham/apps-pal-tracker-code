package pivotal.pal.continuum.timesheets.data;

import io.pivotal.pal.continuum.timesheets.data.TimeEntryRecord;
import io.pivotal.pal.continuum.timesheets.data.TimeEntryRepository;
import org.junit.Test;

import java.time.LocalDate;
import java.util.Optional;

import static io.pivotal.pal.continuum.timesheets.data.TimeEntryRecord.timeEntryRecordBuilder;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.Assert.assertThat;

public class TimeEntryRepositoryTest {

    private TimeEntryRepository repo = new TimeEntryRepository();

    @Test
    public void testFind() {
        Optional<TimeEntryRecord> maybeRecord = repo.find(1);
        TimeEntryRecord expectedRecord = timeEntryRecordBuilder()
            .id(1)
            .projectId(10)
            .userId(20)
            .date(LocalDate.parse("2017-01-30"))
            .hours(8)
            .build();

        assertThat(maybeRecord, equalTo(Optional.of(expectedRecord)));
    }

    @Test
    public void testFind_whenNotFound() {
        Optional<TimeEntryRecord> maybeRecord = repo.find(10);

        assertThat(maybeRecord, equalTo(Optional.empty()));
    }
}
