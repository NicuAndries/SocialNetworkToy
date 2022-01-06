package com.example.socialnetwork.controller;

import com.example.socialnetwork.domain.Eveniment;
import com.example.socialnetwork.domain.EvenimentNotification;
import com.example.socialnetwork.domain.Friend;
import com.example.socialnetwork.exceptions.ServiceException;
import com.example.socialnetwork.service.FriendshipService;
import com.example.socialnetwork.service.Service;
import com.example.socialnetwork.utils.events.EvenimentChangedEvent;
import com.example.socialnetwork.utils.events.Event;
import com.example.socialnetwork.utils.events.FriendshipChangedEvent;
import com.example.socialnetwork.utils.observer.Observer;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Circle;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.List;

public class EventController implements Observer<EvenimentChangedEvent> {
    public ListView<Eveniment> eventsListView;
    public Button createEvent;
    public Button checkNotificationButton;
    public Circle notificationCircle;
    public Label numberOfNotifications;
    public Pane notificationPane;
    public Pane eventPane;
    private ObservableList<Eveniment> evenimentsObservableList = FXCollections.observableArrayList();
    private Service service;
    NotificationController notificationController;
    int notificationCount;

    public void setService(Service service) {
        this.service = service;
        this.service.getEvenimentService().addObserver(this);
        service.deletePastEvents();
        service.sendNotifications();
        notificationCount = service.populateNotificationList().size();
        if (notificationCount != 0) {
            notificationCircle.setFill(Color.WHITE);
            numberOfNotifications.setText(String.valueOf(notificationCount));
        }
        eventPane.toFront();
        notificationController.setService(service);
        populate();
    }

    @FXML
    public void initialize() {
        FXMLLoader notificationLoader = new FXMLLoader(getClass().getClassLoader().getResource("notificationPane.fxml"));
        try {
            notificationPane.getChildren().add(notificationLoader.load());
        } catch (IOException e) {
            e.printStackTrace();
        }
        notificationController = notificationLoader.getController();
        notificationCircle.setFill(Color.TRANSPARENT);
        numberOfNotifications.setText("");
        eventsListView.setItems(evenimentsObservableList);
        eventsListView.setCellFactory(param -> new EventCellView(service));
    }

    public void populate() {
        List<Eveniment> eveniments = service.getEvents();
        evenimentsObservableList.setAll(eveniments);
    }

    @Override
    public void update(EvenimentChangedEvent evenimentChangedEvent) {
        eventsListView.getItems().clear();
        eventsListView.refresh();
        populate();
    }

    public void onCreateEvent(ActionEvent actionEvent) {
        Stage stage = new Stage();
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getClassLoader().getResource("eventCreator.fxml"));
        Scene scene = null;
        try {
            scene = new Scene(fxmlLoader.load());
            EventCreatorController eventCreatorController  = fxmlLoader.getController();
            eventCreatorController.setService(service);
        } catch (IOException exception) {
            System.out.println(exception.getMessage());
        }
        stage.setTitle("Create chat!");
        stage.setScene(scene);
        stage.show();
    }

    public void onCheckNotificationButton(ActionEvent actionEvent) {
        notificationPane.toFront();
        eventPane.toBack();
        service.readNotifications();
        notificationCircle.setFill(Color.TRANSPARENT);
        numberOfNotifications.setText("");
    }
}
