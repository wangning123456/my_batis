package com.lagou.sqlSession;

import com.lagou.config.XMLConfigBuild;
import com.lagou.pojo.Configuration;
import org.dom4j.DocumentException;

import java.beans.PropertyVetoException;
import java.io.InputStream;

/**
 * @author wangning
 */
public class SqlSessionFactoryBuild {





    /**
     * 将dom4j解析出来的内容封装到Configuration中
     * @param io
     * @return
     */
    public SqlSessionFactory build(InputStream io) throws DocumentException, PropertyVetoException {
        //1.解析io流,把数据放到configuration中
        XMLConfigBuild xmlConfigBuild = new XMLConfigBuild();
        Configuration configuration = xmlConfigBuild.parseConnection(io);
        //2.创建 sqlSessionFactory
        DefaultSqlSessionFactory sqlSessionFactory = new DefaultSqlSessionFactory(configuration);
        return sqlSessionFactory;
    }

    //创建 SqlSessionFactory 对象
}
