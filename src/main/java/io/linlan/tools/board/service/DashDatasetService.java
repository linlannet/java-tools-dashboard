package io.linlan.tools.board.service;

import io.linlan.commons.script.json.StringMap;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.List;
import java.util.Map;

import io.linlan.tools.board.dao.DashDatasetDao;
import io.linlan.tools.board.entity.DashDataset;

/**
 *
 * Filename:DashDataset.java
 * Desc:
 *
 * @author hcday
 * @author <a href="mailto:hcday@qq.com">hcday</a>
 * CreateTime:2017-12-18 20:35:17
 *
 * @version 1.0
 * @since 1.0
 *
 */
@Service
public class DashDatasetService {

    /** get the list of entity DashDataset
     * 列表方法，返回{@link List<  DashDataset  >}
     * @param map the input select conditions
     * @return {@link List<  DashDataset  >}
     */
    public List<DashDataset> getList(Map<String, Object> map){
        return dao.getList(map);
    }

    /** find the entity by input id, return entity
     * 对象详情方法，通过id查询对象{@link DashDataset}
     * @param id the input id
     * @return {@link DashDataset}
     */
    public DashDataset findById(String id){
        if (null == id) {
            return null;
        }
        return dao.findById(id);
    }

    /** save the entity with input object
     * 保存对象方法
     * @param dashDataset the input dashDataset
     */
    public void save(DashDataset dashDataset){
        dashDataset.init();
        dao.save(dashDataset);
    }

    /** update the entity with input object
     * 更新对象方法
     * @param dashDataset the input dashDataset
     */
    public void update(DashDataset dashDataset){
        dashDataset.setLastTime(new Timestamp(System.currentTimeMillis()));
        dao.update(dashDataset);
    }

    /** delete the entity by input id
     * 删除方法，通过id删除对象
     * @param id the input id
     */
    public void deleteById(String id){
        dao.deleteById(id);
    }

    /** batch delete the entity by input ids
     * 批量删除方法，通过ids删除对象
     * @param ids the input ids
     */
    public void deleteByIds(String[] ids){
        dao.deleteByIds(ids);
    }

    /** query the total count by input select conditions
     * 通过输入的条件查询记录总数
     * @param map the input select conditions
     * @return total count
     */
    public int queryTotal(Map<String, Object> map){
        return dao.queryTotal(map);
    }


    public int checkDatasetRole(String userId, String datasetId, String patternRead) {
        return dao.checkDatasetRole(userId, datasetId, patternRead);
    }
    public List<String> getCategoryList() {
        return dao.getCategoryList();
    }

    public List<DashDataset> getListByFolderIds(String[] folderIds){
        if (folderIds == null || folderIds.length == 0) {
            return null;
        }
        return getList(new StringMap().put("folderIds", folderIds).map());
    }


    @Autowired
    private DashDatasetDao dao;
}
