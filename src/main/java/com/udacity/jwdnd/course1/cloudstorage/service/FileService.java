package com.udacity.jwdnd.course1.cloudstorage.service;

import com.udacity.jwdnd.course1.cloudstorage.entity.File;
import com.udacity.jwdnd.course1.cloudstorage.entity.User;
import com.udacity.jwdnd.course1.cloudstorage.mapper.IFileMapper;
import com.udacity.jwdnd.course1.cloudstorage.mapper.IUserMapper;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

@Service
public class FileService {

    public static final String ERROR_EXCEPTION = "Oops! Something went wrong, try again.";
    public static final String ERROR_NO_FILE = "Please select a file to upload.";
    public static final String ERROR_DUPLICATE = "A file of that name already exists.";

    private IFileMapper fileMapper;
    private IUserMapper userMapper;

    public FileService(IFileMapper fileMapper, IUserMapper userMapper) {
        this.fileMapper = fileMapper;
        this.userMapper = userMapper;
    }

    public File getFile(Integer fileId, String username) {
        User user = userMapper.getUser(username);
        File file = fileMapper.getFile(fileId, user.getUserId());
        return file;
    }

    public boolean doesFileExist(String fileName, Integer userId) {
        Integer fileId = fileMapper.getFileId(fileName, userId);
        return fileId == null ? false : true;
    }

    public String saveFile(String username, MultipartFile multipartFile) {
        User user = userMapper.getUser(username);

        if (multipartFile.getSize() == 0)
            return ERROR_NO_FILE;
        if (doesFileExist(multipartFile.getOriginalFilename(), user.getUserId()))
            return ERROR_DUPLICATE;

        try {
            InputStream fis = multipartFile.getInputStream();
            String fileName = multipartFile.getOriginalFilename();
            String contentType = multipartFile.getContentType();
            String fileSize = Long.toString(multipartFile.getSize());
            File file = new File();
            file.setFileName(fileName);
            file.setContentType(contentType);
            file.setFileSize(fileSize);;
            file.setUserId(user.getUserId());
            file.setFileData(fis);

            fileMapper.insertFile(file);
        }
        catch (IOException e) {
            e.printStackTrace();
            return ERROR_EXCEPTION;
        }

        return null;
    }

    public boolean deleteFile(Integer fileId, String username) {
        User user = userMapper.getUser(username);
        Integer deletedFiles = fileMapper.deleteFile(fileId, user.getUserId());
        return deletedFiles.intValue() == 1 ? true : false;
    }

    public List<File> getAllFileDetails(String username) {
        User user = userMapper.getUser(username);
        return fileMapper.getAllFileDetails(user.getUserId());
    }


}
