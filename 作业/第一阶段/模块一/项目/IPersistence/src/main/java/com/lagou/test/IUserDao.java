package com.lagou.test;

import java.util.List;

import com.lagou.annotations.Select;

public interface IUserDao {

    //查询所有用户
    public List<User> findAll() throws Exception;

    //根据条件进行用户查询
    public User findByCondition(User user) throws Exception;
    
    int delete(User user);

    int insert(User user);

    int update(User user);
    
    @Select("select * from user where id = #{id} and username = #{username}")
    User findByConditionAnno(User user);
    
    //@Select("select * from user")
    //List<User> findAllAnno();


}
