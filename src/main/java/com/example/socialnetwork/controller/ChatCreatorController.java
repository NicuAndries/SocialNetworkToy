package com.example.socialnetwork.controller;

import com.example.socialnetwork.domain.Chat;
import com.example.socialnetwork.domain.Friend;
import com.example.socialnetwork.domain.Message;
import com.example.socialnetwork.domain.User;
import com.example.socialnetwork.exceptions.RepositoryException;
import com.example.socialnetwork.exceptions.ServiceException;
import com.example.socialnetwork.exceptions.ValidationException;
import com.example.socialnetwork.service.FriendshipService;
import com.example.socialnetwork.service.Service;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.layout.Pane;

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
    public TextField toTextField;
    private ObservableList<Friend> friendsObservableList = FXCollections.observableArrayList();
    List<Long> usersId;
    Service service;
    Long numberOfSelectedElements;

    @FXML
    public void initialize(URL location, ResourceBundle resources) {
        friendListView.setItems(friendsObservableList);
        friendListView.setCellFactory(param -> new ChatCreatorCellView(service));

        friendListView.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);

        friendListView.getSelectionModel().selectedItemProperty().addListener((ObservableValue<? extends Friend> ov, Friend old_val, Friend new_val) -> {
            ObservableList<Friend> selectedItems = friendListView.getSelectionModel().getSelectedItems();
            numberOfSelectedElements = (long) selectedItems.size();
            StringBuilder builder = new StringBuilder();
            usersId = new ArrayList<>();
            usersId.add(service.getIdUser());
            for (int i=0; i<selectedItems.size(); i++) {
                builder.append(selectedItems.get(i).getFirstName()).append(" ").append(selectedItems.get(i).getLastName()).append("   ");
                usersId.add(selectedItems.get(i).getId());
            }
            System.out.println(builder.toString());
            toTextField.setText(builder.toString());
            if (selectedItems.size() == 1)
                chatNameTextField.setText(selectedItems.get(0).getFirstName() + " "  + selectedItems.get(0).getLastName());
            else
                chatNameTextField.setText("");
        });
    }

    public void populate() {
        try {
            List<Friend> friends = service.getListOfFriends();
            friendsObservableList.setAll(friends);
        } catch (ServiceException exception) {
            System.out.println(exception.getMessage());
        }
    }

    public void setService(Service service) {
        this.service = service;
        populate();
    }

    public void onCreateChatButton(ActionEvent actionEvent) {
        Chat chat = null;
        try {
            if (numberOfSelectedElements == 1) {
                List<Chat> chatList = service.getAllChats();
                String chatName = service.getUser().getFirstName() + " " + service.getUser().getLastName() + " " + chatNameTextField.getText();
                for (Chat chat1 : chatList)
                    if (chat1.getMembers().size() == 2 && chat1.getName().equals(chatName)) {
                        service.sendMessage(chat1.getId(), createChatMessageTextField.getText(), null);
                        ((Node) (actionEvent.getSource())).getScene().getWindow().hide();
                        return;
                    }
                chat = new Chat(service.getUser().getFirstName() + " " + service.getUser().getLastName() + " " + chatNameTextField.getText());
            }
            else
                chat = new Chat(chatNameTextField.getText());
        } catch (ServiceException | ValidationException | RepositoryException e) {
            System.out.println(e.getMessage());
        }
        chat.setMembers(usersId);
        Chat chat1 = service.saveChat(chat);
        try {
            service.sendMessage(chat1.getId(), createChatMessageTextField.getText(), null);
        } catch (ValidationException | RepositoryException e) {
            e.printStackTrace();
        }
        ((Node) (actionEvent.getSource())).getScene().getWindow().hide();
    }

    public void onCancelButton(ActionEvent actionEvent) {
    }
}
