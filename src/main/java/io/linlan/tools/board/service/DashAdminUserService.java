package io.linlan.tools.board.service;

import com.google.common.base.Charsets;
import com.google.common.hash.Hashing;
import io.linlan.tools.board.dao.DashAdminUserDao;
import io.linlan.tools.board.entity.DashAdminUser;
import io.linlan.tools.board.entity.DashAdminUserRole;
import org.apache.commons.collections.map.HashedMap;
import org.apache.commons.lang.StringUtils;
import io.linlan.commons.core.Rcode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

/**
 *
 * Filename:DashAdminUserService.java
 * Desc:
 *
 * @author hcday
 * @author <a href="mailto:hcday@qq.com">hcday</a>
 * CreateTime:2017-12-18 20:35:18
 *
 * @version 1.0
 * @since 1.0
 *
 */
@Repository
public class DashAdminUserService {

    /** get the list of entity DashAdminUser
     * 列表方法，返回{@link List<  DashAdminUser  >}
     * @param map the input select conditions
     * @return {@link List<  DashAdminUser  >}
     */
    public List<DashAdminUser> getList(Map<String, Object> map){
        return dao.getList(map);
    }

    /** find the entity by input id, return entity
     * 对象详情方法，通过id查询对象{@link DashAdminUser}
     * @param id the input id
     * @return {@link DashAdminUser}
     */
    public DashAdminUser findById(String id){
        return dao.findById(id);
    }

    /** save the entity with input object
     * 保存对象方法
     * @param adminUser the input adminUser
     */
    public void save(DashAdminUser adminUser){
        adminUser.init();
        dao.save(adminUser);
    }

    public void addUser(String userId, String username, String name, String password) {
        String md5 = Hashing.md5().newHasher().putString(password, Charsets.UTF_8).hash().toString();
        DashAdminUser user = new DashAdminUser();
        user.setUsername(username);
        user.setId(userId);
        user.setName(name);
        user.setPassword(md5);
        user.setStatus(1);
        save(user);
    }

    /** update the entity with input object
     * 更新对象方法
     * @param adminUser the input adminUser
     */
    public void update(DashAdminUser adminUser){
        dao.update(adminUser);
    }

    public void updateUser(String userId, String username, String name, String password) {
        DashAdminUser user = new DashAdminUser();
        user.setUsername(username);
        user.setId(userId);
        user.setName(name);
        if (StringUtils.isNotBlank(password)) {
            String md5 = Hashing.md5().newHasher().putString(password, Charsets.UTF_8).hash().toString();
            user.setPassword(md5);
        }
        update(user);
    }

    /** delete the entity by input id
     * 删除方法，通过id删除对象
     * @param id the input id
     */
    public void deleteById(String id){
        dao.deleteById(id);
    }

    @Transactional
    public void deleteUser(String userId) {
        dao.deleteUserById(userId);
        Map param = new HashMap<String,String>();
        param.put("objUid", userId);
        dao.deleteUserRole(param);
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
    public int queryTotal(Map<String, Object> map){
        return dao.queryTotal(map);
    }

    public Rcode updateUserRole(String[] userIds, String[] roleIds, final String curUid) {

        for (String uid : userIds) {
            Map<String, Object> params = new HashedMap();
            params.put("objUid", uid);
            params.put("curUid", curUid);
            params.put("adminUserId", adminUserId);
//            dao.deleteUserRole(params);
            if (roleIds != null && roleIds.length > 0) {
                List<DashAdminUserRole> list = new ArrayList<>();
                for (String rid : roleIds) {
                    DashAdminUserRole userRole = new DashAdminUserRole();
                    userRole.setUserId(uid);
                    userRole.setRoleId(rid);
                    list.add(userRole);
                }
                dao.saveUserRole(list);
            }
        }
        return Rcode.ok();
    }

    public Rcode deleteUserRoles(String[] userId, String[] roleId, final String curUid) {

        Map<String, Object> params = new HashedMap();
        params.put("userIds", userId);
        params.put("roleIds", roleId);
        params.put("curUid", curUid);
        params.put("adminUserId", adminUserId);
        dao.deleteUserRoles(params);
        return Rcode.ok();
    }


    public Rcode changePwd(String userId, String curPwd, String newPwd, String cfmPwd) {
        curPwd = Hashing.md5().newHasher().putString(curPwd, Charsets.UTF_8).hash().toString();
        newPwd = Hashing.md5().newHasher().putString(newPwd, Charsets.UTF_8).hash().toString();
        cfmPwd = Hashing.md5().newHasher().putString(cfmPwd, Charsets.UTF_8).hash().toString();
        if (newPwd.equals(cfmPwd)) {
            if (dao.updatePassword(userId, curPwd, newPwd) == 1) {
                return Rcode.ok();
            }
        }
        return Rcode.error("密码设置失败!");
    }
    public List<DashAdminUserRole> getUserRoleList() {
        return dao.getUserRoleList();
    }

    public void deleteUserRoleByRoleId(String roleId) {
        dao.deleteUserRoleByRoleId(roleId);
    }

    @Value("${admin_user_id}")
    private String adminUserId;

    @Autowired
    private DashAdminUserDao dao;



}
