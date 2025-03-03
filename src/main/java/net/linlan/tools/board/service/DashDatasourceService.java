package net.linlan.tools.board.service;

import net.linlan.tools.board.dto.ViewDashDatasource;
import net.linlan.tools.board.service.role.RolePermission;
import net.linlan.commons.core.Rcode;
import net.linlan.commons.db.annotation.DataSourceParameter;
import net.linlan.tools.board.dao.DashDatasourceDao;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.List;
import java.util.Map;
import java.util.function.Supplier;
import java.util.stream.Collectors;

import net.linlan.tools.board.entity.DashDatasource;

/**
 *
 * Filename:DashDatasource.java
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
public class DashDatasourceService {
    private Logger logger = LoggerFactory.getLogger(this.getClass());
    /** get the list of entity DashDatasource
     * 列表方法，返回{@link List<  DashDatasource  >}
     * @param map the input select conditions
     * @return {@link List<  DashDatasource  >}
     */
    public List<DashDatasource> getList(Map<String, Object> map){
        return dao.getList(map);
    }

    /** find the entity by input id, return entity
     * 对象详情方法，通过id查询对象{@link DashDatasource}
     * @param id the input id
     * @return {@link DashDatasource}
     */
    public DashDatasource findById(String id){
        return dao.findById(id);
    }

    /** save the entity with input object
     * 保存对象方法
     * @param dashDatasource the input dashDatasource
     */
    public void save(DashDatasource dashDatasource){
        dashDatasource.init();
        dao.save(dashDatasource);
    }

    /** update the entity with input object
     * 更新对象方法
     * @param dashDatasource the input dashDatasource
     */
    public void update(DashDatasource dashDatasource){
        dashDatasource.setLastTime(new Timestamp(System.currentTimeMillis()));
        dao.update(dashDatasource);
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

    public List<ViewDashDatasource> getViewDatasourceList(Supplier<List<DashDatasource>> daoQuery) {
        List<DashDatasource> list = daoQuery.get();
        List<ViewDashDatasource> vlist = list.stream().map(e -> (ViewDashDatasource) ViewDashDatasource.TO.apply(e)).collect(Collectors.toList());
        vlist.forEach(e -> {
            try {
                List<String> fields = DataProviderManager.getProviderFieldByType(e.getType(), DataSourceParameter.Type.Password);
                fields.forEach(f -> {
                    if (e.getContent().containsKey(f)) {
                        e.getContent().put(f, "");
                    }
                });
            } catch (Exception ex) {
                logger.error("", e);
            }
        });
        return vlist;
    }

    public Rcode checkRule(String userId, String id) {
        DashDatasource datasource = dao.findById(id);
        if (dao.checkDatasourceRole(userId, id, RolePermission.PATTERN_READ) == 1) {
            return Rcode.ok();
        } else {
            return Rcode.error(datasource.getName());
        }
    }

    public int checkDatasourceRole(String userId, String datasourceId, String patternRead) {
        return dao.checkDatasourceRole(userId, datasourceId, patternRead);
    }

    @Autowired
    private DashDatasourceDao dao;
}
