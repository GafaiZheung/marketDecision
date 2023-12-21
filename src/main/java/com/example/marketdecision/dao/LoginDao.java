package com.example.marketdecision.dao;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

@Mapper
public interface LoginDao
{
    @Select("select userPassword from user where userID = #{userID}")
    String check(@Param("userID") String userID);
}
