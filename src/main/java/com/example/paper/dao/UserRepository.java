package com.example.paper.dao;

import com.example.paper.entity.po.UserPO;
import org.springframework.stereotype.Repository;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Repository
public interface UserRepository{
    @Select("select * from user where name=#{name};")
    UserPO getByName(@Param("name") String name);

    @Select("select count(*) from user;")
    long count();

    @Insert("insert into user (name,password) values (#{user.name},#{user.password})")
    @Options(useGeneratedKeys = true, keyProperty = "user.id",keyColumn="id")
    int save(@Param("user")UserPO userPO);

    @Select("select * from user where id=#{id};")
    UserPO getById(@Param("id") int id);

    @Select("select user.id from user where 1=1")
    List<Integer> getAllUserId();
}
