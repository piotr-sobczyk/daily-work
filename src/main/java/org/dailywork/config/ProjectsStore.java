package org.dailywork.config;

import java.util.List;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.inject.Inject;

import com.google.common.eventbus.EventBus;
import org.dailywork.projects.Project;

import com.db4o.Db4oEmbedded;
import com.db4o.ObjectContainer;

public class ProjectsStore {
    @Inject
    private Directories directories;
    @Inject
    private EventBus eventBus;

    private ObjectContainer database;

    @PostConstruct
    public void init(){
        database =  Db4oEmbedded.openFile(Db4oEmbedded.newConfiguration(), "test.db");
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
