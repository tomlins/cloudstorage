package com.udacity.jwdnd.course1.cloudstorage.service;

import com.udacity.jwdnd.course1.cloudstorage.entity.Credential;
import com.udacity.jwdnd.course1.cloudstorage.entity.Note;
import com.udacity.jwdnd.course1.cloudstorage.entity.User;
import com.udacity.jwdnd.course1.cloudstorage.mapper.ICredentialMapper;
import com.udacity.jwdnd.course1.cloudstorage.mapper.IUserMapper;
import org.springframework.stereotype.Service;

import java.security.SecureRandom;
import java.util.Base64;
import java.util.List;

@Service
public class CredentialService {

    private ICredentialMapper credentialMapper;
    private IUserMapper userMapper;
    private EncryptionService encryptionService;

    public CredentialService(ICredentialMapper credentialMapper, IUserMapper userMapper, EncryptionService encryptionService) {
        this.credentialMapper = credentialMapper;
        this.userMapper = userMapper;
        this.encryptionService = encryptionService;
    }

    public Integer addCredential(Credential credential, String username) {
        User user = userMapper.getUser(username);
        credential.setUserId(user.getUserId());

        // Generate a random key and encrypt
        SecureRandom random = new SecureRandom();
        byte[] key = new byte[16];
        random.nextBytes(key);
        String encodedKey = Base64.getEncoder().encodeToString(key);
        credential.setKey(encodedKey);

        // encrypt password using key and set in Credential object to replace the users plain text
        String encryptedPassword = encryptionService.encryptValue(credential.getPassword(), encodedKey);
        credential.setPassword(encryptedPassword);

        return credentialMapper.insertCredential(credential);    }

    public Integer updateCredential(Credential credential, String username) {
        User user = userMapper.getUser(username);
        credential.setUserId(user.getUserId());

        // Get key from existing credential
        Credential oldCredential = credentialMapper.getCredential(credential.getCredentialId());
        String encodedKey = oldCredential.getKey();

        // Encode new password with existing key
        String encryptedPassword = encryptionService.encryptValue(credential.getPassword(), encodedKey);
        credential.setPassword(encryptedPassword);

        return credentialMapper.updateCredential(credential);
    }

    public Integer deleteCredential(Integer credentialId, String username) {
        User user = userMapper.getUser(username);
        return credentialMapper.deleteCredential(credentialId, user.getUserId());
    }

    public Credential getCredential(Integer id) {
        Credential credential = credentialMapper.getCredential(id);
        return credential;
    }

    public List<Credential> getAllCredentials(String username) {
        User user = userMapper.getUser(username);
        List<Credential> credentialList = credentialMapper.getAllCredentials(user.getUserId());
        // decrypt credential password for display
        //credentialList.stream().forEach(credential -> decryptPassword(credential));
        return credentialList;
    }

    public void decryptPassword(Credential credential) {
        String encodedKey = credential.getKey();
        String decryptValue = encryptionService.decryptValue(credential.getPassword(), encodedKey);
        credential.setPassword(decryptValue);
    }

}
