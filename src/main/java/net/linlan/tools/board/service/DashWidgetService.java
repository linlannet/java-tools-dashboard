package net.linlan.tools.board.service;

import net.linlan.commons.script.json.StringMap;
import net.linlan.tools.board.dao.DashWidgetDao;
import net.linlan.tools.board.entity.DashWidget;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.List;
import java.util.Map;

/**
 *
 * Filename:DashWidget.java
 * Desc:
 *
 * @author Linlan
 * CreateTime:2017-12-18 20:35:17
 *
 * @version 1.0
 * @since 1.0
 *
 */
@Service
public class DashWidgetService {

    /** get the list of entity DashWidget
     * 列表方法，返回{@link List<   DashWidget   >}
     * @param map the input select conditions
     * @return {@link List<  DashWidget  >}
     */
    public List<DashWidget> getList(Map<String, Object> map){
        return dao.getList(map);
    }

    /** find the entity by input id, return entity
     * 对象详情方法，通过id查询对象{@link DashWidget}
     * @param id the input id
     * @return {@link DashWidget}
     */
    public DashWidget findById(String id){
        return dao.findById(id);
    }

    /** save the entity with input object
     * 保存对象方法
     * @param dashWidget the input dashWidget
     */
    public void save(DashWidget dashWidget){
        dashWidget.init();
//        dashWidget.setId(1l);
        dao.save(dashWidget);
    }

    /** update the entity with input object
     * 更新对象方法
     * @param dashWidget the input dashWidget
     */
    public void update(DashWidget dashWidget){
        dashWidget.setLastTime(new Timestamp(System.currentTimeMillis()));
        dao.update(dashWidget);
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
    public int getCount(Map<String, Object> map){
        return dao.getCount(map);
    }

    public List<String> getCategoryList() {
        return dao.getCategoryList();
    }

    public int checkWidgetRole(String userId, String resId, String patternRead) {
        return dao.checkWidgetRole(userId, resId, patternRead);
    }


    public List<DashWidget> getListByFolderIds(String[] folderIds){
        if (folderIds == null || folderIds.length == 0) {
            return null;
        }

        return getList(new StringMap().put("folderIds", folderIds).map());
    }


    @Autowired
    private DashWidgetDao dao;


}
