package org.dailywork.projects;

import org.dailywork.bus.Bus;
import org.dailywork.time.Time;

public class ProjectProgress {
    private Project project;
    private Time time;
    private boolean isFinished;

    private Bus eventBus;

    public ProjectProgress(Project project, int timeMins, Bus eventBus) {
        this.project = project;
        this.eventBus = eventBus;

        time = new Time(timeMins);
    }

    public Project getProject() {
        return project;
    }

    public void reset() {
        isFinished = false;
        time = new Time(project.getDailyTimeMins());

        eventBus.publish(new Updated(this, Updated.ChangeType.STATUS));
        eventBus.publish(new Updated(this, Updated.ChangeType.TIME));
    }

    public void updateTime(Time time) {
        this.time = time;

        eventBus.publish(new Updated(this, Updated.ChangeType.TIME));
    }

    public Time getTime() {
        return time;
    }

    public void markFinished() {
        isFinished = true;
        eventBus.publish(new Updated(this, Updated.ChangeType.STATUS));
    }

    public boolean isStarted() {
        return time.minutes() < project.getDailyTimeMins();
    }

    public boolean isFinished() {
        return isFinished;
    }

    public static class Updated extends ModelNotification<ProjectProgress> {

        public enum ChangeType {
            STATUS, TIME
        }

        private Updated(ProjectProgress model, ChangeType changeType) {
            super(model, changeType);
        }

    }


}
