package org.tomighty.projects;

import org.tomighty.time.Time;

public class Project {
    private String name;
    private Time time;
    private Time totalDailyTime;
    private boolean isFinished;

    private static final int DISPLAY_NAME_TRIM_THRESHOLD = 18;

    public Project(String name, int timeMins) {
        this.name = name;

        time = new Time(timeMins);
        totalDailyTime = time;
    }

    public String getDisplayName() {
        if (name.length() > DISPLAY_NAME_TRIM_THRESHOLD) {
            return name.substring(0, DISPLAY_NAME_TRIM_THRESHOLD) + "...";
        }
        return name;
    }

    public void markFinished() {
        isFinished = true;
    }

    public void updateTime(Time time) {
        this.time = time;
    }

    public Time getTotalDailyTime() {
        return totalDailyTime;
    }

    public Time getTime() {
        return time;
    }

    public String getName() {
        return name;
    }

    public boolean isFinished() {
        return isFinished;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        Project project = (Project) o;
        return name.equals(project.name);
    }

    @Override
    public int hashCode() {
        return name.hashCode();
    }

    @Override
    public String toString() {
        return name;
    }
}
