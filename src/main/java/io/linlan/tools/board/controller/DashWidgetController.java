package io.linlan.tools.board.controller;

import com.alibaba.fastjson.JSONObject;
import com.google.common.collect.Lists;
import io.linlan.tools.board.dto.ViewDashWidget;
import io.linlan.tools.board.entity.DashDataset;
import io.linlan.tools.board.entity.DashWidget;
import io.linlan.tools.board.service.DashDatasetService;
import io.linlan.tools.board.service.DashDatasourceService;
import io.linlan.tools.board.service.DashWidgetService;
import io.linlan.tools.board.service.role.RolePermission;
import org.apache.commons.lang3.StringUtils;
import io.linlan.commons.core.Rcode;
import io.linlan.commons.script.json.JsonUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;

import java.sql.Timestamp;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 *
 * Filename:DashWidget.java
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
@RequestMapping("/dash/widget")
public class DashWidgetController extends BaseController {

    @RequestMapping("/listCategory")
    public List<String> getWidgetCategoryList() {
        return dashWidgetService.getCategoryList();
    }

    /** get the list with request params
     * 列表方法，返回{@link Rcode}，包括状态和page
     * @return {@link Rcode} with page info
     */
    @RequestMapping("/list")
    public List<ViewDashWidget> list() {
        Map<String, Object> params = new HashMap<>();
        params.put("userId", user.getUserId());
        //查询列表数据
        List<DashWidget> list = dashWidgetService.getList(params);
        return Lists.transform(list, ViewDashWidget.TO);
    }

    @RequestMapping("/listAll")
    public List<ViewDashWidget> getAllWidgetList() {
        Map<String, Object> params = new HashMap<>();
        List<DashWidget> list = dashWidgetService.getList(params);
        return Lists.transform(list, ViewDashWidget.TO);
    }


    @RequestMapping("/listAdmin")
    public List<ViewDashWidget> listAdmin() {
        Map<String, Object> params = new HashMap<>();
        params.put("adminId", user.getUserId());
        List<DashWidget> list = dashWidgetService.getList(params);
        return Lists.transform(list, ViewDashWidget.TO);
    }


    /** get the detail info of entity
     * 详情方法，返回{@link Rcode}，包括状态和dashWidget
     * @param id the input id
     * @return {@link Rcode} with dashWidget info
     */
    @RequestMapping("/info/{id}")
    public ViewDashWidget info(@PathVariable("id") String id){
        DashWidget widget = dashWidgetService.findById(id);
        return new ViewDashWidget(widget);
    }

    /** save entity with object
     * 保存方法，返回{@link Rcode}，状态
     * @param json the input object
     * @return {@link Rcode}
     */
    @RequestMapping("/save")
    public Rcode saveNewWidget(@RequestParam(name = "json") String json) {
        JSONObject jo = JsonUtils.parseJO(json);
        DashWidget widget = new DashWidget();
        widget.setUserId(user.getUserId());
        widget.setName(jo.getString("name"));
        widget.setContent(jo.getString("content"));
        widget.setCataName(jo.getString("cataName"));
        widget.setFolderId(jo.getString("folderId"));

        // 添加 datasourceId 和 datasetId ; add on 20201023
        if (StringUtils.isNotBlank(widget.getContent())) {
            JSONObject joContent = JsonUtils.parseJO(widget.getContent());
            widget.setDatasourceId(joContent.getString("datasource"));
            widget.setDatasetId(joContent.getString("datasetId"));
        }

        if (StringUtils.isEmpty(widget.getCataName())) {
            widget.setCataName("默认分类");
        }
        widget.setCreateTime(new Date());
        widget.setLastTime(new Timestamp(System.currentTimeMillis()));

        Map<String, Object> paramMap = new HashMap<String, Object>();
        paramMap.put("name", widget.getName());
        paramMap.put("userId", widget.getUserId());
        paramMap.put("cataName", widget.getCataName());
        paramMap.put("folderId", widget.getFolderId());


        if (dashWidgetService.getCount(paramMap) <= 0) {
            dashWidgetService.save(widget);
            return Rcode.ok();
        } else {
            return Rcode.error("Duplicated name");
        }
    }

