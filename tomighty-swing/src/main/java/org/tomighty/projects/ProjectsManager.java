package org.tomighty.projects;

import javax.annotation.PostConstruct;
import javax.inject.Inject;

import org.tomighty.bus.Bus;
import org.tomighty.bus.Subscriber;
import org.tomighty.bus.messages.projects.ProjectTimeChanged;
import org.tomighty.bus.messages.timer.TimerFinished;
import org.tomighty.bus.messages.timer.TimerTick;
import org.tomighty.bus.messages.ui.ChangeUiState;
import org.tomighty.time.Time;
import org.tomighty.time.Timer;
import org.tomighty.ui.PopupMenu;
import org.tomighty.ui.Window;
import org.tomighty.ui.state.bursts.BurstPaused;

public class ProjectsManager {

    @Inject
    private Bus bus;
    @Inject
    private Timer timer;
    @Inject
    private Window window;
    @Inject
    private PopupMenu popupMenu;

    private Project currentProject;

    public static final String INITIAL_PROJECT_NAME = "Select a project";

    @PostConstruct
    public void initialize() {
        bus.subscribe(new FinishProject(), TimerFinished.class);
        bus.subscribe(new UpdateTime(), TimerTick.class);

        window.setProjectName(INITIAL_PROJECT_NAME);
    }

    private class UpdateTime implements Subscriber<TimerTick> {
        @Override
        public void receive(TimerTick tick) {
            final Time time = tick.getTime();
            if (currentProject != null) {
                currentProject.updateTime(time);
                bus.publish(new ProjectTimeChanged(currentProject));
            }
        }
    }

    private class FinishProject implements Subscriber<TimerFinished> {
        @Override
        public void receive(TimerFinished message) {
            currentProject.markFinished();
            popupMenu.markProjectAsFinished(currentProject);
        }
    }

    public void changeProject(Project newProject) {
        currentProject = newProject;
        updateTimer(newProject);

        bus.publish(new ChangeUiState(BurstPaused.class));
        window.setProjectName(newProject.getDisplayName());
    }

    private void updateTimer(Project newProject) {
        Time time = newProject.getTime();
        timer.setTime(time);
        timer.pause();
    }

}
