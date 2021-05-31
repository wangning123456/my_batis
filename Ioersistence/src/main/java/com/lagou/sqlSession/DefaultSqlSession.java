package com.lagou.sqlSession;

import com.lagou.pojo.Configuration;
import com.lagou.pojo.MappedStatement;
import com.lagou.pojo.User;
import com.sun.xml.internal.ws.api.model.MEP;

import java.lang.reflect.*;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class DefaultSqlSession implements SqlSession {
    private Configuration configuration;

    public DefaultSqlSession(Configuration configuration) {
        this.configuration = configuration;
    }

    //处理器对象
    /**
     * sql执行方法
     */
    private Executor simpleExcutor = new SimpleExecutor();

    @Override
    public <T> T selectOne(String mappedStatementId, Object... params) throws Exception {
        MappedStatement mappedStatement = configuration.getMappedStatementMap().get(mappedStatementId);
        List<Object> objects = simpleExcutor.query(configuration, mappedStatement, params);
        if (objects.size() == 1) {
            return (T) objects.get(0);
        } else {
            throw new RuntimeException("返回结果过多");
        }
    }

    @Override
    public <E> List<E> selectAll(String mappedStatementId, Object... params) throws Exception {
        //根据key获取对应的sql语句
        MappedStatement mappedStatement = configuration.getMappedStatementMap().get(mappedStatementId);
        List<Object> query = simpleExcutor.query(configuration, mappedStatement, params);
        return (List<E>) query;
    }


    @Override
    public <T> T selectByOne(String mappedStatementId, Object... params) throws Exception {

        return null;
    }

    @Override
    public <T> T getMapper(Class<?> mapperClass) {
        //使用JDK动态代理来为Dao接口生成代理动态代理
        Object o = Proxy.newProxyInstance(SqlSessionFactory.class.getClassLoader(), new Class[]{mapperClass}, new InvocationHandler() {
            //proxy:当前赖利对象的应用
            //method:当前被调用方法的应用
            //args: 传递的参数
            @Override
            public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
              // 准备参数 1.参数statementId:sql 语句的唯一标识,
                // 因为只能获取到 接口全限定名 以及 方法名 所以 mapeer 的 namespace 对应为全限定名  <selectId> 为 方法名


                //获取方法名
                String name = method.getName();
                //全限定名
                String className = method.getDeclaringClass().getName();

                String statementId=className+"."+name;

                //准备参数2  查询参数
                //获取当前被调用返回值类型
                Type genericReturnType = method.getGenericReturnType();
                //判断是否进行了参数类型繁星花
                if (genericReturnType instanceof ParameterizedType){
                    List<User> objects = selectAll(statementId, args);
                    return objects;
                }
                return  selectOne(statementId, args);
            }
        });
        return (T) o;
    }
}
