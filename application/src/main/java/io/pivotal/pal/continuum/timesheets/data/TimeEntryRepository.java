package io.pivotal.pal.continuum.timesheets.data;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.Statement;
import java.util.List;
import java.util.Optional;

import static io.pivotal.pal.continuum.timesheets.data.TimeEntryRecord.timeEntryRecordBuilder;
import static java.util.Collections.unmodifiableList;

@Repository
public class TimeEntryRepository {

    private final JdbcTemplate jdbcTemplate;

    public TimeEntryRepository(DataSource dataSource) {
        this.jdbcTemplate = new JdbcTemplate(dataSource);
    }


    public List<TimeEntryRecord> findAll() {
        List<TimeEntryRecord> records = jdbcTemplate.query("SELECT * FROM time_entry", rowMapper);
        return unmodifiableList(records);
    }

    public Optional<TimeEntryRecord> find(long id) {
        List<TimeEntryRecord> records = jdbcTemplate.query("SELECT * FROM time_entry WHERE id = ? LIMIT 1", rowMapper, id);
        return records.stream().findFirst();
    }

    public TimeEntryRecord create(TimeEntryFields fields) {
        GeneratedKeyHolder keyHolder = new GeneratedKeyHolder();

        jdbcTemplate.update((connection) -> {
            PreparedStatement preparedStatement = connection.prepareStatement(
                "INSERT INTO time_entry (project_id, user_id, date, hours) VALUES (?, ?, ?, ?)",
                Statement.RETURN_GENERATED_KEYS
            );
            preparedStatement.setLong(1, fields.projectId);
            preparedStatement.setLong(2, fields.userId);
            preparedStatement.setDate(3, Date.valueOf(fields.date));
            preparedStatement.setInt(4, fields.hours);
            return preparedStatement;
        }, keyHolder);

        long createdId = keyHolder.getKey().longValue();

        return find(createdId).orElseThrow(() -> new IllegalStateException("Record somehow not created"));
    }

    public TimeEntryRecord update(long id, TimeEntryFields fields) {
        jdbcTemplate.update("UPDATE time_entry SET project_id = ?, user_id = ?, date = ?, hours = ? WHERE id = ?",
            fields.projectId,
            fields.userId,
            fields.date,
            fields.hours,
            id
        );

        return find(id).orElseThrow(() -> new IllegalStateException("Updated record could not be read from db"));
    }

    public void delete(long id) {
        jdbcTemplate.update("DELETE FROM time_entry WHERE id = ?", id);
    }


    private final RowMapper<TimeEntryRecord> rowMapper = (rs, rowNum) -> timeEntryRecordBuilder()
        .id(rs.getLong("id"))
        .projectId(rs.getLong("project_id"))
        .userId(rs.getLong("user_id"))
        .date(rs.getDate("date").toLocalDate())
        .hours(rs.getInt("hours"))
        .build();
}
