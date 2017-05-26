package io.pivotal.pal.continuum.backlog;

import java.util.List;

public class StoryInfoList {

    public final List<StoryInfo> stories;

    public StoryInfoList(List<StoryInfo> stories) {
        this.stories = stories;
    }

    @Override
    public String toString() {
        return "StoryInfoList{" +
            "stories=" + stories +
            '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        StoryInfoList that = (StoryInfoList) o;

        return stories != null ? stories.equals(that.stories) : that.stories == null;
    }

    @Override
    public int hashCode() {
        return stories != null ? stories.hashCode() : 0;
    }
}
