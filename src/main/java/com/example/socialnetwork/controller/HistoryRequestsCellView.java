package com.example.socialnetwork.controller;

import com.example.socialnetwork.dto.RequestDTO;
import com.example.socialnetwork.service.Page;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;

import java.io.IOException;

public class HistoryRequestsCellView extends ListCell<RequestDTO> {
    public Label labelName;
    public AnchorPane historyCell;
    public ImageView userImage;
    public Label labelHistoryInfo;
    private Page page;

    public HistoryRequestsCellView(Page page) {
        this.page = page;
        try {
            FXMLLoader modelLoader = new FXMLLoader();
            modelLoader.setController(this);
            modelLoader.setLocation(getClass().getClassLoader().getResource("historyRequestsCellView.fxml"));
            modelLoader.load();
        } catch (IOException exception) {
            throw new RuntimeException("Creating UI component failed", exception);
        }
    }

    @Override
    protected void updateItem(RequestDTO friend, boolean empty) {
        super.updateItem(friend, empty);

        if (empty || friend == null) {
            setText(null);
            setGraphic(null);
        } else {
            labelName.setText(friend.getFirstName() + " " + friend.getLastName());
            if (friend.getStatus().equals("approved"))
                labelHistoryInfo.setText("You are friends since " + friend.getDate());
            else if (friend.getStatus().equals("rejected"))
                labelHistoryInfo.setText("You declined friend request at " + friend.getDate());
            Image image = new Image(friend.getImage());
            userImage.setImage(image);
            setText(null);
            setGraphic(historyCell);
        }
    }
}
