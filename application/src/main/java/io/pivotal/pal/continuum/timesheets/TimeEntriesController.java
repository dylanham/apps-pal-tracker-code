package io.pivotal.pal.continuum.timesheets;

import io.pivotal.pal.continuum.timesheets.data.TimeEntryFields;
import io.pivotal.pal.continuum.timesheets.data.TimeEntryRecord;
import io.pivotal.pal.continuum.timesheets.data.TimeEntryRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

import static io.pivotal.pal.continuum.timesheets.TimeEntryInfo.timeEntryInfoBuilder;
import static io.pivotal.pal.continuum.timesheets.data.TimeEntryFields.timeEntryFieldsBuilder;
import static java.util.stream.Collectors.toList;


@RestController
@RequestMapping("/time-entries")
public class TimeEntriesController {

    private final TimeEntryRepository repository;

    public TimeEntriesController(TimeEntryRepository repository) {
        this.repository = repository;
    }


    @GetMapping
    public ResponseEntity<TimeEntryInfoList> list() {
        List<TimeEntryInfo> timeEntries = repository.findAll()
            .stream()
            .map(this::present)
            .collect(toList());
        TimeEntryInfoList timeEntryInfoList = new TimeEntryInfoList(timeEntries);

        return new ResponseEntity<>(timeEntryInfoList, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<TimeEntryInfo> show(@PathVariable long id) {
        return repository.find(id)
            .map(record -> new ResponseEntity<>(present(record), HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @PostMapping
    public ResponseEntity<TimeEntryInfo> create(@RequestBody TimeEntryForm form) {
        TimeEntryRecord record = repository.create(formToFields(form));

        return new ResponseEntity<>(present(record), HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<TimeEntryInfo> update(@PathVariable long id, @RequestBody TimeEntryForm form) {
        TimeEntryRecord record = repository.update(id, formToFields(form));

        return new ResponseEntity<>(present(record), HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity delete(@PathVariable long id) {
        repository.delete(id);
        return new ResponseEntity(HttpStatus.OK);
    }


    private TimeEntryFields formToFields(TimeEntryForm form) {
        return timeEntryFieldsBuilder()
            .projectId(form.projectId)
            .userId(form.userId)
            .date(LocalDate.parse(form.date))
            .hours(form.hours)
            .build();
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
