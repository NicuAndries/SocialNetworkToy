package com.example.socialnetwork.controller;

import com.example.socialnetwork.domain.Chat;
import com.example.socialnetwork.domain.Eveniment;
import com.example.socialnetwork.exceptions.RepositoryException;
import com.example.socialnetwork.exceptions.ServiceException;
import com.example.socialnetwork.exceptions.ValidationException;
import com.example.socialnetwork.service.Service;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

public class EventCreatorController implements Initializable {
    public TextField nameTextField;
    public TextField timeTextField;
    public DatePicker datePicker;
    public Button createEventButton;
    public Button selectImageButton;
    public ImageView eventImage;
    public String image;
    Service service;

    public void setService(Service service) {
        this.service = service;
    }

    public void onCreateEventButton(ActionEvent actionEvent) {
        Eveniment eveniment = new Eveniment(nameTextField.getText(), datePicker.getValue(), timeTextField.getText());
        if (image == null)
            image = "images\\defaultImage.jpg";
        eveniment.setImage(image);
        service.saveEvent(eveniment);
        ((Node) (actionEvent.getSource())).getScene().getWindow().hide();
    }


    @Override
    public void initialize(URL location, ResourceBundle resources) {
        FileChooser fileChooser = new FileChooser();
        Stage stage = new Stage();

        selectImageButton.setOnAction((event) -> {
            File file = fileChooser.showOpenDialog(stage);
            if (file != null) {
                image = file.toURI().toString();
                eventImage.setImage(new Image(image));
            }
        });
    }
}
