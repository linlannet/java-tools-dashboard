package net.linlan.tools.board.controller;


import com.alibaba.fastjson.JSONObject;
import com.google.common.base.Functions;
import com.google.common.collect.Maps;
import com.google.common.hash.Hashing;
import net.linlan.tools.board.dto.DataProviderResult;
import net.linlan.tools.board.dto.ViewAggConfig;
import net.linlan.tools.board.service.DataPersistService;
import net.linlan.tools.board.service.DataProviderManager;
import net.linlan.tools.board.service.DataProviderService;
import net.linlan.tools.board.service.DataProviderViewManager;
import net.linlan.tools.security.User;
import net.linlan.tools.board.service.persist.excel.XlsProcessorService;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import net.linlan.commons.core.CharsetUtils;
import net.linlan.commons.core.Rcode;
import net.linlan.commons.script.json.JsonUtils;
import net.linlan.datas.core.provider.config.AggConfig;
import net.linlan.datas.core.provider.result.AggregateResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;


/**
 * 
 * Filename:CommonsController.java
 * Desc:
 *
 * @author Linlan
 * CreateTime:2017/12/19 20:36
 *
 * @version 1.0
 * @since 1.0
 *
 */
@RestController
@RequestMapping("/dash/common")
public class CommonsController extends BaseController {

    @RequestMapping("/getUserDetail")
    public User getUserDetail() {
        return authService.getCurrentUser();
    }


    @RequestMapping("/persist")
    public Rcode persist(@RequestBody String dataStr) {
        JSONObject data = JsonUtils.parseJO(dataStr);
        return dataPersistService.persistCallback(data.getString("persistId"), data.getJSONObject("data"));
    }

    @RequestMapping("/test")
    public Rcode test(@RequestParam(name = "datasource", required = false) String datasource, @RequestParam(name = "query", required = false) String query) {
        JSONObject queryO = JsonUtils.parseJO(query);
        JSONObject datasourceO = JsonUtils.parseJO(datasource);
        return dataProviderService.test(datasourceO, Maps.transformValues(queryO, Functions.toStringFunction()));
    }


    @RequestMapping("/getProviderList")
    public Set<String> getProviderList() {
        return DataProviderManager.getProviderList();
    }

    @RequestMapping("/getConfigParams")
    public List<Map<String, Object>> getConfigParams(@RequestParam(name = "type") String type,
                                                     @RequestParam(name = "page") String page,
                                                     @RequestParam(name = "datasourceId", required = false) String datasourceId) {
        Map<String, String> dataSource = null;
        if (datasourceId != null) {
            dataSource = dataProviderService.getDataSource(datasourceId);
        }
        return DataProviderViewManager.getQueryParams(type, page, dataSource);
    }


    @RequestMapping("/getConfigView")
    public String getConfigView(@RequestParam(name = "type") String type,
                                @RequestParam(name = "page") String page,
                                @RequestParam(name = "datasourceId", required = false) String datasourceId) {
        Map<String, String> dataSource = null;
        if (datasourceId != null) {
            dataSource = dataProviderService.getDataSource(datasourceId);
        }
        return DataProviderViewManager.getQueryView(type, page, dataSource);
    }

    @RequestMapping("/getDatasourceParams")
    public List<Map<String, Object>> getDatasourceParams(@RequestParam(name = "type") String type) {
        return DataProviderViewManager.getDatasourceParams(type);
    }

    @RequestMapping("/getDatasourceView")
    public String getDatasourceView(@RequestParam(name = "type") String type) {
        return DataProviderViewManager.getDatasourceView(type);
    }

    @RequestMapping("/getDimensionValues")
    public String[] getDimensionValues(@RequestParam(name = "datasourceId", required = false) String datasourceId,
                                       @RequestParam(name = "query", required = false) String query,
                                       @RequestParam(name = "datasetId", required = false) String datasetId,
                                       @RequestParam(name = "colmunName", required = true) String colmunName,
                                       @RequestParam(name = "cfg", required = false) String cfg,
                                       @RequestParam(name = "reload", required = false, defaultValue = "false") boolean reload) {
        Map<String, String> strParams = null;
        if (query != null) {
            JSONObject queryO = JsonUtils.parseJO(query);
            strParams = Maps.transformValues(queryO, Functions.toStringFunction());
        }
        AggConfig config = null;
        if (cfg != null) {
            config = ViewAggConfig.getAggConfig(JSONObject.parseObject(cfg, ViewAggConfig.class));
        }
        return dataProviderService.getDimensionValues(datasourceId, strParams, datasetId, colmunName, config, reload);
    }

