package io.linlan.tools.board.dao;

import io.linlan.tools.board.entity.DashBoard;
import io.linlan.tools.board.entity.DashBoardParam;
import org.apache.ibatis.annotations.Mapper;
import io.linlan.commons.db.mybatis.MybatisBaseDao;

import java.util.List;
import java.util.Map;

/**
 * 
 * Filename:DashBoardDao.java
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
public interface DashBoardDao extends MybatisBaseDao<DashBoard>
{

    int checkBoardRole(String userId, String boardId, String permissionPattern);

    DashBoardParam getBoardParam(String boardId, String userId);

    int saveBoardParam(DashBoardParam boardParam);

    int deleteBoardParam(String boardId, String userId);

    List<DashBoard> getBoardListByFolderIds(Map<String, Object> param);

}
