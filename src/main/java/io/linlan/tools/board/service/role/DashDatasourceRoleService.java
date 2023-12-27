package io.linlan.tools.board.service.role;

import io.linlan.tools.board.entity.DashDatasource;
import io.linlan.tools.board.service.AuthService;
import io.linlan.tools.board.service.DashDatasourceService;
import io.linlan.commons.core.Rcode;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

/**
 * 
 * Filename:DashDatasourceRoleService.java
 * Desc:
 *
 * @author hcday
 * @author <a href="mailto:hcday@qq.com">hcday soo</a>
 * CreateTime:2018/1/3 12:02
 *
 * @version 1.0
 * @since 1.0
 *
 */
@Repository
//@Aspect
public class DashDatasourceRoleService {

    @Autowired
    private DashDatasourceService dashDatasourceService;

    @Autowired
    private AuthService authService;

    @Around("execution(* io.linlan.tools.board.service.DashDatasourceService.updates(..))")
    public Object update(ProceedingJoinPoint proceedingJoinPoint) throws Throwable {
//        String json = (String) proceedingJoinPoint.getArgs()[0];
//        JSONObject json = JsonUtils.parseJO(json);
        DashDatasource db = (DashDatasource)proceedingJoinPoint.getArgs()[0];
        String userId = authService.getCurrentUser().getUserId();
        if (dashDatasourceService.checkDatasourceRole(userId, db.getId(), RolePermission.PATTERN_EDIT) > 0) {
            Object value = proceedingJoinPoint.proceed();
            return value;
        } else {
            return Rcode.error("No Permission");
        }
    }

    @Around("execution(* io.linlan.tools.board.service.DashDatasourceService.delete(..))")
    public Object delete(ProceedingJoinPoint proceedingJoinPoint) throws Throwable {
        String id = (String) proceedingJoinPoint.getArgs()[1];
        String userId = authService.getCurrentUser().getUserId();
        if (dashDatasourceService.checkDatasourceRole(userId, id, RolePermission.PATTERN_DELETE) > 0) {
            Object value = proceedingJoinPoint.proceed();
            return value;
        } else {
            return Rcode.error("No Permission");
        }
    }
}
