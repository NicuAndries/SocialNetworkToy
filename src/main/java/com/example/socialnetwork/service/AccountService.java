package com.example.socialnetwork.service;

import com.example.socialnetwork.domain.Account;
import com.example.socialnetwork.exceptions.RepositoryException;
import com.example.socialnetwork.exceptions.ValidationException;
import com.example.socialnetwork.repository.Repository;
import com.example.socialnetwork.utils.Security;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.security.auth.login.CredentialException;
import java.security.InvalidKeyException;

public class AccountService {
    Repository<String, Account> accountRepository;
    Security security;

    public AccountService(Repository<String, Account> accountRepository) {
        this.accountRepository = accountRepository;
        this.security = new Security();
    }

    public Long authenticate(String username, String password) throws IllegalBlockSizeException, BadPaddingException, InvalidKeyException, CredentialException {
        Account account = accountRepository.findOne(username);
        if (account == null)
            throw new CredentialException("The username or password is incorrect!");
        if (security.checkPassword(password, account.getPassword()))
            return account.getUserId();
        throw new CredentialException("The username or password is incorrect!");
    }

    public void save(String username, String password, Long userId) throws IllegalBlockSizeException, BadPaddingException, InvalidKeyException, ValidationException, RepositoryException {
        String pass = security.encryptPassword(password);
        Account account = new Account(username, pass, userId);
        accountRepository.save(account);
    }

    public void update(String username, String password, Long userId) throws IllegalBlockSizeException, BadPaddingException, InvalidKeyException, ValidationException {
        String pass = security.encryptPassword(password);
        Account account = new Account(username, pass, userId);
        accountRepository.update(account);
    }
}
