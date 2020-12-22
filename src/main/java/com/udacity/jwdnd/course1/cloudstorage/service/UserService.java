package com.udacity.jwdnd.course1.cloudstorage.service;

import com.udacity.jwdnd.course1.cloudstorage.entity.User;
import com.udacity.jwdnd.course1.cloudstorage.mapper.IUserMapper;
import org.springframework.stereotype.Service;

import java.security.SecureRandom;
import java.util.Base64;

@Service
public class UserService {

    private HashService hashService;
    private IUserMapper userMapper;

    public UserService(HashService hashService, IUserMapper userMapper) {
        this.hashService = hashService;
        this.userMapper = userMapper;
    }

    public User getUser(String username) {
        return userMapper.getUser(username);
    }

    public boolean isUsernameAvailable(String username) {
        if (getUser(username) == null)
            return true;
        return false;
    }

    public int createUser(User user) {
        SecureRandom random = new SecureRandom();
        byte[] salt = new byte[16];
        random.nextBytes(salt);
        String encodedSalt = Base64.getEncoder().encodeToString(salt);
        String hashedPassword = hashService.getHashedValue(user.getPassword(), encodedSalt);
        return userMapper.insertUser(new User(null, user.getUsername(), encodedSalt, hashedPassword, user.getFirstName(), user.getLastName()));
    }
}
