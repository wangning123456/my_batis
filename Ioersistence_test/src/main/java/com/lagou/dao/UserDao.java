/*
package dao;

import com.lagou.io.Resource;

import com.lagou.pojo.User;
import com.lagou.sqlSession.SqlSession;
import com.lagou.sqlSession.SqlSessionFactory;
import com.lagou.sqlSession.SqlSessionFactoryBuild;

import java.io.InputStream;
import java.util.List;
//舍弃原因 dao的实现类中存在重复的代码，整个操作的过程模板重复(创建sqlsession,调用sqlsession方法，关闭 sqlsession)
// * dao的实现类中存在硬编码，调用sqlsession的方法时，参数statement的id硬编码
//使用代理模式来创建接口的代理对象

public class UserDao implements IUserDao{
    @Override
    public List<User> selectAll() throws Exception {
        InputStream resourceAsStream = Resource.getResourceAsStream("sqlMapConfig.xml");
        SqlSessionFactory build = new SqlSessionFactoryBuild().build(resourceAsStream);
        SqlSession sqlSession = build.sqlSession();
        User user = new User();
        user.setId(1);
        user.setUsername("lucy");
        List<User> objects = sqlSession.selectAll("user.selectList");
        return objects;
    }

    @Override
    public User selectOne() throws Exception {
        InputStream resourceAsStream = Resource.getResourceAsStream("sqlMapConfig.xml");
        SqlSessionFactory build = new SqlSessionFactoryBuild().build(resourceAsStream);
        SqlSession sqlSession = build.sqlSession();
        User user = new User();
        user.setId(1);
        user.setUsername("lucy");
        User o = sqlSession.selectByOne("user.selectOne", user);
        System.out.println(o);
        return o;
    }
}
*/
