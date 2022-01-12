package com.example.socialnetwork.repository;

import com.example.socialnetwork.domain.Message;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

public class MessageDatabaseRepository implements Repository<Long, Message>{
    private String url;
    private String username;
    private String password;

    public MessageDatabaseRepository(String url, String username, String password) {
        this.url = url;
        this.username = username;
        this.password = password;
    }

    @Override
    public Message findOne(Long id) {
        String sql = "select * from messages where message_id = ?";
        try (Connection connection = DriverManager.getConnection(url, username, password);
             PreparedStatement ps = connection.prepareStatement(sql)) {

            ps.setLong(1, id);
            ResultSet resultSet = ps.executeQuery();
            if (!resultSet.isBeforeFirst() ) {
                return null;
            }
            resultSet.next();
            long userSenderId = resultSet.getLong("sender_id");
            long chatId = resultSet.getLong("chat_id");
            String text = resultSet.getString("text");
            LocalDateTime time = resultSet.getTimestamp("time").toLocalDateTime();
            long reply = resultSet.getLong("reply");

            Message message= new Message(userSenderId, chatId, text, time);
            if(reply != 0)
                message.setReply(reply);
            message.setId(id);

            return message;
        } catch (SQLException exception) {
            System.out.println(exception.getMessage());
        }
        return null;
    }

    @Override
    public Iterable<Message> findAll() {
        Set<Message> messages = new HashSet<>();
        try (Connection connection = DriverManager.getConnection(url, username, password);
             PreparedStatement statement = connection.prepareStatement("select * from messages");
             ResultSet resultSet = statement.executeQuery()) {
            while (resultSet.next()) {
                long message_id = resultSet.getLong("message_id");
                long userSenderId = resultSet.getLong("sender_id");
                long chatId = resultSet.getLong("chat_id");
                String text = resultSet.getString("text");
                LocalDateTime time = resultSet.getTimestamp("time").toLocalDateTime();
                long reply = resultSet.getLong("reply");
                Message message= new Message(userSenderId, chatId, text, time);
                if(reply != 0)
                    message.setReply(reply);

                message.setId(message_id);
                messages.add(message);
            }
            return messages;
        } catch (SQLException exception) {
            System.out.println(exception.getMessage());
        }
        return null;
    }

    @Override
    public Message save(Message entity) throws IllegalArgumentException {
        String sql = "insert into messages (sender_id, text, time, reply, chat_id) values ( ?, ?, ?, ?, ?) returning message_id";
        try (Connection connection = DriverManager.getConnection(url, username, password);
             PreparedStatement ps = connection.prepareStatement(sql)) {

            ps.setLong(1, entity.getUserSenderId());
            ps.setString(2, entity.getText());
            ps.setTimestamp(3, Timestamp.valueOf(entity.getTime()));
            if(entity.getReply() != null)
                ps.setLong(4, entity.getReply());
            else
                ps.setNull(4, java.sql.Types.INTEGER);
            ps.setLong(5, entity.getChatId());
            ResultSet resultSet = ps.executeQuery();
            resultSet.next();

            Long id = resultSet.getLong("message_id");

            return null;
        } catch (SQLException exception) {
            System.out.println(exception.getMessage());
        }
        return entity;
    }

    @Override
    public Message delete(Long id) {
        Message entity = findOne(id);
        if(entity == null)
            return null;
        String sql = "delete from messages where message_id = ?";
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
    public Message update(Message entity) {
        String sql = "update messages set text = ?, reply = ? where message_id = ?";
        try (Connection connection = DriverManager.getConnection(url, username, password);
             PreparedStatement ps = connection.prepareStatement(sql)) {

            ps.setString(1, entity.getText());
            ps.setLong(2, entity.getReply());
            ps.setInt(3, entity.getId().intValue());
            int lines = ps.executeUpdate();
            if(lines == 1)
                return null;
        } catch (SQLException exception) {
            System.out.println(exception.getMessage());
        }
        return entity;
    }
}
