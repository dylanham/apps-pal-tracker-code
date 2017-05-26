package test.pivotal.pal.continuum.timesheets.data;

import io.pivotal.pal.continuum.timesheets.data.TimeEntryFields;
import io.pivotal.pal.continuum.timesheets.data.TimeEntryRecord;
import io.pivotal.pal.continuum.timesheets.data.TimeEntryRepository;
import org.junit.Test;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static io.pivotal.pal.continuum.timesheets.data.TimeEntryFields.timeEntryFieldsBuilder;
import static io.pivotal.pal.continuum.timesheets.data.TimeEntryRecord.timeEntryRecordBuilder;
import static org.assertj.core.api.Assertions.assertThat;

public class TimeEntryRepositoryTest {

    private TimeEntryRepository repo = new TimeEntryRepository();


    @Test
    public void testFindAll() {
        TimeEntryRecord createdRecord = repo.create(
            timeEntryFieldsBuilder()
                .projectId(52L)
                .userId(12L)
                .date(LocalDate.parse("2016-02-22"))
                .hours(4)
                .build()
        );


        List<TimeEntryRecord> foundRecords = repo.findAll();


        assertThat(foundRecords).containsExactlyInAnyOrder(
            createdRecord,
            timeEntryRecordBuilder()
                .id(1)
                .projectId(10)
                .userId(20)
                .date(LocalDate.parse("2017-01-30"))
                .hours(8)
                .build()
        );
    }

    @Test
    public void testFind() {
        Optional<TimeEntryRecord> maybeRecord = repo.find(1);


        assertThat(maybeRecord).hasValue(timeEntryRecordBuilder()
            .id(1)
            .projectId(10)
            .userId(20)
            .date(LocalDate.parse("2017-01-30"))
            .hours(8)
            .build());
    }

    @Test
    public void testFind_whenNotFound() {
        Optional<TimeEntryRecord> maybeRecord = repo.find(10);

        assertThat(maybeRecord).isEmpty();
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


        assertThat(created.id).isNotEqualTo(0L);
        assertThat(created.projectId).isEqualTo(3L);
        assertThat(created.userId).isEqualTo(2L);
        assertThat(created.date).isEqualTo(LocalDate.parse("2018-02-25"));
        assertThat(created.hours).isEqualTo(16);

        assertThat(repo.find(created.id)).hasValue(timeEntryRecordBuilder()
            .id(created.id)
            .projectId(3)
            .userId(2)
            .date(LocalDate.parse("2018-02-25"))
            .hours(16)
            .build()
        );
    }
}
