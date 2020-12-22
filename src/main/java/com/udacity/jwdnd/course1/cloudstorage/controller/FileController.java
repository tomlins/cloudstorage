package com.udacity.jwdnd.course1.cloudstorage.controller;

import com.udacity.jwdnd.course1.cloudstorage.entity.File;
import com.udacity.jwdnd.course1.cloudstorage.service.FileService;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.FileInputStream;

@Controller
public class FileController {

    private FileService fileService;

    public FileController(FileService fileService) {
        this.fileService = fileService;
    }

    @PostMapping("/uploadfile")
    public String uploadFile(Authentication authentication, @RequestParam("fileUpload") MultipartFile file, RedirectAttributes redirectAttrs) {
        String username = authentication.getName();
        String outcome = fileService.saveFile(username, file);
        redirectAttrs.addFlashAttribute("message", outcome);
        redirectAttrs.addFlashAttribute("activeTab", "files");
        return "redirect:/home";
    }

    @GetMapping("deletefile/{fileId}")
    public String deleteFile(Authentication authentication, @PathVariable("fileId") Integer fileId, RedirectAttributes redirectAttrs) {
        String username = authentication.getName();
        boolean success = fileService.deleteFile(fileId, username);
        if (!success)
            redirectAttrs.addFlashAttribute("message", "Unable to delete file!");
        redirectAttrs.addFlashAttribute("activeTab", "files");
        return "redirect:/home";
    }

    @GetMapping("downloadfile/{fileId}")
    public ResponseEntity<Resource> viewFile(Authentication authentication, @PathVariable("fileId") Integer fileId) {
        String username = authentication.getName();

        File file = fileService.getFile(fileId, username);
        if (file == null) {
            return null; // No such file for given file name and user ID
        }

        HttpHeaders header = new HttpHeaders();
        header.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + file.getFileName());
        header.add("Cache-Control", "no-cache, no-store, must-revalidate");
        header.add("Pragma", "no-cache");
        header.add("Expires", "0");

        InputStreamResource resource = new InputStreamResource(file.getFileData());

        return ResponseEntity.ok()
                .headers(header)
                .contentLength(Long.parseLong(file.getFileSize()))
                .contentType(MediaType.APPLICATION_OCTET_STREAM)
                .body(resource);
    }


}