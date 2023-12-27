package io.linlan.config;

import org.apache.commons.pool2.PooledObject;
import org.apache.commons.pool2.PooledObjectFactory;
import org.apache.commons.pool2.impl.DefaultPooledObject;
import org.apache.solr.client.solrj.SolrClient;
import org.apache.solr.client.solrj.impl.HttpSolrClient;

/**
 *
 * Filename:SolrServerFactory.java
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
public class SolrServerFactory implements PooledObjectFactory<SolrClient> {

    private String[] servers;

    public SolrServerFactory(String solrServices, String collectionName) {
        String[] tempServers = solrServices.split(",");
        servers = new String[tempServers.length];
        for (int i = 0; i < tempServers.length; i++) {
            servers[i] = "http://" + tempServers[i] + "/solr/" + collectionName;
        }
    }

    public PooledObject<SolrClient> makeObject() throws Exception {
        //hcday modify 2017-12-20
        SolrClient solrClient = new HttpSolrClient.Builder(servers[0]).withConnectionTimeout(10000)
                .withSocketTimeout(60000)
                .build();
        return new DefaultPooledObject(solrClient);
    }

    public void destroyObject(PooledObject<SolrClient> pool) throws Exception {
        SolrClient solrClient = pool.getObject();
        if (solrClient != null) {
            solrClient.close();
            solrClient = null;
        }
    }

    public void activateObject(PooledObject<SolrClient> pool) throws Exception {
    }

    public void passivateObject(PooledObject<SolrClient> pool) throws Exception {
    }

    public boolean validateObject(PooledObject<SolrClient> pool) {
        return false;
    }

}