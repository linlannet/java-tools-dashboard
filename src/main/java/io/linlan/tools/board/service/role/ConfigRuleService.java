package io.linlan.tools.board.service.role;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.google.common.base.Function;
import com.google.common.collect.Lists;
import io.linlan.tools.board.entity.DashAdminRole;
import io.linlan.tools.board.service.*;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Repository;

import javax.annotation.Nullable;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 
 * Filename:ConfigRuleService.java
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
@Aspect
@Order(2)
public class ConfigRuleService {

    @Around("execution(* io.linlan.tools.board.service.DashWidgetService.save(..)) || " +
            "execution(* io.linlan.tools.board.service.DashWidgetService.update(..)) || " +
            "execution(* io.linlan.tools.board.service.DashWidgetService.deleteById(..))")
    public Object widgetRule(ProceedingJoinPoint proceedingJoinPoint) throws Throwable {
        String userId = authService.getCurrentUser().getUserId();
        if (userId.equals(adminUserId)) {
            return proceedingJoinPoint.proceed();
        } else {
            List<Long> menuIdList = dashMenuService.getMenuIdByUserRole(userId);
            if (menuIdList.contains(1L) && menuIdList.contains(4L)) {
                return proceedingJoinPoint.proceed();
            }
        }
        return null;
    }

    @Around("execution(* io.linlan.tools.board.service.DashDatasetService.save(..)) || " +
            "execution(* io.linlan.tools.board.service.DashDatasetService.update(..)) || " +
            "execution(* io.linlan.tools.board.service.DashDatasetService.deleteById(..))")
    public Object datasetRule(ProceedingJoinPoint proceedingJoinPoint) throws Throwable {
        String userId = authService.getCurrentUser().getUserId();
        if (userId.equals(adminUserId)) {
            return proceedingJoinPoint.proceed();
        } else {
            List<Long> menuIdList = dashMenuService.getMenuIdByUserRole(userId);
            if (menuIdList.contains(1L) && menuIdList.contains(3L)) {
                return proceedingJoinPoint.proceed();
            }
        }
        return null;
    }

    @Around("execution(* io.linlan.tools.board.service.DashDatasourceService.save(..)) || " +
            "execution(* io.linlan.tools.board.service.DashDatasourceService.update(..)) || " +
            "execution(* io.linlan.tools.board.service.DashDatasourceService.deleteById(..))")
    public Object datasourceRule(ProceedingJoinPoint proceedingJoinPoint) throws Throwable {
        String userId = authService.getCurrentUser().getUserId();
        if (userId.equals(adminUserId)) {
            return proceedingJoinPoint.proceed();
        } else {
            List<Long> menuIdList = dashMenuService.getMenuIdByUserRole(userId);
            if (menuIdList.contains(1L) && menuIdList.contains(2L)) {
                return proceedingJoinPoint.proceed();
            }
        }
        return null;
    }

    @Around("execution(* io.linlan.tools.board.service.DashBoardService.save(..)) || " +
            "execution(* io.linlan.tools.board.service.DashBoardService.update(..)) || " +
            "execution(* io.linlan.tools.board.service.DashBoardService.deleteById(..))")
    public Object boardRule(ProceedingJoinPoint proceedingJoinPoint) throws Throwable {
        String userId = authService.getCurrentUser().getUserId();
        if (userId.equals(adminUserId)) {
            return proceedingJoinPoint.proceed();
        } else {
            List<Long> menuIdList = dashMenuService.getMenuIdByUserRole(userId);
            if (menuIdList.contains(1L) && menuIdList.contains(5L)) {
                return proceedingJoinPoint.proceed();
            }
        }
        return null;
    }

    @Around("execution(* io.linlan.tools.board.service.DashAdminUserService.addUser(..)) || " +
            "execution(* io.linlan.tools.board.service.DashAdminUserService.updateUser(..)))")
    public Object userAdminRule(ProceedingJoinPoint proceedingJoinPoint) throws Throwable {
        String userId = authService.getCurrentUser().getUserId();
        if (userId.equals(adminUserId)) {
            return proceedingJoinPoint.proceed();
        }
        return null;
    }

