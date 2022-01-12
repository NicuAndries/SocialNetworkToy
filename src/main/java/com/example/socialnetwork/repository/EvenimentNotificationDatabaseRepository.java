package com.example.socialnetwork.repository;

import com.example.socialnetwork.domain.Eveniment;
import com.example.socialnetwork.domain.EvenimentNotification;

import java.sql.*;
import java.time.LocalDate;
import java.util.*;

public class EvenimentNotificationDatabaseRepository {
    private String url;
    private String username;
    private String password;

    public EvenimentNotificationDatabaseRepository(String url, String username, String password) {
        this.url = url;
        this.username = username;
        this.password = password;
    }

    public EvenimentNotification findOne(EvenimentNotification evenimentNotification) {
        String sql = "select * from event_members where (event_id = ? and user_id = ?)";
        try (Connection connection = DriverManager.getConnection(url, username, password);
             PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setLong(1, evenimentNotification.getEveniment_id());
            ps.setLong(2, evenimentNotification.getUser_id());
            ResultSet resultSet = ps.executeQuery();
            if (resultSet.next()) {
                Long event_id = resultSet.getLong("event_id");
                Long user_id = resultSet.getLong("user_id");
                String notification = resultSet.getString("notification");
                String status = resultSet.getString("status");
                EvenimentNotification eveniment = new EvenimentNotification(event_id, user_id, notification, status);
                return eveniment;
            }
        } catch (SQLException exception) {
            System.out.println(exception.getMessage());
        }
        return null;
    }

    public void deletePastEvents() {
        String sql = "delete from events where date < current_date";
        try (Connection connection = DriverManager.getConnection(url, username, password);
             PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.executeUpdate();
        } catch (SQLException exception) {
            System.out.println(exception.getMessage());
        }
    }

    public List<EvenimentNotification> findAll() {
        List<EvenimentNotification> eveniments = new ArrayList<>();
        String sql = "select * from event_members";
        try (Connection connection = DriverManager.getConnection(url, username, password);
             PreparedStatement ps = connection.prepareStatement(sql)) {
            ResultSet resultSet = ps.executeQuery();
            while (resultSet.next()) {
                Long event_id = resultSet.getLong("event_id");
                Long user_id = resultSet.getLong("user_id");
                String notification = resultSet.getString("notification");
                String status = resultSet.getString("status");
                EvenimentNotification eveniment = new EvenimentNotification(event_id, user_id, notification, status);
                eveniments.add(eveniment);
            }
            return eveniments;
        } catch (SQLException exception) {
            System.out.println(exception.getMessage());
        }
        return null;
    }

    public EvenimentNotification save(EvenimentNotification entity) throws IllegalArgumentException {
        String sql = "insert into event_members (event_id, user_id, notification, status) values (?, ?, ?, ?)";
        try (Connection connection = DriverManager.getConnection(url, username, password);
             PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setLong(1, entity.getEveniment_id());
            ps.setLong(2, entity.getUser_id());
            ps.setString(3, entity.getNotification());
            ps.setString(4, entity.getStatus());
            int lines = ps.executeUpdate();
            if(lines == 1)
                return null;
        } catch (SQLException exception) {
            System.out.println(exception.getMessage());
        }
        return entity;
    }

    public EvenimentNotification delete(EvenimentNotification evenimentNotification) {
        String sql = "delete from event_members where event_id = ? and user_id = ?";
        try (Connection connection = DriverManager.getConnection(url, username, password);
             PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setLong(1, evenimentNotification.getEveniment_id());
            ps.setLong(2, evenimentNotification.getUser_id());
            int lines = ps.executeUpdate();
            if(lines == 1)
                return null;
        } catch (SQLException exception) {
            System.out.println(exception.getMessage());
        }
        return evenimentNotification;
    }

    public EvenimentNotification update(EvenimentNotification entity) {
        String sql = "update event_members set event_id = ?, user_id = ?, notification = ?, status = ? where event_id = ?";
        try (Connection connection = DriverManager.getConnection(url, username, password);
             PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setLong(1, entity.getEveniment_id());
            ps.setLong(2, entity.getUser_id());
            ps.setString(3, entity.getNotification());
            ps.setString(4, entity.getStatus());
            ps.setLong(5, entity.getEveniment_id());
            int lines = ps.executeUpdate();
            if(lines == 1)
                return null;
        } catch (SQLException exception) {
            System.out.println(exception.getMessage());
        }
        return entity;
    }
}
