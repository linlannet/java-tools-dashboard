package io.linlan.config;

import org.apache.commons.pool2.impl.GenericObjectPool;
import org.apache.commons.pool2.impl.GenericObjectPoolConfig;
import org.apache.solr.client.solrj.SolrClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

/**
 *
 * Filename:SolrServerPoolFactory.java
 * Desc:
 *
 * @author hcday of Howai.org
 * @author <a href="mailto:hcday@qq.com">hcday soo</a>
 * CreateTime:2017/12/20 22:15
 *
 * @version 1.0
 * @since 1.0
 *
 */
public class SolrServerPoolFactory {
    private Logger logger = LoggerFactory.getLogger(this.getClass());
    private GenericObjectPool<SolrClient> pool;

    public SolrServerPoolFactory(GenericObjectPoolConfig config, String solrServices, String collectionName) {
        SolrServerFactory factory = new SolrServerFactory(solrServices, collectionName);
        pool = new GenericObjectPool(factory, config);
    }

    public SolrClient getConnection() {
        try {
            return pool.borrowObject();
        } catch (Exception e) {
            logger.error("", e);
            return null;
        }
    }

    public void releaseConnection(SolrClient solrClient) {
        try {
            pool.returnObject(solrClient);
        } catch (Exception e) {
            if (solrClient != null) {
                try {
                    solrClient.close();
                } catch (IOException e1) {
                    logger.error("", e1);
                }
                solrClient = null;
            }
            logger.error("", e);
        }
    }

    public void closePool() {
        if (pool != null) {
            try {
                pool.close();
                pool = null;
            } catch (Exception e) {
                logger.error("", e);
            }
        }
    }
}
