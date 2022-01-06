package com.example.socialnetwork.controller;

import com.example.socialnetwork.domain.Friend;
import com.example.socialnetwork.exceptions.ServiceException;
import com.example.socialnetwork.service.Service;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;

import java.io.IOException;

public class ChatCreatorCellView extends ListCell<Friend> {
    public Label labelName;
    public AnchorPane anchorPane;
    public Button deleteChatButton;
    public Label labelBirthdata;
    public ImageView userImage;
    private FXMLLoader modelLoader;
    private Service service;

    public ChatCreatorCellView(Service service) {
        this.service = service;
        try {
            FXMLLoader modelLoader = new FXMLLoader();
            modelLoader.setController(this);
            modelLoader.setLocation(getClass().getClassLoader().getResource("friendsChatCellView.fxml"));
            modelLoader.load();
        } catch (IOException exception) {
            throw new RuntimeException("Creating UI component failed", exception);
        }
    }

    @Override
    protected void updateItem(Friend friend, boolean empty) {
        super.updateItem(friend, empty);

        if (empty || friend == null) {
            setText(null);
            setGraphic(null);
        } else {
            labelName.setText(friend.getFirstName() + " " + friend.getLastName());
            labelBirthdata.setText(String.valueOf(friend.getBirthdate()));
            Image image = new Image("@../../images/imagine3.jpg");
            userImage.setImage(image);
            setText(null);
            setGraphic(anchorPane);
        }
    }
}