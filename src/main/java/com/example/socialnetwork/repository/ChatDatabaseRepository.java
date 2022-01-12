package com.example.socialnetwork.repository;

import com.example.socialnetwork.domain.Chat;

import java.sql.*;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class ChatDatabaseRepository implements Repository<Long, Chat> {
    private String url;
    private String username;
    private String password;

    public ChatDatabaseRepository(String url, String username, String password) {
        this.url = url;
        this.username = username;
        this.password = password;
    }

    @Override
    public Chat findOne(Long id) {
        String sql = "select * from chats where chat_id = ?";
        try (Connection connection = DriverManager.getConnection(url, username, password);
             PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setLong(1, id);
            ResultSet resultSet = ps.executeQuery();
            if (!resultSet.isBeforeFirst() ) {
                return null;
            }
            resultSet.next();
            String name = resultSet.getString("name");
            Chat chat = new Chat(name);
            chat.setMembers(getMembers(id));
            chat.setId(id);
            return chat;
        } catch (SQLException exception) {
            System.out.println(exception.getMessage());
        }
        return null;
    }

    public List<Long> getMembers(Long idGroup) {
        String sql = "select * from chat_members where chat_id = ?";
        try (Connection connection = DriverManager.getConnection(url, username, password);
             PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setLong(1, idGroup);
            ResultSet resultSet = ps.executeQuery();
            List<Long> receivers = new ArrayList<>();
            while (resultSet.next()) {
                long user_id = resultSet.getLong("user_id");
                receivers.add(user_id);
            }
            return receivers;
        } catch (SQLException exception) {
            System.out.println(exception.getMessage());
        }
        return null;
    }

    @Override
    public Iterable<Chat> findAll() {
        Set<Chat> chats = new HashSet<>();
        try (Connection connection = DriverManager.getConnection(url, username, password);
             PreparedStatement statement = connection.prepareStatement("select * from chats");
             ResultSet resultSet = statement.executeQuery()) {
            while (resultSet.next()) {
                long id = resultSet.getLong("chat_id");
                String name = resultSet.getString("name");
                List<Long> members = getMembers(id);
                Chat chat = new Chat(name);
                chat.setMembers(members);
                chat.setId(id);
                chats.add(chat);
            }
            return chats;
        } catch (SQLException exception) {
            System.out.println(exception.getMessage());
        }
        return null;
    }

    @Override
    public Chat save(Chat entity) throws IllegalArgumentException {
        String sql = "insert into chats (name) values (?) returning chat_id";
        try (Connection connection = DriverManager.getConnection(url, username, password);
             PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setString(1, entity.getName());
            ResultSet resultSet = ps.executeQuery();
            resultSet.next();
            Long id = resultSet.getLong("chat_id");
            saveMembers(id, entity.getMembers());
            entity.setId(id);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return entity;
    }

    @Override
    public Chat delete(Long id) {
        Chat entity = findOne(id);
        if(entity == null)
            return null;
        String sql = "delete from chats where chat_id = ?";
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
    public Chat update(Chat entity) {
        String sql = "update chats set name = ? where chat_id = ?";
        try (Connection connection = DriverManager.getConnection(url, username, password);
             PreparedStatement ps = connection.prepareStatement(sql)) {

            ps.setString(1, entity.getName());
            ps.setLong(2, entity.getId());
            int lines = ps.executeUpdate();
            deleteMembers(entity.getId());
            saveMembers(entity.getId(), entity.getMembers());
            if(lines == 1)
                return null;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return entity;
    }

    private void deleteMembers(long id){
        String sql = "delete from chat_members where chat_id = ?";
        try (Connection connection = DriverManager.getConnection(url, username, password);
             PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setLong(1, id);
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void saveMembers(long id, List<Long> members){
        String sql = "insert into chat_members (chat_id, user_id) values (?, ?)";
        try (Connection connection = DriverManager.getConnection(url, username, password);
             PreparedStatement ps = connection.prepareStatement(sql)) {
            members.forEach(member ->
                    {
                        try {
                            ps.setLong(1, id);
                            ps.setLong(2, member);
                            ps.executeUpdate();
                        } catch (SQLException exception) {
                            System.out.println(exception.getMessage());
                        }
                    }
            );
        } catch (SQLException exception) {
            System.out.println(exception.getMessage());
        }
    }
}
