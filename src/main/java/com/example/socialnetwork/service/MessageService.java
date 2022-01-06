package com.example.socialnetwork.service;

import com.example.socialnetwork.domain.Chat;
import com.example.socialnetwork.domain.Message;
import com.example.socialnetwork.exceptions.RepositoryException;
import com.example.socialnetwork.exceptions.ServiceException;
import com.example.socialnetwork.exceptions.ValidationException;
import com.example.socialnetwork.repository.Repository;
import com.example.socialnetwork.utils.events.ChangeEventType;
import com.example.socialnetwork.utils.events.ChatChangedEvent;
import com.example.socialnetwork.utils.events.FriendshipChangedEvent;
import com.example.socialnetwork.utils.events.MessageChangedEvent;
import com.example.socialnetwork.utils.observer.Observable;
import com.example.socialnetwork.utils.observer.Observer;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

public class MessageService implements Observable<MessageChangedEvent> {
    Repository<Long, Message> messages;
    private List<Observer<MessageChangedEvent>> observers = new ArrayList<>();

    public MessageService(Repository<Long, Message> messages) {
        this.messages = messages;
    }

    public void send(long idSender, long idGroup, String text, Long reply) throws ValidationException, RepositoryException {
        Message message = new Message(idSender, idGroup, text);
        message.setReply(reply);
        messages.save(message);
        notifyObservers(new MessageChangedEvent(ChangeEventType.ADD, message));
    }

    public void reply(long replyAt, long idSender, String text) throws ValidationException, ServiceException, RepositoryException {
        Message message = messages.findOne(replyAt);
        if(message == null)
            throw new ServiceException("No message with the given id.");
        Message reply = new Message(idSender, message.getChatId(), text);
        reply.setReply(replyAt);
        messages.save(reply);
        notifyObservers(new MessageChangedEvent(ChangeEventType.ADD, message));
    }

    public Iterable<Message> findAll(){
        return messages.findAll();
    }

    public List<Message> getGroupChat(Long idGroup){
        Iterable<Message> allMessages = messages.findAll();
        return StreamSupport.stream(allMessages.spliterator(), false).
                filter(message -> message.getChatId().equals(idGroup)).
                collect(Collectors.toList());
    }

    public Message findOne(long id) throws ServiceException {
        Message message= messages.findOne(id);
        if(message == null)
            throw new ServiceException("No message with the given id.");
        return message;
    }

    public void delete(Long id) throws IllegalArgumentException, ServiceException{
        Message message = messages.delete(id);
        if(message == null)
            throw new ServiceException("No message with the given id.");
        notifyObservers(new MessageChangedEvent(ChangeEventType.DELETE, message));
    }

    @Override
    public void addObserver(Observer<MessageChangedEvent> e) {
        observers.add(e);
    }

    @Override
    public void removeObserver(Observer<MessageChangedEvent> e) {
        observers.remove(e);
    }

    @Override
    public void notifyObservers(MessageChangedEvent t) {
        observers.forEach(obs->obs.update(t));
    }
}
