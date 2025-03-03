package net.linlan.tools.board.service.role;

import com.alibaba.fastjson.JSONObject;
import net.linlan.tools.board.entity.DashDataset;
import net.linlan.tools.board.service.AuthService;
import net.linlan.tools.board.service.DashDatasetService;
import net.linlan.tools.board.service.DashFolderService;
import net.linlan.commons.core.Rcode;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import net.linlan.commons.script.json.JsonUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

/**
 * 
 * Filename:DashDatasetRoleService.java
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
//@Aspect
public class DashDatasetRoleService {

    @Autowired
    private DashDatasetService dashDatasetService;

    @Autowired
    private AuthService authService;

    @Autowired
    private DashFolderService dashFolderService;

    @Around("execution(* net.linlan.tools.board.service.DashDatasetService.update(..))")
    public Object update(ProceedingJoinPoint proceedingJoinPoint) throws Throwable {
        String json = (String) proceedingJoinPoint.getArgs()[1];
        JSONObject jo = JsonUtils.parseJO(json);
        String userId = authService.getCurrentUser().getUserId();
        if ((dashDatasetService.checkDatasetRole(userId, jo.getString("id"), RolePermission.PATTERN_EDIT) > 0)
                || dashFolderService.checkFolderAuth(userId, jo.getString("folderId"))) {
            Object value = proceedingJoinPoint.proceed();
            return value;
        } else {
            return Rcode.error("No Permission");
        }
    }

    @Around("execution(* net.linlan.tools.board.service.DashDatasetService.deleteById(..))")
    public Object delete(ProceedingJoinPoint proceedingJoinPoint) throws Throwable {
        String id = (String) proceedingJoinPoint.getArgs()[1];
        String userId = authService.getCurrentUser().getUserId();
        DashDataset ds = dashDatasetService.findById(id);
        String folderId = "";
        if (ds != null){
            folderId = ds.getFolderId();
        }
        if (dashDatasetService.checkDatasetRole(userId, id, RolePermission.PATTERN_DELETE) > 0
                || dashFolderService.checkFolderAuth(userId, folderId)) {
            Object value = proceedingJoinPoint.proceed();
            return value;
        } else {
            return Rcode.error("No Permission");
        }
    }
}
