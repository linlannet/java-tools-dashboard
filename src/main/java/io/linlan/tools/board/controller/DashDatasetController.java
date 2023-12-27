package io.linlan.tools.board.controller;

import com.alibaba.fastjson.JSONObject;
import com.google.common.collect.Lists;
import io.linlan.tools.board.dto.ViewDashDataset;
import io.linlan.tools.board.entity.DashDataset;
import io.linlan.tools.board.service.DashDatasetService;
import io.linlan.tools.board.service.DashFolderService;
import io.linlan.tools.board.service.DashAdminRoleService;
import org.apache.commons.lang3.StringUtils;
import io.linlan.commons.core.Rcode;
import io.linlan.commons.script.json.JsonUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.sql.Timestamp;
import java.util.*;
import java.util.function.Consumer;

/**
 *
 * Filename:DashDataset.java
 * Desc:
 *
 * @author hcday
 * @author <a href="mailto:hcday@qq.com">hcday</a>
 * CreateTime:2017-12-18 20:35:17
 *
 * @version 1.0
 * @since 1.0
 *
 */
@RestController
@RequestMapping("/dash/dataset")
public class DashDatasetController extends BaseController {

    @RequestMapping("/list")
    public List<ViewDashDataset> list() {
        Map<String, Object> params = new HashMap<>();
        params.put("userId", user.getUserId());
        List<DashDataset> list = dashDatasetService.getList(params);
//        if (!adminUserId.equals(user.getUserId())) {
//            List<DashAdminRoleRes> resList = dashAdminRoleService.getUserRoleResList(user.getUserId(), "dataset");
//            List<String> resIdList = resList.stream().filter(e -> RolePermission.isEdit(e.getPermission())).map(e -> e.getResId()).distinct().collect(Collectors.toList());
//            list.stream().filter(e -> resIdList.contains(e.getId()) || e.getUserId().equals(user.getUserId())).collect(Collectors.toList());
//        }
//        //get Folder's auth
//        List<DashAdminRoleRes> roleres = dashAdminRoleService.getUserRoleResList(user.getUserId(), "folder");
//        Set<String> resIds = roleres.stream().map(r -> r.getResId().longValue()).collect(Collectors.toSet());
//
//        //get folder decsendant
//        Set<DashFolder> itemFolders = dashFolderService.getFolderDescendant(resIds);
//
//        String[] para = itemFolders.stream().map(i -> i.getId()).collect(Collectors.toSet()).toArray(new String[itemFolders.size()]);
//
//        Set<DashDataset> merge = new LinkedHashSet<>(list);
//
//        if (para != null && para.length > 0) {
//            merge.addAll(dashDatasetService.getDatasetListByFolderIds(para));
//        }
//        list = new ArrayList<>(merge);

        //get folder path
//        for (DashDataset ds : list){
//            ds.setFolderPath(dashFolderService.getFolderPath(ds.getFolderId()));
//        }

        return Lists.transform(list, ViewDashDataset.TO);
    }


    @RequestMapping("/listAll")
    public List<ViewDashDataset> listAll() {
        List<DashDataset> list = dashDatasetService.getList(new HashMap<>());
        return Lists.transform(list, ViewDashDataset.TO);
    }


    @RequestMapping("/listCategory")
    public List<String> listCategory() {
        return dashDatasetService.getCategoryList();
    }

    @RequestMapping("/listAdmin")
    public List<ViewDashDataset> listAdmin() {
        Map<String, Object> params = new HashMap<>();
        params.put("adminId", user.getUserId());
        List<DashDataset> list = dashDatasetService.getList(params);
        return Lists.transform(list, ViewDashDataset.TO);
    }

    @RequestMapping(value = "/getListByFolderIds")
    public List<ViewDashDataset> getListByFolderIds(@RequestParam(name = "folderIds") String[] folderIds) {
        List<DashDataset> list = dashDatasetService.getListByFolderIds(folderIds);
        return Lists.transform(list, ViewDashDataset.TO);
    }

    @RequestMapping(value = "/findById")
    public ViewDashDataset findById(@RequestParam(name = "id") String id) {
        DashDataset list = dashDatasetService.findById(id);
        return (ViewDashDataset) ViewDashDataset.TO.apply(list);
    }


