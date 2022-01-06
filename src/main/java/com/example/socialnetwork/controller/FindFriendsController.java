package com.example.socialnetwork.controller;

import com.example.socialnetwork.domain.User;
import com.example.socialnetwork.service.FriendshipService;
import com.example.socialnetwork.service.Page;
import com.example.socialnetwork.utils.events.RequestChangedEvent;
import com.example.socialnetwork.utils.observer.Observer;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.layout.Pane;

import java.io.IOException;
import java.util.List;

public class FindFriendsController implements Observer<RequestChangedEvent> {
    public ListView<User> findFriendListView;
    public Pane findFriendsPane;
    public Hyperlink viewReceivedRequests;
    public Hyperlink viewHistoryRequests;
    private ObservableList<User> findfriendsObservableList = FXCollections.observableArrayList();
    public TextField searchFriendsTextField;
    public Hyperlink viewSentRequests;
    public Pane historyRequestsPane;
    public Pane friendRequestsPane;
    public Pane sentFriendRequestsPane;
    private Page page;
    private FriendshipService friendshipService;
    private FilteredList<User> filteredList;
    private HistoryRequestsController historyRequestsController;
    private FriendRequestsController friendRequestsController;
    private SentFriendRequestsController sentFriendRequestsController;


    @FXML
    public void initialize() {
        findFriendListView.setItems(findfriendsObservableList);
        findFriendListView.setCellFactory(userListView -> new FindFriendsCellView(page));
        try{
            FXMLLoader historyLoader = new FXMLLoader(getClass().getClassLoader().getResource("historyRequestsPane.fxml"));
            historyRequestsPane.getChildren().add(historyLoader.load());

            FXMLLoader friendRequests = new FXMLLoader(getClass().getClassLoader().getResource("friendRequestsPane.fxml"));
            friendRequestsPane.getChildren().add(friendRequests.load());

            FXMLLoader sentFriendRequests = new FXMLLoader(getClass().getClassLoader().getResource("sentFriendRequestsPane.fxml"));
            sentFriendRequestsPane.getChildren().add(sentFriendRequests.load());

            historyRequestsController = historyLoader.getController();
            friendRequestsController = friendRequests.getController();
            sentFriendRequestsController = sentFriendRequests.getController();

        } catch (IOException exception) {
            System.out.println(exception.getMessage());
        }
    }

    public void populate() {
        List<User> friends = page.getAllNonFriends();
        findfriendsObservableList.setAll(friends);
    }

    public void setService(Page page, FriendshipService friendshipService) {
        this.page = page;
        this.friendshipService = friendshipService;
        page.getServiceFriendRequest().addObserver(this);
        findFriendsPane.toFront();
        historyRequestsPane.toBack();
        friendRequestsPane.toBack();
        sentFriendRequestsPane.toBack();

        historyRequestsController.setService(page);
        friendRequestsController.setService(page, friendshipService);
        sentFriendRequestsController.setService(page);

        populate();
    }

    private void handleFilter() {
       String filter = searchFriendsTextField.getText();
        if (filter == null || filter.length() == 0) {
            filteredList.setPredicate(s -> true);
        } else {
            filteredList.setPredicate(s -> s.getFirstName().contains(filter));
        }
        findFriendListView.setCellFactory(userListView -> new FindFriendsCellView(page));
    }

    @Override
    public void update(RequestChangedEvent requestChangeEvent) {
        findFriendListView.getItems().clear();
        findFriendListView.refresh();
        populate();
    }

    public void onViewSentRequests(ActionEvent actionEvent) {
        sentFriendRequestsPane.toFront();
        findFriendsPane.toBack();
        friendRequestsPane.toBack();
        historyRequestsPane.toBack();
    }

    public void onViewReceivedRequests(ActionEvent actionEvent) {
        friendRequestsPane.toFront();
        findFriendsPane.toBack();
        historyRequestsPane.toBack();
        sentFriendRequestsPane.toBack();
    }

    public void onViewHistoryRequests(ActionEvent actionEvent) {
        historyRequestsPane.toFront();
        findFriendsPane.toBack();
        friendRequestsPane.toBack();
        sentFriendRequestsPane.toBack();
    }
}
