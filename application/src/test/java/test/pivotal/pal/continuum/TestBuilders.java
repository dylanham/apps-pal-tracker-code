package test.pivotal.pal.continuum;

import io.pivotal.pal.continuum.timesheets.TimeEntryForm;
import io.pivotal.pal.continuum.timesheets.TimeEntryInfo;
import io.pivotal.pal.continuum.timesheets.data.TimeEntryFields;
import io.pivotal.pal.continuum.timesheets.data.TimeEntryRecord;

import java.time.LocalDate;

import static io.pivotal.pal.continuum.timesheets.TimeEntryForm.timeEntryFormBuilder;
import static io.pivotal.pal.continuum.timesheets.TimeEntryInfo.timeEntryInfoBuilder;
import static io.pivotal.pal.continuum.timesheets.data.TimeEntryFields.timeEntryFieldsBuilder;
import static io.pivotal.pal.continuum.timesheets.data.TimeEntryRecord.timeEntryRecordBuilder;

public class TestBuilders {

    public static TimeEntryRecord.Builder testTimeEntryRecordBuilder() {
        return timeEntryRecordBuilder()
            .id(100)
            .projectId(2000)
            .userId(1000)
            .date(LocalDate.parse("2017-01-31"))
            .hours(8);
    }

    public static TimeEntryInfo.Builder testTimeEntryInfoBuilder() {
        return timeEntryInfoBuilder()
            .id(100)
            .projectId(2000)
            .userId(1000)
            .date("2017-01-31")
            .hours(8);
    }

    public static TimeEntryFields.Builder testTimeEntryFieldsBuilder() {
        return timeEntryFieldsBuilder()
            .projectId(2000)
            .userId(1000)
            .date(LocalDate.parse("2017-01-31"))
            .hours(8);
    }

    public static TimeEntryForm.Builder testTimeEntryFormBuilder() {
        return timeEntryFormBuilder()
            .projectId(2000)
            .userId(1000)
            .date("2017-01-31")
            .hours(8);
    }
}
