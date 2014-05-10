package org.dailywork.quartz;

import javax.inject.Inject;
import javax.swing.SwingUtilities;

import org.dailywork.bus.Bus;
import org.dailywork.bus.messages.general.StateReset;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

public class ResetProjectsJob implements Job {

    @Inject
    private Bus bus;

    @Override
    public void execute(JobExecutionContext context) throws JobExecutionException {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                bus.publish(new StateReset());
            }
        });
    }
}