    @Around("execution(* io.linlan.tools.board.service.DashAdminRoleService.addRole(..)) || " +
            "execution(* io.linlan.tools.board.service.DashAdminRoleService.updateRole(..)) || " +
            "execution(* io.linlan.tools.board.service.DashAdminRoleService.updateRoleRes(..))")
    public Object resAdminRule(ProceedingJoinPoint proceedingJoinPoint) throws Throwable {
        String userId = authService.getCurrentUser().getUserId();
        if (userId.equals(adminUserId)) {
            return proceedingJoinPoint.proceed();
        } else {
            List<Long> menuIdList = dashMenuService.getMenuIdByUserRole(userId);
            if (menuIdList.contains(7L) && menuIdList.contains(8L)) {
                return proceedingJoinPoint.proceed();
            }
        }
        return null;
    }

    @Around("execution(* io.linlan.tools.board.service.DashAdminRoleService.updateRole(..))")
    public Object updateRole(ProceedingJoinPoint proceedingJoinPoint) throws Throwable {
        String userId = authService.getCurrentUser().getUserId();
        if (userId.equals(adminUserId)) {
            return proceedingJoinPoint.proceed();
        } else {
            DashAdminRole role = dashAdminRoleService.findById((String) proceedingJoinPoint.getArgs()[0]);
            if (userId.equals(role.getUserId())) {
                return proceedingJoinPoint.proceed();
            }
        }
        return null;
    }

    @Around("execution(* io.linlan.tools.board.service.DashAdminUserService.updateUserRole(..)) ||" +
            "execution(* io.linlan.tools.board.service.DashAdminUserService.deleteUserRoles(..))")
    public Object updateUserRole(ProceedingJoinPoint proceedingJoinPoint) throws Throwable {
        String userId = authService.getCurrentUser().getUserId();
        if (userId.equals(adminUserId)) {
            return proceedingJoinPoint.proceed();
        } else {
            Map<String, Object> params = new HashMap<>();
            params.put("userId", userId);
            List<String> roleId = Lists.transform(dashAdminRoleService.getList(params), new Function<DashAdminRole, String>() {
                @Nullable
                @Override
                public String apply(@Nullable DashAdminRole role) {
                    return role.getId().toString();
                }
            });
            Object[] args = proceedingJoinPoint.getArgs();
            String[] argRoleId = (String[]) args[1];
            roleId.retainAll(Arrays.asList(argRoleId));
            args[1] = roleId.toArray(new String[]{});
            return proceedingJoinPoint.proceed(args);
        }
    }

    @Around("execution(* io.linlan.tools.board.service.DashAdminRoleService.updateRoleResUser(..))")
    public Object updateRoleResUser(ProceedingJoinPoint proceedingJoinPoint) throws Throwable {
        String userId = authService.getCurrentUser().getUserId();
        if (userId.equals(adminUserId)) {
            return proceedingJoinPoint.proceed(proceedingJoinPoint.getArgs());
        } else {
            Object[] args = proceedingJoinPoint.getArgs();
            JSONArray arr = JSONArray.parseArray(args[1].toString());
            List<Object> filtered = arr.stream().filter(e -> {
                JSONObject jo = (JSONObject) e;
                switch (jo.getString("resType")) {
                    case "widget":
                        return dashWidgetService.checkWidgetRole(userId, jo.getString("resId"), RolePermission.PATTERN_READ) > 0;
                    case "dataset":
                        return dashDatasetService.checkDatasetRole(userId, jo.getString("resId"), RolePermission.PATTERN_READ) > 0;
                    case "board":
                        return dashBoardService.checkBoardRole(userId, jo.getString("resId"), RolePermission.PATTERN_READ) > 0;
                    default:
                        return false;
                }
            }).collect(Collectors.toList());
            args[1] = JSONArray.toJSON(filtered).toString();
            return proceedingJoinPoint.proceed(args);
        }
    }


    @Autowired
    private AuthService authService;

    @Autowired
    private DashMenuService dashMenuService;

    @Autowired
    private DashAdminRoleService dashAdminRoleService;

    @Value("${admin_user_id}")
    private String adminUserId;

    @Autowired
    private DashDatasetService dashDatasetService;

    @Autowired
    private DashWidgetService dashWidgetService;

    @Autowired
    private DashBoardService dashBoardService;
}
