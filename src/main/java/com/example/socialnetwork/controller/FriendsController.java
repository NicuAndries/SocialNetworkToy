package com.example.socialnetwork.controller;

import com.example.socialnetwork.domain.Friend;
import com.example.socialnetwork.exceptions.ServiceException;
import com.example.socialnetwork.service.FriendshipService;
import com.example.socialnetwork.service.Page;
import com.example.socialnetwork.utils.events.FriendshipChangedEvent;
import com.example.socialnetwork.utils.observer.Observer;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.ListView;
import javafx.scene.layout.Pane;

import java.util.List;

public class FriendsController implements Observer<FriendshipChangedEvent> {
    public ListView<Friend> friendListView;
    private ObservableList<Friend> friendsObservableList = FXCollections.observableArrayList();
    public Pane friendsPane;
    private Page page;
    private FriendshipService friendshipService;

    @FXML
    public void initialize() {
        friendListView.setItems(friendsObservableList);
        friendListView.setCellFactory(param -> new FriendCellView(page));
    }

    public void populate() {
        try {
            List<Friend> friends = page.getListOfFriends();
            friendsObservableList.setAll(friends);
        } catch (ServiceException exception) {
            System.out.println(exception.getMessage());
        }
    }

    public void setService(Page page, FriendshipService friendshipService) {
        this.page = page;
        this.friendshipService = friendshipService;
        friendshipService.addObserver(this);
        populate();

    }

    @Override
    public void update(FriendshipChangedEvent friendshipChangedEvent) {
        friendListView.getItems().clear();
        friendListView.refresh();
        populate();
    }

}