    @RequestMapping("/getColumns")
    public DataProviderResult getColumns(@RequestParam(name = "datasourceId", required = false) String datasourceId,
                                         @RequestParam(name = "query", required = false) String query,
                                         @RequestParam(name = "datasetId", required = false) String datasetId,
                                         @RequestParam(name = "reload", required = false, defaultValue = "false") boolean reload) {
        Map<String, String> strParams = null;
        if (query != null) {
            JSONObject queryO = JsonUtils.parseJO(query);
            strParams = Maps.transformValues(queryO, Functions.toStringFunction());
        }
        return dataProviderService.getColumns(datasourceId, strParams, datasetId, reload);
    }

    @RequestMapping("/getAggregateData")
    public AggregateResult getAggregateData(@RequestParam(name = "datasourceId", required = false) String datasourceId,
                                            @RequestParam(name = "query", required = false) String query,
                                            @RequestParam(name = "datasetId", required = false) String datasetId,
                                            @RequestParam(name = "cfg") String cfg,
                                            @RequestParam(name = "reload", required = false, defaultValue = "false") boolean reload) {
        Map<String, String> strParams = null;
        if (query != null) {
            JSONObject queryO = JsonUtils.parseJO(query);
            strParams = Maps.transformValues(queryO, Functions.toStringFunction());
        }
        AggregateResult aggResult = null;
        // data source aggreagtor instance need not lock
        boolean isDataSourceAggInstance = dataProviderService.isDataSourceAggInstance(datasourceId, strParams, datasetId);
        String randomFlag = isDataSourceAggInstance ? UUID.randomUUID().toString() : "1";
        String lockString = Hashing.md5().newHasher()
                .putString(datasourceId + query + datasetId + user.getUserId() + randomFlag, CharsetUtils.CHARSET_UTF_8)
                .hash().toString();
        synchronized (lockString.intern()) {
            AggConfig config = ViewAggConfig.getAggConfig(JSONObject.parseObject(cfg, ViewAggConfig.class));

            aggResult = dataProviderService.queryAggData(datasourceId, strParams, datasetId, config, reload);
        }
        return aggResult;
    }

    @RequestMapping("/viewAggDataQuery")
    public String[] viewAggDataQuery(@RequestParam(name = "datasourceId", required = false) String datasourceId,
                                     @RequestParam(name = "query", required = false) String query,
                                     @RequestParam(name = "datasetId", required = false) String datasetId,
                                     @RequestParam(name = "cfg") String cfg) {
        Map<String, String> strParams = null;
        if (query != null) {
            JSONObject queryO = JsonUtils.parseJO(query);
            strParams = Maps.transformValues(queryO, Functions.toStringFunction());
        }
        AggConfig config = ViewAggConfig.getAggConfig(JSONObject.parseObject(cfg, ViewAggConfig.class));
        return new String[]{dataProviderService.viewAggDataQuery(datasourceId, strParams, datasetId, config)};
    }


    @RequestMapping("/tableToxls")
    public ResponseEntity<byte[]> tableToxls(@RequestParam(name = "data") String data) {
        HSSFWorkbook wb = xlsProcessorService.tableToxls(JsonUtils.parseJO(data));
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        try {
            wb.write(out);
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
            headers.setContentDispositionFormData("attachment", "table.xls");
            return new ResponseEntity<>(out.toByteArray(), headers, HttpStatus.CREATED);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    @ExceptionHandler
    public Rcode exp(HttpServletResponse response, Exception ex) {
        response.setStatus(500);
        logger.error("Gloal exception Handler", ex);
        return Rcode.error(ex.getMessage());
    }

    @Autowired
    private DataProviderService dataProviderService;

    @Autowired
    private XlsProcessorService xlsProcessorService;

    @Autowired
    private DataPersistService dataPersistService;
}
