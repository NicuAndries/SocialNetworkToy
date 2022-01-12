package com.example.socialnetwork.service;

import com.example.socialnetwork.domain.Friend;
import com.example.socialnetwork.domain.Friendship;
import com.example.socialnetwork.domain.User;
import com.example.socialnetwork.dto.FriendDTO;
import com.example.socialnetwork.dto.FriendshipDTO;
import com.example.socialnetwork.exceptions.RepositoryException;
import com.example.socialnetwork.exceptions.ServiceException;
import com.example.socialnetwork.exceptions.ValidationException;
import com.example.socialnetwork.repository.Repository;
import com.example.socialnetwork.utils.Pair;
import com.example.socialnetwork.utils.events.ChangeEventType;
import com.example.socialnetwork.utils.events.FriendshipChangedEvent;
import com.example.socialnetwork.utils.observer.Observable;
import com.example.socialnetwork.utils.observer.Observer;

import java.time.LocalDate;
import java.time.Month;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class FriendshipService implements Observable<FriendshipChangedEvent> {
    private Repository<Pair<Long, Long>, Friendship> friendshipRepository;
    private List<Observer<FriendshipChangedEvent>> observers = new ArrayList<>();

    public FriendshipService(Repository<Pair<Long, Long>, Friendship> friendshipRepository) {
        this.friendshipRepository = friendshipRepository;
    }

    public void save(User user1, User user2) throws ValidationException, IllegalArgumentException, ServiceException, RepositoryException {
        Friendship friendship = new Friendship(user1, user2);
        friendship.setId(new Pair<>(user1.getId(), user2.getId()));
        friendshipRepository.save(friendship);
        notifyObservers(new FriendshipChangedEvent(ChangeEventType.ADD, friendship));
    }


    public void delete(Long idUser1, Long idUser2) throws IllegalArgumentException, ServiceException{
        Pair<Long, Long> id = new Pair<>(idUser1, idUser2);
        Friendship friendship = friendshipRepository.delete(id);
        if(friendship == null)
            throw new ServiceException("No frienship was found between the tho given users.");
        notifyObservers(new FriendshipChangedEvent(ChangeEventType.DELETE, friendship));
    }

    public ArrayList<FriendshipDTO> findAll(){
        ArrayList<FriendshipDTO> friends = new ArrayList<>();
        friendshipRepository.findAll().forEach(r -> {
            User user1 = r.getFirstUser();
            User user2 = r.getSecondUser();
            LocalDate date = r.getDate();
            FriendshipDTO relationDTO = new FriendshipDTO(user1, user2, date);
            friends.add(relationDTO);
        });
        return friends;
    }

    public List<Friend> getFriends(User user){
        return user.getFriendsList();
    }

    public List<FriendDTO> getFriendsDTO(User user){
        return user.getFriendsList().stream()
                .map(dto -> new FriendDTO(dto.getId(), dto.getFirstName(), dto.getLastName(), dto.getDate()))
                .collect(Collectors.toList());
    }

    public List<FriendDTO> getUserFriendsDTOByDate(User user, String monthString){
        Month month = Month.valueOf(monthString.toUpperCase());
        return user.getFriendsList().stream()
                .filter(f -> f.getDate().getMonth().equals(month))
                .map(dto -> new FriendDTO(dto.getId(), dto.getFirstName(), dto.getLastName(), dto.getDate()))
                .collect(Collectors.toList());
    }

    public Friendship findOne(Long id1, long id2){
        return friendshipRepository.findOne(new Pair<>(id1, id2));
    }

    @Override
    public void addObserver(Observer<FriendshipChangedEvent> e) {
        observers.add(e);
    }

    @Override
    public void removeObserver(Observer<FriendshipChangedEvent> e) {
        observers.remove(e);
    }

    @Override
    public void notifyObservers(FriendshipChangedEvent t) {
        observers.forEach(obs->obs.update(t));
    }
}
