package org.tomighty.bus.messages.ui;

import org.tomighty.projects.Project;

public class ProjectChanged {

    private Project newProject;

    public ProjectChanged(Project newProject) {
        this.newProject = newProject;
    }

    public Project getNewProject() {
        return newProject;
    }
}
