package com.udacity.jwdnd.course1.cloudstorage.mapper;

import com.udacity.jwdnd.course1.cloudstorage.entity.File;
import org.apache.ibatis.annotations.*;

import java.util.ArrayList;
import java.util.List;

@Mapper
public interface IFileMapper {

    @Insert("INSERT INTO FILES (filename, contenttype, filesize, userid, filedata) VALUES(#{fileName}, #{contentType}, #{fileSize}, #{userId}, #{fileData})")
    @Options(useGeneratedKeys = true, keyProperty = "fileId")
    Integer insertFile(File file);

    @Delete("DELETE FROM FILES WHERE fileId = #{fileId} AND userId = #{userId}")
    Integer deleteFile(Integer fileId, Integer userId);

    @Select("SELECT * FROM FILES WHERE fileId = #{fileId} AND userId = #{userId}")
    File getFile(Integer fileId, Integer userId);

    @Select("SELECT fileId, filename FROM FILES WHERE userid = #{userId}")
    List<File> getAllFileDetails(Integer userId);

    @Select("SELECT fileId FROM FILES WHERE filename = #{filename} AND userId = #{userId}")
    Integer getFileId(String filename, Integer userId);

}
