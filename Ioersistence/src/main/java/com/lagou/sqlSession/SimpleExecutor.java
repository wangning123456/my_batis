package com.lagou.sqlSession;

import com.lagou.pojo.BoundSql;
import com.lagou.pojo.Configuration;
import com.lagou.pojo.MappedStatement;
import com.lagou.utils.GenericTokenParser;
import com.lagou.utils.ParameterMapping;
import com.lagou.utils.ParameterMappingTokenHandler;
import com.lagou.utils.TokenHandler;
import com.mysql.jdbc.StringUtils;

import java.beans.PropertyDescriptor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.sql.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

public class SimpleExecutor implements Executor {

    private Connection connection = null;

    @Override
    public <E> List<E> query(Configuration configuration, MappedStatement mappedStatement, Object... param) throws Exception {
        // 1. 获取数据库配置以及连接池,连接数据库
         connection = configuration.getDataSource().getConnection();


        // xml里面的语句 select * from user where id=#{id} and username=#{username}
        //转换成 select * from user where id=? and username=?

        String sql = mappedStatement.getSqlText();

        //解析XML里的sql,变成可运行的sql 对sql进行处理

        BoundSql boundsql = getBoundSql(sql);



        //获取 // select * from where id = ? and username = ?
        String sqlText = boundsql.getSqlText();
        PreparedStatement preparedStatement = connection.prepareStatement(sqlText);

        //获取参数实体的地址
        String paramterType = mappedStatement.getParamterType();

        String resultType = mappedStatement.getResultType();

        //获取获取参数实体类

        Class<?> paramterClass = getClass(paramterType);


        Class<?> resultClass = getClass(resultType);



        List<Object> objects = new ArrayList<>();

        //处理参数
        List<ParameterMapping> parameterMappings = boundsql.getParameterMappings();
        for (int i = 0; i < parameterMappings.size(); i++) {
            //获取参数值
            ParameterMapping parameterMapping = parameterMappings.get(i);
            //获取#{}里面的值
            String content = parameterMapping.getContent();
            //反射
            Field declaredField = paramterClass.getDeclaredField(content);
            //暴力
            declaredField.setAccessible(true);
            Object o= declaredField.get(param[0]);
            preparedStatement.setObject(i+1,o);
        }


        //运行sql
        ResultSet resultSet = preparedStatement.executeQuery();
        while (resultSet.next()){
            Object resultObject = resultClass.newInstance();
            //获取源数据
            ResultSetMetaData metaData = resultSet.getMetaData();
            for (int i=1;i<=metaData.getColumnCount();i++){

                //字段的属性值，获取列名,从1开始
                String column = metaData.getColumnName(i);
                //字段的value
                Object value = resultSet.getObject(column);
                //使用反射或者内省,根据数据库和实体的对应关系封装
                //内省的一个方法,根据属性跟类生成读写方法
                PropertyDescriptor propertyDescriptor = new PropertyDescriptor(column, resultClass);
                Method writMethod = propertyDescriptor.getWriteMethod();
                writMethod.invoke(resultObject, value);

            }

            objects.add(resultObject);
        }
        //封装返回值





        return (List<E>) objects;
    }

    @Override
    public void close() throws SQLException {
        connection.close();
    }

    private  Class<?> getClass(String paramterType) throws ClassNotFoundException {
        if (!StringUtils.isNullOrEmpty(paramterType)){
            Class<?> aClass = Class.forName(paramterType);
            return aClass;
        }

        return null;

    }

    private BoundSql getBoundSql(String sql) {
        //标记处理类：主要是配合通用标记解析器GenericTokenParser类完成对配置文件等的解 析工 作，其中TokenHandler主要完成处理
        ParameterMappingTokenHandler tokenHandler = new ParameterMappingTokenHandler();
        //GenericTokenParser :通用的标记解析器，完成了代码片段中的占位符的解析，然后再根 据 给定的标记处理器(TokenHandler)来进行表达式的处理 //三个参数：分别为openToken (开始标记)、closeToken (结束标记)、handler (标记处 理器)
        GenericTokenParser genericTokenParser = new GenericTokenParser("#{","}",tokenHandler);
        String parse = genericTokenParser.parse(sql);
        List<ParameterMapping> parameterMappings = tokenHandler.getParameterMappings();
        BoundSql boundSql = new BoundSql(parse, parameterMappings);
        return boundSql;

    }
}
