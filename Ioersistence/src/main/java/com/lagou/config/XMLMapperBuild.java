package com.lagou.config;

import com.lagou.pojo.Configuration;
import com.lagou.pojo.MappedStatement;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

import javax.xml.parsers.SAXParser;
import java.io.InputStream;
import java.util.List;

/**
 * @author wangning
 */
public class XMLMapperBuild {
    private Configuration configuration;

    public XMLMapperBuild(Configuration configuration) {
        this.configuration = configuration;
    }

    public void parse(InputStream io) throws DocumentException {
        Document read = new SAXReader().read(io);
        Element rootElement = read.getRootElement();
        String namespace = rootElement.attributeValue("namespace");
        //获取全部select标签
        List<Element> list = rootElement.selectNodes("//select");
        for (Element element : list) {
            String id = element.attributeValue("id");
            String resultType = element.attributeValue("resultType");
            String paramterType = element.attributeValue("paramterType");
            //sql语句
            String sqlText = element.getTextTrim();
            MappedStatement mappedStatement = new MappedStatement();
            mappedStatement.setId(id);
            mappedStatement.setParamterType(paramterType);
            mappedStatement.setSqlText(sqlText);
            mappedStatement.setResultType(resultType);
            String key=namespace+"."+id;
            configuration.getMappedStatementMap().put(key,mappedStatement);
        }

    }
}
