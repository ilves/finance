package ee.golive.personal.model;

import java.util.Date;

public class WorkoutStats {

    private long duration;
    private int year;
    private int month;
    private Date date_created;

    public WorkoutStats() {

    }

    public WorkoutStats(long duration, int year, int month, Date date_created) {
        this.duration = duration;
        this.year = year;
        this.month = month;
        this.date_created = date_created;
    }

    public long getDuration() {
        return duration;
    }

    public void setDuration(long duration) {
        this.duration = duration;
    }

    public long getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }

    public long getMonth() {
        return month;
    }

    public void setMonth(int month) {
        this.month = month;
    }

    public String toString() {
        return "Y: " + this.getYear() + " M: " + this.getMonth() + " D: " + this.getDuration();
    }

    public Date getDate_created() {
        return date_created;
    }

    public void setDate_created(Date date_created) {
        this.date_created = date_created;
    }
}
