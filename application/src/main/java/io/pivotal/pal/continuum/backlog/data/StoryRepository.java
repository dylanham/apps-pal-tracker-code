package io.pivotal.pal.continuum.backlog.data;

import org.springframework.stereotype.Repository;

import java.util.*;

import static io.pivotal.pal.continuum.backlog.data.StoryRecord.storyRecordBuilder;
import static java.util.Collections.unmodifiableList;

@Repository
public class StoryRepository {

    private final Set<StoryRecord> records = new HashSet<StoryRecord>() {{
        add(storyRecordBuilder()
            .id(1)
            .projectId(10)
            .name("Initialize Git Repo")
            .build());
    }};

    private long lastId = 1L;


    public List<StoryRecord> findAll() {
        return unmodifiableList(new ArrayList<>(records));
    }

    public Optional<StoryRecord> find(long id) {
        return records.stream()
            .filter(record -> record.id == id)
            .findFirst();
    }

    public StoryRecord create(StoryFields fields) {
        StoryRecord newRecord = buildRecord(++lastId, fields);
        records.add(newRecord);
        return newRecord;
    }

    public StoryRecord update(long id, StoryFields fields) {
        find(id).ifPresent(record -> {
            records.remove(record);
            records.add(buildRecord(id, fields));
        });

        return find(id).orElse(null);
    }

    public void delete(long id) {
        find(id).ifPresent(records::remove);
    }


    private StoryRecord buildRecord(long id, StoryFields fields) {
        return storyRecordBuilder()
            .id(id)
            .projectId(fields.projectId)
            .name(fields.name)
            .build();
    }
}
