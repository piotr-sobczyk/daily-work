package org.tomighty.bus.messages.projects;

import org.tomighty.projects.Project;

public class ProjectTimeChanged {
    private Project project;

    public ProjectTimeChanged(Project project) {
        this.project = project;
    }

    public Project getProject() {
        return project;
    }
}
