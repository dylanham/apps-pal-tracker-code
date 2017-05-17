package io.pivotal.pal.continuum.timesheets.data;

import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.Optional;

import static io.pivotal.pal.continuum.timesheets.data.TimeEntryRecord.timeEntryRecordBuilder;

@Repository
public class TimeEntryRepository {

    private final TimeEntryRecord timeEntry = timeEntryRecordBuilder()
        .id(1)
        .projectId(10)
        .userId(20)
        .date(LocalDate.parse("2017-01-30"))
        .hours(8)
        .build();


    public Optional<TimeEntryRecord> find(long id) {
        if (id == timeEntry.id) {
            return Optional.of(timeEntry);
        }

        return Optional.empty();
    }
}
