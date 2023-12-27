package io.linlan.tools.board.dao;

import io.linlan.tools.board.entity.DashDataset;
import org.apache.ibatis.annotations.Mapper;
import io.linlan.commons.db.mybatis.MybatisBaseDao;

import java.util.List;

/**
 * 
 * Filename:DashDatasetDao.java
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
public interface DashDatasetDao extends MybatisBaseDao<DashDataset> {

    List<String> getCategoryList();

    int checkDatasetRole(String userId, String datasetId, String permissionPattern);

}
