package net.linlan.tools.board.controller;

import com.alibaba.fastjson2.JSONObject;
import net.linlan.tools.board.dto.ViewDashOperationJob;
import net.linlan.tools.board.entity.DashOperationJob;
import net.linlan.tools.board.service.job.DashOperationJobService;
import net.linlan.commons.core.Rcode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TimeZone;
import java.util.stream.Collectors;

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
@RestController
@RequestMapping("/dash/operationjob")
public class DashOperationJobController extends BaseController {

    @RequestMapping("/save")
    public Rcode save(@RequestParam(name = "json") String json) {
        JSONObject jo = JSONObject.parseObject(json);
        DashOperationJob job = new DashOperationJob();
        job.setUserId(user.getUserId());
        job.setName(jo.getString("name"));
        job.setConfig(jo.getString("config"));
        job.setCronExp(jo.getString("cronExp"));
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
        format.setTimeZone(TimeZone.getTimeZone("UTC"));
        try {
            job.setStartDate(format.parse(jo.getJSONObject("daterange").getString("startDate")));
            job.setEndDate(format.parse(jo.getJSONObject("daterange").getString("endDate")));
        } catch (ParseException e) {
            logger.error("" , e);
        }
        job.setJobType(jo.getString("jobType"));
        dashboardJobService.save(job);
        dashboardJobService.configScheduler();
        return Rcode.ok();
    }

    @RequestMapping("/update")
    public Rcode update(@RequestParam(name = "json") String json) {
        JSONObject jo = JSONObject.parseObject(json);
        DashOperationJob job = new DashOperationJob();
        job.setId(jo.getLong("id"));
        job.setName(jo.getString("name"));
        job.setConfig(jo.getString("config"));
        job.setCronExp(jo.getString("cronExp"));
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
        format.setTimeZone(TimeZone.getTimeZone("UTC"));
        try {
            job.setStartDate(format.parse(jo.getJSONObject("daterange").getString("startDate")));
            job.setEndDate(format.parse(jo.getJSONObject("daterange").getString("endDate")));
        } catch (ParseException e) {
            logger.error("" , e);
        }
        job.setJobType(jo.getString("jobType"));
        dashboardJobService.update(job);
        dashboardJobService.configScheduler();
        return Rcode.ok();
    }

    @RequestMapping("/list")
    public List<ViewDashOperationJob> list() {
        Map<String, Object> params = new HashMap<>();
        params.put("userId", user.getUserId());
        List<DashOperationJob> list = dashboardJobService.getList(params);
        return list.stream().map(ViewDashOperationJob::new).collect(Collectors.toList());
    }


    @RequestMapping("/listAdmin")
    public List<ViewDashOperationJob> listAdmin() {
        Map<String, Object> params = new HashMap<>();
        params.put("adminId", user.getUserId());
        return dashboardJobService.getList(params).stream().map(ViewDashOperationJob::new).collect(Collectors.toList());
    }

    @RequestMapping("/delete")
    public Rcode deleteJob(@RequestParam(name = "id") Long id) {
        dashboardJobService.deleteById(id);
        return new Rcode().ok();
    }

    @RequestMapping("/execute")
    public Rcode execJob(@RequestParam(name = "id") Long id) {
        return dashboardJobService.exec(id);
    }

    @RequestMapping("/info")
    public ViewDashOperationJob info(@RequestParam(name = "id") Long id) {
        return new ViewDashOperationJob(dashboardJobService.findById(id));
    }


    @Autowired
    private DashOperationJobService dashboardJobService;
}
