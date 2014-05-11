package org.dailywork.projects;

import com.google.common.eventbus.EventBus;
import org.dailywork.time.Time;

public class ProjectProgress {
    private Project project;
    private Time time;
    private boolean isFinished;

    private EventBus eventBus;

    public ProjectProgress(Project project, int timeMins, EventBus eventBus) {
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

        eventBus.post(new Updated(this, Updated.ChangeType.STATUS));
        eventBus.post(new Updated(this, Updated.ChangeType.TIME));
    }

    public void updateTime(Time time) {
        this.time = time;

        eventBus.post(new Updated(this, Updated.ChangeType.TIME));
    }

    public Time getTime() {
        return time;
    }

    public void markFinished() {
        isFinished = true;
        eventBus.post(new Updated(this, Updated.ChangeType.STATUS));
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
