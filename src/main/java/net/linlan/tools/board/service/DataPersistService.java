package net.linlan.tools.board.service;

import com.alibaba.fastjson.JSONObject;
import net.linlan.tools.board.dao.DashBoardDao;
import net.linlan.commons.core.CoreException;
import net.linlan.tools.security.service.LocalSecurityFilter;
import net.linlan.tools.board.service.persist.PersistContext;
import net.linlan.commons.core.RandomUtils;
import net.linlan.commons.core.Rcode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.InputStreamReader;
import java.io.LineNumberReader;
import java.net.URLDecoder;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

/**
 * 
 * Filename:DataPersistService.java
 * Desc:
 *
 * @author Linlan
 * CreateTime:2018/1/3 12:01
 *
 * @version 1.0
 * @since 1.0
 *
 */
@Service
public class DataPersistService {

    private static final Logger logger = LoggerFactory.getLogger(DataPersistService.class);

    private String scriptPath = new File(this.getClass().getResource("/phantom.js").getFile()).getPath();

    private static final ConcurrentMap<String, PersistContext> TASK_MAP = new ConcurrentHashMap<>();

    public PersistContext persist(String dashboardId, String userId) {
        String persistId = UUID.randomUUID().toString().replaceAll("-", "");
        Process process = null;
        try {
            if (dashBoardDao.findById(dashboardId) == null) {
                throw new CoreException(String.format("Dashbaord ID [%s] doesn't exist!", dashboardId));
            }
            PersistContext context = new PersistContext(dashboardId);
            TASK_MAP.put(persistId, context);
            String uuid = RandomUtils.UUID32();
            LocalSecurityFilter.put(uuid, userId);
            String phantomUrl = new StringBuffer("http://127.0.0.1:8080")
                    .append(LocalSecurityFilter.getContext())
                    .append("/render.html")
                    .append("?sid=").append(uuid)
                    .append("#?id=").append(dashboardId)
                    .append("&pid=").append(persistId)
                    .toString();
            scriptPath = URLDecoder.decode(scriptPath, "UTF-8"); // decode whitespace
            String cmd = String.format("%s %s %s", phantomjsPath, scriptPath, phantomUrl);
            logger.info("Run phantomjs command: {}", cmd);
            process = Runtime.getRuntime().exec(cmd);
            final Process p = process;
            new Thread(() -> {
                InputStreamReader ir = new InputStreamReader(p.getInputStream());
                LineNumberReader input = new LineNumberReader(ir);
                String line;
                try {
                    while ((line = input.readLine()) != null) {
                        logger.info(line);
                    }
                    logger.info("Finished command " + cmd);
                } catch (Exception e) {
                    logger.error("Error", e);
                    p.destroy();
                }
            }).start();
            synchronized (context) {
                context.wait(10 * 60 * 1000);
            }
            process.destroy();
            TASK_MAP.remove(persistId);
            return context;
        } catch (Exception e) {
            if (process != null) {
                process.destroy();
            }
            logger.error(getClass().getName(), e);
            throw new CoreException(e.getMessage());
        }
    }

    public Rcode persistCallback(String persistId, JSONObject data) {
        PersistContext context = TASK_MAP.get(persistId);
        synchronized (context) {
            context.setData(data);
            context.notify();
        }
        return Rcode.ok();
    }

    @Value("${phantomjs_path}")
    private String phantomjsPath;


    @Autowired
    private DashBoardDao dashBoardDao;

}
