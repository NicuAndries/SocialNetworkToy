package com.example.socialnetwork.repository;

import com.example.socialnetwork.domain.Friend;
import com.example.socialnetwork.domain.User;
import com.example.socialnetwork.exceptions.RepositoryException;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class UserDatabaseRepository implements Repository<Long, User>{
    private String url;
    private String username;
    private String password;

    public UserDatabaseRepository(String url, String username, String password) {
        this.url = url;
        this.username = username;
        this.password = password;
    }

    @Override
    public User findOne(Long id) {
        String sql = "select * from users where user_id = ?";
        try (Connection connection = DriverManager.getConnection(url, username, password);
             PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setLong(1, id);
            ResultSet resultSet = ps.executeQuery();
            if (!resultSet.isBeforeFirst() ) {
                return null;
            }
            resultSet.next();
            String firstName = resultSet.getString("first_name");
            String lastName = resultSet.getString("last_name");
            String gender = resultSet.getString("gender");
            LocalDate birthdate = resultSet.getDate("birthdate").toLocalDate();
            String profilePicture = resultSet.getString("profile_image");
            User user = new User(firstName, lastName, gender, birthdate);
            if(profilePicture!=null)
                user.setProfilePicture(profilePicture);
            user.setId(id);
            List<Friend> friends = getFriendsList(user);
            user.setFriendsList(friends);
            return user;
        } catch (SQLException exception) {
            System.out.println(exception.getMessage());
        }
        return null;
    }

    @Override
    public Iterable<User> findAll() {
        Set<User> users = new HashSet<>();
        try (Connection connection = DriverManager.getConnection(url, username, password);
             PreparedStatement statement = connection.prepareStatement("select * from users");
             ResultSet resultSet = statement.executeQuery()) {
            while (resultSet.next()) {
                Long user_id = resultSet.getLong("user_id");
                String firstName = resultSet.getString("first_name");
                String lastName = resultSet.getString("last_name");
                String gender = resultSet.getString("gender");
                LocalDate birthdate = resultSet.getDate("birthdate").toLocalDate();
                String profilePicture = resultSet.getString("profile_image");
                User user = new User(firstName, lastName, gender, birthdate);
                user.setProfilePicture(profilePicture);
                user.setId(user_id);
                users.add(user);
            }
            return users;
        } catch (SQLException exception) {
            System.out.println(exception.getMessage());
        }
        return null;
    }

    private List<Friend> getFriendsList(User user){
        List<Friend> friends = new ArrayList<>();
        String sql = "SELECT u.user_id, u.first_name, u.last_name, u.gender, u.birthdate, u.profile_image, f.date from users as u " +
                "inner join friendships as f on u.user_id = f.user2_id where f.user1_id = ? union " +
                "SELECT u.user_id, u.first_name, u.last_name, u.gender, u.birthdate, u.profile_image, f.date from users as u " +
                "inner join friendships as f on u.user_id = f.user1_id where f.user2_id = ?";
        try (Connection connection = DriverManager.getConnection(url, username, password);
             PreparedStatement statement = connection.prepareStatement(sql)){
            statement.setLong(1, user.getId());
            statement.setLong(2, user.getId());
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                Long user_id = resultSet.getLong("user_id");
                String firstName = resultSet.getString("first_name");
                String lastName = resultSet.getString("last_name");
                String gender = resultSet.getString("gender");
                LocalDate birthdate = resultSet.getDate("birthdate").toLocalDate();
                String profilePicture = resultSet.getString("profile_image");

                LocalDate date = resultSet.getDate("date").toLocalDate();
                User friendUser = new User(firstName, lastName, gender, birthdate);
                friendUser.setProfilePicture(profilePicture);
                friendUser.setId(user_id);
                Friend friend = new Friend(friendUser, date);
                friends.add(friend);
            }
            return friends;
        } catch (SQLException exception) {
            System.out.println(exception.getMessage());
        }
        return null;
    }


    @Override
    public User save(User entity) throws RepositoryException {
        String sql = "insert into users (first_name, last_name, gender, birthdate, profile_image) values (?, ?, ?, ?, ?)";
        try (Connection connection = DriverManager.getConnection(url, username, password);
             PreparedStatement ps = connection.prepareStatement(sql)) {

            ps.setString(1, entity.getFirstName());
            ps.setString(2, entity.getLastName());
            ps.setString(3, entity.getGender());
            ps.setDate(4, Date.valueOf(entity.getBirthdate()));
            ps.setString(5, entity.getProfilePicture());
            int lines = ps.executeUpdate();
            if(lines == 1)
                return null;
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return entity;
    }


    @Override
    public User delete(Long id) {
        User entity = findOne(id);
        if(entity == null)
            return null;
        String sql = "delete from users where user_id = ?";
        try (Connection connection = DriverManager.getConnection(url, username, password);
             PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setLong(1, id);
            ps.executeUpdate();
        } catch (SQLException exception) {
            System.out.println(exception.getMessage());
        }
        return entity;
    }


    @Override
    public User update(User entity) {
        String sql = "update users set first_name = ?, last_name= ?, gender = ?, birthdate = ?, profile_image = ? where id = ?";
        try (Connection connection = DriverManager.getConnection(url, username, password);
             PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setString(1, entity.getFirstName());
            ps.setString(2, entity.getLastName());
            ps.setString(3, entity.getGender().toString());
            ps.setDate(5, Date.valueOf(entity.getBirthdate()));
            if(entity.getProfilePicture()!=null)
                ps.setString(6, entity.getProfilePicture());
            else
                ps.setNull(6, Types.VARCHAR);
            ps.setInt(7, entity.getId().intValue());
            int lines = ps.executeUpdate();
            if(lines == 1)
                return null;
        } catch (SQLException exception) {
            System.out.println(exception.getMessage());
        }
        return entity;

    }
}
