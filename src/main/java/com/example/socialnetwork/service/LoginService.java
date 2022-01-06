package com.example.socialnetwork.service;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.security.auth.login.CredentialException;
import java.security.InvalidKeyException;

public class LoginService {
    private AccountService accountService;

    public LoginService(AccountService accountService) {
        this.accountService = accountService;
    }

    public Long authenticate(String username, String password) throws CredentialException, IllegalBlockSizeException, BadPaddingException, InvalidKeyException {
        return accountService.authenticate(username, password);
    }
}
