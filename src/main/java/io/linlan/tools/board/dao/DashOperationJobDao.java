package io.linlan.tools.board.dao;


import io.linlan.tools.board.entity.DashOperationJob;
import org.apache.ibatis.annotations.Mapper;
import io.linlan.commons.db.mybatis.MybatisBaseDao;

import java.util.Date;

/**
 * 
 * Filename:DashOperationJobDao.java
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
public interface DashOperationJobDao extends MybatisBaseDao<DashOperationJob> {

    int updateLastExecTime(Long jobId, Date date);

    int updateStatus(Long jobId, Long status, String log);

    int checkJobRole(String userId, Long jobId, String permissionPattern);

}
