package com.example.socialnetwork.controller;

import com.example.socialnetwork.domain.Friend;
import com.example.socialnetwork.exceptions.ServiceException;
import com.example.socialnetwork.service.FriendshipService;
import com.example.socialnetwork.service.Page;
import com.example.socialnetwork.utils.events.FriendshipChangedEvent;
import com.example.socialnetwork.utils.observer.Observer;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.fxml.FXML;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.layout.Pane;

import java.util.List;

public class FriendsController implements Observer<FriendshipChangedEvent> {
    public ListView<Friend> friendListView;
    public TextField searchTextField;
    private ObservableList<Friend> friendsObservableList = FXCollections.observableArrayList();
    private FilteredList<Friend> filteredList;
    public Pane friendsPane;
    private Page page;
    private FriendshipService friendshipService;

    @FXML
    public void initialize() {
        filteredList = new FilteredList<>(friendsObservableList);
        searchTextField.textProperty().addListener(object -> handleFilter());
        friendListView.setItems(filteredList);
        friendListView.setCellFactory(param -> new FriendCellView(page));
    }

    private void handleFilter() {
        String filter = searchTextField.getText();
        if(filter == null || filter.length() == 0) {
            filteredList.setPredicate(s -> true);
        }
        else {
            filteredList.setPredicate(s -> s.getFirstName().contains(filter));
        }
        friendListView.setCellFactory(userListView -> new FriendCellView(page));
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
        populate();
    }

}
