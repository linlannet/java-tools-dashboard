package io.linlan.tools.board.dao;

import io.linlan.tools.board.entity.DashDatasource;
import org.apache.ibatis.annotations.Mapper;
import io.linlan.commons.db.mybatis.MybatisBaseDao;

/**
 * 
 * Filename:DashDatasourceDao.java
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
public interface DashDatasourceDao extends MybatisBaseDao<DashDatasource> {

    int delete(Long id, String userId);

    int checkDatasourceRole(String userId, String datasourceId, String permissionPattern);
}
