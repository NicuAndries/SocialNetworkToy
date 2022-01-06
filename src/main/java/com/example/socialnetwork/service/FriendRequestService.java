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
    private Repository<Pair<Long, Long>, FriendRequest> friendRequests;
    private List<Observer<RequestChangedEvent>> observers=new ArrayList<>();

    public FriendRequestService(Repository<Pair<Long, Long>, FriendRequest> friendRequests) {
        this.friendRequests = friendRequests;
    }

    public void sendFriendRequest(Long id_sender, Long id_receiver) throws ValidationException, ServiceException, RepositoryException {
        FriendRequest friendRequest = friendRequests.findOne(new Pair<>(id_receiver, id_sender));
        if(friendRequest != null) {
            String status = friendRequest.getStatus();
            switch (status) {
                case "pending" -> throw new ServiceException("There is already a friend request from " + id_receiver + " to " + id_sender);
                case "approved" -> throw new ServiceException("The users are already friends");
                case "rejected" -> friendRequests.delete(new Pair<>(id_receiver, id_sender));
            }
        }

        friendRequest = friendRequests.save(new FriendRequest(id_sender, id_receiver));

        if(friendRequest != null) {
            friendRequest = friendRequests.findOne(new Pair<>(id_sender, id_receiver));
            if(friendRequest.getStatus().equals("rejected")) {
                FriendRequest newFriendRequest = new FriendRequest(id_sender, id_receiver, "pending", LocalDate.now());
                friendRequests.update(newFriendRequest);
                notifyObservers(new RequestChangedEvent(ChangeEventType.UPDATE, newFriendRequest, friendRequest));
            }
            else
                throw new ServiceException("The request already exists");
        }
        notifyObservers(new RequestChangedEvent(ChangeEventType.ADD, null));
    }

    public void deleteFriendRequest(Long id_sender, Long id_receiver) throws ServiceException {
        FriendRequest friendRequest = friendRequests.delete(new Pair<>(id_sender, id_receiver));
        if(friendRequest == null)
            throw new ServiceException("The friend request does not exist");
        else
            notifyObservers(new RequestChangedEvent(ChangeEventType.DELETE, friendRequest));
    }

    public void updateFriendRequest(Long id_sender, Long id_receiver, String newStatus) throws ValidationException, ServiceException {
        FriendRequest friendRequest = friendRequests.update(new FriendRequest(id_sender, id_receiver, newStatus));
        if (friendRequest != null)
            throw new ServiceException("The request does not exist");
        else
            notifyObservers(new RequestChangedEvent(ChangeEventType.UPDATE, friendRequest, friendRequest));
    }

    public Iterable<FriendRequest> findAll() {
        return friendRequests.findAll();
    }

    public FriendRequest findOne(Long idSender, Long idReceiver){
        return friendRequests.findOne(new Pair<>(idSender, idReceiver));
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
