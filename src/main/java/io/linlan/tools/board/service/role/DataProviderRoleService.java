package io.linlan.tools.board.service.role;

import io.linlan.tools.board.entity.DashDataset;
import io.linlan.tools.board.service.AuthService;
import io.linlan.tools.board.service.DashDatasetService;
import io.linlan.tools.board.service.DashDatasourceService;
import io.linlan.tools.board.service.DashFolderService;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Repository;

/**
 * 
 * Filename:DataProviderRoleService.java
 * Desc:
 *
 * @author hcday
 * @author <a href="mailto:hcday@qq.com">hcday soo</a>
 * CreateTime:2018/1/3 12:04
 *
 * @version 1.0
 * @since 1.0
 *
 */
@Repository
@Aspect
@Order(2)
public class DataProviderRoleService {

    @Autowired
    private AuthService authService;

    @Autowired
    private DashDatasourceService dashDatasourceService;

    @Autowired
    private DashDatasetService dashDatasetService;

    @Autowired
    private DashFolderService dashFolderService;

    @Value("${admin_user_id}")
    private String adminUserId;

    @Around("execution(* io.linlan.tools.board.service.DataProviderService.getDimensionValues(..)) ||" +
            "execution(* io.linlan.tools.board.service.DataProviderService.getColumns(..)) ||" +
            "execution(* io.linlan.tools.board.service.DataProviderService.queryAggData(..)) ||" +
            "execution(* io.linlan.tools.board.service.DataProviderService.viewAggDataQuery(..))")
    public Object query(ProceedingJoinPoint proceedingJoinPoint) throws Throwable {
        String datasourceId = (String) proceedingJoinPoint.getArgs()[0];
        String datasetId = (String) proceedingJoinPoint.getArgs()[2];
        String userId = authService.getCurrentUser().getUserId();
        DashDataset ds = dashDatasetService.findById(datasetId);
        if (ds != null) {
            if ((dashDatasetService.checkDatasetRole(userId, datasetId, RolePermission.PATTERN_READ) > 0)
                    || dashFolderService.checkFolderAuth(userId, ds.getFolderId())) {
                return proceedingJoinPoint.proceed();
            }
        } else {
            if (dashDatasourceService.checkDatasourceRole(userId, datasourceId, RolePermission.PATTERN_READ) > 0) {
                return proceedingJoinPoint.proceed();
            }
        }
        return null;
    }

}
