package com.udacity.jwdnd.course1.cloudstorage.service;

import com.udacity.jwdnd.course1.cloudstorage.entity.Note;
import com.udacity.jwdnd.course1.cloudstorage.entity.User;
import com.udacity.jwdnd.course1.cloudstorage.mapper.INoteMapper;
import com.udacity.jwdnd.course1.cloudstorage.mapper.IUserMapper;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class NoteService {

    public static final String UPDATE_SUCCESS = "Your note has been updated successfully.";
    public static final String UPDATE_FAIL = "Oops. We could not update your note. Try again.";

    public static final String ADD_SUCCESS = "Your note has been added successfully.";
    public static final String ADD_FAIL = "Oops. We could not add your note. Try again.";

    public static final String DELETE_SUCCESS = "Your note has been deleted.";
    public static final String DELETE_FAIL = "Oops. We could not delete your note. Try again.";


    private INoteMapper noteMapper;
    private IUserMapper userMapper;

    public NoteService(INoteMapper noteMapper, IUserMapper userMapper) {
        this.noteMapper = noteMapper;
        this.userMapper = userMapper;
    }

    public Integer addNote(Note note, String username) {
        User user = userMapper.getUser(username);
        note.setUserId(user.getUserId());
        return noteMapper.insertNote(note);    }

    public Integer editNote(Note note, String username) {
        User user = userMapper.getUser(username);
        note.setUserId(user.getUserId());
        return noteMapper.updateNote(note);
    }

    public Integer deleteNote(Integer noteId, String username) {
        User user = userMapper.getUser(username);
        return noteMapper.deleteNote(noteId, user.getUserId());
    }

    public List<Note> getAllNotes(String username) {
        User user = userMapper.getUser(username);
        return noteMapper.getAllNotes(user.getUserId());
    }

}
