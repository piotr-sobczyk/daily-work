package org.dailywork.config;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import javax.annotation.PostConstruct;
import javax.inject.Inject;

import org.dailywork.bus.Bus;
import org.dailywork.projects.Project;

public class ProjectsLoader {
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

    public List<Project> loadProjects() {
        List<Project> projects = new ArrayList<Project>();
        for (String projectName : properties.stringPropertyNames()) {
            int dailyTimeMins = Integer.parseInt(properties.getProperty(projectName));
            projects.add(new Project(projectName, dailyTimeMins, bus));
        }
        return projects;
    }

}
