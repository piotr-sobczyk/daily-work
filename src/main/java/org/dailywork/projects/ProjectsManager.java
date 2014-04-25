package org.dailywork.projects;

import java.util.List;

import javax.annotation.PostConstruct;
import javax.inject.Inject;

import org.dailywork.bus.Bus;
import org.dailywork.bus.Subscriber;
import org.dailywork.bus.messages.ui.ChangeUiState;
import org.dailywork.bus.timer.TimerFinished;
import org.dailywork.bus.timer.TimerTick;
import org.dailywork.config.ProjectsLoader;
import org.dailywork.time.Time;
import org.dailywork.time.Timer;
import org.dailywork.ui.PopupMenu;
import org.dailywork.ui.Window;
import org.dailywork.ui.state.bursts.BurstPaused;

public class ProjectsManager {

    @Inject
    private Bus bus;
    @Inject
    private Timer timer;
    @Inject
    private Window window;
    @Inject
    private PopupMenu popupMenu;

    private List<Project> projects;
    private Project currentProject;

    public static final String INITIAL_PROJECT_NAME = "Select a project";

    @Inject
    public ProjectsManager(ProjectsLoader projectsLoader) {
        projects = projectsLoader.loadProjects();
    }

    @PostConstruct
    public void initialize() {
        bus.subscribe(new FinishProject(), TimerFinished.class);
        bus.subscribe(new UpdateTime(), TimerTick.class);

        window.setProjectName(INITIAL_PROJECT_NAME);
    }

    public List<Project> getProjects() {
        return projects;
    }

    public void resetProjects() {
        for (Project project : projects) {
            project.reset();
        }
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
