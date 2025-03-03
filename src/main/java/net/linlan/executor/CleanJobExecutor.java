package net.linlan.executor;

import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.quartz.SchedulerException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;

/**
 *
 * Filename:CleanJobExecutor.java
 * Desc:
 *
 * @author Linlan
 * CreateTime:2017/12/20 22:10
 *
 * @version 1.0
 * @since 1.0
 *
 */
public class CleanJobExecutor implements Job {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Override
    public void execute(JobExecutionContext jobExecutionContext) throws JobExecutionException {
        try {
            ApplicationContext springAppContext = (ApplicationContext) jobExecutionContext.getScheduler().getContext().get("applicationContext");
            CleanerService cleanerService = springAppContext.getBean(CleanerService.class);
            cleanerService.cleanDB();
        } catch (SchedulerException e) {
            logger.error("", e);
        }
    }
}
