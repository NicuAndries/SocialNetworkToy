package com.example.socialnetwork.controller;

import com.example.socialnetwork.Main;
import com.example.socialnetwork.domain.Chat;
import com.example.socialnetwork.domain.Message;
import com.example.socialnetwork.domain.User;
import com.example.socialnetwork.exceptions.RepositoryException;
import com.example.socialnetwork.exceptions.ServiceException;
import com.example.socialnetwork.exceptions.ValidationException;
import com.example.socialnetwork.service.Service;
import com.example.socialnetwork.utils.events.ChatChangedEvent;
import com.example.socialnetwork.utils.events.MessageChangedEvent;
import com.example.socialnetwork.utils.observer.Observer;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Cursor;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.effect.Light;
import javafx.scene.image.Image;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;

public class ChatController implements Observer<MessageChangedEvent> {
    public ListView<Chat> chatListView;
    public TextField messageTextField;
    public ListView<Message> messageListView;
    public Button sendMessageButton;
    public Button createChatButton;
    public Label messagesChatName;
    public Label userStatus;
    public Label toReplyLabel;
    public AnchorPane toReplyAnchorPane;
    public Label toMessageLabel;
    public Button cancelReplyButton;
    private ObservableList<Chat> chatObservableList = FXCollections.observableArrayList();
    private ObservableList<Message> messageObservableList = FXCollections.observableArrayList();
    private Service service;
    private Long replyId;
    private Long chatId;

    @FXML
    public void initialize() {
        chatListView.setItems(chatObservableList);
        toReplyAnchorPane.toBack();
        chatListView.setCellFactory(param -> new ChatCellView(service));
        userStatus.setVisible(false);
        messagesChatName.setVisible(false);
        messageListView.getStylesheets().add(Objects.requireNonNull(getClass().getClassLoader().getResource("listcell.css")).toExternalForm());

        messageListView.getSelectionModel().selectedItemProperty().addListener((ObservableValue<? extends Message> observableValue, Message oldValue, Message newValue) -> {
            Message selectedMessage = messageListView.getSelectionModel().getSelectedItem();
            if (selectedMessage != null) {
                toMessageLabel.setText("Message : " + selectedMessage.getText());
                try {
                    User user = service.getServiceUser().findOne(selectedMessage.getUserSenderId());
                    toReplyLabel.setText("Replying to : " + user.getFirstName() + " " + user.getLastName());
                    replyId = selectedMessage.getId();
                } catch (ServiceException exception) {
                    System.out.println(exception.getMessage());
                }
                toReplyAnchorPane.toFront();
            }
        });

        chatListView.getSelectionModel().selectedItemProperty().addListener((ObservableValue<? extends Chat> observableValue, Chat oldValue, Chat newValue) -> {
            Chat selectedItem = chatListView.getSelectionModel().getSelectedItem();
            if (selectedItem != null) {
                chatId = selectedItem.getId();
                if(selectedItem.getMembers().size() == 2 && service.getIdUser().equals(selectedItem.getMembers().get(0))) {
                    List<String> name = List.of(selectedItem.getName().split(" "));
                    userStatus.setVisible(true);
                    messagesChatName.setVisible(true);
                    messagesChatName.setText(name.get(2) + " " + name.get(3));
                    userStatus.setText("Offline");
                } else if(selectedItem.getMembers().size() == 2 && service.getIdUser().equals(selectedItem.getMembers().get(1))) {
                    userStatus.setVisible(true);
                    messagesChatName.setVisible(true);
                    List<String> name = List.of(selectedItem.getName().split(" "));
                    userStatus.setText("Offline");
                    messagesChatName.setText(name.get(0) + " " + name.get(1));
                }
                else {
                    userStatus.setVisible(true);
                    messagesChatName.setVisible(true);
                    messagesChatName.setText(selectedItem.getName());
                    userStatus.setText(selectedItem.getMembers().size() + " members");
                }
            }
            populateChat(chatId);
            messageListView.setItems(messageObservableList);
            messageListView.setCellFactory(param -> new MessageCellView(service));
            messageListView.scrollTo(messageListView.getItems().size());
        });
    }

    public void populateChat(Long chatId) {
        List<Message> messages = service.getChatMessages(chatId);
        messages.sort(Comparator.comparing(Message::getTime));
        messageObservableList.setAll(messages);
    }

    public void populate() {
        List<Chat> chats = service.getAllChats();
        chatObservableList.setAll(chats);
    }

    public void setService(Service service) {
        this.service = service;
        this.service.getServiceMessage().addObserver(this);
        populate();
    }

    public void onSendMessageButton(ActionEvent mouseEvent) {
        String text = messageTextField.getText();
        try {
            service.sendMessage(chatId, text, replyId);
            toReplyAnchorPane.toBack();
            replyId = null;
        } catch (ValidationException | RepositoryException e) {
            System.out.println(e.getMessage());
        }
        messageTextField.clear();
    }

    @Override
    public void update(MessageChangedEvent messageChangedEvent) {
        populate();
        messageListView.getItems().clear();
        populateChat(chatId);
        messageListView.scrollTo(messageListView.getItems().size());
    }

    public void onCreateChatButton(ActionEvent actionEvent) {
        Stage stage = new Stage();
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getClassLoader().getResource("chatCreator.fxml"));
        Scene scene = null;
        try {
            scene = new Scene(fxmlLoader.load());
            ChatCreatorController chatCreatorController  = fxmlLoader.getController();
            chatCreatorController.setService(service);
        } catch (IOException exception) {
            System.out.println(exception.getMessage());
        }
        stage.setTitle("Create chat!");
        stage.setScene(scene);
        stage.show();
    }

    public void onCancelReplyButton(ActionEvent actionEvent) {
        toReplyAnchorPane.toBack();
        replyId = null;
    }
}
