package com.example.socialnetwork.controller;

import com.example.socialnetwork.domain.Chat;
import com.example.socialnetwork.domain.User;
import com.example.socialnetwork.exceptions.RepositoryException;
import com.example.socialnetwork.exceptions.ServiceException;
import com.example.socialnetwork.exceptions.ValidationException;
import com.example.socialnetwork.service.Page;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;

import java.io.IOException;
import java.util.List;

public class ChatCellView extends ListCell<Chat> {
    public Label chatName;
    public AnchorPane anchorPane;
    public ImageView userImage;
    public Button deleteChatButton;
    public HBox hBox;
    private Page page;

    public ChatCellView(Page page) {
        this.page = page;
        try {
            FXMLLoader modelLoader = new FXMLLoader();
            modelLoader.setController(this);
            modelLoader.setLocation(getClass().getClassLoader().getResource("chatCellView.fxml"));
            modelLoader.load();
        } catch (IOException exception) {
            throw new RuntimeException("Creating UI component failed", exception);
        }
    }

    @Override
    protected void updateItem(Chat chat, boolean empty) {
        super.updateItem(chat, empty);

        if (empty || chat == null) {
            setText(null);
            setStyle("-fx-background-color: #242131");
            setGraphic(null);
        } else {
            if(chat.getMembers().size() == 2 && page.getIdUser().equals(chat.getMembers().get(0))) {
                try {
                    User user = page.getServiceUser().findOne(chat.getMembers().get(1));
                    Image image = new Image(user.getProfilePicture());
                    userImage.setImage(image);
                } catch (ServiceException e) {
                    e.printStackTrace();
                }
                List<String> name = List.of(chat.getName().split(" "));
                chatName.setText(name.get(2) + " " + name.get(3));
            } else if(chat.getMembers().size() == 2 && page.getIdUser().equals(chat.getMembers().get(1))) {
                List<String> name = List.of(chat.getName().split(" "));
                chatName.setText(name.get(0) + " " + name.get(1));
                Image image = new Image(chat.getImage());
                userImage.setImage(image);
            }
            else {
                chatName.setText(chat.getName());
                Image image = new Image(chat.getImage());
                userImage.setImage(image);
            }
            deleteChatButton.setOnAction(event -> {
                page.deleteChat(getItem());
                try {
                    page.sendMessage(1L, null, null);
                } catch (ValidationException | RepositoryException e) {
                    e.printStackTrace();
                }
                super.updateItem(chat, true);
            });
            setStyle("-fx-background-color: #242131");
            setGraphic(anchorPane);
        }
    }
}
