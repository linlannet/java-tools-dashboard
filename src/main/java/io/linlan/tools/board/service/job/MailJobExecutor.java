package io.linlan.tools.board.service.job;

import io.linlan.tools.board.entity.DashOperationJob;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.quartz.SchedulerException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;

/**
 * 
 * Filename:MailJobExecutor.java
 * Desc:
 *
 * @author Linlan
 * CreateTime:2017/12/19 16:20
 *
 * @version 1.0
 * @since 1.0
 *
 */
public class MailJobExecutor implements Job {
    private Logger logger = LoggerFactory.getLogger(this.getClass());
    @Override
    public void execute(JobExecutionContext jobExecutionContext) throws JobExecutionException {
        try {
            DashOperationJobService dashboardJobService = ((ApplicationContext) jobExecutionContext.getScheduler().getContext().get("applicationContext")).getBean(DashOperationJobService.class);
            dashboardJobService.sendMail((DashOperationJob) jobExecutionContext.getMergedJobDataMap().get("job"));
        } catch (SchedulerException e) {
            logger.error("", e);
        }
    }
}
