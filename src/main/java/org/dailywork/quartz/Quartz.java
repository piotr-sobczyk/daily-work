package org.dailywork.quartz;

import static org.quartz.JobBuilder.newJob;
import static org.quartz.TriggerBuilder.newTrigger;

import javax.annotation.PostConstruct;
import javax.inject.Inject;

import org.quartz.CronScheduleBuilder;
import org.quartz.JobDetail;
import org.quartz.Scheduler;
import org.quartz.SchedulerFactory;
import org.quartz.Trigger;
import org.quartz.impl.StdSchedulerFactory;

public class Quartz {

    @Inject
    private GuiceJobFactory jobFactory;

    @PostConstruct
    public void initialize() {
        schedule();
    }

    public void schedule() {
        try {
            doSchedule();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private void doSchedule() throws Exception {
        SchedulerFactory factory = new StdSchedulerFactory();
        Scheduler scheduler = factory.getScheduler();
        scheduler.setJobFactory(jobFactory);

        JobDetail job = newJob(ResetProjectsJob.class)
                .withIdentity("resetState")
                .build();

        Trigger trigger = newTrigger().withIdentity("resetProjects").
                withSchedule(CronScheduleBuilder.dailyAtHourAndMinute(13, 12)).build();

        scheduler.scheduleJob(job, trigger);
        scheduler.start();
    }

}
