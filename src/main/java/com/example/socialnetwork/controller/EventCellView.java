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
    public ImageView goingImage;
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
            //WED, DEC 29 AT 3:00 AM
            String month = String.valueOf(chat.getDate().getMonth());
            String dayString = String.valueOf(chat.getDate().getDayOfWeek());
            String day = String.valueOf(chat.getDate().getDayOfMonth());
            eventDate.setText(dayString + ", " + month + " " + day + " AT " + chat.getTime());
            EvenimentNotification eventNotification = page.findOneSubscribe(new EvenimentNotification(getItem().getId(), page.getIdUser(), "", ""));
            if (eventNotification != null) {
                Image image = new Image("images/checked.png");
                goingImage.setImage(image);
                if (eventNotification.getNotification().equals("on"))
                    notificationButton.setText("Notifications: On");
                 else
                    notificationButton.setText("Notifications: Off");
            } else {
                goingImage.setImage(null);
                notificationButton.setText("Notifications: Off");
            }

            goingButton.setOnAction(event -> {
                EvenimentNotification eveniment = new EvenimentNotification(getItem().getId(), page.getIdUser(), "off", "notsent");
                EvenimentNotification evenimentNotification = page.findOneSubscribe(eveniment);
                if (evenimentNotification == null) {
                    page.subscribeToEvent(eveniment);
                    Image image = new Image("images/checked.png");
                    goingImage.setImage(image);
                }
                else {
                    page.unsubscribeFromEvent(eveniment);
                    notificationButton.setText("Notifications: Off");
                    goingImage.setImage(null);
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
                    notificationButton.setText("Notifications: On");
                } else if (evenimentNotification.getNotification().equals("on")) {
                    evenimentNotification.setNotification("off");
                    page.updateEventNotification(evenimentNotification);
                    notificationButton.setText("Notifications: Off");
                }
            });

            eventImage.setImage(new Image(chat.getImage()));
            setGraphic(anchorPane);
        }
    }
}
