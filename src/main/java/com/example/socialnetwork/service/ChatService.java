package com.example.socialnetwork.service;

import com.example.socialnetwork.domain.Chat;
import com.example.socialnetwork.exceptions.RepositoryException;
import com.example.socialnetwork.exceptions.ValidationException;
import com.example.socialnetwork.repository.Repository;

import java.util.List;
import java.util.stream.StreamSupport;

public class ChatService{
    private Repository<Long, Chat> chatRepository;

    public ChatService(Repository<Long, Chat> chatRepository) {
        this.chatRepository = chatRepository;
    }

    public List<Chat> findAll(Long user_id) {
        List<Chat> chatList = StreamSupport.stream(chatRepository.findAll().spliterator(), false).toList();
       return chatList.stream().filter(x -> x.getMembers().contains(user_id)).toList();
    }

    public Chat save(Chat chat) {
        try {
            return chatRepository.save(chat);
        } catch (ValidationException | RepositoryException exception) {
            System.out.println(exception.getMessage());
        }
        return null;
    }

    public Chat findOne(Long chatId) {
        return chatRepository.findOne(chatId);
    }

    public Chat delete(Chat chat) {
        return chatRepository.delete(chat.getId());
    }
}
