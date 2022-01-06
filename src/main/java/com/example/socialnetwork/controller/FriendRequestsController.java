package com.example.socialnetwork.controller;

import com.example.socialnetwork.dto.RequestDTO;
import com.example.socialnetwork.service.FriendshipService;
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

public class FriendRequestsController implements Observer<RequestChangedEvent> {
    public ListView<RequestDTO> friendRequestsListView;
    public Button backToFindFriendsButton;
    private ObservableList<RequestDTO> friendRequestsObservableList = FXCollections.observableArrayList();
    public Pane friendRequestsPane;
    private Page page;
    private FriendshipService friendshipService;

    public void initialize() {
        friendRequestsListView.setItems(friendRequestsObservableList);
        friendRequestsListView.setCellFactory(param -> new FriendRequestsCellView(page));
    }

    public void populate() {
        List<RequestDTO> friendRequests = StreamSupport.stream(page.getAllPendingForReceiverDTO().spliterator(), false).toList();
        friendRequestsObservableList.setAll(friendRequests);
    }

    public void setService(Page page, FriendshipService friendshipService) {
        this.page = page;
        this.friendshipService = friendshipService;
        page.getServiceFriendRequest().addObserver(this);
        populate();

    }

    @Override
    public void update(RequestChangedEvent requestChangeEvent) {
        friendRequestsListView.getItems().clear();
        friendRequestsListView.refresh();
        populate();
    }

    public void onBackToFindFriendsButton(ActionEvent actionEvent) {
        friendRequestsPane.getParent().toBack();
    }
}
