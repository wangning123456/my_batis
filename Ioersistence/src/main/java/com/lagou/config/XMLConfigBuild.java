package com.lagou.config;

import com.lagou.io.Resource;
import com.lagou.pojo.Configuration;
import com.mchange.v2.c3p0.ComboPooledDataSource;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

import java.beans.PropertyVetoException;
import java.io.InputStream;
import java.util.List;
import java.util.Properties;


public class XMLConfigBuild {

    private Configuration configuration;

    public XMLConfigBuild() {
        this.configuration = new Configuration();
    }

    /**
     * 将解析出来的文件放到Configuration中
     * @param io
     * @return
     */
    public Configuration parseConnection(InputStream io) throws DocumentException, PropertyVetoException {
        Document read = new SAXReader().read(io);
        //获取元素
        Element rootElement = read.getRootElement();
        Properties properties = new Properties();
        List<Element> property = rootElement.selectNodes("//property");
        for (Element element : property) {
            String name = element.attributeValue("name");
            String value = element.attributeValue("value");
            properties.setProperty(name,value);
        }
        //c3p0 连接池
        ComboPooledDataSource dataSource = new ComboPooledDataSource();
        dataSource.setDriverClass(properties.getProperty("driverClass"));
        dataSource.setJdbcUrl(properties.getProperty("jdbcUrl"));
        dataSource.setUser(properties.getProperty("username"));
        dataSource.setPassword(properties.getProperty("password"));
        configuration.setDataSource(dataSource);
        //解析Mapper.xml   拿到地址->io->dom4J解析
        //获取路径
        List<Element> mapList = rootElement.selectNodes("//mapper");
        for (Element element : mapList) {
            String resource = element.attributeValue("resource");
            InputStream resourceAsStream = Resource.getResourceAsStream(resource);
            XMLMapperBuild xmlMapperBuild = new XMLMapperBuild(configuration);
            xmlMapperBuild.parse(resourceAsStream);
        }
        return configuration;
    }
}
