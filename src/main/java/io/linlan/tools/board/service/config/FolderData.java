package io.linlan.tools.board.service.config;

import io.linlan.tools.board.entity.DashConfigVersion;
import io.linlan.tools.board.entity.DashDataset;
import io.linlan.tools.board.entity.DashFolder;
import io.linlan.tools.board.entity.DashWidget;
import io.linlan.tools.board.service.DashConfigVersionService;
import io.linlan.tools.board.service.DashDatasetService;
import io.linlan.tools.board.service.DashFolderService;
import io.linlan.tools.board.service.DashWidgetService;
import io.linlan.commons.script.json.StringMap;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;

/**
 *
 * Filename:FolderData.java
 * Desc:
 *
 * @author Linlan
 * CreateTime:2018/6/25 9:28
 *
 * @version 1.0
 * @since 1.0
 *
 */
@Service
public class FolderData implements InitializingBean {
    private static boolean isStart = false;

    @Autowired
    private DashFolderService dashFolderService;

    @Autowired
    private DashConfigVersionService configVersionService;

    @Autowired
    private DashDatasetService dashDatasetService;

    @Autowired
    private DashWidgetService dashWidgetService;

    private String currentVersion = "Folder";

    @Value("${admin_user_id}")
    private String userId;

    private String generateFolder(String cataName) {
        String parentId = "10000";
        DashFolder entity;
        if (cataName.indexOf("/") == -1) {

            parentId = initFolder(parentId, cataName);
        } else {
            String[] arr = cataName.split("/");
            for (String s : arr) {
                parentId = initFolder(parentId, s);
            }
        }
        return parentId;
    }

    private String initFolder(String parentId, String name) {
        DashFolder entity;
        entity = dashFolderService.getByParams(new StringMap().put("parentId", parentId).put("name", name).map());
        if (entity == null){
            entity = new DashFolder();
            entity.setName(name);
            entity.setParentId(parentId);
            dashFolderService.save(entity);
//                parentId = ;
        }else{
            parentId = entity.getId();
        }
        return parentId;
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        DashConfigVersion version = configVersionService.getByName(currentVersion);

        if (version != null && version.getStatus() == 0) {

            List<DashDataset> ds = dashDatasetService.getList(new HashMap<>());
            List<DashWidget> widgt = dashWidgetService.getList(new HashMap<>());

            for (DashDataset d : ds) {
                d.setFolderId(generateFolder(d.getCataName()));
                dashDatasetService.update(d);
            }

            for (DashWidget w : widgt) {
                w.setFolderId(generateFolder(w.getCataName()));
                dashWidgetService.update(w);
            }

            //修改状态
            version.setStatus(1);
            configVersionService.update(version);
        }
    }
}
