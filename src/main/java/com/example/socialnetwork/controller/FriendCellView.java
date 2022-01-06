package com.example.socialnetwork.controller;

import com.example.socialnetwork.domain.Friend;
import com.example.socialnetwork.exceptions.ServiceException;
import com.example.socialnetwork.service.Page;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;

import java.io.IOException;

public class FriendCellView extends ListCell<Friend> {
    public Button unfriendButton;
    public Label labelName;
    public AnchorPane anchorPane;
    public Label labelBirthdata;
    public ImageView userImage;
    private FXMLLoader modelLoader;
    private Page page;

    public FriendCellView(Page page) {
        this.page = page;
        try {
            FXMLLoader modelLoader = new FXMLLoader();
            modelLoader.setController(this);
            modelLoader.setLocation(getClass().getClassLoader().getResource("friendsCellView.fxml"));
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
            Image image = new Image(friend.getProfilePicture());
            userImage.setImage(image);
            unfriendButton.setOnAction(event -> {
                try {
                    page.deleteFriendship(getItem().getId());
                    getListView().refresh();
                    super.updateItem(friend, true);
                } catch (ServiceException exception) {
                    MessageAlert.showErrorMessage(null, exception.getMessage());
                }
            });
            setText(null);
            setGraphic(anchorPane);
        }
    }
}