package com.lagou.sqlSession;


import com.lagou.pojo.Configuration;

import javax.sql.DataSource;

public class DefaultSqlSessionFactory implements SqlSessionFactory{

    private Configuration configuration;

    public DefaultSqlSessionFactory(Configuration configuration) {
        this.configuration = configuration;
    }

    @Override
    public SqlSession sqlSession() {
        return new DefaultSqlSession(configuration);
    }
}
