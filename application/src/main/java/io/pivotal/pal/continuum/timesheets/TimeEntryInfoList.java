package io.pivotal.pal.continuum.timesheets;

import java.util.List;

public class TimeEntryInfoList {

    public final List<TimeEntryInfo> timeEntries;

    public TimeEntryInfoList(List<TimeEntryInfo> timeEntries) {
        this.timeEntries = timeEntries;
    }

    @Override
    public String toString() {
        return "TimeEntryInfoList{" +
            "timeEntries=" + timeEntries +
            '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        TimeEntryInfoList that = (TimeEntryInfoList) o;

        return timeEntries != null ? timeEntries.equals(that.timeEntries) : that.timeEntries == null;
    }

    @Override
    public int hashCode() {
        return timeEntries != null ? timeEntries.hashCode() : 0;
    }
}
