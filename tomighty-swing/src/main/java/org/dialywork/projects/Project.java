package org.dialywork.projects;

import org.dialywork.bus.Bus;
import org.dialywork.time.Time;

public class Project {
    private String name;
    private Time time;
    private Time totalDailyTime;
    private boolean isFinished;

    private Bus eventBus;

    private static final int DISPLAY_NAME_TRIM_THRESHOLD = 18;

    public Project(String name, int timeMins, Bus eventBus) {
        this.name = name;
        this.eventBus = eventBus;

        time = new Time(timeMins);
        totalDailyTime = new Time(timeMins);
    }

    public String getDisplayName() {
        if (name.length() > DISPLAY_NAME_TRIM_THRESHOLD) {
            return name.substring(0, DISPLAY_NAME_TRIM_THRESHOLD) + "...";
        }
        return name;
    }

    public void markFinished() {
        isFinished = true;

        eventBus.publish(new Updated(this, Updated.ChangeType.STATUS));
    }

    public void reset() {
        isFinished = false;
        time = new Time(totalDailyTime.minutes());

        eventBus.publish(new Updated(this, Updated.ChangeType.STATUS));
        eventBus.publish(new Updated(this, Updated.ChangeType.TIME));
    }

    public void updateTime(Time time) {
        this.time = time;

        eventBus.publish(new Updated(this, Updated.ChangeType.TIME));
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

    public static class Updated extends ModelNotification<Project> {

        public enum ChangeType {
            STATUS, TIME
        }

        private Updated(Project model, ChangeType changeType) {
            super(model, changeType);
        }

    }

}
