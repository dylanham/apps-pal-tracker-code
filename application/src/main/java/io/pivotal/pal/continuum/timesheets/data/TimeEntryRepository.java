package io.pivotal.pal.continuum.timesheets.data;

import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

import static io.pivotal.pal.continuum.timesheets.data.TimeEntryRecord.timeEntryRecordBuilder;

@Repository
public class TimeEntryRepository {

    private final Set<TimeEntryRecord> records = new HashSet<TimeEntryRecord>() {{
        add(timeEntryRecordBuilder()
            .id(1)
            .projectId(10)
            .userId(20)
            .date(LocalDate.parse("2017-01-30"))
            .hours(8)
            .build());
    }};

    private long lastId = 1L;


    public Optional<TimeEntryRecord> find(long id) {
        return records.stream()
            .filter(record -> record.id == id)
            .findFirst();
    }

    public TimeEntryRecord create(TimeEntryFields fields) {
        TimeEntryRecord newRecord = buildRecord(++lastId, fields);
        records.add(newRecord);
        return newRecord;
    }


    private TimeEntryRecord buildRecord(long id, TimeEntryFields fields) {
        return timeEntryRecordBuilder()
            .id(id)
            .projectId(fields.projectId)
            .userId(fields.userId)
            .date(fields.date)
            .hours(fields.hours)
            .build();
    }
}