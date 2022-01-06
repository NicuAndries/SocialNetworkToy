package com.example.socialnetwork.service;

import com.example.socialnetwork.domain.User;
import com.example.socialnetwork.exceptions.RepositoryException;
import com.example.socialnetwork.exceptions.ServiceException;
import com.example.socialnetwork.exceptions.ValidationException;
import com.example.socialnetwork.repository.Repository;
import com.example.socialnetwork.utils.events.ChangeEventType;
import com.example.socialnetwork.utils.events.UserChangedEvent;
import com.example.socialnetwork.utils.observer.Observable;
import com.example.socialnetwork.utils.observer.Observer;

import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.StreamSupport;

public class UserService implements Observable<UserChangedEvent> {
    private Repository<Long, User> users;
    private List<Observer<UserChangedEvent>> observers = new ArrayList<>();

    public UserService(Repository<Long, User> users) {
        this.users = users;
    }

    public void save(String first_name, String last_name, String gender, LocalDate birthdate) throws ValidationException, IllegalArgumentException, RepositoryException, ServiceException, DateTimeParseException {
        User user = new User(first_name, last_name, gender, birthdate);
        User save = users.save(user);
        if(save != null)
            throw new ServiceException("Id already in use!");
        notifyObservers(new UserChangedEvent(ChangeEventType.ADD, user));
    }

    public void delete(Long id) throws IllegalArgumentException, ServiceException{
        User user = users.delete(id);
        if(user == null)
            throw new ServiceException("No user with the given id.");
        notifyObservers(new UserChangedEvent(ChangeEventType.DELETE, user));
    }

    public void update(long id, String first_name, String last_name, String gender, LocalDate birthdate)throws ValidationException, IllegalArgumentException, RepositoryException, ServiceException, DateTimeParseException{
        User user = new User(first_name, last_name, gender, birthdate);
        user.setId(id);
        User update = users.update(user);
        if(update != null)
            throw new ServiceException("No user with the given id.");
        notifyObservers(new UserChangedEvent(ChangeEventType.UPDATE, user, update));
    }

    public Iterable<User> findAll(){
        return users.findAll();
    }

    public User findOne(long id) throws ServiceException {
        User user = users.findOne(id);
        if(user == null)
            throw new ServiceException("No user with the given id.");
        return user;
    }

    public void updateProfilePicture(User user, String path) throws ValidationException {
        user.setProfilePicture(path);
        users.update(user);
    }

    @Override
    public void addObserver(Observer<UserChangedEvent> e) {
        observers.add(e);
    }

    @Override
    public void removeObserver(Observer<UserChangedEvent> e) {
        observers.remove(e);
    }

    @Override
    public void notifyObservers(UserChangedEvent t) {
        observers.stream().forEach(obs->obs.update(t));
    }

}
