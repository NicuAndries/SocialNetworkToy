package com.example.socialnetwork.repository;

import com.example.socialnetwork.domain.Friendship;
import com.example.socialnetwork.domain.User;
import com.example.socialnetwork.exceptions.RepositoryException;
import com.example.socialnetwork.exceptions.ValidationException;
import com.example.socialnetwork.utils.Pair;

import java.sql.*;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

public class FriendshipDatabaseRepository implements Repository<Pair<Long, Long>, Friendship> {
    private String url;
    private String username;
    private String password;

    public FriendshipDatabaseRepository(String url, String username, String password) {
        this.url = url;
        this.username = username;
        this.password = password;
    }

    @Override
    public Friendship findOne(Pair<Long, Long> id) {
        String sql = "select u1.user_id as id_1,u1.first_name as first_name_1,u1.last_name as last_name_1,u1.gender as gender_1, u1.birthdate as birthdate_1, u1.profile_image as image1," +
                "u2.user_id as id_2, u2.first_name as first_name_2, u2.last_name as last_name_2,u2.gender as gender_2, u2.birthdate as birthdate_2, u2.profile_image as image2, f.date " +
                "from users as u1 inner join friendships as f on u1.user_id = f.user1_id inner join users as u2 on u2.user_id = f.user2_id " +
                "where user1_id = ? and user2_id = ?";
        try (Connection connection = DriverManager.getConnection(url, username, password);
             PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setLong(1, Long.min(id.getFirst(), id.getSecond()));
            ps.setLong(2, Long.max(id.getFirst(), id.getSecond()));
            ResultSet resultSet = ps.executeQuery();
            if (!resultSet.isBeforeFirst() ) {
                return null;
            }
            resultSet.next();
            Long user1_id = resultSet.getLong("id_1");
            String first_name_1 = resultSet.getString("first_name_1");
            String last_name_1 = resultSet.getString("last_name_1");
            String gender_1 = resultSet.getString("gender_1");
            LocalDate birthdate_1 = resultSet.getDate("birthdate_1").toLocalDate();
            String image = resultSet.getString("image1");

            Long user2_id = resultSet.getLong("id_2");
            String first_name_2 = resultSet.getString("first_name_2");
            String last_name_2 = resultSet.getString("last_name_2");
            String gender_2 = resultSet.getString("gender_2");
            LocalDate birthdate_2 = resultSet.getDate("birthdate_2").toLocalDate();
            String image2 = resultSet.getString("image2");

            LocalDate date = resultSet.getDate("date").toLocalDate();

            User user1 = new User(first_name_1, last_name_1, gender_1, birthdate_1);
            user1.setId(user1_id);
            user1.setProfilePicture(image);
            User user2 = new User(first_name_2, last_name_2, gender_2, birthdate_2);
            user2.setProfilePicture(image2);
            user2.setId(user2_id);

            Friendship friendship = new Friendship(user1, user2, date);

            friendship.setId(new Pair<>(user1_id, user2_id));
            return friendship;
        } catch (SQLException exception) {
            System.out.println(exception.getMessage());
        }
        return null;
    }

    @Override
    public Iterable<Friendship> findAll() {
        Set<Friendship> relations = new HashSet<>();
        String sql = "select u1.user_id as id_1, u1.first_name as first_name_1, u1.last_name as last_name_1, u1.gender as gender_1, u1.birthdate as birthdate_1, u1.profile_image as image1," +
                "u2.user_id as id_2, u2.first_name as first_name_2, u2.last_name as last_name_2, u2.gender as gender_2, u2.birthdate as birthdate_2, u1.profile_image as image2, f.date " +
                "from users as u1 inner join friendships as f on u1.user_id = f.user1_id inner join users as u2 on u2.user_id = f.user2_id";
        try (Connection connection = DriverManager.getConnection(url, username, password);
             PreparedStatement statement = connection.prepareStatement(sql);
             ResultSet resultSet = statement.executeQuery()) {

            while (resultSet.next()) {
                Long user1_id = resultSet.getLong("id_1");
                String first_name_1 = resultSet.getString("first_name_1");
                String last_name_1 = resultSet.getString("last_name_1");
                String gender_1 = resultSet.getString("gender_1");
                LocalDate birthdate_1 = resultSet.getDate("birthdate_1").toLocalDate();
                String image = resultSet.getString("image1");

                Long user2_id = resultSet.getLong("id_2");
                String first_name_2 = resultSet.getString("first_name_2");
                String last_name_2 = resultSet.getString("last_name_2");
                String gender_2 = resultSet.getString("gender_2");
                LocalDate birthdate_2 = resultSet.getDate("birthdate_2").toLocalDate();
                String image2 = resultSet.getString("image2");

                LocalDate date = resultSet.getDate("date").toLocalDate();

                User user1 = new User(first_name_1, last_name_1, gender_1, birthdate_1);
                user1.setId(user1_id);
                user1.setProfilePicture(image);
                User user2 = new User(first_name_2, last_name_2, gender_2, birthdate_2);
                user2.setProfilePicture(image2);
                user2.setId(user2_id);

                Friendship friendship = new Friendship(user1, user2, date);
                friendship.setId(new Pair<>(user1_id, user2_id));
                relations.add(friendship);
            }
            return relations;
        } catch (SQLException exception) {
            System.out.println(exception.getMessage());
        }
        return null;
    }


    @Override
    public Friendship save(Friendship entity) throws IllegalArgumentException, RepositoryException {
        String sql = "insert into friendships (user1_id, user2_id, date) values (?, ?, ?)";
        try (Connection connection = DriverManager.getConnection(url, username, password);
             PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setLong(1, Long.min(entity.getFirstUser().getId(), entity.getSecondUser().getId()));
            ps.setLong(2, Long.max(entity.getFirstUser().getId(), entity.getSecondUser().getId()));
            ps.setDate(3, Date.valueOf(entity.getDate()));
            int lines = ps.executeUpdate();
            if(lines == 1)
                return null;
        } catch (SQLException exception) {
            System.out.println(exception.getMessage());
        }
        return entity;
    }

    @Override
    public Friendship delete(Pair<Long, Long> id) {
        Friendship entity = findOne(id);
        String sql = "delete from friendships where user1_id = ? and user2_id = ?";
        try (Connection connection = DriverManager.getConnection(url, username, password);
             PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setLong(1, Long.min(id.getFirst(), id.getSecond()));
            ps.setLong(2, Long.max(id.getFirst(), id.getSecond()));
            ps.executeUpdate();
            return entity;
        } catch (SQLException exception) {
            System.out.println(exception.getMessage());
        }
        return null;
    }

    @Override
    public Friendship update(Friendship entity) {
        return null;
    }
}
