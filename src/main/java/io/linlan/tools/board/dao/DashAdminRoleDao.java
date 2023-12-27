package io.linlan.tools.board.dao;

import io.linlan.tools.board.entity.DashAdminRole;
import io.linlan.tools.board.entity.DashAdminRoleRes;
import org.apache.ibatis.annotations.Mapper;
import io.linlan.commons.db.mybatis.MybatisBaseDao;

import java.util.List;

/**
 * 
 * Filename:DashAdminRoleDao.java
 * Desc:
 *
 * @author hcday
 * @author <a href="mailto:hcday@qq.com">hcday soo</a>
 * CreateTime:2017/12/18 15:44
 *
 * @version 1.0
 * @since 1.0
 *
 */
@Mapper
public interface DashAdminRoleDao extends MybatisBaseDao<DashAdminRole> {

    List<DashAdminRoleRes> getRoleResList();

    int saveRoleRes(DashAdminRoleRes item);

    int deleteRoleRes(String roleId);

    int deleteRoleResByResId(Long resId,String resType);

    List<DashAdminRoleRes> getUserRoleResList(String userId, String resType);
}
