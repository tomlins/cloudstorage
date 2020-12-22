package com.udacity.jwdnd.course1.cloudstorage.mapper;

import com.udacity.jwdnd.course1.cloudstorage.entity.User;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.Select;

@Mapper
public interface IUserMapper {
    @Select("SELECT * FROM USERS WHERE username = #{username}")
    User getUser(String username);

    @Insert("INSERT INTO USERS (username, password, firstname, lastname, salt) VALUES(#{username}, #{password}, #{firstName}, #{lastName}, #{salt})")
    @Options(useGeneratedKeys = true, keyProperty = "userId")
    Integer insertUser(User user);
}