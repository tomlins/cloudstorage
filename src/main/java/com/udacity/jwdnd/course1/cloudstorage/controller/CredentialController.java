package com.udacity.jwdnd.course1.cloudstorage.controller;

import com.udacity.jwdnd.course1.cloudstorage.entity.Credential;
import com.udacity.jwdnd.course1.cloudstorage.service.CredentialService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
public class CredentialController {

    private CredentialService credentialService;

    public CredentialController(CredentialService credentialService) {
        this.credentialService = credentialService;
    }

    @GetMapping("/credential/{credentialId}")
    @ResponseBody
    public ResponseEntity<Credential> getCredential(@PathVariable("credentialId") String credentialId) {
        try {
            Integer id = Integer.parseInt(credentialId);
            Credential credential = credentialService.getCredential(id);
            credentialService.decryptPassword(credential);
            return new ResponseEntity<>(credential, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    @PostMapping("/addeditcred")
    public String addEditCredential(Authentication authentication, @ModelAttribute Credential credential, Model model) {
        String username = authentication.getName();

        Integer result;
        if (credential.getCredentialId() == null) {
            result = credentialService.addCredential(credential, username);
            if (result != 1) {
                model.addAttribute("message", CredentialService.ADD_FAIL);
                model.addAttribute("success", false);
            } else {
                model.addAttribute("message", CredentialService.ADD_SUCCESS);
                model.addAttribute("success", true);
            }
        }
        else {
            result = credentialService.updateCredential(credential, username);
            if (result != 1) {
                model.addAttribute("message", CredentialService.UPDATE_FAIL);
                model.addAttribute("success", false);
            } else {
                model.addAttribute("message", CredentialService.UPDATE_SUCCESS);
                model.addAttribute("success", true);
            }
        }

        model.addAttribute("activeTab", "creds");
        return "result";
    }

    @GetMapping("/deletecred/{credId}")
    public String deleteCredential(Authentication authentication, @PathVariable("credId") Integer credentialId, Model model) {
        String username = authentication.getName();
        Integer result = credentialService.deleteCredential(credentialId, username);

        if (result != 1) {
            model.addAttribute("message", CredentialService.DELETE_FAIL);
            model.addAttribute("success", false);
        } else {
            model.addAttribute("message", CredentialService.DELETE_SUCCESS);
            model.addAttribute("success", true);
        }

        model.addAttribute("activeTab", "creds");
        return "result";
    }

}
