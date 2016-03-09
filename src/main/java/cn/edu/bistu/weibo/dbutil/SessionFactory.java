package cn.edu.bistu.weibo.dbutil;

import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;

import java.io.InputStream;

/**
 * Created by tanjie on 10/16/15.
 */
public class SessionFactory {
    private static InputStream inputStream = SessionFactory.class.getResourceAsStream("/mybatisConfig.xml");
    private static SqlSessionFactoryBuilder builder = new SqlSessionFactoryBuilder();
    private static SqlSessionFactory factory = builder.build(inputStream);

    public static SqlSessionFactory getFactory(){
        return factory;
    }
}
