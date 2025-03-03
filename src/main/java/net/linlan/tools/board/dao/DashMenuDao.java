package net.linlan.tools.board.dao;

import net.linlan.tools.board.dto.ViewDashMenu;
import org.apache.ibatis.annotations.Mapper;
import net.linlan.commons.db.mybatis.MybatisBaseDao;

import java.util.List;

/**
 * 
 * Filename:DashMenuDao.java
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
public interface DashMenuDao extends MybatisBaseDao<ViewDashMenu> {
    List<Long> getMenuIdByUserRole(String userId);

    List<Long> getMenuIdByRoleAdmin(String userId);
}
