package org.dialywork.quartz;

import javax.inject.Inject;
import javax.swing.SwingUtilities;

import org.dialywork.projects.ProjectsManager;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

public class ResetProjectsJob implements Job {

    @Inject
    private ProjectsManager projectsManager;

    @Override
    public void execute(JobExecutionContext context) throws JobExecutionException {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                projectsManager.resetProjects();
            }
        });
    }
}
