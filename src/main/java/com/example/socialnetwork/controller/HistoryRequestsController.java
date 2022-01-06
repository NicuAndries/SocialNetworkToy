package com.example.socialnetwork.controller;

import com.example.socialnetwork.dto.RequestDTO;
import com.example.socialnetwork.service.Service;
import com.example.socialnetwork.utils.events.RequestChangedEvent;
import com.example.socialnetwork.utils.observer.Observer;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.layout.Pane;

import java.util.List;
import java.util.stream.StreamSupport;

public class HistoryRequestsController implements Observer<RequestChangedEvent> {
    public Button backToFindFriendsButton;
    private Service service;
    private ObservableList<RequestDTO> historyRequestsObservable = FXCollections.observableArrayList();
    @FXML
    private ListView<RequestDTO> historyRequestsListView;
    @FXML
    private Pane historyRequestsPane;


    public void setService(Service service) {
        this.service = service;
        service.getServiceFriendRequest().addObserver(this);
        populate();
    }

    public void onBackToFindFriendsButton(ActionEvent actionEvent) {
        historyRequestsPane.getParent().toBack();
    }

    public void initialize() {
        historyRequestsListView.setItems(historyRequestsObservable);
        historyRequestsListView.setCellFactory(userListView -> new HistoryRequestsCellView(service));
    }

    public void populate() {
        List<RequestDTO> historyRequests = StreamSupport.stream(service.getAllRejectedAndApprovedForReceiverDTO().spliterator(), false).toList();
        historyRequestsObservable.setAll(historyRequests);
    }

    @Override
    public void update(RequestChangedEvent requestChangeEvent) {
        historyRequestsListView.getItems().clear();
        populate();
    }
}
