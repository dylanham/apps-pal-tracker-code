package pivotal.pal.continuum.timesheets.data;

import io.pivotal.pal.continuum.timesheets.data.TimeEntryFields;
import io.pivotal.pal.continuum.timesheets.data.TimeEntryRecord;
import io.pivotal.pal.continuum.timesheets.data.TimeEntryRepository;
import org.junit.Test;

import java.time.LocalDate;
import java.util.Optional;

import static io.pivotal.pal.continuum.timesheets.data.TimeEntryFields.timeEntryFieldsBuilder;
import static io.pivotal.pal.continuum.timesheets.data.TimeEntryRecord.timeEntryRecordBuilder;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.not;
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

    @Test
    public void testCreate() {
        TimeEntryFields fields = timeEntryFieldsBuilder()
            .projectId(3)
            .userId(2)
            .date(LocalDate.parse("2018-02-25"))
            .hours(16)
            .build();


        TimeEntryRecord created = repo.create(fields);


        assertThat(created.id, not(equalTo(0L)));
        assertThat(created.projectId, equalTo(3L));
        assertThat(created.userId, equalTo(2L));
        assertThat(created.date, equalTo(LocalDate.parse("2018-02-25")));
        assertThat(created.hours, equalTo(16));

        TimeEntryRecord expectedRecord = timeEntryRecordBuilder()
            .id(created.id)
            .projectId(3)
            .userId(2)
            .date(LocalDate.parse("2018-02-25"))
            .hours(16)
            .build();
        Optional<TimeEntryRecord> maybePersistedRecord = repo.find(created.id);
        assertThat(maybePersistedRecord, equalTo(Optional.of(expectedRecord)));
    }
}
