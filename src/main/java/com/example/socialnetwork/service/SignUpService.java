package com.example.socialnetwork.service;

import com.example.socialnetwork.domain.User;
import com.example.socialnetwork.exceptions.RepositoryException;
import com.example.socialnetwork.exceptions.ServiceException;
import com.example.socialnetwork.exceptions.ValidationException;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import java.security.InvalidKeyException;
import java.time.LocalDate;
import java.util.Comparator;
import java.util.List;
import java.util.stream.StreamSupport;

public class SignUpService {
    AccountService accountService;
    UserService userService;

    public SignUpService(AccountService accountService, UserService userService) {
        this.accountService = accountService;
        this.userService = userService;
    }

    public void signUp(String username, String pass, String first_name, String last_name, String gender, LocalDate birthdate, String image) throws ValidationException, ServiceException, IllegalBlockSizeException, BadPaddingException, InvalidKeyException, RepositoryException {
        userService.save(first_name, last_name, gender, birthdate, image);
        List<User> userList = StreamSupport.stream(userService.findAll().spliterator(), false).toList();
        Long user_id = userList.stream().max(Comparator.comparing(User::getId)).get().getId();
        accountService.save(username, pass, user_id);
    }
}
