package com.example.socialnetwork.controller;

import com.example.socialnetwork.domain.Chat;
import com.example.socialnetwork.domain.Friend;
import com.example.socialnetwork.exceptions.RepositoryException;
import com.example.socialnetwork.exceptions.ServiceException;
import com.example.socialnetwork.exceptions.ValidationException;
import com.example.socialnetwork.service.Page;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

public class ChatCreatorController implements Initializable {
    public ListView<Friend> friendListView;
    public TextField chatNameTextField;
    public TextArea createChatMessageTextField;
    public Button createChatButton;
    public Button cancelButton;
    public Button selectImageButton;
    public TextField toTextField;
    public ImageView chatImage;
    public String selectedImage;
    public String image = "@../../images/addImageAlb.png";
    private ObservableList<Friend> friendsObservableList = FXCollections.observableArrayList();
    List<Long> usersId;
    Page page;
    Long numberOfSelectedElements;

    @FXML
    public void initialize(URL location, ResourceBundle resources) {
        friendListView.setItems(friendsObservableList);
        friendListView.setCellFactory(param -> new ChatCreatorCellView(page));

        friendListView.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);


        FileChooser fileChooser = new FileChooser();
        Stage stage = new Stage();

        selectImageButton.setOnAction((event) -> {
            File file = fileChooser.showOpenDialog(stage);
            if (file != null) {
                selectedImage = file.toURI().toString();
                chatImage.setImage(new Image(selectedImage));
            }
        });


        friendListView.getSelectionModel().selectedItemProperty().addListener((ObservableValue<? extends Friend> ov, Friend old_val, Friend new_val) -> {
            ObservableList<Friend> selectedItems = friendListView.getSelectionModel().getSelectedItems();
            numberOfSelectedElements = (long) selectedItems.size();
            StringBuilder builder = new StringBuilder();
            usersId = new ArrayList<>();
            usersId.add(page.getIdUser());
            for (Friend selectedItem : selectedItems) {
                builder.append(selectedItem.getFirstName()).append(" ").append(selectedItem.getLastName()).append("   ");
                usersId.add(selectedItem.getId());
            }
            toTextField.setText(builder.toString());
            if (selectedItems.size() == 1) {
                chatNameTextField.setText(selectedItems.get(0).getFirstName() + " " + selectedItems.get(0).getLastName());
                image = selectedItems.get(0).getProfilePicture();
                chatImage.setImage(new Image(image));
            }
            else {
                chatNameTextField.setText("");
                image = "@../../images/addImageAlb.png";
                chatImage.setImage(new Image(image));
            }
        });
    }

    public void populate() {
        try {
            List<Friend> friends = page.getListOfFriends();
            friendsObservableList.setAll(friends);
        } catch (ServiceException exception) {
            System.out.println(exception.getMessage());
        }
    }

    public void setService(Page page) {
        this.page = page;
        populate();
    }

    public void onCreateChatButton(ActionEvent actionEvent) {
        Chat chat = null;
        try {
            if (numberOfSelectedElements == 1) {
                List<Chat> chatList = page.getAllChats();
                String chatName = page.getUser().getFirstName() + " " + page.getUser().getLastName() + " " + chatNameTextField.getText();
                for (Chat chat1 : chatList)
                    if (chat1.getMembers().size() == 2 && chat1.getName().equals(chatName)) {
                        page.sendMessage(chat1.getId(), createChatMessageTextField.getText(), null);
                        ((Node) (actionEvent.getSource())).getScene().getWindow().hide();
                        return;
                    }
                chat = new Chat(page.getUser().getFirstName() + " " + page.getUser().getLastName() + " " + chatNameTextField.getText());
                chat.setImage(image);
            }
            else {
                chat = new Chat(chatNameTextField.getText());
                chat.setImage(selectedImage);
            }
        } catch (ServiceException | ValidationException | RepositoryException e) {
            System.out.println(e.getMessage());
        }
        chat.setMembers(usersId);
        Chat chat1 = page.saveChat(chat);
        try {
            page.sendMessage(chat1.getId(), createChatMessageTextField.getText(), null);
        } catch (ValidationException | RepositoryException e) {
            e.printStackTrace();
        }
        ((Node) (actionEvent.getSource())).getScene().getWindow().hide();
    }

    public void onCancelButton(ActionEvent actionEvent) {
        ((Node) (actionEvent.getSource())).getScene().getWindow().hide();
    }
}
