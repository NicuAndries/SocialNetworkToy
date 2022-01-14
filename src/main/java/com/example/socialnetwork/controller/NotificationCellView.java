package com.example.socialnetwork.controller;

import com.example.socialnetwork.domain.Eveniment;
import com.example.socialnetwork.service.Page;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;

import java.io.IOException;

public class NotificationCellView extends ListCell<Eveniment> {
    public Label labelName;
    public Label labelHistoryInfo;
    public ImageView userImage;
    public AnchorPane notificationCell;
    private Page page;

    public NotificationCellView(Page page) {
        this.page = page;
        try {
            FXMLLoader modelLoader = new FXMLLoader();
            modelLoader.setController(this);
            modelLoader.setLocation(getClass().getClassLoader().getResource("notificationCellView.fxml"));
            modelLoader.load();
        } catch (IOException exception) {
            throw new RuntimeException("Creating UI component failed", exception);
        }
    }

    @Override
    protected void updateItem(Eveniment friend, boolean empty) {
        super.updateItem(friend, empty);
        if (empty || friend == null) {
            setText(null);
            setGraphic(null);
        } else {
            labelName.setText(friend.getName());
            labelHistoryInfo.setText("You have an upcoming event, tommorow at: " + friend.getTime());
            Image image = new Image(friend.getImage());
            userImage.setImage(image);
            setText(null);
            setGraphic(notificationCell);
        }
    }
}