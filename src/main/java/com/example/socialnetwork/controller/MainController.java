package com.example.socialnetwork.controller;

import com.example.socialnetwork.domain.User;
import com.example.socialnetwork.exceptions.ServiceException;
import com.example.socialnetwork.service.FriendRequestService;
import com.example.socialnetwork.service.FriendshipService;
import com.example.socialnetwork.service.Page;
import javafx.collections.transformation.FilteredList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import javafx.stage.Window;
import javax.security.auth.login.CredentialException;
import java.io.IOException;


public class MainController {
    public Button messageButton;
    public Button eventsButton;
    public Button profileButton;
    public Pane profilePane;
    public Label profileNameLabel;
    public Pane messagePane;
    public ImageView profileImage;
    public Button exportButton;
    private Page page;
    public Button friendsButton;
    public Button findFriendsButton;
    public Pane findFriendsPane;
    public Pane friendsPane;
    public Pane eventPane;
    public StackPane stackPane;
    private FriendshipService friendshipService;
    private FriendRequestService friendRequestService;
    private FriendsController friendsController;
    private FindFriendsController findFriendsController;
    private ChatController messageController;
    private EventController eventController;
    private Stage loginStage;

    private FilteredList<User> filteredList;

    public void setService(Page page, FriendRequestService friendRequestService, Long user_id) {
        this.friendRequestService = friendRequestService;
        this.page = page;
        try {
            this.page.setIdUser(user_id);
        } catch (CredentialException exception) {
            System.out.println(exception.getMessage());
        }
        friendsController.setService(page, page.getServiceFriendship());
        findFriendsController.setService(page, page.getServiceFriendship());
        messageController.setService(page);
        eventController.setService(page);
        profilePane.toFront();
        setProfileInformation();
    }

    public void setStage(Window stage){
        loginStage = (Stage) stage;
    }

    @FXML
    public void initialize() {
        FXMLLoader friendsLoader = null;
        FXMLLoader findFriendsLoader = null;
        FXMLLoader messageLoader = null;
        FXMLLoader eventsLoader = null;
        try {
            friendsLoader = new FXMLLoader(getClass().getClassLoader().getResource("friendsPane.fxml"));
            friendsPane.getChildren().add(friendsLoader.load());

            findFriendsLoader = new FXMLLoader(getClass().getClassLoader().getResource("findFriendsPane.fxml"));
            findFriendsPane.getChildren().add(findFriendsLoader.load());

            messageLoader = new FXMLLoader(getClass().getClassLoader().getResource("chatPane.fxml"));
            messagePane.getChildren().add(messageLoader.load());

            eventsLoader = new FXMLLoader(getClass().getClassLoader().getResource("eventPane.fxml"));
            eventPane.getChildren().add(eventsLoader.load());

        } catch (IOException exception) {
            System.out.println(exception.getMessage());
        }
        friendsController = friendsLoader.getController();
        findFriendsController = findFriendsLoader.getController();
        messageController = messageLoader.getController();
        eventController = eventsLoader.getController();
    }

    public void setProfileInformation() {
        try {
            profileNameLabel.setText(page.getUser().getFirstName() + " " + page.getUser().getLastName());
            profileImage.setImage(new Image(page.getUser().getProfilePicture()));
        } catch (ServiceException exception) {
            System.out.println(exception.getMessage());
        }
    }

    public void onFriendsButton(ActionEvent actionEvent) {
        friendsPane.toFront();
        findFriendsPane.toBack();
        messagePane.toBack();
    }

    public void onFindFriendsButton(ActionEvent actionEvent) {
        findFriendsPane.toFront();
        friendsPane.toBack();
        messagePane.toBack();
    }

    public void onMessageButton(ActionEvent actionEvent) {
        messagePane.toFront();
        friendsPane.toBack();
        findFriendsPane.toBack();
    }

    public void onEventsButton(ActionEvent actionEvent) {
        eventPane.toFront();
    }

    public void onProfileButton(ActionEvent actionEvent) {
        profilePane.toFront();
    }

    public void onExportButton(ActionEvent actionEvent) {
        Stage stage = new Stage();
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getClassLoader().getResource("activitiesDatePickerWindow.fxml"));
        Scene scene = null;
        try {
            scene = new Scene(fxmlLoader.load());
            ActivitiesReportGeneratorController activitiesReportGeneratorController = fxmlLoader.getController();
            activitiesReportGeneratorController.setPage(page);
        } catch (IOException exception) {
            System.out.println(exception.getMessage());
        }
        stage.setTitle("Date picker!");
        stage.setScene(scene);
        stage.show();
    }
}
