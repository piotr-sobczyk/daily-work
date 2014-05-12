package org.dailywork.config;

import java.io.File;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.inject.Inject;

import org.dailywork.projects.Project;

import com.db4o.Db4oEmbedded;
import com.db4o.ObjectContainer;
import com.google.common.eventbus.EventBus;

public class ProjectsStore {
    @Inject
    private Directories directories;
    @Inject
    private EventBus eventBus;

    private ObjectContainer database;

    @PostConstruct
    public void init(){
        File databaseFile = new File(directories.configuration(), "database.db");
        database = Db4oEmbedded.openFile(databaseFile.getAbsolutePath());
    }

    @PreDestroy
    public void destroy(){
        database.close();
    }

    public List<Project> loadProjects() {
        return database.query(Project.class);
    }

    public void saveProject(Project project) {
        database.store(project);
        database.commit();
    }

    public void removeProject(Project project) {
        database.delete(project);
        database.commit();
    }

}
