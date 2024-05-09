package io.linlan.tools.board.dao;

import io.linlan.tools.board.entity.DashAdminUser;
import io.linlan.tools.board.entity.DashAdminUserRole;
import org.apache.ibatis.annotations.Mapper;
import io.linlan.commons.db.mybatis.MybatisBaseDao;

import java.util.List;
import java.util.Map;

/**
 * 
 * Filename:DashAdminUserDao.java
 * Desc:
 *
 * @author Linlan
 * CreateTime:2017/12/18 15:44
 *
 * @version 1.0
 * @since 1.0
 *
 */
@Mapper
public interface DashAdminUserDao extends MybatisBaseDao<DashAdminUser> {

    int deleteUserById(String userId);


    int saveUserRole(List<DashAdminUserRole> list);

    int deleteUserRole(Map<String, Object> param);

    List<DashAdminUserRole> getUserRoleList();

    DashAdminUser getByUsername(String username);

    int saveNewUser(String userId, String username, String realName);

    int updatePassword(String userId, String password, String newPassword);

    int deleteUserRoleByRoleId(String roleId);

    int deleteUserRoles(Map<String, Object> param);

}
