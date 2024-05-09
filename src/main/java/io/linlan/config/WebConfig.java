package io.linlan.config;

import org.apache.ibatis.mapping.DatabaseIdProvider;
import org.apache.ibatis.mapping.VendorDatabaseIdProvider;
import org.springframework.context.annotation.*;
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerAdapter;

import java.nio.charset.Charset;
import java.util.Iterator;
import java.util.List;
import java.util.Properties;

/**
 * the web config of project to load properties and scan inject
 * Filename:WebConfig.java
 * Desc: the WebConfig class to autowired
 *
 * @author Linlan
 * CreateTime:2017/12/13 11:45
 *
 * @version 1.0
 * @since 1.0
 *
 */
@Configuration
@EnableWebMvc
@ComponentScan(basePackages = {"io.linlan"})
@PropertySource(value = {"classpath:config.properties"})
@EnableAspectJAutoProxy(proxyTargetClass = true)
public class WebConfig implements WebMvcConfigurer {

    @Bean
    public RequestMappingHandlerAdapter requestMappingHandlerAdapter() {
        RequestMappingHandlerAdapter adapter = new RequestMappingHandlerAdapter();
        // @ResponseBody注解靠HttpMessageConverter解析
        List<HttpMessageConverter<?>> converters = adapter.getMessageConverters();
        Iterator<HttpMessageConverter<?>> iterator = converters.iterator();
        while (iterator.hasNext()) {
            HttpMessageConverter<?> converter = iterator.next();
            if (converter instanceof StringHttpMessageConverter) {  // 移除默认编码为ISO8859-1的字符串解析器
                iterator.remove();
            }
        }
        converters.add(new StringHttpMessageConverter(Charset.forName("UTF-8")));  // 字符串才使用UTF-8解析
        converters.add(new MappingJackson2HttpMessageConverter());  // 解析json
        adapter.setMessageConverters(converters);
        return adapter;
    }

    @Bean
    public static PropertySourcesPlaceholderConfigurer propertyConfigIn() {
        return new PropertySourcesPlaceholderConfigurer();
    }

    /**
     * 自动识别使用的数据库类型
     * 在mapper.xml中databaseId的值就是跟这里对应，
     * 如果没有databaseId选择则说明该sql适用所有数据库
     * */
    @Bean
    public DatabaseIdProvider getDatabaseIdProvider(){
        DatabaseIdProvider databaseIdProvider = new VendorDatabaseIdProvider();
        Properties properties = new Properties();
        properties.setProperty("Oracle","oracle");
        properties.setProperty("MySQL","mysql");
        properties.setProperty("DM DBMS", "dm7");
        databaseIdProvider.setProperties(properties);
        return databaseIdProvider;
    }

}
