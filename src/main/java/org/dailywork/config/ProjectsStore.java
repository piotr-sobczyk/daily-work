package org.dailywork.config;

import java.util.List;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.inject.Inject;

import org.dailywork.bus.Bus;
import org.dailywork.projects.Project;

import com.db4o.Db4oEmbedded;
import com.db4o.ObjectContainer;

public class ProjectsStore {
    @Inject
    private Directories directories;
    @Inject
    private Bus bus;

    private ObjectContainer database;

    @PostConstruct
    public void init(){
        database =  Db4oEmbedded.openFile(Db4oEmbedded.newConfiguration(), "test.db");
    }

    @PreDestroy
    public void destroy(){
        System.out.println("destroying...");
        database.close();
    }

    public List<Project> loadProjects() {
        List<Project> queryResult = database.query(Project.class);
        return queryResult;
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
