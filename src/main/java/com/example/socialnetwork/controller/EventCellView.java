package com.example.socialnetwork.controller;

import com.example.socialnetwork.domain.Eveniment;
import com.example.socialnetwork.domain.EvenimentNotification;
import com.example.socialnetwork.service.Page;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;

import java.io.IOException;

public class EventCellView extends ListCell<Eveniment> {
    public Label eventName;
    public Label eventDate;
    public Button goingButton;
    public Button notificationButton;
    public AnchorPane anchorPane;
    private Page page;
    public ImageView eventImage;

    public EventCellView(Page page) {
        this.page = page;
        try {
            FXMLLoader modelLoader = new FXMLLoader();
            modelLoader.setController(this);
            modelLoader.setLocation(getClass().getClassLoader().getResource("eventCellView.fxml"));
            modelLoader.load();
        } catch (IOException exception) {
            throw new RuntimeException("Creating UI component failed", exception);
        }
    }

    @Override
    protected void updateItem(Eveniment chat, boolean empty) {
        super.updateItem(chat, empty);

        if (empty || chat == null) {
            setText(null);
            setGraphic(null);
        } else {
            eventName.setText(chat.getName());
            eventDate.setText(String.valueOf(chat.getDate()));
            EvenimentNotification eventNotification = page.findOneSubscribe(new EvenimentNotification(getItem().getId(), page.getIdUser(), "", ""));
            if (eventNotification != null) {
                goingButton.setStyle("-fx-background-color: #FFA500");
                if (eventNotification.getNotification().equals("on"))
                    notificationButton.setStyle("-fx-background-color: #FFA500");
                else
                    notificationButton.setStyle(null);
            } else {
                goingButton.setStyle(null);
                notificationButton.setStyle(null);
            }

            goingButton.setOnAction(event -> {
                EvenimentNotification eveniment = new EvenimentNotification(getItem().getId(), page.getIdUser(), "off", "notsent");
                EvenimentNotification evenimentNotification = page.findOneSubscribe(eveniment);
                if (evenimentNotification == null) {
                    goingButton.setStyle("-fx-background-color: #FFA500");
                    page.subscribeToEvent(eveniment);
                }
                else {
                    page.unsubscribeFromEvent(eveniment);
                    goingButton.setStyle(null);
                }
            });

            notificationButton.setOnAction(event -> {
                EvenimentNotification eveniment = new EvenimentNotification(getItem().getId(), page.getIdUser(), "", "");
                EvenimentNotification evenimentNotification = page.findOneSubscribe(eveniment);
                if (evenimentNotification == null)
                    MessageAlert.showErrorMessage(null, "You must subscribe to event!");
                else if (evenimentNotification.getNotification().equals("off")) {
                    evenimentNotification.setNotification("on");
                    page.updateEventNotification(evenimentNotification);
                    notificationButton.setStyle("-fx-background-color: #FFA500");
                } else if (evenimentNotification.getNotification().equals("on")) {
                    evenimentNotification.setNotification("off");
                    page.updateEventNotification(evenimentNotification);
                    notificationButton.setStyle(null);
                }
            });

            eventImage.setImage(new Image(chat.getImage()));
            setGraphic(anchorPane);
        }
    }
}
