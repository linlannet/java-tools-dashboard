package net.linlan.tools.board.service;

import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONArray;
import com.alibaba.fastjson2.JSONObject;
import net.linlan.tools.board.dao.DashBoardDao;
import net.linlan.tools.board.dto.ViewDashBoard;
import net.linlan.tools.board.dto.ViewDashWidget;
import net.linlan.tools.board.entity.DashBoard;
import net.linlan.tools.board.entity.DashBoardParam;
import net.linlan.tools.board.entity.DashWidget;
import net.linlan.tools.board.service.persist.excel.XlsProcessorService;
import net.linlan.tools.board.service.persist.PersistContext;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import net.linlan.commons.script.json.StringMap;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.sql.Timestamp;
import java.util.*;

/**
 *
 * Filename:DashBoard.java
 * Desc:
 *
 * @author Linlan
 * CreateTime:2017-12-18 20:35:17
 *
 * @version 1.0
 * @since 1.0
 *
 */
@Repository
public class DashBoardService {

    private Logger logger = LoggerFactory.getLogger(this.getClass());

    /** get the list of entity DashBoard
     * 列表方法，返回{@link List<   DashBoard   >}
     * @param map the input select conditions
     * @return {@link List<  DashBoard  >}
     */
    public List<DashBoard> getList(Map<String, Object> map) {
        return dao.getList(map);
    }

    /** find the entity by input id, return entity
     * 对象详情方法，通过id查询对象{@link DashBoard}
     * @param id the input id
     * @return {@link DashBoard}
     */
    public DashBoard findById(String id){
        return dao.findById(id);
    }

    /** save the entity with input object
     * 保存对象方法
     * @param dashBoard the input dashBoard
     */
    public void save(DashBoard dashBoard){
        dashBoard.init();
//        dashBoard.setId(1l);
        dao.save(dashBoard);
    }

    /** update the entity with input object
     * 更新对象方法
     * @param dashBoard the input dashBoard
     */
    public void update(DashBoard dashBoard){
        dashBoard.setLastTime(new Timestamp(System.currentTimeMillis()));
        dao.update(dashBoard);
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

    public ViewDashBoard getBoardData(String id) {
        DashBoard board = dao.findById(id);
        JSONObject layout = JSONObject.parseObject(board.getLayout());
        JSONArray rows = layout.getJSONArray("rows");
        for (Object row : rows) {
            JSONObject o = (JSONObject) row;
            if ("param".equals(o.getString("type"))) {
                layout.put("containsParam", true);
                continue;
            }
            JSONArray widgets = o.getJSONArray("widgets");
            for (Object w : widgets) {
                JSONObject ww = (JSONObject) w;
                String widgetId = ww.getString("widgetId");
                DashWidget widget = dashWidgetService.findById(widgetId);
                JSONObject dataJson = JSONObject.parseObject(widget.getContent());
                //DataProviderResult data = dataProviderService.getData(dataJson.getString("datasource"), Maps.transformValues(dataJson.getJSONObject("query"), Functions.toStringFunction()));
                JSONObject widgetJson = (JSONObject) JSON.toJSON(new ViewDashWidget(widget));
                //widgetJson.put("queryData", data.getContent());
                ww.put("widget", widgetJson);
                ww.put("show", false);
            }
        }
        ViewDashBoard view = new ViewDashBoard(board);
        view.setLayout(layout);
        return view;
    }

    public byte[] exportBoard(String id, String userId) {
        PersistContext persistContext = dataPersistService.persist(id, userId);
        List<PersistContext> workbookList = new ArrayList<>();
        workbookList.add(persistContext);
        HSSFWorkbook workbook = xlsProcessorService.dashboardToXls(workbookList);
        try {
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            workbook.write(outputStream);
            outputStream.close();
            return outputStream.toByteArray();
        } catch (IOException e) {
            logger.error("", e);
        }
        return null;
    }

    public int deleteBoardParam(String boardId, String userId) {
        return dao.deleteBoardParam(boardId, userId);
    }

    public int saveBoardParam(DashBoardParam boardParam) {
        return dao.saveBoardParam(boardParam);
    }

    public DashBoardParam getBoardParam(String boardId, String userId) {
        return dao.getBoardParam(boardId, userId);
    }

    public int checkBoardRole(String userId, String resId, String patternRead) {
        return dao.checkBoardRole(userId, resId, patternRead);
    }

    public List<DashBoard> getBoardListByFolderIds(String[] folderIds){
        if (folderIds == null || folderIds.length == 0) {
            return null;
        }

        return dao.getBoardListByFolderIds(new StringMap().put("folderIds", folderIds).map());
    }


    @Autowired
    private DashBoardDao dao;

    @Autowired
    private DashWidgetService dashWidgetService;

    @Autowired
    private DataPersistService dataPersistService;

    @Autowired
    private XlsProcessorService xlsProcessorService;


}
