package io.linlan.tools.board.dao;

import io.linlan.tools.board.dto.ViewDashMenu;
import org.apache.ibatis.annotations.Mapper;
import io.linlan.commons.db.mybatis.MybatisBaseDao;

import java.util.List;

/**
 * 
 * Filename:DashMenuDao.java
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
public interface DashMenuDao extends MybatisBaseDao<ViewDashMenu> {
    List<Long> getMenuIdByUserRole(String userId);

    List<Long> getMenuIdByRoleAdmin(String userId);
}
