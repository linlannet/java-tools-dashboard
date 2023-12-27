package io.linlan.tools.board.dao;

import org.apache.ibatis.annotations.Mapper;
import io.linlan.commons.db.mybatis.MybatisBaseDao;
import io.linlan.tools.board.entity.DashFolder;

import java.util.List;
import java.util.Map;

/**
 *
 * Filename:DashFolder.java
 * Desc:
 *
 * @author hcday
 * @author <a href="mailto:hcday@qq.com">hcday</a>
 * CreateTime:2018-05-05 15:11:56
 *
 * @version 1.0
 * @since 1.0
 *
 */
@Mapper
public interface DashFolderDao extends MybatisBaseDao<DashFolder> {

    List<DashFolder> getByParams(Map<String, Object> map);
}
