package org.tomighty.config;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import javax.annotation.PostConstruct;
import javax.inject.Inject;

public class Projects {
    @Inject
    private Directories directories;
    @Inject
    private PropertyStore propertyStore;

    private File projectsFile;
    private Properties properties;

    @PostConstruct
    public void initialize() {
        projectsFile = new File(directories.configuration(), "projects.conf");
        properties = propertyStore.load(projectsFile);
    }

    public List<String> getProjects() {
        return new ArrayList<String>(properties.stringPropertyNames());
    }

    public int getTimeInMinutesForProject(String projectName) {
        return Integer.parseInt(properties.getProperty(projectName));
    }

}
