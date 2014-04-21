package org.tomighty.projects;

import org.tomighty.time.Time;

public class Project {
    private String name;
    private Time time;
    private Time totalDialyTime;
    private boolean isFinished;

    public Project(String name, int timeMins) {
        this.name = name;

        time = new Time(timeMins);
        totalDialyTime = time;
    }

    public void markFinished() {
        isFinished = true;
    }

    public void updateTime(Time time) {
        this.time = time;
    }

    public Time getTotalDialyTime() {
        return totalDialyTime;
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
