package net.linlan.tools.board.service.role;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import net.linlan.tools.board.entity.DashBoard;
import net.linlan.tools.board.entity.DashWidget;
import net.linlan.tools.board.service.*;
import net.linlan.tools.board.dto.ViewDashBoard;
import net.linlan.tools.board.service.*;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import net.linlan.tools.board.entity.DashDataset;
import net.linlan.commons.core.Rcode;
import net.linlan.commons.script.json.JsonUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;

/**
 * 
 * Filename:DashBoardRoleService.java
 * Desc:
 *
 * @author Linlan
 * CreateTime:2018/1/3 12:04
 *
 * @version 1.0
 * @since 1.0
 *
 */
@Repository
// @Aspect
public class DashBoardRoleService {

    @Autowired
    private DashBoardService dashBoardService;

    @Autowired
    private DashWidgetService dashWidgetService;

    @Autowired
    private DashDatasetService dashDatasetService;

    @Autowired
    private DashDatasourceService dashDatasourceService;

    @Autowired
    private AuthService authService;

    @Autowired
    private DashFolderService dashFolderService;

    @Around("execution(* net.linlan.tools.board.service.DashBoardService.findById(*))")
    public Object getBoardData(ProceedingJoinPoint proceedingJoinPoint) throws Throwable {
        String id = (String) proceedingJoinPoint.getArgs()[0];
        String userId = authService.getCurrentUser().getUserId();
        DashBoard board = dashBoardService.findById(id);
        String folderId = "";
        if (board != null) {
            folderId = board.getFolderId();
        }

        if (dashBoardService.checkBoardRole(userId, id, RolePermission.PATTERN_READ) > 0
                || dashFolderService.checkFolderAuth(userId, folderId)) {
            ViewDashBoard value = (ViewDashBoard) proceedingJoinPoint.proceed();
            JSONArray rows = (JSONArray) value.getLayout().get("rows");
            for (Object row : rows) {
                JSONArray widgets = ((JSONObject) row).getJSONArray("widgets");
                if (widgets == null) {
                    continue;
                }
                for (Object widget : widgets) {
                    JSONObject vw = ((JSONObject) widget).getJSONObject("widget");
                    String widgetId = vw.getString("id");
                    String datasetId = vw.getJSONObject("data").getString("datasetId");
                    String datasourceId = vw.getJSONObject("data").getString("datasource");
                    List<Res> roleInfo = new ArrayList<>();
                    DashWidget dwidget = dashWidgetService.findById(widgetId);
                    if (dwidget != null) {
                        folderId = dwidget.getFolderId();
                    }
                    if (dashWidgetService.checkWidgetRole(userId, widgetId, RolePermission.PATTERN_READ) <= 0
                            && !dashFolderService.checkFolderAuth(userId, folderId)) {
                        ((JSONObject) widget).put("hasRole", false);
                        roleInfo.add(new Res("ADMIN.WIDGET", vw.getString("cataName") + "/" + vw.getString("name")));
                    }
                    DashDataset dataset = dashDatasetService.findById(datasetId);
                    if (dataset != null) {
                        folderId = dataset.getFolderId();
                    }
                    if (datasetId != null && dashDatasetService.checkDatasetRole(userId, datasetId, RolePermission.PATTERN_READ) <= 0
                            && !dashFolderService.checkFolderAuth(userId, folderId)) {
                        ((JSONObject) widget).put("hasRole", false);
                        DashDataset ds = dashDatasetService.findById(datasetId);
                        roleInfo.add(new Res("ADMIN.DATASET", ds.getCataName() + "/" + ds.getName()));
                    }
                    if (datasourceId != null && dashDatasourceService.checkDatasourceRole(userId, datasourceId, RolePermission.PATTERN_READ) <= 0) {
                        ((JSONObject) widget).put("hasRole", false);
                        roleInfo.add(new Res("ADMIN.DATASOURCE", dashDatasourceService.findById(datasourceId).getName()));
                    }
                    ((JSONObject) widget).put("roleInfo", roleInfo);
                }
            }
            return value;
        } else {
            return null;
        }
    }

    @Around("execution(* net.linlan.tools.board.service.DashBoardService.update(..))")
    public Object update(ProceedingJoinPoint proceedingJoinPoint) throws Throwable {
        String json = (String) proceedingJoinPoint.getArgs()[1];
        JSONObject jo = JsonUtils.parseJO(json);
        String userId = authService.getCurrentUser().getUserId();
        if ((dashBoardService.checkBoardRole(userId, jo.getString("id"), RolePermission.PATTERN_EDIT) > 0)
                || dashFolderService.checkFolderAuth(userId, jo.getString("folderId"))) {
            Object value = proceedingJoinPoint.proceed();
            return value;
        } else {
            return Rcode.error("No Permission");
        }
    }

    @Around("execution(* net.linlan.tools.board.service.DashBoardService.deleteById(..))")
    public Object delete(ProceedingJoinPoint proceedingJoinPoint) throws Throwable {
        String id = (String) proceedingJoinPoint.getArgs()[1];
        String userId = authService.getCurrentUser().getUserId();
        DashBoard board = dashBoardService.findById(id);
        String folderId = "";
        if (board != null){
            folderId = board.getFolderId();
        }
        if (dashBoardService.checkBoardRole(userId, id, RolePermission.PATTERN_DELETE) > 0
                || dashFolderService.checkFolderAuth(userId, folderId)) {
            Object value = proceedingJoinPoint.proceed();
            return value;
        } else {
            return Rcode.error("No Permission");
        }
    }

    private class Res {
        private String type;
        private String name;

        public Res(String type, String name) {
            this.type = type;
            this.name = name;
        }

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }
    }
}
