package net.linlan.tools.board.dao;

import net.linlan.tools.board.entity.DashWidget;
import org.apache.ibatis.annotations.Mapper;
import net.linlan.commons.db.mybatis.MybatisBaseDao;

import java.util.List;

/**
 * 
 * Filename:DashWidgetDao.java
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
public interface DashWidgetDao extends MybatisBaseDao<DashWidget> {

    List<String> getCategoryList();

    int checkWidgetRole(String userId, String widgetId, String permissionPattern);

}
