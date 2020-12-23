package com.udacity.jwdnd.course1.cloudstorage.config;

import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.multipart.MaxUploadSizeExceededException;
import org.springframework.web.multipart.MultipartException;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@ControllerAdvice
public class ExceptionControllerAdvice {

    @ExceptionHandler(MaxUploadSizeExceededException.class)
    public ModelAndView handleMultipartException(MultipartException ex, RedirectAttributes redirectAttrs) {

        ModelAndView modelAndView = new ModelAndView("forward:/home"); // forward does a POST, see PostMapping in HomeController
        if (ex instanceof MaxUploadSizeExceededException)
            modelAndView.getModel().put("message", "File size exceeded, 10MB Max please.");

        return modelAndView;
    }

}
