package io.linlan.tools.board.controller;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import io.linlan.tools.board.entity.DashConfigVersion;
import io.linlan.tools.board.service.DashConfigVersionService;
import io.linlan.commons.db.page.Pagination;
import io.linlan.commons.db.query.Query;
import io.linlan.commons.core.Rcode;

/**
 *
 * Filename:ConfigVersion.java
 * Desc:
 *
 * @author hcday
 * @author <a href="mailto:hcday@qq.com">hcday</a>
 * CreateTime:2018-05-05 15:11:56
 *
 * @version 1.0
 * @since 1.0
 *
 */
@RestController
@RequestMapping("dash/configversion")
public class DashConfigVersionController {

    /** get the list with request params
     * 列表方法，返回{@link Rcode}，包括状态和page
     * @param params the input params
     * @return {@link Rcode} with page info
     */
    @RequestMapping("/list")
    public Rcode list(@RequestParam Map<String, Object> params){
        //查询列表数据
        Query query = new Query(params);

        List<DashConfigVersion> configVersionList = configVersionService.getList(query);
        int total = configVersionService.queryTotal(query);

        Pagination pagination = new Pagination(configVersionList, total, query.getLimit(), query.getPage());

        return Rcode.ok().put("page", pagination);
    }


    /** get the detail info of entity
     * 详情方法，返回{@link Rcode}，包括状态和configVersion
     * @param id the input id
     * @return {@link Rcode} with configVersion info
     */
    @RequestMapping("/info/{id}")
    public Rcode info(@PathVariable("id") Long id){
        DashConfigVersion configVersion = configVersionService.findById(id);

        return Rcode.ok().put("configVersion", configVersion);
    }

    /** save entity with object
     * 保存方法，返回{@link Rcode}，状态
     * @param configVersion the input object
     * @return {@link Rcode}
     */
    @RequestMapping("/save")
    public Rcode save(@RequestBody DashConfigVersion configVersion){
        configVersionService.save(configVersion);

        return Rcode.ok();
    }

    /** update entity with object
     * 更新方法，返回{@link Rcode}，状态
     * @param configVersion the input object
     * @return {@link Rcode}
     */
    @RequestMapping("/update")
    public Rcode update(@RequestBody DashConfigVersion configVersion){
        configVersionService.update(configVersion);

        return Rcode.ok();
    }

    /** delete entity with input ids
     * 删除方法，返回{@link Rcode}，状态
     * @param ids the input ids
     * @return {@link Rcode}
     */
    @RequestMapping("/delete")
    public Rcode delete(@RequestBody Long[] ids){
        configVersionService.deleteByIds(ids);

        return Rcode.ok();
    }

    @Autowired
    private DashConfigVersionService configVersionService;


}
