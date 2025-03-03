package net.linlan.tools.board.controller;

import com.alibaba.fastjson.JSONObject;
import com.google.common.collect.Lists;
import net.linlan.tools.board.dto.ViewDashBoard;
import net.linlan.tools.board.entity.DashBoard;
import net.linlan.tools.board.entity.DashBoardParam;
import net.linlan.tools.board.service.DashBoardService;
import net.linlan.commons.core.Rcode;
import net.linlan.commons.script.json.JsonUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;


import java.sql.Timestamp;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
@RestController
@RequestMapping("/dash/board")
public class DashBoardController extends BaseController {

    @RequestMapping("/list")
    public List<ViewDashBoard> list() {
        Map<String, Object> map = new HashMap<>();
        List<DashBoard>  list = dashBoardService.getList(map);
       // return Rcode.ok().put("params", list);
        return  Lists.transform(list, ViewDashBoard.TO);
    }

    @RequestMapping("/save")
    public Rcode save(@RequestParam(name = "json") String json) {
        JSONObject jo = JsonUtils.parseJO(json);
        DashBoard board = new DashBoard();
        board.setUserId(user.getUserId());
        board.setName(jo.getString("name"));
        board.setFolderId(jo.getString("folderId"));
        board.setLayout(jo.getString("layout"));
        board.setCreateTime(new Date());
        board.setLastTime(new Timestamp(System.currentTimeMillis()));

        Map<String, Object> paramMap = new HashMap<>();
        paramMap.put("userId", board.getUserId());
        paramMap.put("name", board.getName());
        paramMap.put("folderId", board.getFolderId());
        if (dashBoardService.getCount(paramMap) <= 0) {
            dashBoardService.save(board);
            return Rcode.ok().put("boardId", board.getId());
        } else {
            return Rcode.error("Duplicated name");
        }
    }

    @RequestMapping("/update")
    public Rcode update(@RequestParam(name = "json") String json) {
        JSONObject jo = JsonUtils.parseJO(json);
        DashBoard board = new DashBoard();
        board.setId(jo.getString("id"));
        board.setUserId(user.getUserId());
        board.setName(jo.getString("name"));
        board.setFolderId(jo.getString("folderId"));
        board.setLayout(jo.getString("layout"));
        board.setLastTime(new Timestamp(System.currentTimeMillis()));

        Map<String, Object> paramMap = new HashMap<>();
        paramMap.put("boardId", board.getId());
        paramMap.put("userId", board.getUserId());
        paramMap.put("name", board.getName());
        paramMap.put("folderId", board.getFolderId());
        if (dashBoardService.getCount(paramMap) <= 0) {
            dashBoardService.update(board);
            return Rcode.ok();
        } else {
            return Rcode.error("Duplicated name");
        }
    }

    @RequestMapping("/delete")
    public Rcode delete(@RequestParam(name = "id") String id) {
        dashBoardService.deleteById(id);
        return Rcode.ok();
    }

    @RequestMapping("/info")
    public ViewDashBoard info(@RequestParam(name = "id") String id) {
        ViewDashBoard board = dashBoardService.getBoardData(id);
        return board;
    }

    @RequestMapping("/export")
    public ResponseEntity<byte[]> exportBoard(@RequestParam(name = "id") String id) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
        headers.setContentDispositionFormData("attachment", "report.xls");
        return new ResponseEntity<>(dashBoardService.exportBoard(id, user.getUserId()), headers, HttpStatus.CREATED);
    }

    @RequestMapping("/getParam")
    public DashBoardParam getBoardParam(@RequestParam(name = "boardId") String boardId) {
        String userId = authService.getCurrentUser().getUserId();
        return dashBoardService.getBoardParam(boardId, userId);
    }

    @RequestMapping("/saveParam")
    @Transactional
    public Rcode saveBoardParam(@RequestParam(name = "boardId") String boardId, @RequestParam(name = "content") String content) {
        if (boardId == null) {
            return Rcode.error("Null");
        }
        DashBoardParam boardParam = new DashBoardParam();
        boardParam.setBoardId(boardId);
        boardParam.setUserId(user.getUserId());
        boardParam.setContent(content);
        dashBoardService.deleteBoardParam(boardId, user.getUserId());
        dashBoardService.saveBoardParam(boardParam);
        return Rcode.ok();
    }

    @RequestMapping("/listAdmin")
    public List<ViewDashBoard> listAdmin() {
        Map<String, Object> params = new HashMap<>();
        params.put("adminId", user.getUserId());
        List<DashBoard> list = dashBoardService.getList(params);
        return Lists.transform(list, ViewDashBoard.TO);
    }

    @RequestMapping("/saveData")
    public Rcode saveData(@RequestParam(name = "json") String json) {
        JSONObject jo = JsonUtils.parseJO(json);
        DashBoard board = new DashBoard();
        board.setUserId(user.getUserId());
        board.setName(jo.getString("name"));
        board.setFolderId(jo.getString("folderId"));
        board.setLayout(jo.getString("layout"));
        board.setCreateTime(new Date());
        board.setLastTime(new Timestamp(System.currentTimeMillis()));

        Map<String, Object> paramMap = new HashMap<>();
        paramMap.put("userId", board.getUserId());
        paramMap.put("name", board.getName());
        paramMap.put("folderId", board.getFolderId());
        if (dashBoardService.getCount(paramMap) <= 0) {
            dashBoardService.save(board);
            return Rcode.ok().put("boardId", board.getId());
        } else {
            return Rcode.error("Duplicated name");
        }
    }

    @RequestMapping("/getData")
    public List<ViewDashBoard> getData() {
        Map<String, Object> params = new HashMap<>();
        params.put("adminId", user.getUserId());
        List<DashBoard> list = dashBoardService.getList(params);
        return Lists.transform(list, ViewDashBoard.TO);
    }
    @Autowired
    private DashBoardService dashBoardService;
}
