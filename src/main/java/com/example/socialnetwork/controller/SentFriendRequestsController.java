package com.example.socialnetwork.controller;

import com.example.socialnetwork.domain.User;
import com.example.socialnetwork.service.Page;
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
    private Page page;

    public void initialize() {
        sentFriendRequestsListView.setItems(sentFriendRequestsObservableList);
        sentFriendRequestsListView.setCellFactory(param -> new SentFriendRequestsCellView(page));
    }

    public void populate() {
        List<User> friendRequests = StreamSupport.stream(page.getAllPendingForSender().spliterator(), false).toList();
        sentFriendRequestsObservableList.setAll(friendRequests);
    }

    public void setService(Page page) {
        this.page = page;
        page.getServiceFriendRequest().addObserver(this);
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
