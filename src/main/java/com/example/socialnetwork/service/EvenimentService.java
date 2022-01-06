package com.example.socialnetwork.service;

import com.example.socialnetwork.domain.Eveniment;
import com.example.socialnetwork.domain.EvenimentNotification;
import com.example.socialnetwork.exceptions.RepositoryException;
import com.example.socialnetwork.exceptions.ValidationException;
import com.example.socialnetwork.repository.EvenimentNotificationDatabaseRepository;
import com.example.socialnetwork.repository.Repository;
import com.example.socialnetwork.utils.events.ChangeEventType;
import com.example.socialnetwork.utils.events.EvenimentChangedEvent;
import com.example.socialnetwork.utils.events.MessageChangedEvent;
import com.example.socialnetwork.utils.observer.Observable;
import com.example.socialnetwork.utils.observer.Observer;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;
import java.util.stream.StreamSupport;

public class EvenimentService implements Observable<EvenimentChangedEvent> {
    private Repository<Long, Eveniment> evenimentRepository;
    private EvenimentNotificationDatabaseRepository evenimentNotificationDatabaseRepository;
    private List<Observer<EvenimentChangedEvent>> observers = new ArrayList<>();

    public EvenimentService(Repository<Long, Eveniment> evenimentRepository, EvenimentNotificationDatabaseRepository evenimentNotificationDatabaseRepository) {
        this.evenimentRepository = evenimentRepository;
        this.evenimentNotificationDatabaseRepository = evenimentNotificationDatabaseRepository;
    }

    public EvenimentNotification findOneEvenimentNotification(EvenimentNotification evenimentNotification) {
        return evenimentNotificationDatabaseRepository.findOne(evenimentNotification);
    }

    public Eveniment findOneEveniment(Long id) {
        return evenimentRepository.findOne(id);
    }

    public List<Eveniment> findAll() {
        List<Eveniment> evenimentList = StreamSupport.stream(evenimentRepository.findAll().spliterator(), false).toList();
        return evenimentList;
    }

    public List<EvenimentNotification> findAllNotifications(Long user_id) {
        List<EvenimentNotification> evenimentNotificationList = evenimentNotificationDatabaseRepository.findAll();
        return evenimentNotificationList.stream().filter(x -> x.getUser_id().equals(user_id)).toList();
    }

    public void deletePastEvents() {
        evenimentNotificationDatabaseRepository.deletePastEvents();
    }

    public void save(Eveniment eveniment, Long id) {
        try {
            evenimentRepository.save(eveniment);
            notifyObservers(new EvenimentChangedEvent(ChangeEventType.ADD, new EvenimentNotification(eveniment.getId(), id, "", "")));
        } catch (ValidationException | RepositoryException exception) {
            exception.printStackTrace();
        }
    }

    public void saveEventNotification(EvenimentNotification evenimentNotification) {
        evenimentNotificationDatabaseRepository.save(evenimentNotification);
        notifyObservers(new EvenimentChangedEvent(ChangeEventType.ADD, evenimentNotification));
    }

    public void deleteEventNotification(EvenimentNotification evenimentNotification) {
        evenimentNotificationDatabaseRepository.delete(evenimentNotification);
        notifyObservers(new EvenimentChangedEvent(ChangeEventType.DELETE, evenimentNotification));
    }

    public void updateEventNotification(EvenimentNotification evenimentNotification) {
        evenimentNotificationDatabaseRepository.update(evenimentNotification);
        notifyObservers(new EvenimentChangedEvent(ChangeEventType.ADD, evenimentNotification, null));
    }

    @Override
    public void addObserver(Observer<EvenimentChangedEvent> e) {
        observers.add(e);
    }

    @Override
    public void removeObserver(Observer<EvenimentChangedEvent> e) {
        observers.remove(e);
    }

    @Override
    public void notifyObservers(EvenimentChangedEvent t) {
        observers.forEach(e -> e.update(t));
    }
}
