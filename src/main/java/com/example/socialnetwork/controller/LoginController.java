package com.example.socialnetwork.controller;

import com.example.socialnetwork.Main;
import com.example.socialnetwork.service.FriendRequestService;
import com.example.socialnetwork.service.LoginService;
import com.example.socialnetwork.service.Service;
import com.example.socialnetwork.service.SignUpService;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import javafx.stage.Window;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.security.auth.login.CredentialException;
import java.io.IOException;
import java.security.InvalidKeyException;

public class LoginController {
    public TextField usernameTextField;
    public PasswordField passwordTextField;
    public Button loginButton;
    public Button registerButton;
    public Label loginMessageLabel;

    private LoginService loginService;
    private SignUpService signUpService;
    private Service service;
    private FriendRequestService friendRequestService;

    public void setServices(LoginService loginService, SignUpService signUpService, Service service, FriendRequestService friendRequestService){
        this.loginService = loginService;
        this.signUpService = signUpService;
        this.service = service;
        this.friendRequestService = friendRequestService;
    }

    public void onRegisterButton(ActionEvent actionEvent) {
        Stage stage = new Stage();
        FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("registration.fxml"));
        Scene scene = null;
        try {
            scene = new Scene(fxmlLoader.load());
            SignUpController signUpController  = fxmlLoader.getController();
            signUpController.setService(signUpService);
        } catch (IOException exception) {
            System.out.println(exception.getMessage());
        }
        stage.setTitle("Smeshariki");
        stage.setScene(scene);
        stage.show();
    }

    public void onLoginButton(ActionEvent actionEvent) {
        if(usernameTextField.getText().isBlank())
            loginMessageLabel.setText("Username can't be null!");
        if(passwordTextField.getText().isBlank())
            loginMessageLabel.setText("Password can't be null!");
        String username = usernameTextField.getText();
        String password = passwordTextField.getText();
        try {
            Long user_id = loginService.authenticate(username, password);
            usernameTextField.setText("");
            passwordTextField.setText("");
            connectUser(user_id, ((Node) (actionEvent.getSource())).getScene().getWindow());
            ((Node) (actionEvent.getSource())).getScene().getWindow().hide();
        } catch (CredentialException exception) {
            loginMessageLabel.setText(exception.getMessage());
        } catch (IllegalBlockSizeException | InvalidKeyException | BadPaddingException exception) {
            System.out.println(exception.getMessage());
        }
    }

    private void connectUser(Long user_id, Window loginStage) {
        Stage stage = new Stage();
        FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("main.fxml"));
        Scene scene = null;
        try {
            scene = new Scene(fxmlLoader.load());
            MainController profileController = fxmlLoader.getController();
            profileController.setService(service, friendRequestService, user_id);
            profileController.setStage(loginStage);
           // profileController.populateWithData();
        } catch (IOException e) {
            e.printStackTrace();
        }

        stage.setTitle("Mandarin");
        stage.setScene(scene);
        stage.show();
    }
}