    @RequestMapping("/save")
    public Rcode save(@RequestParam(name = "json") String json) {
        JSONObject jo = JsonUtils.parseJO(json);
        DashDataset dataset = new DashDataset();
        dataset.setUserId(user.getUserId());
        dataset.setName(jo.getString("name"));
        dataset.setContent(jo.getString("content"));
        dataset.setCataName(jo.getString("cataName"));
        dataset.setFolderId(jo.getString("folderId"));
        dataset.setDatasourceId(jo.getString("datasourceId"));

        if (StringUtils.isEmpty(dataset.getCataName())) {
            dataset.setCataName("默认分类");
        }
        dataset.setCreateTime(new Date());
        dataset.setLastTime(new Timestamp(System.currentTimeMillis()));

        Map<String, Object> paramMap = new HashMap<String, Object>();
        paramMap.put("name", dataset.getName());
        paramMap.put("userId", dataset.getUserId());
        paramMap.put("cateName", dataset.getCataName());
        paramMap.put("folderId", dataset.getFolderId());
        if (dashDatasetService.queryTotal(paramMap) <= 0) {
            dashDatasetService.save(dataset);
            return Rcode.ok();
        } else {
            return Rcode.error("Duplicated name");
        }
    }


    @RequestMapping("/update")
    public Rcode update(@RequestParam(name = "json") String json) {
        JSONObject jo = JsonUtils.parseJO(json);
        DashDataset dataset = new DashDataset();
        dataset.setUserId(user.getUserId());
        dataset.setId(jo.getString("id"));
        dataset.setName(jo.getString("name"));
        dataset.setCataName(jo.getString("cataName"));
        dataset.setFolderId(jo.getString("folderId"));
        dataset.setContent(jo.getString("content"));
        // 更新 datasourceId ; add by cys on 20201023
        if (StringUtils.isNotBlank(jo.getString("datasourceId"))) {
            dataset.setDatasourceId(jo.getString("datasourceId"));
        }
        dataset.setLastTime(new Timestamp(System.currentTimeMillis()));
        if (StringUtils.isEmpty(dataset.getCataName())) {
            dataset.setCataName("默认分类");
        }
        Map<String, Object> paramMap = new HashMap<String, Object>();
        paramMap.put("name", dataset.getName());
        paramMap.put("userId", dataset.getUserId());
        paramMap.put("datasetId", dataset.getId());
        paramMap.put("cataName", dataset.getCataName());
        paramMap.put("folderId", dataset.getFolderId());

        if (dashDatasetService.queryTotal(paramMap) <= 0) {
            dashDatasetService.update(dataset);
            return Rcode.ok();
        } else {
            return Rcode.error("Duplicated name");
        }
    }

    @RequestMapping("/delete")
    public Rcode delete(@RequestParam(name = "id") String id) {
        dashDatasetService.deleteById(id);
        return Rcode.ok();
    }

    @RequestMapping("/content")
    public Rcode dataset() {
        String userId = user.getUserId();
        if (!adminUserId.equals(userId)) {
            return Rcode.error("Null");
        }
        Map<String, Object> params = new HashMap<>();
        params.put("userId", user.getUserId());
        List<DashDataset> datasetList = dashDatasetService.getList(params);

        Consumer<Object> addId = o -> {
            JSONObject _j = (JSONObject) o;
            if (!_j.containsKey("id")) {
                _j.put("id", UUID.randomUUID().toString());
            }
        };

        datasetList.forEach(dataset -> {
            JSONObject json = JsonUtils.parseJO(dataset.getContent());
            JSONObject schema = json.getJSONObject("schema");
            if (schema != null) {
                schema.getJSONArray("measure").forEach(addId);
                schema.getJSONArray("dimension").forEach(d -> {
                    addId.accept(d);
                    JSONObject _j = (JSONObject) d;
                    if ("level".equals(_j.getString("type"))) {
                        _j.getJSONArray("columns").forEach(addId);
                    }
                });
            }
            if (json.containsKey("filters")) {
                json.getJSONArray("filters").forEach(addId);
            }
            if (json.containsKey("expressions")) {
                json.getJSONArray("expressions").forEach(addId);
            }
            dataset.setContent(json.toJSONString());
            dashDatasetService.update(dataset);
        });
        return Rcode.ok();
    }

    @Value("${admin_user_id}")
    private String adminUserId;


    @Autowired
    private DashDatasetService dashDatasetService;

    @Autowired
    private DashAdminRoleService dashAdminRoleService;

    @Autowired
    private DashFolderService dashFolderService;
}
