package org.dialywork.config;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import javax.annotation.PostConstruct;
import javax.inject.Inject;

import org.dialywork.bus.Bus;
import org.dialywork.projects.Project;

public class Projects {
    @Inject
    private Directories directories;
    @Inject
    private PropertyStore propertyStore;
    @Inject
    private Bus bus;

    private File projectsFile;
    private Properties properties;

    @PostConstruct
    public void initialize() {
        projectsFile = new File(directories.configuration(), "projects.conf");
        properties = propertyStore.load(projectsFile);
    }

    public List<Project> getProjects() {
        List<Project> projects = new ArrayList<Project>();
        for (String projectName : properties.stringPropertyNames()) {
            int dialyTimeMins = Integer.parseInt(properties.getProperty(projectName));
            projects.add(new Project(projectName, dialyTimeMins, bus));
        }
        return projects;
    }

    public int getTimeInMinutesForProject(String projectName) {
        return Integer.parseInt(properties.getProperty(projectName));
    }

}
