package com.example.socialnetwork.controller;

import com.example.socialnetwork.domain.User;
import com.example.socialnetwork.service.Service;
import com.example.socialnetwork.utils.events.RequestChangedEvent;
import com.example.socialnetwork.utils.observer.Observer;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.layout.Pane;

import java.util.List;
import java.util.stream.StreamSupport;

public class SentFriendRequestsController implements Observer<RequestChangedEvent> {
    public ListView<User> sentFriendRequestsListView;
    public Pane sentFriendRequestsPane;
    public Button backToFindFriendsButton;
    private ObservableList<User> sentFriendRequestsObservableList = FXCollections.observableArrayList();
    private Service service;

    public void initialize() {
        sentFriendRequestsListView.setItems(sentFriendRequestsObservableList);
        sentFriendRequestsListView.setCellFactory(param -> new SentFriendRequestsCellView(service));
    }

    public void populate() {
        List<User> friendRequests = StreamSupport.stream(service.getAllPendingForSender().spliterator(), false).toList();
        sentFriendRequestsObservableList.setAll(friendRequests);
    }

    public void setService(Service service) {
        this.service = service;
        service.getServiceFriendRequest().addObserver(this);
        populate();
    }

    @Override
    public void update(RequestChangedEvent requestChangeEvent) {
        sentFriendRequestsListView.getItems().clear();
        sentFriendRequestsListView.refresh();
        populate();
    }

    public void onBackToFindFriendsButton(ActionEvent actionEvent) {
        sentFriendRequestsPane.getParent().toBack();
    }
}
