package net.linlan.tools.board.service;

import com.alibaba.fastjson2.JSONObject;
import net.linlan.tools.board.entity.DashAdminRoleRes;
import net.linlan.commons.core.Rcode;
import net.linlan.tools.board.dao.DashAdminRoleDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.*;
import java.util.stream.Collectors;

import net.linlan.tools.board.entity.DashAdminRole;
import org.springframework.transaction.annotation.Transactional;

/**
 *
 * Filename:DashAdminRoleService.java
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
public class DashAdminRoleService {

    /** get the list of entity DashAdminRole
     * 列表方法，返回{@link List<  DashAdminRole  >}
     * @param map the input select conditions
     * @return {@link List<  DashAdminRole  >}
     */
    public List<DashAdminRole> getList(Map<String, Object> map){
        return dao.getList(map);
    }

    /** find the entity by input id, return entity
     * 对象详情方法，通过id查询对象{@link DashAdminRole}
     * @param id the input id
     * @return {@link DashAdminRole}
     */
    public DashAdminRole findById(String id){
        return dao.findById(id);
    }

    /** save the entity with input object
     * 保存对象方法
     * @param dashboardRole the input dashboardRole
     */
    public void save(DashAdminRole dashboardRole){
        dashboardRole.init();
        dao.save(dashboardRole);
    }

    /** update the entity with input object
     * 更新对象方法
     * @param dashboardRole the input dashboardRole
     */
    public void update(DashAdminRole dashboardRole){
        dashboardRole.setLastTime(new Timestamp(System.currentTimeMillis()));
        dao.update(dashboardRole);
    }

    /** delete the entity by input id
     * 删除方法，通过id删除对象
     * @param id the input id
     */
    public void deleteById(String id){
        dao.deleteById(id);
    }

    /** batch delete the entity by input ids
     * 批量删除方法，通过ids删除对象
     * @param ids the input ids
     */
    public void deleteByIds(String[] ids){
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

    public void addRole(String roleId, String roleName, String userId) {
        DashAdminRole role = new DashAdminRole();
        role.setId(roleId);
        role.setName(roleName);
        role.setUserId(userId);
        save(role);
    }

    public void updateRole(String roleId, String roleName, String userId) {
        DashAdminRole role = new DashAdminRole();
        role.setId(roleId);
        role.setName(roleName);
        role.setUserId(userId);
        update(role);
    }

    @Transactional
    public void deleteRole(String roleId) {
        deleteRoleRes(roleId);
        deleteById(roleId);
    }

    public List<DashAdminRole> getResRole(String userId, String resType) {
        Map<String, Object> params = new HashMap<>();
        params.put("userId", userId);
        params.put("resType", resType);
        List<DashAdminRole> roles = getList(params);
        return roles;
    }

    public List<DashAdminRoleRes> getUserRoleResList(String userId, String resType) {
        return dao.getUserRoleResList(userId, resType);
    }

    public void deleteRoleRes(String roleId) {
        dao.deleteRoleRes(roleId);
    }

    public void deleteRoleResByResId(Long resId, String resType) {
        dao.deleteRoleResByResId(resId, resType);
    }

    public void saveRoleRes(DashAdminRoleRes roleRes) {
        dao.saveRoleRes(roleRes);
    }

    public Rcode updateRoleRes(String[] roleIds, List<Object> resInfos) {
        for (String roldId : roleIds) {
            deleteRoleRes(roldId);
            if (resInfos != null && resInfos.size() > 0) {
                for (Object res : resInfos) {
                    JSONObject jo = (JSONObject) res;
                    DashAdminRoleRes roleRes = new DashAdminRoleRes();
                    roleRes.setRoleId(roldId);
                    roleRes.setResId(jo.getString("resId"));
                    roleRes.setResType(jo.getString("resType"));
                    boolean edit = jo.getBooleanValue("edit");
                    boolean delete = jo.getBooleanValue("delete");
                    roleRes.setPermission("" + (edit ? 1 : 0) + (delete ? 1 : 0));
                    saveRoleRes(roleRes);
                }
            }
        }
        return Rcode.ok();
    }


    public Rcode updateRoleResUser(String[] roleIds, String[] resIds) {
        for (Object res : resIds) {
            JSONObject jo = (JSONObject) res;
            deleteRoleResByResId(jo.getLong("resId"), jo.getString("resType"));
            for (String roleId : roleIds) {
                DashAdminRoleRes roleRes = new DashAdminRoleRes();
                roleRes.setRoleId(roleId);
                roleRes.setResId(jo.getString("resId"));
                roleRes.setResType(jo.getString("resType"));
                roleRes.setPermission("" + (false ? 1 : 0) + (false ? 1 : 0));
                saveRoleRes(roleRes);
            }
        }
        return new Rcode().ok();
    }

    public List<DashAdminRoleRes> getRoleResList() {
        return dao.getRoleResList();
    }

    public Set<String> getFolderIds(String userId){
        Set<String> resIds = new HashSet<>();
        //get Folder's auth
        List<DashAdminRoleRes> roleres = dao.getUserRoleResList(userId, "folder");
        if (roleres != null && roleres.size() > 0) {
            resIds = roleres.stream().map(r -> r.getResId()).collect(Collectors.toSet());
        }
        return resIds;
    }

    
    @Autowired
    private DashAdminRoleDao dao;


}
