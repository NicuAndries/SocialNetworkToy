package com.example.socialnetwork.controller;

import com.example.socialnetwork.dto.RequestDTO;
import com.example.socialnetwork.exceptions.RepositoryException;
import com.example.socialnetwork.exceptions.ServiceException;
import com.example.socialnetwork.exceptions.ValidationException;
import com.example.socialnetwork.service.Service;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;

import java.io.IOException;

public class FriendRequestsCellView extends ListCell<RequestDTO> {
    public Label labelName;
    public Button addFriendButton;
    public Button cancelFriendButton;
    public Label labelBirthdata;
    public ImageView userImage;
    public AnchorPane friendRequestsPane;
    private Service service;

    public FriendRequestsCellView(Service service) {
        this.service = service;
        try {
            FXMLLoader modelLoader = new FXMLLoader();
            modelLoader.setController(this);
            modelLoader.setLocation(getClass().getClassLoader().getResource("friendRequestsCellView.fxml"));
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
            labelBirthdata.setText(String.valueOf(friend.getDate()));
            Image image = new Image(friend.getImage());
            userImage.setImage(image);
            addFriendButton.setOnAction(event -> {
                try {
                    service.acceptFriendRequest(getItem().getIdUser());
                    getListView().refresh();
                    super.updateItem(friend, true);
                } catch (ServiceException | ValidationException | RepositoryException exception) {
                    MessageAlert.showErrorMessage(null, exception.getMessage());
                }
            });
            cancelFriendButton.setOnAction(event -> {
                try {
                    service.deleteFriendship(getItem().getIdUser());
                    getListView().refresh();
                    super.updateItem(friend, true);
                } catch (ServiceException exception) {
                    MessageAlert.showErrorMessage(null, exception.getMessage());
                }
            });
            setText(null);
            setGraphic(friendRequestsPane);
        }
    }
}
