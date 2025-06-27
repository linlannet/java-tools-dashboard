package net.linlan.tools.data.provider;

import com.alibaba.fastjson2.JSONArray;
import com.alibaba.fastjson2.JSONObject;
import com.google.common.base.Joiner;
import net.linlan.commons.core.NumberUtils;
import net.linlan.commons.core.RandomUtils;
import net.linlan.commons.db.annotation.DataProviderName;
import net.linlan.commons.db.annotation.DataQueryParameter;
import net.linlan.commons.db.annotation.DataSourceParameter;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;


/**
 *
 * Filename:SaikuDataProvider.java
 * Desc:
 *
 * @author Linlan
 * CreateTime:2017/12/20 22:15
 *
 * @version 1.0
 * @since 1.0
 *
 */
@DataProviderName(name = "saiku")
public class SaikuDataProvider extends DataProvider {

    @DataSourceParameter(label = "Saiku Server (http://domain:port)", type = DataSourceParameter.Type.Input, order = 1)
    private String SERVERIP = "serverIp";

    @DataSourceParameter(label = "User Name (for Saiku Server)", type = DataSourceParameter.Type.Input, order = 2)
    private String USERNAME = "username";

    @DataSourceParameter(label = "Password", type = DataSourceParameter.Type.Password, order = 3)
    private String PASSWORD = "password";

    @DataQueryParameter(label = "Repo Path of Report", type = DataQueryParameter.Type.Input)
    private String FILE = "file";

    @Override
    public boolean doAggregationInDataSource() {
        return false;
    }

    @Override
    public String[][] getData() throws Exception {
        String serverIp = dataSource.get(SERVERIP);
        String username = dataSource.get(USERNAME);
        String password = dataSource.get(PASSWORD);

        String file = query.get(FILE);

        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<String> a = restTemplate.postForEntity(serverIp + "/saiku/rest/saiku/session?username={username}&password={password}", null, String.class, username, password);
        HttpHeaders headers = new HttpHeaders();
        headers.set("Cookie", Joiner.on(";").join(a.getHeaders().get("Set-Cookie")));
        String uuid = RandomUtils.UUID();
        restTemplate.exchange(serverIp + "/saiku/rest/saiku/api/query/{id}?file={file}&formatter={formatter}&type={type}", HttpMethod.POST, new HttpEntity<>(headers), String.class, uuid, file, "flattened", "QM");
        a = restTemplate.exchange(serverIp + "/saiku/rest/saiku/api/query/{id}/result/flattened", HttpMethod.GET, new HttpEntity<>(headers), String.class, uuid);
        JSONObject json = JSONObject.parseObject(new String(a.getBody().getBytes("ISO8859-1"), "UTF-8"));
        JSONArray array = json.getJSONArray("cellset");

        String[] columnHeader = new String[json.getInteger("width")];
        int i;
        for (i = 0; i < array.size(); i++) {
            JSONArray cols = array.getJSONArray(i);
            if ("ROW_HEADER".equals(cols.getJSONObject(0).get("type"))) {
                break;
            } else {
                for (int j = 0; j < cols.size(); j++) {
                    String value = cols.getJSONObject(j).getString("value");
                    if (columnHeader[j] == null) {
                        columnHeader[j] = value.equals("null") ? "" : value;
                    } else {
                        columnHeader[j] += value.equals("null") ? "" : value;
                    }
                }
            }
        }

        String[][] result = new String[json.getInteger("height") - (i - 1)][json.getInteger("width")];
        result[0] = columnHeader;
        String[] rowHeader = new String[json.getInteger("width")];
        for (int j = i; j < array.size(); j++) {
            JSONArray cols = array.getJSONArray(j);
            for (int k = 0; k < cols.size(); k++) {
                if ("DATA_CELL".equals(cols.getJSONObject(k).getString("type"))) {
                    String raw = cols.getJSONObject(k).getJSONObject("properties").getString("raw");
                    if (NumberUtils.isNumber(raw)) {
                        result[j - i + 1][k] = raw;
                    } else {
                        result[j - i + 1][k] = "0";
                    }
                } else {
                    String v = cols.getJSONObject(k).getString("value");
                    if (!"null".equals(v)) {
                        result[j - i + 1][k] = cols.getJSONObject(k).getString("value");
                        rowHeader[k] = cols.getJSONObject(k).getString("value");
                    } else {
                        result[j - i + 1][k] = rowHeader[k];
                    }

                }
            }
        }

        return result;
    }
}
