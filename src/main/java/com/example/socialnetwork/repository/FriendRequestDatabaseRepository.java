package com.example.socialnetwork.repository;

import com.example.socialnetwork.domain.FriendRequest;
import com.example.socialnetwork.utils.Pair;

import java.sql.*;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

public class FriendRequestDatabaseRepository implements Repository<Pair<Long, Long>, FriendRequest> {
    private String url;
    private String username;
    private String password;

    public FriendRequestDatabaseRepository(String url, String username, String password) {
        this.url = url;
        this.username = username;
        this.password = password;
    }

    @Override
    public FriendRequest findOne(Pair<Long, Long> pair) {
        String sql = "select * from friend_requests where sender_id = ? and receiver_id = ?";
        try(Connection connection = DriverManager.getConnection(url, username, password);
            PreparedStatement ps = connection.prepareStatement(sql);) {
            ps.setLong(1, pair.getFirst());
            ps.setLong(2, pair.getSecond());
            ResultSet resultSet = ps.executeQuery();
            if (!resultSet.next())
                return null;
            Long sender_id = resultSet.getLong("sender_id");
            Long receiver_id = resultSet.getLong("receiver_id");
            String status = resultSet.getString("status");
            LocalDate date = resultSet.getDate("date").toLocalDate();
            FriendRequest friendship = new FriendRequest(sender_id, receiver_id, status, date);
            friendship.setId(pair);
            return friendship;
        } catch (SQLException exception) {
            System.out.println(exception.getMessage());
        }
        return null;
    }

    @Override
    public Iterable<FriendRequest> findAll() {
        Set<FriendRequest> friendRequests = new HashSet<>();
        String sql = "select * from friend_requests";
        try (Connection connection = DriverManager.getConnection(url, username, password);
             PreparedStatement statement = connection.prepareStatement(sql);
             ResultSet resultSet = statement.executeQuery()){
            while (resultSet.next()){
                Long sender_id = resultSet.getLong("sender_id");
                Long receiver_id = resultSet.getLong("receiver_id");
                String status = resultSet.getString("status");
                LocalDate date = resultSet.getDate("date").toLocalDate();
                FriendRequest friendRequest = new FriendRequest(sender_id, receiver_id, status, date);
                friendRequest.setId(new Pair<>(sender_id, receiver_id));
                friendRequests.add(friendRequest);
            }
        } catch (SQLException exception) {
            System.out.println(exception.getMessage());
        }
        return friendRequests;
    }

    @Override
    public FriendRequest save(FriendRequest entity) throws IllegalArgumentException {
        String sql = "insert into friend_requests (sender_id, receiver_id, status, date) values (?, ?, ?, ?)";
        try (Connection connection = DriverManager.getConnection(url, username, password);
             PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setLong(1, entity.getIdSendingUser());
            ps.setLong(2, entity.getIdReceivingUser());
            ps.setString(3, entity.getStatus());
            ps.setDate(4, Date.valueOf(entity.getDate()));
            try {
                ps.executeUpdate();
            }catch (SQLException exception){
                return entity;
            }
        } catch (SQLException exception) {
            System.out.println(exception.getMessage());
        }
        return null;
    }

    @Override
    public FriendRequest delete(Pair<Long, Long> pair) {
        String sqlDelete = "delete from friend_requests where sender_id = ? and receiver_id = ?";
        String sqlSearch =  "select * from friend_requests where sender_id = ? and receiver_id = ?";
        try(Connection connection = DriverManager.getConnection(url, username, password);
            PreparedStatement psDelete = connection.prepareStatement(sqlDelete);
            PreparedStatement psSearch = connection.prepareStatement(sqlSearch)) {
            psSearch.setLong(1, pair.getFirst());
            psSearch.setLong(2, pair.getSecond());
            ResultSet resultSet = psSearch.executeQuery();
            if (!resultSet.next())
                return null;
            Long sender_id = resultSet.getLong("sender_id");
            Long receiver_id = resultSet.getLong("receiver_id");
            String status = resultSet.getString("status");
            LocalDate date = resultSet.getDate("date").toLocalDate();
            FriendRequest friendRequest = new FriendRequest(sender_id, receiver_id, status, date);
            friendRequest.setId(new Pair<>(sender_id, receiver_id));
            psDelete.setLong(1, pair.getFirst());
            psDelete.setLong(2, pair.getSecond());
            psDelete.executeUpdate();
            return friendRequest;
        } catch (SQLException exception) {
            System.out.println(exception.getMessage());
        }
        return null;
    }

    @Override
    public FriendRequest update(FriendRequest entity) {
        String sql = "update friend_requests set status = ? where sender_id = ? and receiver_id = ?";
        try (Connection connection = DriverManager.getConnection(url, username, password);
             PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setString(1, entity.getStatus().toString());
            ps.setLong(2, entity.getIdSendingUser());
            ps.setLong(3, entity.getIdReceivingUser());
            int lines = ps.executeUpdate();
            if(lines == 1)
                return null;
        } catch (SQLException exception) {
            System.out.println(exception.getMessage());
        }
        return entity;
    }
}
