package com.example.socialnetwork;

import com.example.socialnetwork.controller.LoginController;
import com.example.socialnetwork.domain.*;
import com.example.socialnetwork.repository.*;
import com.example.socialnetwork.service.*;
import com.example.socialnetwork.utils.Constants;
import com.example.socialnetwork.utils.Pair;
import com.example.socialnetwork.utils.Security;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

public class Main extends Application {

    @Override
    public void start(Stage stage) throws Exception {
        Repository<Long, User> userRepository = new UserDatabaseRepository(Constants.URL, Constants.USERNAME, Constants.PASSWORD);
        Repository<Pair<Long, Long>, Friendship> friendshipRepository = new FriendshipDatabaseRepository(Constants.URL, Constants.USERNAME, Constants.PASSWORD);
        Repository<Long, Message> messageRepository = new MessageDatabaseRepository(Constants.URL, Constants.USERNAME, Constants.PASSWORD);
        Repository<String, Account> accountRepository = new AccountDatabaseRepository(Constants.URL, Constants.USERNAME, Constants.PASSWORD);
        Repository<Pair<Long, Long>, FriendRequest> friendRequestRepository= new FriendRequestDatabaseRepository(Constants.URL, Constants.USERNAME, Constants.PASSWORD);
        Repository<Long, Chat> chatRepository = new ChatDatabaseRepository(Constants.URL, Constants.USERNAME, Constants.PASSWORD);
        Repository<Long, Eveniment> evenimentRepository = new EvenimentDatabaseRepository(Constants.URL, Constants.USERNAME, Constants.PASSWORD);

        UserService userService = new UserService(userRepository);
        FriendshipService friendshipService = new FriendshipService(friendshipRepository);
        MessageService messageService = new MessageService(messageRepository);
        AccountService accountService = new AccountService(accountRepository);
        FriendRequestService friendRequestService = new FriendRequestService(friendRequestRepository);
        LoginService loginService = new LoginService(accountService);
        SignUpService signUpService = new SignUpService(accountService, userService);
        ChatService chatService = new ChatService(chatRepository);
        EvenimentNotificationDatabaseRepository evenimentNotificationDatabaseRepository = new EvenimentNotificationDatabaseRepository(Constants.URL, Constants.USERNAME, Constants.PASSWORD);
        EvenimentService evenimentService = new EvenimentService(evenimentRepository, evenimentNotificationDatabaseRepository);
        Service service = new Service(userService, friendshipService, friendRequestService, messageService, chatService, evenimentService);
        Security security = new Security();

        FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("login.fxml"));
        BorderPane root = fxmlLoader.load();
        LoginController loginController = fxmlLoader.getController();
        loginController.setServices(loginService, signUpService, service, friendRequestService);

        Scene scene = new Scene(root);
        stage.setTitle("Hello!");
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}
