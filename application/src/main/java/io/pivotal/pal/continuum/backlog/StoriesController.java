package io.pivotal.pal.continuum.backlog;

import io.pivotal.pal.continuum.backlog.data.StoryFields;
import io.pivotal.pal.continuum.backlog.data.StoryRecord;
import io.pivotal.pal.continuum.backlog.data.StoryRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static io.pivotal.pal.continuum.backlog.StoryInfo.storyInfoBuilder;
import static io.pivotal.pal.continuum.backlog.data.StoryFields.storyFieldsBuilder;
import static java.util.stream.Collectors.toList;


@RestController
@RequestMapping("/stories")
public class StoriesController {

    private final StoryRepository repository;

    public StoriesController(StoryRepository repository) {
        this.repository = repository;
    }


    @GetMapping
    public ResponseEntity<StoryInfoList> list() {
        List<StoryInfo> timeEntries = repository.findAll()
            .stream()
            .map(this::present)
            .collect(toList());
        StoryInfoList storyInfoList = new StoryInfoList(timeEntries);

        return new ResponseEntity<>(storyInfoList, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<StoryInfo> show(@PathVariable long id) {
        return repository.find(id)
            .map(record -> new ResponseEntity<>(present(record), HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @PostMapping
    public ResponseEntity<StoryInfo> create(@RequestBody StoryForm form) {
        StoryRecord record = repository.create(formToFields(form));

        return new ResponseEntity<>(present(record), HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<StoryInfo> update(@PathVariable long id, @RequestBody StoryForm form) {
        StoryRecord record = repository.update(id, formToFields(form));

        return new ResponseEntity<>(present(record), HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity delete(@PathVariable long id) {
        repository.delete(id);
        return new ResponseEntity(HttpStatus.OK);
    }


    private StoryFields formToFields(StoryForm form) {
        return storyFieldsBuilder()
            .projectId(form.projectId)
            .name(form.name)
            .build();
    }

    private StoryInfo present(StoryRecord record) {
        return storyInfoBuilder()
            .id(record.id)
            .projectId(record.projectId)
            .name(record.name)
            .build();
    }
}
