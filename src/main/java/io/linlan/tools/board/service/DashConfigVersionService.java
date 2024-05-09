package io.linlan.tools.board.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.List;
import java.util.Map;

import io.linlan.tools.board.dao.DashConfigVersionDao;
import io.linlan.tools.board.entity.DashConfigVersion;

/**
 *
 * Filename:ConfigVersion.java
 * Desc:
 *
 * @author Linlan
 * CreateTime:2018-05-05 15:11:56
 *
 * @version 1.0
 * @since 1.0
 *
 */
@Service
public class DashConfigVersionService {

    /** get the list of entity ConfigVersion
     * 列表方法，返回{@link List<  DashConfigVersion  >}
     * @param map the input select conditions
     * @return {@link List<  DashConfigVersion  >}
     */
    public List<DashConfigVersion> getList(Map<String, Object> map){
        return dao.getList(map);
    }

    /** find the entity by input id, return entity
     * 对象详情方法，通过id查询对象{@link DashConfigVersion}
     * @param id the input id
     * @return {@link DashConfigVersion}
     */
    public DashConfigVersion findById(Long id){
        return dao.findById(id);
    }

    /** save the entity with input object
     * 保存对象方法
     * @param configVersion the input configVersion
     */
    public void save(DashConfigVersion configVersion){
        configVersion.init();
        dao.save(configVersion);
    }

    /** update the entity with input object
     * 更新对象方法
     * @param configVersion the input configVersion
     */
    public void update(DashConfigVersion configVersion){
        configVersion.setLastTime(new Timestamp(System.currentTimeMillis()));
        dao.update(configVersion);
    }

    /** delete the entity by input id
     * 删除方法，通过id删除对象
     * @param id the input id
     */
    public void deleteById(Long id){
        dao.deleteById(id);
    }

    /** batch delete the entity by input ids
     * 批量删除方法，通过ids删除对象
     * @param ids the input ids
     */
    public void deleteByIds(Long[] ids){
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

    public DashConfigVersion getByName(String name) {
        return dao.getByName(name);
    }

    @Autowired
    private DashConfigVersionDao dao;


}
