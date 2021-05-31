package com.lagou.dao;

import com.lagou.pojo.User;

import java.util.List;


/**
 * dao的实现类中存在重复的代码，整个操作的过程模板重复(创建sqlsession,调用sqlsession方法，关闭 sqlsession)
 * dao的实现类中存在硬编码，调用sqlsession的方法时，参数statement的id硬编码
 */
public interface IUserDao {


    List<User> findAll() throws Exception;

    User findOne(User user) throws Exception;
}
