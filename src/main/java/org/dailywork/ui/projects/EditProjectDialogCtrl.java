package org.dailywork.ui.projects;

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
        view.showDialog();
    }

    public void setProject(Project project) {
        this.project = project;

        view.setProjectName(project.getName());
        view.setDailyTime(project.getDailyTimeMins());
    }

    private String validate(String projectName, int dailyTime) {
        return "Validation error";
    }

    public void projectSaveRequested(EditProjectDialog view) {
        String projectName = view.getProjectName();
        int dailyTime = view.getDailyTime();

        String errorMsg = validate(projectName, dailyTime);
        if (errorMsg != null) {
            view.displayErrorMessage(errorMsg);
            return;
        }

        if (project == null) {
            projectsManager.addProject(projectName, dailyTime);
        } else {
            project.setName(projectName);
            project.setDailyTimeMins(dailyTime);
            projectsManager.updateProjectMetadata(project);
        }

        view.hideDialog();
    }

    public void cancelRequested() {
        view.hideDialog();
    }

}
