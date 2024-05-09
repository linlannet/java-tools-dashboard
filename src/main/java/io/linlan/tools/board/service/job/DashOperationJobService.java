package io.linlan.tools.board.service.job;

import io.linlan.tools.board.dto.ViewDashOperationJob;
import io.linlan.tools.board.service.DashMailService;
import org.apache.commons.lang3.exception.ExceptionUtils;
import io.linlan.commons.core.Rcode;
import org.quartz.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.quartz.SchedulerFactoryBean;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import io.linlan.tools.board.dao.DashOperationJobDao;
import io.linlan.tools.board.entity.DashOperationJob;

/**
 *
 * Filename:DashOperationJob.java
 * Desc:
 *
 * @author Linlan
 * CreateTime:2017-12-18 20:35:17
 *
 * @version 1.0
 * @since 1.0
 *
 */
@Service
public class DashOperationJobService implements InitializingBean {

    private Logger logger = LoggerFactory.getLogger(this.getClass());

    /** get the list of entity DashOperationJob
     * 列表方法，返回{@link List<  DashOperationJob  >}
     * @param map the input select conditions
     * @return {@link List<  DashOperationJob  >}
     */
    public List<DashOperationJob> getList(Map<String, Object> map){
        return dao.getList(map);
    }

    /** find the entity by input id, return entity
     * 对象详情方法，通过id查询对象{@link DashOperationJob}
     * @param id the input id
     * @return {@link DashOperationJob}
     */
    public DashOperationJob findById(Long id){
        return dao.findById(id);
    }

    /** save the entity with input object
     * 保存对象方法
     * @param dashboardJob the input dashboardJob
     */
    public void save(DashOperationJob dashboardJob){
        dashboardJob.init();
        dao.save(dashboardJob);
    }

    /** update the entity with input object
     * 更新对象方法
     * @param dashboardJob the input dashboardJob
     */
    public void update(DashOperationJob dashboardJob){
        dashboardJob.setLastExecTime(new Timestamp(System.currentTimeMillis()));
        dao.update(dashboardJob);
    }

    /** delete the entity by input id
     * 删除方法，通过id删除对象
     * @param id the input id
     */
    public void deleteById(Long id){
        dao.deleteById(id);
    }

    /** batch delete the entity by input ids
     * 批量删除方法，通过ids删除对象
     * @param ids the input ids
     */
    public void deleteByIds(Long[] ids){
        dao.deleteByIds(ids);
    }

    /** query the total count by input select conditions
     * 通过输入的条件查询记录总数
     * @param map the input select conditions
     * @return total count
     */
    public int getCount(Map<String, Object> map){
        return dao.getCount(map);
    }

    public Rcode exec(Long id) {
        DashOperationJob job = dao.findById(id);
        new Thread(() ->
                sendMail(job)
        ).start();
        return Rcode.ok();
    }

    public void configScheduler() {
        Scheduler scheduler = schedulerFactoryBean.getScheduler();

        try {
            scheduler.clear();
        } catch (SchedulerException e) {
            logger.error("" , e);
        }
        Map<String, Object> params = new HashMap<>();
        params.put("adminId", adminUserId);
        List<DashOperationJob> jobList = dao.getList(params);
        for (DashOperationJob job : jobList) {
            try {
                long startTimeStamp = job.getStartDate().getTime();
                long endTimeStamp = job.getEndDate().getTime();
                if (endTimeStamp < System.currentTimeMillis()) {
                    // Skip expired job
                    continue;
                }
                JobDetail jobDetail = JobBuilder.newJob(getJobExecutor(job)).withIdentity(job.getId().toString()).build();
                CronTrigger trigger = TriggerBuilder.newTrigger()
                        .startAt(new Date().getTime() < startTimeStamp ? job.getStartDate() : new Date())
                        .withSchedule(CronScheduleBuilder.cronSchedule(job.getCronExp()))
                        .endAt(job.getEndDate())
                        .build();
                jobDetail.getJobDataMap().put("job", job);
                scheduler.scheduleJob(jobDetail, trigger);
            } catch (SchedulerException e) {
                logger.error("{} Job id: {}", e.getMessage(), job.getId());
            } catch (Exception e) {
                logger.error("" , e);
            }
        }
    }

    private Class<? extends Job> getJobExecutor(DashOperationJob job) {
        switch (job.getJobType()) {
            case "mail":
                return MailJobExecutor.class;
        }
        return null;
    }

    protected void sendMail(DashOperationJob job) {
        dao.updateLastExecTime(job.getId(), new Date());
        try {
            dao.updateStatus(job.getId(), ViewDashOperationJob.STATUS_PROCESSING, "");
            dashMailService.sendDashboard(job);
            dao.updateStatus(job.getId(), ViewDashOperationJob.STATUS_FINISH, "");
        } catch (Exception e) {
            logger.error("" , e);
            dao.updateStatus(job.getId(), ViewDashOperationJob.STATUS_FAIL, ExceptionUtils.getStackTrace(e));
        }
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        configScheduler();
    }

    public int checkJobRole(String userId, Long id, String patternEdit) {
        return dao.checkJobRole(userId, id, patternEdit);
    }

    @Autowired
    private SchedulerFactoryBean schedulerFactoryBean;

    @Autowired
    private DashOperationJobDao dao;

    @Value("${admin_user_id}")
    private String adminUserId;

    @Autowired
    private DashMailService dashMailService;


}
