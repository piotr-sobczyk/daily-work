package org.dailywork.ui.projects;

import java.util.HashSet;
import java.util.Set;

import javax.inject.Inject;

import org.dailywork.projects.Project;
import org.dailywork.projects.ProjectsManager;
import org.dailywork.resources.Resources;

public class EditProjectDialogCtrl {

    @Inject
    private ProjectsManager projectsManager;

    private Project project;

    private EditProjectDialog view;

    @Inject
    public EditProjectDialogCtrl(Resources resources) {
        view = new EditProjectDialog(this, resources);
    }

    public void showDialog() {
        if (project == null) {
            view.showAddProjectDialog();
        } else {
            view.showEditProjectDialog();
        }
    }

    public void setProject(Project project) {
        this.project = project;

        view.setProjectName(project.getName());
        view.setDailyTime(project.getDailyTimeMins());
    }

    private String validate(String projectName) {
        if (projectName.isEmpty()) {
            return "Project name must be specified";
        }

        Set<Project> otherProjects = new HashSet<>(projectsManager.getProjects());
        otherProjects.remove(project);
        for (Project otherProject : otherProjects) {
            if (otherProject.getName().equals(projectName)) {
                return "Other project already uses this name";
            }
        }

        //No need to validate dailyTime as Swing spinner component
        //already enforces reasonable value

        return null;
    }

    public void projectSaveRequested(EditProjectDialog view) {
        String projectName = view.getProjectName();
        int dailyTime = view.getDailyTime();

        String errorMsg = validate(projectName);
        if (errorMsg != null) {
            view.displayErrorMessage(errorMsg);
            return;
        }

        addOrUpdateProject(projectName, dailyTime);
        view.hideDialog();
    }

    private void addOrUpdateProject(String projectName, int dailyTime) {
        if (project == null) {
            projectsManager.addProject(projectName, dailyTime);
        } else {
            project.setName(projectName);
            project.setDailyTimeMins(dailyTime);
            projectsManager.updateProjectMetadata(project);
        }
    }

    public void cancelRequested() {
        view.hideDialog();
    }

}
