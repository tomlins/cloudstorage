package com.udacity.jwdnd.course1.cloudstorage.config;

import org.apache.ibatis.exceptions.PersistenceException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.multipart.MaxUploadSizeExceededException;
import org.springframework.web.multipart.MultipartException;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.servlet.view.RedirectView;

@ControllerAdvice
public class ExceptionControllerAdvice {

    @ExceptionHandler(MultipartException.class)
    public ModelAndView handleMultipartException(MultipartException ex, RedirectAttributes redirectAttrs) {

        ModelAndView modelAndView = new ModelAndView("forward:/home"); // forward does a POST, see PostMapping in HomeController
        if (ex instanceof MaxUploadSizeExceededException)
            modelAndView.getModel().put("message", "File size exceeded, 10MB Max please.");

        return modelAndView;
    }

//    @ExceptionHandler(PersistenceException.class)
//    public ModelAndView handlePersistenceException(PersistenceException ex, RedirectAttributes redirectAttrs) {
//
//        ModelAndView modelAndView = new ModelAndView("forward:/home");
//        modelAndView.getModel().put("activeTab", "files");
//
//        if (ex instanceof PersistenceException)
//            modelAndView.getModel().put("message", "File size exceeded, 10MB Max please.");
//
//        return modelAndView;
//    }

}
