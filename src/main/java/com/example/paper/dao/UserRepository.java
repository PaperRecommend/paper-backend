package com.example.paper.dao;

import com.example.paper.entity.po.UserPO;
import org.springframework.stereotype.Repository;
import org.apache.ibatis.annotations.*;

@Repository
public interface UserRepository{
    @Select("select * from user where name=#{name};")
    UserPO getByName(@Param("name") String name);

    @Select("select count(*) from user;")
    long count();

    @Insert("insert into user (name,password) values (#{user.name},#{user.password});")
    void save(@Param("user")UserPO userPO);

    @Select("select * from user where id=#{id};")
    UserPO getById(@Param("id") int id);
}
