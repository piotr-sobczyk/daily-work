package org.tomighty.projects;

import org.tomighty.time.Time;

public class Project {
    private String name;
    private Time time;

    public Project(String name, int time) {
        this.name = name;
        this.time = new Time(0, time);
    }

    public void updateTime(Time time) {
        this.time = time;
    }

    public Time getTime() {
        return time;
    }

    public String getName() {
        return name;
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
