package com.example.socialnetwork.repository;

import com.example.socialnetwork.domain.Account;
import com.example.socialnetwork.exceptions.ValidationException;
import java.sql.*;

public class AccountDatabaseRepository implements Repository<String, Account>{
    private String url;
    private String username;
    private String password;

    public AccountDatabaseRepository(String url, String username, String password) {
        this.url = url;
        this.username = username;
        this.password = password;
    }

    @Override
    public Account findOne(String inputUsername) {
        String sql = "select * from accounts where username = ?";
        try (Connection connection = DriverManager.getConnection(url, username, password);
             PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setString(1, inputUsername);
            ResultSet resultSet = ps.executeQuery();
            if (!resultSet.isBeforeFirst() ) {
                return null;
            }
            resultSet.next();
            String username = resultSet.getString("username");
            String password = resultSet.getString("password");
            Long userId = resultSet.getLong("user_id");
            Account account = new Account(username, password, userId);
            account.setId(username);
            return account;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public Iterable<Account> findAll() {
        return null;
    }

    @Override
    public Account save(Account entity) throws ValidationException, IllegalArgumentException {
        String sql = "insert into accounts (username, password, user_id) values (?, ?, ?)";
        try (Connection connection = DriverManager.getConnection(url, username, password);
             PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setString(1, entity.getUsername());
            ps.setString(2, entity.getPassword());
            ps.setLong(3, entity.getUserId());
            int lines = ps.executeUpdate();
            if(lines == 1)
                return null;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return entity;
    }

    @Override
    public Account delete(String s) {
        return null;
    }

    @Override
    public Account update(Account entity) throws ValidationException {
        String sql = "update accounts set password = ? where username = ?";
        try (Connection connection = DriverManager.getConnection(url, username, password);
             PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setString(1, entity.getPassword());
            ps.setString(2, entity.getUsername());
            int lines = ps.executeUpdate();
            if(lines == 1)
                return null;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return entity;
    }
}
