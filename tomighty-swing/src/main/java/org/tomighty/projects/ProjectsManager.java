package org.tomighty.projects;

import javax.annotation.PostConstruct;
import javax.inject.Inject;

import org.tomighty.bus.Bus;
import org.tomighty.bus.Subscriber;
import org.tomighty.bus.messages.projects.ProjectChanged;
import org.tomighty.bus.messages.timer.TimerFinished;
import org.tomighty.bus.messages.ui.ChangeUiState;
import org.tomighty.time.Time;
import org.tomighty.time.Timer;
import org.tomighty.ui.PopupMenu;
import org.tomighty.ui.Window;
import org.tomighty.ui.state.bursts.BurstPaused;
import org.tomighty.util.VisibleForTesting;

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
    private static final int PROJECT_NAME_TRIM_THRESHOLD = 18;

    @PostConstruct
    public void initialize() {
        bus.subscribe(new ChangeProject(), ProjectChanged.class);
        bus.subscribe(new FinishProject(), TimerFinished.class);

        window.setProjectName(INITIAL_PROJECT_NAME);
    }

    @VisibleForTesting
    static String normalizeProjectName(String projectName) {
        if (projectName.length() > PROJECT_NAME_TRIM_THRESHOLD) {
            return projectName.substring(0, PROJECT_NAME_TRIM_THRESHOLD) + "...";
        }
        return projectName;
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

    private class FinishProject implements Subscriber<TimerFinished> {
        @Override
        public void receive(TimerFinished message) {
            popupMenu.markProjectAsFinished(currentProject);
        }
    }

    private void projectChange(Project newProject) {
        updateProject(newProject);
        updateTimer(newProject);

        bus.publish(new ChangeUiState(BurstPaused.class));
        String projectDisplayName = normalizeProjectName(newProject.getName());
        window.setProjectName(projectDisplayName);
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
