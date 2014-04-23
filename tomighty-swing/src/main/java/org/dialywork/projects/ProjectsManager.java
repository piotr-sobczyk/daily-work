package org.dialywork.projects;

import javax.annotation.PostConstruct;
import javax.inject.Inject;

import org.dialywork.bus.Bus;
import org.dialywork.bus.Subscriber;
import org.dialywork.bus.messages.ui.ChangeUiState;
import org.dialywork.bus.timer.TimerFinished;
import org.dialywork.bus.timer.TimerTick;
import org.dialywork.time.Time;
import org.dialywork.time.Timer;
import org.dialywork.ui.PopupMenu;
import org.dialywork.ui.Window;
import org.dialywork.ui.state.bursts.BurstPaused;

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
            }
        }
    }

    private class FinishProject implements Subscriber<TimerFinished> {
        @Override
        public void receive(TimerFinished message) {
            currentProject.markFinished();
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
