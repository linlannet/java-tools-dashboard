package io.linlan.executor;

import org.apache.commons.dbcp2.BasicDataSource;
import org.quartz.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.quartz.SchedulerFactoryBean;
import org.springframework.stereotype.Component;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Date;

/**
 *
 * Filename:CleanerService.java
 * Desc:
 *
 * @author hcday
 * @author <a href="mailto:hcday@qq.com">hcday soo</a>
 * CreateTime:2017/12/20 22:10
 *
 * @version 1.0
 * @since 1.0
 *
 */
@Component
public class CleanerService implements InitializingBean {

    private Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private SchedulerFactoryBean schedulerFactoryBean;

    @Autowired
    @Qualifier("h2DataSource")
    private BasicDataSource jdbcDataSource;

    @Value("${dash.aggregator.h2.database.name}")
    private String h2DbName;

    @Value("${dash.aggregator.h2.cleanjob.quartz}")
    private String quartz;

    @Override
    public void afterPropertiesSet() throws Exception {
        logger.info("========================Initialize H2 DataBase CleanerJob=================================");
        Scheduler scheduler = schedulerFactoryBean.getScheduler();
        JobDetail jobDetail = JobBuilder.newJob(CleanJobExecutor.class).build();
        CronTrigger trigger = TriggerBuilder.newTrigger()
                .startAt(new Date())
                .withSchedule(CronScheduleBuilder.cronSchedule(quartz))
                .build();
        scheduler.scheduleJob(jobDetail, trigger);
    }


    protected void cleanDB() {
        try (Connection conn = jdbcDataSource.getConnection();
             Statement statmt = conn.createStatement()) {
            String resetDB = "DROP ALL OBJECTS DELETE FILES";
            logger.info("Execute: {}", resetDB);
            statmt.execute(resetDB);
        } catch (SQLException e) {
            logger.error("", e);
        }
    }

}
