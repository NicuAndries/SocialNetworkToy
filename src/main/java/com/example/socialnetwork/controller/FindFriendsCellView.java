package com.example.socialnetwork.controller;

import com.example.socialnetwork.domain.User;
import com.example.socialnetwork.exceptions.RepositoryException;
import com.example.socialnetwork.exceptions.ServiceException;
import com.example.socialnetwork.exceptions.ValidationException;
import com.example.socialnetwork.service.Page;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;

import java.io.IOException;

public class FindFriendsCellView extends ListCell<User> {
    @FXML
    public Label labelName;
    @FXML
    public Button addFriendButton;
    @FXML
    public AnchorPane anchorPane;
    @FXML
    public Label labelBirthdata;
    @FXML
    public ImageView userImage;
    private Page page;

    public FindFriendsCellView(Page page) {
        this.page = page;
        try {
            FXMLLoader modelLoader = new FXMLLoader();
            modelLoader.setController(this);
            modelLoader.setLocation(getClass().getClassLoader().getResource("findFriendsCellView.fxml"));
            modelLoader.load();
        } catch (IOException exception) {
            throw new RuntimeException("Creating UI component failed", exception);
        }
    }

    @Override
    protected void updateItem(User user, boolean empty) {
        super.updateItem(user, empty);
        if (empty || user == null) {
            setText(null);
            setGraphic(null);
        } else {
            labelName.setText(user.getFirstName() + " " + user.getLastName());
            labelBirthdata.setText(String.valueOf(user.getBirthdate()));
            Image image = new Image(user.getProfilePicture());
            userImage.setImage(image);

            addFriendButton.setOnAction(event -> {
                try {
                    page.sendFriendRequest(getItem().getId());
                    getListView().refresh();
                    super.updateItem(user, true);
                } catch (RepositoryException | ServiceException | ValidationException exception) {
                    MessageAlert.showErrorMessage(null, exception.getMessage());
                }
            });
            setText(null);
            setGraphic(anchorPane);
        }
    }
}
