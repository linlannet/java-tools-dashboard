package io.linlan.tools.board.controller;

import com.alibaba.fastjson.JSONObject;
import com.google.common.collect.Lists;
import io.linlan.tools.board.dto.ViewDashFolder;
import io.linlan.tools.board.entity.DashFolder;
import io.linlan.tools.board.service.DashAdminRoleService;
import io.linlan.tools.board.service.DashFolderService;
import io.linlan.commons.core.Rcode;
import io.linlan.commons.script.json.JsonUtils;
import io.linlan.commons.script.json.StringMap;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.sql.Timestamp;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 *
 * Filename:DashFolder.java
 * Desc:
 *
 * @author Linlan
 * CreateTime:2018-05-05 15:11:56
 *
 * @version 1.0
 * @since 1.0
 *
 */
@RestController
@RequestMapping("dash/folder")
public class DashFolderController extends BaseController {

    /** get the list with request params
     * 列表方法，返回{@link Rcode}，包括状态和page
     * @param params the input params
     * @return {@link Rcode} with page info
     */
    @RequestMapping("/list")
    public List<ViewDashFolder> list(@RequestParam Map<String, Object> params){
        //查询列表数据
        List<DashFolder> dashFolderList = dashFolderService.getList(new HashMap<>());

//        return Rcode.ok().put("page", dashFolderList);
        return Lists.transform(dashFolderList, ViewDashFolder.TO);
    }

    @RequestMapping(value = "/familyTree")
    public List<ViewDashFolder> getFolderFamilyTree(@RequestParam(name = "folderIds") String folderIds) {
        //get Folder's auth
//        List<DashAdminRoleRes> roleres = dashAdminRoleService.getUserRoleResList(user.getUserId(), "folder");
//        Set<Long> resIds = roleres.stream().map(r -> r.getResId().longValue()).collect(Collectors.toSet());
//
//        //get creators
//        List<DashFolder> creator = dashFolderService.getList(new StringMap().put("userId", user.getUserId()).map());
//
//        Integer[] folders = JSONArray.parseArray(folderIds).toArray(new Integer[]{});
//
//        //merg folderids and auth folderids and creator
//        for (int f : folders){
//            resIds.add(Long.valueOf(f));
//        }
//        for (DashFolder fo : creator){
//            resIds.add(fo.getId());
//        }
//
//        //get family tree
//        Set<DashFolder> itemFolders = dashFolderService.getFolderFamilyTree(resIds);
//
//        List<DashFolder> list = new ArrayList<>();
//        list.addAll(itemFolders);
        List<DashFolder> list = dashFolderService.getList(new HashMap<>());
        return Lists.transform(list, ViewDashFolder.TO);
    }


    /** get the detail info of entity
     * 详情方法，返回{@link Rcode}，包括状态和dashFolder
     * @param id the input id
     * @return {@link Rcode} with dashFolder info
     */
    @RequestMapping("/info/{id}")
    public Rcode info(@PathVariable("id") String id){
        DashFolder dashFolder = dashFolderService.findById(id);

        return Rcode.ok().put("dashFolder", dashFolder);
    }

    @RequestMapping(value = "/getByParams")
    public ViewDashFolder getFolder(@RequestParam(name = "parentId") int parentId, @RequestParam(name = "name") String name) {
        DashFolder folder = dashFolderService.getByParams(new StringMap().put("parentId", parentId).put("name", name).map());
        return folder == null ? null : new ViewDashFolder(folder);
    }

    /** save entity with object
     * 保存方法，返回{@link Rcode}，状态
//     * @param dashFolder the input object
     * @return {@link Rcode}
     */
    @RequestMapping("/save")
    public Rcode save(@RequestParam(name = "json") String json) {
//    public Rcode save(@RequestBody DashFolder dashFolder){
//        dashFolderService.save(dashFolder);

        JSONObject jo = JsonUtils.parseJO(json);
        DashFolder dashFolder = new DashFolder();
        dashFolder.setUserId(user.getUserId());
        dashFolder.setName(jo.getString("name"));
        dashFolder.setParentId(jo.getString("parentId"));

        dashFolder.setCreateTime(new Date());
        dashFolder.setLastTime(new Timestamp(System.currentTimeMillis()));
        Map<String, Object> paramMap = new HashMap<String, Object>();
        paramMap.put("userId", dashFolder.getUserId());
        paramMap.put("name", dashFolder.getName());
        paramMap.put("parentId", dashFolder.getParentId());
        if (dashFolderService.getCount(paramMap) <= 0) {
            dashFolderService.save(dashFolder);
            return Rcode.ok().put("dashFolder", dashFolder);
        } else {
            return Rcode.error("dash/folder/save有重复数据！");
        }
    }

    /** update entity with object
     * 更新方法，返回{@link Rcode}，状态
//     * @param dashFolder the input object
     * @return {@link Rcode}
     */
    @RequestMapping("/update")
    public Rcode update(@RequestParam(name = "json") String json) {
//    public Rcode update(@RequestBody DashFolder dashFolder){
//        dashFolderService.update(dashFolder);

        JSONObject jo = JsonUtils.parseJO(json);
        DashFolder dashFolder = new DashFolder();
        dashFolder.setUserId(user.getUserId());
        dashFolder.setId(jo.getString("id"));
        dashFolder.setName(jo.getString("name"));
        dashFolder.setParentId(jo.getString("parentId"));
        Map<String, Object> paramMap = new HashMap<String, Object>();
        paramMap.put("userId", dashFolder.getUserId());
        paramMap.put("name", dashFolder.getName());
        paramMap.put("parentId", dashFolder.getParentId());
        if (dashFolderService.getCount(paramMap) <= 0) {
            dashFolderService.update(dashFolder);
            return Rcode.ok().put("dashFolder", dashFolder);
        } else {
            return Rcode.error("dash/folder/update有重复数据！");
        }
    }

    /** delete entity with input ids
     * 删除方法，返回{@link Rcode}，状态
//     * @param ids the input ids
     * @return {@link Rcode}
     */
    @RequestMapping("/delete")
    public Rcode delete(@RequestParam(name = "id") String id) {
        dashFolderService.deleteById(id);
//    public Rcode delete(@RequestBody String[] ids){
//        dashFolderService.deleteByIds(ids);

        return Rcode.ok();
    }

    @Autowired
    private DashFolderService dashFolderService;

    @Autowired
    private DashAdminRoleService dashAdminRoleService;


}
