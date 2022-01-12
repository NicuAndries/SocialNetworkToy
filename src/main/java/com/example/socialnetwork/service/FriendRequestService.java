package com.example.socialnetwork.service;

import com.example.socialnetwork.domain.FriendRequest;
import com.example.socialnetwork.exceptions.RepositoryException;
import com.example.socialnetwork.exceptions.ServiceException;
import com.example.socialnetwork.exceptions.ValidationException;
import com.example.socialnetwork.repository.Repository;
import com.example.socialnetwork.utils.Pair;
import com.example.socialnetwork.utils.events.ChangeEventType;
import com.example.socialnetwork.utils.events.RequestChangedEvent;
import com.example.socialnetwork.utils.observer.Observable;
import com.example.socialnetwork.utils.observer.Observer;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class FriendRequestService implements Observable<RequestChangedEvent> {
    private Repository<Pair<Long, Long>, FriendRequest> friendRequestRepository;
    private List<Observer<RequestChangedEvent>> observers=new ArrayList<>();

    public FriendRequestService(Repository<Pair<Long, Long>, FriendRequest> friendRequestRepository) {
        this.friendRequestRepository = friendRequestRepository;
    }

    public void sendFriendRequest(Long senderId, Long receiverId) throws ValidationException, ServiceException, RepositoryException {
        FriendRequest friendRequest = friendRequestRepository.findOne(new Pair<>(receiverId, senderId));
        if(friendRequest != null) {
            String status = friendRequest.getStatus();
            switch (status) {
                case "pending" -> throw new ServiceException("There is already a friend request from " + receiverId + " to " + senderId);
                case "approved" -> throw new ServiceException("The users are already friends!");
                case "rejected" -> friendRequestRepository.delete(new Pair<>(receiverId, senderId));
            }
        }
        friendRequest = friendRequestRepository.save(new FriendRequest(senderId, receiverId));
        if(friendRequest != null) {
            friendRequest = friendRequestRepository.findOne(new Pair<>(senderId, receiverId));
            if(friendRequest.getStatus().equals("rejected")) {
                FriendRequest newFriendRequest = new FriendRequest(senderId, receiverId, "pending", LocalDate.now());
                friendRequestRepository.update(newFriendRequest);
                notifyObservers(new RequestChangedEvent(ChangeEventType.UPDATE, newFriendRequest, friendRequest));
            }
            else
                throw new ServiceException("The request already exists!");
        }
        notifyObservers(new RequestChangedEvent(ChangeEventType.ADD, null));
    }

    public void deleteFriendRequest(Long senderId, Long receiverId) throws ServiceException {
        FriendRequest friendRequest = friendRequestRepository.delete(new Pair<>(senderId, receiverId));
        if(friendRequest == null)
            throw new ServiceException("Friend request does not exist!");
        else
            notifyObservers(new RequestChangedEvent(ChangeEventType.DELETE, friendRequest));
    }

    public void updateFriendRequest(Long senderId, Long receiverId, String newStatus) throws ValidationException, ServiceException {
        FriendRequest friendRequest = friendRequestRepository.update(new FriendRequest(senderId, receiverId, newStatus));
        if (friendRequest != null)
            throw new ServiceException("Friend request does not exist!");
        else
            notifyObservers(new RequestChangedEvent(ChangeEventType.UPDATE, friendRequest, friendRequest));
    }

    public Iterable<FriendRequest> findAll() {
        return friendRequestRepository.findAll();
    }

    public FriendRequest findOne(Long senderId, Long receiverId){
        return friendRequestRepository.findOne(new Pair<>(senderId, receiverId));
    }

    @Override
    public void addObserver(Observer<RequestChangedEvent> e) {
        observers.add(e);
    }

    @Override
    public void removeObserver(Observer<RequestChangedEvent> e) {
        observers.remove(e);
    }

    @Override
    public void notifyObservers(RequestChangedEvent t) {
        observers.forEach(x->x.update(t));
    }
}
