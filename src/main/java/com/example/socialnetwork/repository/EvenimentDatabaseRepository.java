package com.example.socialnetwork.repository;

import com.example.socialnetwork.domain.Chat;
import com.example.socialnetwork.domain.Eveniment;
import com.example.socialnetwork.utils.events.Event;

import java.sql.*;
import java.sql.Date;
import java.time.LocalDate;
import java.util.*;

public class EvenimentDatabaseRepository implements Repository<Long, Eveniment> {
    private String url;
    private String username;
    private String password;

    public EvenimentDatabaseRepository(String url, String username, String password) {
        this.url = url;
        this.username = username;
        this.password = password;
    }

    @Override
    public Eveniment findOne(Long id) {
        String sql = "select * from events where event_id = ?";
        try (Connection connection = DriverManager.getConnection(url, username, password);
             PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setLong(1, id);
            ResultSet resultSet = ps.executeQuery();
            if (resultSet.next()) {
                String name = resultSet.getString("name");
                LocalDate date = resultSet.getDate("date").toLocalDate();
                String time = resultSet.getString("time");
                String image = resultSet.getString("image");
                Eveniment eveniment = new Eveniment(name, date, time);
                eveniment.setImage(image);
                eveniment.setEventParticipants(getMembers(id));
                eveniment.setId(id);
                return eveniment;
            }
        } catch (SQLException exception) {
            System.out.println(exception.getMessage());
        }
        return null;
    }

    public Map<Long, String> getMembers(Long event_id) {
        String sql = "select * from event_members where event_id = ?";
        try (Connection connection = DriverManager.getConnection(url, username, password);
             PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setLong(1, event_id);
            ResultSet resultSet = ps.executeQuery();
            Map<Long, String> participants = new HashMap<>();
            while (resultSet.next()) {
                long user_id = resultSet.getLong("user_id");
                String notification = resultSet.getString("notification");
                participants.put(user_id, notification);
            }
            return participants;
        } catch (SQLException exception) {
            System.out.println(exception.getMessage());
        }
        return null;
    }

    @Override
    public Iterable<Eveniment> findAll() {
        Set<Eveniment> eveniments = new HashSet<>();
        try (Connection connection = DriverManager.getConnection(url, username, password);
             PreparedStatement statement = connection.prepareStatement("select * from events");
             ResultSet resultSet = statement.executeQuery()) {
            while (resultSet.next()) {
                long id = resultSet.getLong("event_id");
                String name = resultSet.getString("name");
                LocalDate date = resultSet.getDate("date").toLocalDate();
                String time = resultSet.getString("time");
                String image = resultSet.getString("image");
                Map<Long, String> members = getMembers(id);
                Eveniment eveniment = new Eveniment(name, date, time);
                if (!members.isEmpty())
                    eveniment.setEventParticipants(members);
                eveniment.setId(id);
                eveniment.setImage(image);
                eveniments.add(eveniment);
            }
            return eveniments;
        } catch (SQLException exception) {
            System.out.println(exception.getMessage());
        }
        return null;
    }

    @Override
    public Eveniment save(Eveniment entity) throws IllegalArgumentException {
        String sql = "insert into events (name, date, time, image) values (?, ?, ?, ?) returning event_id";
        try (Connection connection = DriverManager.getConnection(url, username, password);
             PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setString(1, entity.getName());
            ps.setDate(2, Date.valueOf(entity.getDate()));
            ps.setString(3, entity.getTime());
            ps.setString(4, entity.getImage());
            ResultSet resultSet = ps.executeQuery();
            resultSet.next();
            Long id = resultSet.getLong("event_id");
            entity.setId(id);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return entity;
    }

    @Override
    public Eveniment delete(Long id) {
        Eveniment entity = findOne(id);
        if(entity == null)
            return null;
        String sql = "delete from events where event_id = ?";
        try (Connection connection = DriverManager.getConnection(url, username, password);
             PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setLong(1, id);
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return entity;
    }

    @Override
    public Eveniment update(Eveniment entity) {
        String sql = "update events set name = ?, date = ?, time = ? where event_id = ?";
        try (Connection connection = DriverManager.getConnection(url, username, password);
             PreparedStatement ps = connection.prepareStatement(sql)) {

            ps.setString(1, entity.getName());
            ps.setDate(2, Date.valueOf(entity.getDate()));
            ps.setString(2, entity.getTime());
            ps.setLong(3, entity.getId());
            int lines = ps.executeUpdate();
            deleteMembers(entity.getId());
            saveMembers(entity.getId(), entity.getEventParticipants());
            if(lines == 1)
                return null;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return entity;
    }

    private void deleteMembers(long id){
        String sql = "delete from event_members where event_id = ?";
        try (Connection connection = DriverManager.getConnection(url, username, password);
             PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setLong(1, id);
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void saveMembers(long id, Map<Long, String> members){
        String sql = "insert into event_members (event_id, user_id, notification) values (?, ?, ?)";
        try (Connection connection = DriverManager.getConnection(url, username, password);
             PreparedStatement ps = connection.prepareStatement(sql)) {
            members.keySet().forEach(key ->
                    {
                        try {
                            ps.setLong(1, id);
                            ps.setLong(2, key);
                            ps.setString(3, members.get(key));
                            ps.executeUpdate();
                        } catch (SQLException e) {
                            e.printStackTrace();
                        }
                    }
            );
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }
}
