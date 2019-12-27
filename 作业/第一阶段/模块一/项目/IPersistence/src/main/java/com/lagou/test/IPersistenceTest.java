package com.lagou.test;

import java.beans.PropertyVetoException;
import java.io.InputStream;
import java.util.List;

import org.dom4j.DocumentException;
import org.junit.Before;
import org.junit.Test;

import com.lagou.io.Resources;
import com.lagou.sqlSession.SqlSession;
import com.lagou.sqlSession.SqlSessionFactory;
import com.lagou.sqlSession.SqlSessionFactoryBuilder;

public class IPersistenceTest {
	
	SqlSession sqlSession;
	IUserDao userDao;
	
	@Before
	public void before() throws DocumentException, PropertyVetoException{
		InputStream resourceAsSteam = Resources.getResourceAsSteam("sqlMapConfig.xml");
        SqlSessionFactory sqlSessionFactory = new SqlSessionFactoryBuilder().build(resourceAsSteam);
        sqlSession = sqlSessionFactory.openSession();
        
        userDao = sqlSession.getMapper(IUserDao.class);
	}
	
	/**
	 * 查询
	 * @throws Exception
	 */
    @Test
    public void query() throws Exception {
        //调用
        User user = new User();
        user.setId(1);
        user.setUsername("张三");
        //User user1 = sqlSession.selectOne("com.lagou.test.IUserDao.findByCondition", user);
        List<Object> user1 = sqlSession.selectList("com.lagou.test.IUserDao.findAll");
        //User user1 = userDao.findByCondition(user);
        System.out.println(user1);

       /* List<User> users = sqlSession.selectList("user.selectList");
        for (User user1 : users) {
            System.out.println(user1);
        }*/

       /* 
        List<User> all = userDao.findAll();
        for (User user1 : all) {
            System.out.println(user1);
        }*/
        
    }
    
    /**
     * 添加
     * @throws Exception
     */
    @Test
    public void insert() throws Exception {
        //调用
        User user = new User();
        user.setId(3);
        user.setUsername("添加");
        
        int num = sqlSession.insert("com.lagou.test.IUserDao.insert", user);
        
        //int num = sqlSession.insert("com.lagou.test.IUserDao.insert", 33,"添加1");
        
        System.out.println("添加成功数量："+num);

    }
    
    /**
     * 修改
     * @throws Exception
     */
    @Test
    public void update() throws Exception {
        //调用
        User user = new User();
        user.setId(3);
        user.setUsername("修改");
        
        int num = sqlSession.update("com.lagou.test.IUserDao.update", user);
        
        //int num = sqlSession.update("com.lagou.test.IUserDao.update", 33,"修改1");
        
        System.out.println("修改成功数量："+num);

    }
    
    /**
     * 删除
     * @throws Exception
     */
    @Test
    public void delete() throws Exception {
    	User user = new User();
        user.setId(3);
        //调用
        int num = sqlSession.delete("com.lagou.test.IUserDao.delete", user);
        //调用
        //int num = sqlSession.delete("com.lagou.test.IUserDao.delete", 33);
        
        System.out.println("删除成功数量："+num);

    }
    
    /**
     * 添加--基于接口动态代理
     * @throws Exception
     */
    @Test
    public void insertProxy() throws Exception {
        //调用
        User user = new User();
        user.setId(4);
        user.setUsername("添加Proxy");
        
        int num = userDao.insert(user);
        
        System.out.println("Proxy添加成功数量："+num);

    }
    
    /**
     * 修改--基于接口动态代理
     * @throws Exception
     */
    @Test
    public void updateProxy() throws Exception {
        //调用
        User user = new User();
        user.setId(4);
        user.setUsername("修改Proxy");
        
        int num = userDao.update(user);
        
        System.out.println("Proxy修改成功数量："+num);

    }
    
    /**
     * 删除--基于接口动态代理
     * @throws Exception
     */
    @Test
    public void deleteProxy() throws Exception {
        //调用
    	User user = new User();
        user.setId(4);
        int num = userDao.delete(user);
        
        System.out.println("Proxy删除成功数量："+num);

    }
    
    /**
     * 查询----基于注解
     * @throws Exception
     */
    @Test
    public void queryAnno() throws Exception {
        //调用
    	User user = new User();
        user.setId(1);
        user.setUsername("张三");
        User list = userDao.findByConditionAnno(user);
        
        System.out.println("Annotation查询结果："+list);
        
        //List<User> findAllAnno = userDao.findAllAnno();
        //System.out.println("Annotation查询结果："+findAllAnno);
        

    }


}