    /** update entity with object
     * 更新方法，返回{@link Rcode}，状态
     * @param json the input object
     * @return {@link Rcode}
     */
    @RequestMapping("/update")
    public Rcode updateWidget(@RequestParam(name = "json") String json) {
        JSONObject jo = JsonUtils.parseJO(json);
        DashWidget widget = new DashWidget();
        widget.setUserId(user.getUserId());
        widget.setId(jo.getString("id"));
        widget.setName(jo.getString("name"));
        widget.setCataName(jo.getString("cataName"));
        widget.setFolderId(jo.getString("folderId"));
        widget.setContent(jo.getString("content"));

        // 更新 datasourceId 和 datasetId ; add by 20201023
        if (StringUtils.isNotBlank(widget.getContent())) {
            JSONObject joContent = JsonUtils.parseJO(widget.getContent());
            widget.setDatasourceId(joContent.getString("datasource"));
            widget.setDatasetId(joContent.getString("datasetId"));
        }

        widget.setLastTime(new Timestamp(System.currentTimeMillis()));
        if (StringUtils.isEmpty(widget.getCataName())) {
            widget.setCataName("默认分类");
        }
        Map<String, Object> paramMap = new HashMap<String, Object>();
        paramMap.put("name", widget.getName());
        paramMap.put("userId", widget.getUserId());
        paramMap.put("widgetId", widget.getId());
        paramMap.put("cataName", widget.getCataName());
        paramMap.put("folderId", widget.getFolderId());

        if (dashWidgetService.getCount(paramMap) <= 0) {
            dashWidgetService.update(widget);
            return Rcode.ok();
        } else {
            return Rcode.error("Duplicated name");
        }
    }


    /** delete entity with input ids
     * 删除方法，返回{@link Rcode}，状态
     * @param id the input ids
     * @return {@link Rcode}
     */
    @RequestMapping("/delete")
    public Rcode deleteWidget(@RequestParam(name = "id") String id) {
        dashWidgetService.deleteById(id);
        return Rcode.ok();
    }

    @RequestMapping("/checkRule")
    public Rcode checkWidget(@RequestParam(name = "id") String id) {
        DashWidget widget = dashWidgetService.findById(id);
        if (widget == null) {
            return Rcode.error("Null");
        }
        JSONObject object = (JSONObject) JSONObject.parse(widget.getContent());
        String datasetId = object.getString("datasetId");
        if (datasetId != null) {
            long count = dashDatasetService.checkDatasetRole(user.getUserId(), datasetId, RolePermission.PATTERN_READ);
            if (count == 1) {
                return Rcode.ok();
            } else {
                DashDataset ds = dashDatasetService.findById(datasetId);
                return Rcode.error(ds.getCataName() + "/" + ds.getName());
            }
        } else {
            String datasourceId = object.getString("datasource");
            if (dashDatasourceService.checkDatasourceRole(user.getUserId(), datasourceId, RolePermission.PATTERN_READ) == 1) {
                return Rcode.ok();
            } else {
                return Rcode.error(dashDatasourceService.findById(datasourceId).getName());
            }
        }
    }

    @RequestMapping("/content")
    public Rcode widget() {
        String userId = user.getUserId();
        if (!adminUserId.equals(userId)) {
            return Rcode.error("Null");
        }
        Map<String, Object> params = new HashMap<>();
        params.put("userId", user.getUserId());
        List<DashWidget> widgetList = dashWidgetService.getList(params);
        widgetList.forEach(widget -> {
            String datasetId = JsonUtils.parseJO(widget.getContent()).getString("datasetId");
            DashDataset dataset = dashDatasetService.findById(datasetId);
            if (dataset != null) {
                JSONObject _dataset = JsonUtils.parseJO(dataset.getContent());
                JSONObject data = JsonUtils.parseJO(widget.getContent());
                JSONObject content = data.getJSONObject("content");
                if (content.containsKey("keys")) {
                    content.getJSONArray("keys").forEach(k -> addDimensionId(_dataset, k));
                }
                if (content.containsKey("groups")) {
                    content.getJSONArray("groups").forEach(k -> addDimensionId(_dataset, k));
                }
                if (content.containsKey("values")) {
                    content.getJSONArray("values").forEach(v -> {
                        JSONObject _v = (JSONObject) v;
                        _v.getJSONArray("cols").forEach(c -> addExpressionId(_dataset, c));
                    });
                }
                if (content.containsKey("filters")) {
                    content.getJSONArray("filters").forEach(f -> addFilterGroupId(_dataset, f));
                }
                widget.setContent(data.toJSONString());
                dashWidgetService.update(widget);
            }
        });

        return Rcode.ok();
    }

    @Value("${admin_user_id}")
    private String adminUserId;

    @Autowired
    private DashWidgetService dashWidgetService;

    @Autowired
    private DashDatasetService dashDatasetService;

    @Autowired
    private DashDatasourceService dashDatasourceService;

}
