package io.pivotal.pal.continuum.timesheets.data;

import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.*;

import static io.pivotal.pal.continuum.timesheets.data.TimeEntryRecord.timeEntryRecordBuilder;
import static java.util.Collections.unmodifiableList;

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


    public List<TimeEntryRecord> findAll() {
        return unmodifiableList(new ArrayList<>(records));
    }

    public Optional<TimeEntryRecord> find(long id) {
        return records.stream()
            .filter(record -> record.id == id)
            .findFirst();
    }

    public TimeEntryRecord create(TimeEntryFields fields) {
        TimeEntryRecord newRecord = timeEntryRecordBuilder()
            .id(++lastId)
            .projectId(fields.projectId)
            .userId(fields.userId)
            .date(fields.date)
            .hours(fields.hours)
            .build();

        records.add(newRecord);
        return newRecord;
    }
}
