package io.linlan.tools.board.controller;

import com.alibaba.fastjson.JSONObject;
import io.linlan.tools.board.dto.ViewDashDatasource;
import io.linlan.tools.board.entity.DashDatasource;
import io.linlan.tools.board.service.DashDatasourceService;
import io.linlan.commons.core.Rcode;
import io.linlan.commons.script.json.JsonUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 *
 * Filename:DashDatasource.java
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
@RequestMapping("/dash/datasource")
public class DashDatasourceController extends BaseController {

    @RequestMapping("/list")
    public List<ViewDashDatasource> getList() {
        Map<String, Object> params = new HashMap<>();
        params.put("userId", user.getUserId());
        List<DashDatasource> list = dashDatasourceService.getList(params);
        return dashDatasourceService.getViewDatasourceList(() -> list);
    }

    @RequestMapping("/getDatasourceList")
    public List<ViewDashDatasource> getDatasourceList() {
        Map<String, Object> params = new HashMap<>();
        params.put("adminId", user.getUserId());
        List<DashDatasource> list = dashDatasourceService.getList(params);
        return dashDatasourceService.getViewDatasourceList(() -> list);
    }


    @RequestMapping("/save")
    public Rcode save(@RequestParam(name = "json") String json) {
        JSONObject jo = JsonUtils.parseJO(json);
        DashDatasource datasource = new DashDatasource();
        datasource.setUserId(user.getUserId());
        datasource.setName(jo.getString("name"));
        datasource.setType(jo.getString("type"));
        datasource.setContent(jo.getString("content"));

        Map<String, Object> paramMap = new HashMap<String, Object>();
        paramMap.put("userId", datasource.getUserId());
        paramMap.put("name", datasource.getName());
        if (dashDatasourceService.getCount(paramMap) <= 0) {
            dashDatasourceService.save(datasource);
            return Rcode.ok();
        } else {
            return Rcode.error("Duplicated Name!");
        }
    }

    @RequestMapping("/update")
    public Rcode update(@RequestParam(name = "json") String json) {
        JSONObject jo = JsonUtils.parseJO(json);
        DashDatasource datasource = new DashDatasource();
        datasource.setUserId(user.getUserId());
        datasource.setName(jo.getString("name"));
        datasource.setType(jo.getString("type"));
        datasource.setContent(jo.getString("content"));
        datasource.setId(jo.getString("id"));

        Map<String, Object> paramMap = new HashMap<String, Object>();
        paramMap.put("datasourceId", datasource.getId());
        paramMap.put("userId", datasource.getUserId());
        paramMap.put("name", datasource.getName());
        if (dashDatasourceService.getCount(paramMap) <= 0) {
            dashDatasourceService.update(datasource);
            return Rcode.ok();
        } else {
            return Rcode.error("Duplicated Name!");
        }
    }

    @RequestMapping("/delete")
    public Rcode delete(@RequestParam(name = "id") String id) {
        dashDatasourceService.deleteById(id);
        return Rcode.ok();
    }

    @RequestMapping("/checkRule")
    public Rcode checkRule(@RequestParam(name = "id") String id) {
        return dashDatasourceService.checkRule(user.getUserId(), id);
    }

    @Autowired
    private DashDatasourceService dashDatasourceService;

}
