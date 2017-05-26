package io.pivotal.pal.continuum.timesheets;

import io.pivotal.pal.continuum.timesheets.data.TimeEntryRecord;
import io.pivotal.pal.continuum.timesheets.data.TimeEntryRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static io.pivotal.pal.continuum.timesheets.TimeEntryInfo.timeEntryInfoBuilder;


@RestController
@RequestMapping("/time-entries")
public class TimeEntriesController {

    private final TimeEntryRepository repository;

    public TimeEntriesController(TimeEntryRepository repository) {
        this.repository = repository;
    }


    @GetMapping("/{id}")
    public ResponseEntity<TimeEntryInfo> show(@PathVariable long id) {
        return repository.find(id)
            .map(record -> new ResponseEntity<>(present(record), HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }


    private TimeEntryInfo present(TimeEntryRecord record) {
        return timeEntryInfoBuilder()
            .id(record.id)
            .projectId(record.projectId)
            .userId(record.userId)
            .date(record.date.toString())
            .hours(record.hours)
            .build();
    }
}
