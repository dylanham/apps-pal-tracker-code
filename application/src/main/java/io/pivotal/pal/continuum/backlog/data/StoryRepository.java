package io.pivotal.pal.continuum.backlog.data;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface StoryRepository extends CrudRepository<StoryRecord, Long> {
}
