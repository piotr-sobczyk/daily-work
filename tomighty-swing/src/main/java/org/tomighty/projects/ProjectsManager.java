package org.tomighty.projects;

import javax.annotation.PostConstruct;
import javax.inject.Inject;

import org.tomighty.bus.Bus;
import org.tomighty.bus.Subscriber;
import org.tomighty.bus.messages.ui.ChangeUiState;
import org.tomighty.bus.messages.ui.ProjectChanged;
import org.tomighty.time.Time;
import org.tomighty.time.Timer;
import org.tomighty.ui.Window;
import org.tomighty.ui.state.bursts.BurstPaused;

public class ProjectsManager {

    @Inject
    private Bus bus;
    @Inject
    private Timer timer;
    @Inject
    private Window window;

    private Project currentProject;

    @PostConstruct
    public void initialize() {
        bus.subscribe(new ChangeProject(), ProjectChanged.class);
    }

    private class ChangeProject implements Subscriber<ProjectChanged> {
        @Override
        public void receive(ProjectChanged message) {
            Project newProject = message.getNewProject();
            if (!newProject.equals(currentProject)) {
                projectChange(newProject);
            }
        }
    }

    private void projectChange(Project newProject) {
        updateProject(newProject);
        updateTimer(newProject);

        bus.publish(new ChangeUiState(BurstPaused.class));
        window.setProjectName(newProject.getName());
    }

    private void updateTimer(Project newProject) {
        Time time = newProject.getTime();
        timer.setTime(time);
        timer.pause();
    }

    private void updateProject(Project newProject) {
        if (currentProject != null) {
            currentProject.updateTime(timer.getTime());
        }
        currentProject = newProject;
    }

}
