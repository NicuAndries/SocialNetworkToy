package com.example.socialnetwork.controller;

import com.example.socialnetwork.exceptions.RepositoryException;
import com.example.socialnetwork.exceptions.ServiceException;
import com.example.socialnetwork.exceptions.ValidationException;
import com.example.socialnetwork.service.SignUpService;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.*;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import java.net.URL;
import java.security.InvalidKeyException;
import java.time.LocalDate;
import java.util.ResourceBundle;

public class SignUpController implements Initializable {
    public PasswordField confirmPasswordTextField;
    public Button signUpButton;
    public TextField secondNameTextField;
    public TextField firstNameTextField;
    public TextField usernameSignUpTextField;
    public PasswordField passwordTextField;
    public ComboBox<String> genderComboBox;
    public DatePicker birthdateComboBox;
    public Label signUpLabel;
    public SignUpService signUpService;

    public void setService(SignUpService signUpService){
        this.signUpService = signUpService;
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        genderComboBox.getItems().add("Male");
        genderComboBox.getItems().add("Female");
    }

    @FXML
    public void onSignUpButton(ActionEvent actionEvent) {
        String username, password, confirmPassword, firstName, lastName, gender;
        LocalDate birthdate;
        username = usernameSignUpTextField.getText();
        password = passwordTextField.getText();
        confirmPassword = confirmPasswordTextField.getText();
        firstName = firstNameTextField.getText();
        lastName = secondNameTextField.getText();
        gender = genderComboBox.getValue();
        birthdate = birthdateComboBox.getValue();
        if(!password.equals(confirmPassword)){
            System.out.println("Passwords must be the same!");
        }
        else{
            try {
                signUpService.signUp(username, password, firstName, lastName, gender, birthdate);
                ((Node) (actionEvent.getSource())).getScene().getWindow().hide();
            } catch (ValidationException | InvalidKeyException | BadPaddingException | IllegalBlockSizeException | ServiceException | RepositoryException exception) {
                System.out.println(exception.getMessage());
            }
        }
    }
}
