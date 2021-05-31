package com.lagou.sqlSession;

import com.lagou.pojo.Configuration;

import java.sql.SQLException;
import java.util.List;
import java.util.Objects;

public interface SqlSession {
    /**
     * 查找全部集合
     * @param <E>
     * @return
     */
    <T> T selectOne(String mappedStatementId, Object ... params) throws  Exception;


    /**
     * 查找全部集合
     * @param <E>
     * @return
     */
     <E> List<E> selectAll(String mappedStatementId,Object...params) throws  Exception;


     <T> T selectByOne(String mappedStatementId, Object... params) throws Exception;

    public <T> T getMapper(Class<?> mapperClass);
}
