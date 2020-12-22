package com.udacity.jwdnd.course1.cloudstorage.controller;

import com.udacity.jwdnd.course1.cloudstorage.entity.Note;
import com.udacity.jwdnd.course1.cloudstorage.service.NoteService;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
public class NoteController {

    private static final String ERROR_INSERT = "Could not insert/edit note.";
    private static final String ERROR_DELETE = "Could not delete note.";

    private NoteService noteService;

    public NoteController(NoteService noteService) {
        this.noteService = noteService;
    }

    @PostMapping("/addeditnote")
    public String addNote(Authentication authentication, @ModelAttribute Note note, Model model) {

        String username = authentication.getName();
        Integer result;

        if (note.getNoteId() == null)
            result = noteService.addNote(note, username);
        else
            result = noteService.editNote(note, username);

        model.addAttribute("success", true);
        if (result!=1)
            model.addAttribute("success", false);

        model.addAttribute("activeTab", "notes");

        return "result";
    }

    @GetMapping("/deletenote/{noteId}")
    public String deleteNote(Authentication authentication, @PathVariable("noteId") Integer noteId, Model model) {

        String username = authentication.getName();
        Integer result = noteService.deleteNote(noteId, username);

        model.addAttribute("success", true);
        if (result!=1)
            model.addAttribute("success", false);

        model.addAttribute("activeTab", "notes");

        return "result";
    }

}
