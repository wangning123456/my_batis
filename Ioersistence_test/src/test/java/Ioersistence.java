import com.lagou.dao.IUserDao;
import com.lagou.io.Resource;

import com.lagou.pojo.User;
import com.lagou.sqlSession.SqlSession;
import com.lagou.sqlSession.SqlSessionFactory;
import com.lagou.sqlSession.SqlSessionFactoryBuild;



import org.junit.Test;


import java.beans.PropertyVetoException;
import java.io.InputStream;
import java.util.List;

public class Ioersistence {

    @Test
    public void test() throws PropertyVetoException, Exception {
        InputStream resourceAsStream = Resource.getResourceAsStream("sqlMapConfig.xml");
        SqlSessionFactory build = new SqlSessionFactoryBuild().build(resourceAsStream);
        SqlSession sqlSession = build.sqlSession();
        User user = new User();
        user.setId(1);
        user.setUsername("lucy");

        IUserDao userDao = sqlSession.getMapper(IUserDao.class);
        List<User> all = userDao.findAll();
        System.out.println(all);

        User one = userDao.findOne(user);
        System.out.println(one);

    }
}
