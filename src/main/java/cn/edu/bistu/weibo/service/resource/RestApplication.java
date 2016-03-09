package cn.edu.bistu.weibo.service.resource;

import com.fasterxml.jackson.jaxrs.json.JacksonJsonProvider;
import org.apache.log4j.Logger;
import org.glassfish.jersey.server.ResourceConfig;

/**
 * Created by tanjie on 10/16/15.
 */
public class RestApplication extends ResourceConfig {
    protected static Logger log = Logger.getLogger(RestApplication.class);
    /**
     *  载入
     */
    public RestApplication() {
        log.info("loading service package: " + "cn.edu.bistu.weibo.service");
        //载入rest包
        this.packages("cn.edu.bistu.weibo.service");
        //注册json支持组件
        this.register(JacksonJsonProvider.class);

        log.info("loading service package success...");
    }
}
