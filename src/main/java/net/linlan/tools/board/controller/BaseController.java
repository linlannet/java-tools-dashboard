package net.linlan.tools.board.controller;

import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.JSONPath;
import net.linlan.tools.board.dto.BaseControllerLog;
import net.linlan.tools.security.User;
import org.apache.commons.lang.StringUtils;
import net.linlan.tools.board.service.AuthService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.ModelAttribute;

import javax.servlet.http.HttpServletRequest;
import java.util.regex.Pattern;


/**
 * the base controller for sub to extend and provide common method
 * Filename:BaseController.java
 * Desc: the BaseController provide user operations
 *
 * @author Linlan
 * CreateTime:2017/12/16 21:37
 *
 * @version 1.0
 * @since 1.0
 *
 */
public class BaseController {

    Logger logger = LoggerFactory.getLogger(this.getClass());

    @ModelAttribute
    public void initialAuthUser(HttpServletRequest request) {
        String url = request.getRequestURL().toString();
        user = authService.getCurrentUser();
        String log = new BaseControllerLog(user, url).toString();

        boolean isNegativeMatch = false, isPositiveMatch = true;

        if (StringUtils.isNotBlank(positiveFilter)) {
            isPositiveMatch = Pattern.compile(positiveFilter).matcher(log).find();
        }

        if (StringUtils.isNotBlank(negativeFilter)) {
            isNegativeMatch = Pattern.compile(negativeFilter).matcher(log).find();
        }

        if (user != null && !isNegativeMatch && isPositiveMatch) {
            logger.info(log);
        }
    }

    public void addFilterGroupId(JSONObject dataset, Object o) {
        JSONObject object = (JSONObject) o;
        if (!object.containsKey("group") || object.containsKey("id")) {
            return;
        }
        if (dataset.getJSONArray("filters") == null) {
            return;
        }
        String group = object.getString("group");
        String id = (String) JSONPath.eval(dataset, "$.filters[group='" + group + "'][0].id");
        if (id != null) {
            object.put("id", id);
        }
    }

    public void addExpressionId(JSONObject dataset, Object o) {
        JSONObject object = (JSONObject) o;
        if (!"exp".equals(object.getString("type")) || object.containsKey("id")) {
            return;
        }
        if (dataset.getJSONArray("expressions") == null) {
            return;
        }
        String alias = object.getString("alias");
        String id = (String) JSONPath.eval(dataset, "$.expressions[alias='" + alias + "'][type='exp'][0].id");
        if (id != null) {
            object.put("id", id);
        }
    }

    public void addDimensionId(JSONObject dataset, Object o) {
        JSONObject object = (JSONObject) o;
        if (object.containsKey("id")) {
            return;
        }
        if (!dataset.containsKey("schema")) {
            return;
        }
        String column = object.getString("col");
        String alias = object.getString("alias");

        String path = "$.dimension";
        if (org.apache.commons.lang3.StringUtils.isNotEmpty(alias)) {
            path += "[alias='" + alias + "']";
        }
        if (org.apache.commons.lang3.StringUtils.isNotEmpty(column)) {
            path += "[column='" + column + "']";
        }
        path += "[0].id";
        String id = (String) JSONPath.eval(dataset.getJSONObject("schema"), path);
        if (id == null) {
            path = "$.dimension[type='level'].columns";
            if (org.apache.commons.lang3.StringUtils.isNotEmpty(alias)) {
                path += "[alias='" + alias + "']";
            }
            if (org.apache.commons.lang3.StringUtils.isNotEmpty(column)) {
                path += "[column='" + column + "']";
            }
            path += "[0].id";
            id = (String) JSONPath.eval(dataset.getJSONObject("schema"), path);
        }
        if (id != null) {
            object.put("id", id);
        }
    }

    @Autowired
    protected AuthService authService;

    protected User user;

    @Value("${log.negativeFilter}")
    protected String negativeFilter;

    @Value("${log.positiveFilter}")
    protected String positiveFilter;

}
