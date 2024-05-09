package io.linlan.tools.board.service.role;

import org.apache.commons.lang.StringUtils;

/**
 * 
 * Filename:RolePermission.java
 * Desc:
 *
 * @author Linlan
 * CreateTime:2018/1/3 12:04
 *
 * @version 1.0
 * @since 1.0
 *
 */
public class RolePermission {

    public static final String PATTERN_EDIT = "1_";
    public static final String PATTERN_DELETE = "_1";
    public static final String PATTERN_READ = "%";

    public static boolean isEdit(String permission) {
        return get(permission, 0);
    }

    public static boolean isDelete(String permission) {
        return get(permission, 1);
    }

    public static boolean get(String permission, int bit) {
        if (StringUtils.isEmpty(permission) || permission.length() - 1 < bit) {
            return false;
        } else {
            return permission.toCharArray()[bit] == '1';
        }
    }
}
