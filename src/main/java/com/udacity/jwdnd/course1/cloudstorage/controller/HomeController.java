package com.udacity.jwdnd.course1.cloudstorage.controller;

import com.udacity.jwdnd.course1.cloudstorage.service.CredentialService;
import com.udacity.jwdnd.course1.cloudstorage.service.EncryptionService;
import com.udacity.jwdnd.course1.cloudstorage.service.FileService;
import com.udacity.jwdnd.course1.cloudstorage.service.NoteService;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Optional;

@Controller
public class HomeController {

    protected FileService fileService;
    protected NoteService noteService;
    protected CredentialService credentialService;
    protected EncryptionService encryptionService;

    public HomeController(FileService fileService, NoteService noteService, CredentialService credentialService, EncryptionService encryptionService) {
        this.fileService = fileService;
        this.noteService = noteService;
        this.credentialService = credentialService;
        this.encryptionService = encryptionService;
    }

    @GetMapping("/home")
    public String homeView(Authentication authentication, Model model, @RequestParam("activeTab") Optional<String> activeTab) {

        String username = authentication.getName();
        model.addAttribute("fileDetails", fileService.getAllFileDetails(username));
        model.addAttribute("notes", noteService.getAllNotes(username));
        model.addAttribute("creds", credentialService.getAllCredentials(username));

        model.addAttribute("encryptionService", encryptionService);

        if (activeTab!=null && !activeTab.isEmpty()) {
            // Detect if we have a query param passed if coming from result.html
            //
            if (activeTab.get().equals("files"))
                model.addAttribute("activeTab", "files");
            else if (activeTab.get().equals("notes"))
                model.addAttribute("activeTab", "notes");
            else if (activeTab.get().equals("creds"))
                model.addAttribute("activeTab", "creds");

        } else {
            // Otherwise, if we're coming from a redirect from our controller
            //
            if (model.getAttribute("activeTab") == null)
                model.addAttribute("activeTab", "files");
        }

        return "home";
    }

    @PostMapping("/home")
    public String postHomeView(Authentication authentication, Model model) {
        return homeView(authentication, model, null);
    }
}
