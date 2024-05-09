package io.linlan.tools.board.service;

import io.linlan.tools.board.dao.DashFolderDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.*;
import java.util.stream.Collectors;

import io.linlan.tools.board.entity.DashFolder;

/**
 *
 * Filename:DashFolder.java
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
public class DashFolderService {

    /** get the list of entity DashFolder
     * 列表方法，返回{@link List<  DashFolder  >}
     * @param map the input select conditions
     * @return {@link List<  DashFolder  >}
     */
    public List<DashFolder> getList(Map<String, Object> map){
        return dao.getList(map);
    }

    /** find the entity by input id, return entity
     * 对象详情方法，通过id查询对象{@link DashFolder}
     * @param id the input id
     * @return {@link DashFolder}
     */
    public DashFolder findById(String id){
        return dao.findById(id);
    }

    /** save the entity with input object
     * 保存对象方法
     * @param dashFolder the input dashFolder
     */
    public void save(DashFolder dashFolder){
        dashFolder.init();
        dao.save(dashFolder);
    }

    /** update the entity with input object
     * 更新对象方法
     * @param dashFolder the input dashFolder
     */
    public void update(DashFolder dashFolder){
        dashFolder.setLastTime(new Timestamp(System.currentTimeMillis()));
        dao.update(dashFolder);
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

    public boolean checkFolderAuth(String userId, String folderId) {
        Set<DashFolder> ret = getFolderDescendant(dashAdminRoleService.getFolderIds(userId));

        for (DashFolder f : ret){
            if (f.getId() == folderId)
                return true;
        }
        return false;

    }

    public Set<DashFolder> getFolderDescendant(Set<String> folderIds){
        Set<DashFolder> ret = new HashSet<>();

        if (folderIds == null || folderIds.size() == 0){
            return ret;
        }

        List<DashFolder> folders = dao.getList(new HashMap<>());

        for (String folderId : folderIds) {
            String parentId = folderId;

            //get itself
            ret.addAll(folders.stream().filter(f -> f.getId() == folderId).collect(Collectors.toSet()));

            //get decsendant
            List<DashFolder> children = folders.stream().filter(f -> f.getParentId() == parentId).collect(Collectors.toList());
            List<DashFolder> tmp = new ArrayList<>();
            while (children.size() != 0) {

                for (DashFolder c : children) {
                    ret.add(c);
                    String cid = c.getId();

                    tmp.addAll(folders.stream().filter(f -> f.getParentId() == cid).collect(Collectors.toList()));
                }
                children.clear();
                children.addAll(tmp);
                tmp.clear();
            }
        }
        return ret;
    }

    public String getFolderPath(String folderId){
        String ret = "";
        Set<String> folderIds = new HashSet<>();
        folderIds.add(folderId);

        Set<DashFolder> folders = getAncestry(folderIds);

        for (DashFolder f : folders){
            ret += "\\" + f.getName();
        }

        return ret.length() > 0 ? ret.substring(1) : ret;
    }

    public Set<DashFolder> getFolderFamilyTree(Set<String> folderIds){
        //get folders descendant
        Set<DashFolder> ret = getFolderDescendant(folderIds);

        ret.addAll(getAncestry(folderIds));

        return ret;
    }

    public Set<DashFolder> getAncestry(Set<String> folderIds){
        Set<DashFolder> ret = new HashSet<>();

        List<DashFolder> folders = dao.getList(new HashMap<>());
        for (String folderId : folderIds) {
            //get Ancestry
            while (folderId != "-1") {
                for (DashFolder f : folders) {
                    if (f.getId().equals(folderId)) {
                        ret.add(f);
                        folderId = f.getParentId();
                        break;
                    }
                }
            }
        }
        return ret;
    }

    public DashFolder getByParams(Map<String, Object> map) {
        List<DashFolder> list = dao.getByParams(map);
        if (list != null && list.size() > 0){
            return list.get(0);
        }
        return null;
    }


    @Autowired
    private DashFolderDao dao;

    @Autowired
    private DashAdminRoleService dashAdminRoleService;



}
