package org.tomighty.bus.messages.projects;

import org.tomighty.projects.Project;

public class ProjectChange {

    private Project newProject;

    public ProjectChange(Project newProject) {
        this.newProject = newProject;
    }

    public Project getNewProject() {
        return newProject;
    }
}
