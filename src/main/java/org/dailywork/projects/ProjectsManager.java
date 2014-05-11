package org.dailywork.projects;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.inject.Inject;

import com.google.common.eventbus.EventBus;
import com.google.common.eventbus.Subscribe;
import org.dailywork.bus.messages.general.StateReset;
import org.dailywork.bus.messages.ui.ChangeUiState;
import org.dailywork.bus.timer.TimerFinished;
import org.dailywork.bus.timer.TimerTick;
import org.dailywork.config.ProjectsStore;
import org.dailywork.time.Time;
import org.dailywork.time.Timer;
import org.dailywork.ui.PopupMenu;
import org.dailywork.ui.UiState;
import org.dailywork.ui.Window;
import org.dailywork.ui.state.InitialState;
import org.dailywork.ui.state.work.WorkPaused;
import org.dailywork.ui.state.work.WorkToBeStarted;

public class ProjectsManager {

    @Inject
    private Timer timer;
    @Inject
    private Window window;
    @Inject
    private PopupMenu popupMenu;

    private ProjectsStore projectsStore;
    private EventBus eventBus;

    private List<Project> projects;
    private Map<Project, ProjectProgress> projectProgresses = new HashMap<>();

    private Project currentProject;
    private ProjectProgress currentProjectProgress;

    public ProjectProgress getProgressForProject(Project project) {
        return projectProgresses.get(project);
    }

    @Inject
    public ProjectsManager(ProjectsStore projectsStore, EventBus eventBus) {
        this.projectsStore = projectsStore;
        this.eventBus = eventBus;

        reloadProjects();

        for (Project project : projects) {
            projectProgresses.put(project, createProjectStatus(project));
        }

        eventBus.register(this);
    }

    private void reloadProjects() {
        projects = projectsStore.loadProjects();
    }

    public void addProject(String projectName, int dailyTimeInMins) {
        Project project = new Project(projectName, dailyTimeInMins);
        //TODO: add checking if doesn't exist
        projectsStore.saveProject(project);

        ProjectProgress projectProgress = createProjectStatus(project);

        reloadProjects();
        projectProgresses.put(project, projectProgress);

        popupMenu.reloadMenu();
    }

    private ProjectProgress createProjectStatus(Project project) {
        return new ProjectProgress(project, project.getDailyTimeMins(), eventBus);
    }


    public void removeProject(Project project) {
        projectsStore.removeProject(project);

        reloadProjects();
        projectProgresses.remove(project);

        popupMenu.reloadMenu();
    }

    public void updateProjectMetadata(Project project) {
        projectsStore.saveProject(project);
        //TODO: handle this!
        reloadProjects();
        popupMenu.reloadMenu();
    }

    public List<Project> getProjects() {
        return projects;
    }

    @Subscribe
    public void resetState(StateReset message) {
        for (ProjectProgress projectProgress : projectProgresses.values()) {
            projectProgress.reset();
        }
        timer.pause();
        eventBus.post(new ChangeUiState(InitialState.class));
    }

    @Subscribe
    public void updateTime(TimerTick tick) {
        final Time time = tick.getTime();
        if (currentProjectProgress != null) {
            currentProjectProgress.updateTime(time);
        }
    }

    @Subscribe
    public void finishProject(TimerFinished message) {
        currentProjectProgress.markFinished();
    }

    public void changeProject(Project newProject) {
        currentProject = newProject;
        currentProjectProgress = projectProgresses.get(newProject);

        updateTimer(currentProjectProgress);

        Class<? extends UiState> nextState = currentProjectProgress.isStarted() ? WorkPaused.class : WorkToBeStarted.class;
        eventBus.post(new ChangeUiState(nextState));
        window.setProjectName(newProject.getDisplayName());
    }

    private void updateTimer(ProjectProgress newProjectProgress) {
        Time time = newProjectProgress.getTime();
        timer.setTime(time);
        timer.pause();
    }

}
