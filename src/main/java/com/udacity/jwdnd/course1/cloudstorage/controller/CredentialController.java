package com.udacity.jwdnd.course1.cloudstorage.controller;

import com.udacity.jwdnd.course1.cloudstorage.entity.Credential;
import com.udacity.jwdnd.course1.cloudstorage.service.CredentialService;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
public class CredentialController {

    private CredentialService credentialService;

    public CredentialController(CredentialService credentialService) {
        this.credentialService = credentialService;
    }

    @PostMapping("/addeditcred")
    public String addEditCredential(Authentication authentication, @ModelAttribute Credential credential, Model model) {
        String username = authentication.getName();

        Integer result;
        if (credential.getCredentialId() == null)
            result = credentialService.addCredential(credential, username);
        else
            result = credentialService.updateCredential(credential, username);

        model.addAttribute("success", true);
        if (result!=1)
            model.addAttribute("success", false);

        model.addAttribute("activeTab", "creds");
        return "result";
    }

    @GetMapping("/deletecred/{credId}")
    public String deleteCredential(Authentication authentication, @PathVariable("credId") Integer credentialId, Model model) {
        String username = authentication.getName();

        Integer result = credentialService.deleteCredential(credentialId, username);
        model.addAttribute("success", true);
        if (result!=1)
            model.addAttribute("success", false);

        model.addAttribute("activeTab", "creds");

        return "result";
    }

}
