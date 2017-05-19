package io.pivotal.pal.continuum.backlog.data;

import javax.persistence.*;

@Entity
@Table(name = "story")
public class StoryRecord {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    public final Long id;
    public final long projectId;
    public final String name;

    private StoryRecord(Builder builder) {
        id = builder.id;
        projectId = builder.projectId;
        name = builder.name;
    }

    private StoryRecord() {
        this(storyRecordBuilder());
    }

    public static Builder storyRecordBuilder() {
        return new Builder();
    }

    public static class Builder {
        private Long id;
        private long projectId;
        private String name;

        public StoryRecord build() {
            return new StoryRecord(this);
        }

        public Builder id(Long id) {
            this.id = id;
            return this;
        }

        public Builder projectId(long projectId) {
            this.projectId = projectId;
            return this;
        }

        public Builder name(String name) {
            this.name = name;
            return this;
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        StoryRecord that = (StoryRecord) o;

        if (projectId != that.projectId) return false;
        if (id != null ? !id.equals(that.id) : that.id != null) return false;
        return name != null ? name.equals(that.name) : that.name == null;
    }

    @Override
    public int hashCode() {
        int result = id != null ? id.hashCode() : 0;
        result = 31 * result + (int) (projectId ^ (projectId >>> 32));
        result = 31 * result + (name != null ? name.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "StoryRecord{" +
            "id=" + id +
            ", projectId=" + projectId +
            ", name='" + name + '\'' +
            '}';
    }
}
