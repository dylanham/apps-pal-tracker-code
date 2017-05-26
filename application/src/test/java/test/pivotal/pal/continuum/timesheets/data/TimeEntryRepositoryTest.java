package test.pivotal.pal.continuum.timesheets.data;

import io.pivotal.pal.continuum.timesheets.data.TimeEntryFields;
import io.pivotal.pal.continuum.timesheets.data.TimeEntryRecord;
import io.pivotal.pal.continuum.timesheets.data.TimeEntryRepository;
import org.junit.Before;
import org.junit.Test;
import org.springframework.jdbc.core.JdbcTemplate;
import test.pivotal.pal.continuum.TestDataSourceFactory;

import javax.sql.DataSource;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.TimeZone;

import static io.pivotal.pal.continuum.timesheets.data.TimeEntryFields.timeEntryFieldsBuilder;
import static io.pivotal.pal.continuum.timesheets.data.TimeEntryRecord.timeEntryRecordBuilder;
import static org.assertj.core.api.Assertions.assertThat;

public class TimeEntryRepositoryTest {

    private DataSource dataSource = TestDataSourceFactory.create();
    private JdbcTemplate template = new JdbcTemplate(dataSource);
    private TimeEntryRepository repo = new TimeEntryRepository(dataSource);

    @Before
    public void setup() {
        TimeZone.setDefault(TimeZone.getTimeZone("UTC"));
        template.execute("DELETE FROM time_entry;");
    }

    @Test
    public void testFindAll() {
        template.execute("INSERT INTO time_entry (id, project_id, user_id, date, hours) VALUES " +
            "(1, 10, 20, '2017-01-30', 8), " +
            "(2, 52, 12, '2016-02-22', 7)");


        List<TimeEntryRecord> foundRecords = repo.findAll();


        assertThat(foundRecords).containsExactlyInAnyOrder(
            timeEntryRecordBuilder()
                .id(1)
                .projectId(10)
                .userId(20)
                .date(LocalDate.parse("2017-01-30"))
                .hours(8)
                .build(),
            timeEntryRecordBuilder()
                .id(2)
                .projectId(52)
                .userId(12)
                .date(LocalDate.parse("2016-02-22"))
                .hours(7)
                .build()
        );
    }

    @Test
    public void testFind() {
        template.execute("INSERT INTO time_entry (id, project_id, user_id, date, hours) VALUES (11, 10, 20, '2017-01-30', 8)");


        Optional<TimeEntryRecord> maybeRecord = repo.find(11);


        TimeEntryRecord expectedRecord = timeEntryRecordBuilder()
            .id(11)
            .projectId(10)
            .userId(20)
            .date(LocalDate.parse("2017-01-30"))
            .hours(8)
            .build();

        assertThat(maybeRecord).isEqualTo(Optional.of(expectedRecord));
    }

    @Test
    public void testFind_whenNotFound() {
        Optional<TimeEntryRecord> maybeRecord = repo.find(10);

        assertThat(maybeRecord).isEqualTo(Optional.empty());
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

        Map<String, Object> persisted = template.queryForMap("SELECT * FROM time_entry WHERE id = ?", created.id);
        assertThat(persisted.get("project_id")).isEqualTo(3L);
        assertThat(persisted.get("user_id")).isEqualTo(2L);
        assertThat(persisted.get("date")).isEqualTo(Timestamp.valueOf("2018-02-25 00:00:00"));
        assertThat(persisted.get("hours")).isEqualTo(16);
    }

    @Test
    public void testUpdate() {
        template.execute("INSERT INTO time_entry (id, project_id, user_id, date, hours) VALUES " +
            "(11, 10, 20, '2017-01-30', 8), " +
            "(12, 52, 12, '2016-02-22', 7)");

        TimeEntryFields fields = timeEntryFieldsBuilder()
            .projectId(3)
            .userId(2)
            .date(LocalDate.parse("2018-02-25"))
            .hours(16)
            .build();


        TimeEntryRecord updated = repo.update(11, fields);


        assertThat(updated.id).isEqualTo(11L);
        assertThat(updated.projectId).isEqualTo(3L);
        assertThat(updated.userId).isEqualTo(2L);
        assertThat(updated.date).isEqualTo(LocalDate.parse("2018-02-25"));
        assertThat(updated.hours).isEqualTo(16);

        Map<String, Object> persisted = template.queryForMap("SELECT * FROM time_entry WHERE id = 11");
        assertThat(persisted.get("project_id")).isEqualTo(3L);
        assertThat(persisted.get("user_id")).isEqualTo(2L);
        assertThat(persisted.get("date")).isEqualTo(Timestamp.valueOf("2018-02-25 00:00:00"));
        assertThat(persisted.get("hours")).isEqualTo(16);

        Map<String, Object> unchanged = template.queryForMap("SELECT * FROM time_entry WHERE id = 12");
        assertThat(unchanged.get("project_id")).isEqualTo(52L);
    }

    @Test
    public void testDelete() {
        template.execute("INSERT INTO time_entry (id, project_id, user_id, date, hours) VALUES " +
            "(11, 10, 20, '2017-01-30', 8), " +
            "(12, 52, 12, '2016-02-22', 7)");


        repo.delete(11);


        assertThat(repo.find(11)).isNotPresent();
        assertThat(repo.find(12)).isPresent();
    }
}
