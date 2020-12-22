package com.udacity.jwdnd.course1.cloudstorage.mapper;

import com.udacity.jwdnd.course1.cloudstorage.entity.File;
import com.udacity.jwdnd.course1.cloudstorage.entity.Note;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface INoteMapper {

    @Insert("INSERT INTO NOTES (notetitle, notedescription, userid) VALUES(#{noteTitle}, #{noteDescription}, #{userId})")
    @Options(useGeneratedKeys = true, keyProperty = "noteId")
    Integer insertNote(Note note);

    @Delete("DELETE FROM NOTES WHERE noteId = #{noteId} AND userId = #{userId}")
    Integer deleteNote(Integer noteId, Integer userId);

    @Update("UPDATE NOTES SET noteTitle=#{noteTitle}, noteDescription=#{noteDescription} WHERE noteId=#{noteId} AND  userId=#{userId}")
    Integer updateNote(Note note);

    @Select("SELECT * FROM NOTES WHERE noteId = #{noteId} AND userId = #{userId}")
    Note getNote(Integer noteId, Integer userId);

    @Select("SELECT * FROM NOTES WHERE userid = #{userId}")
    List<Note> getAllNotes(Integer userId);

}
