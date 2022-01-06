package com.example.socialnetwork.controller;

import com.example.socialnetwork.domain.Message;
import com.example.socialnetwork.domain.User;
import com.example.socialnetwork.exceptions.ServiceException;
import com.example.socialnetwork.service.Page;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;

import java.io.IOException;

public class MessageCellView extends ListCell<Message> {
    public Text replyToText;
    public AnchorPane messageCell;
    public ImageView userImage;
    public ImageView replyImage;
    public Button replyText;
    public Button messageText;
    public HBox hBox;
    public VBox vBox;
    private Page page;

    public MessageCellView(Page page) {
        this.page = page;
        try {
            FXMLLoader modelLoader = new FXMLLoader();
            modelLoader.setController(this);
            modelLoader.setLocation(getClass().getClassLoader().getResource("messageCellView.fxml"));
            modelLoader.load();
        } catch (IOException exception) {
            throw new RuntimeException("Creating UI component failed", exception);
        }
    }

    @Override
    protected void updateItem(Message friend, boolean empty) {
        super.updateItem(friend, empty);

        if (empty || friend == null) {
            setText(null);
            setGraphic(null);
        } else {

            if (friend.getUserSenderId().equals(page.getIdUser())) {
                if (friend.getReply() == null) {
                    try {
                        hBox.getChildren().clear();

                        User user = page.getServiceUser().findOne(friend.getUserSenderId());
                        Image image = new Image(user.getProfilePicture());

                        Button button = new Button(friend.getText());
                        button.setStyle("-fx-background-color: #0000ff; -fx-background-radius: 30; -fx-border-radius: 30;");
                        button.setTextFill(Color.WHITE);

                        Image image1 = new Image("@../../images/replyRotated.png");
                        ImageView imageView = new ImageView(image1);
                        imageView.setFitHeight(21);
                        imageView.setFitWidth(21);

                        ImageView imageView2 = new ImageView(image);
                        imageView2.setFitHeight(65);
                        imageView2.setFitWidth(65);

                        hBox.getChildren().addAll(imageView, button, imageView2);
                        hBox.setAlignment(Pos.CENTER_RIGHT);

                    } catch (ServiceException e) {
                        e.printStackTrace();
                    }
                }
                else {
                    try {
                        hBox.getChildren().clear();
                        vBox = new VBox();
                        User user = page.getServiceUser().findOne(friend.getUserSenderId());

                        try {
                            Message message = page.getServiceMessage().findOne(friend.getReply());
                            User user1 = page.getServiceUser().findOne(message.getUserSenderId());

                            Button buttonReply = new Button(message.getText());
                            buttonReply.setStyle("-fx-background-color: #8a8d91; -fx-border-radius: 30; -fx-background-radius: 30;");
                            buttonReply.setTextFill(Color.WHITE);

                            Text text = new Text();
                            text.setText("You replied to : " + user1.getFirstName() + " " + user1.getLastName());

                            Image image = new Image(user.getProfilePicture());

                            Button button = new Button(friend.getText());
                            button.setStyle("-fx-background-color: #0000ff; -fx-background-radius: 30; -fx-border-radius: 30;");
                            button.setTextFill(Color.WHITE);

                            Image image1 = new Image("@../../images/replyRotated.png");
                            ImageView imageView = new ImageView(image1);
                            imageView.setFitHeight(21);
                            imageView.setFitWidth(21);

                            ImageView imageView2 = new ImageView(image);
                            imageView2.setFitHeight(65);
                            imageView2.setFitWidth(65);

                            HBox hBox1 = new HBox();
                            hBox1.setAlignment(Pos.CENTER_RIGHT);
                            hBox1.getChildren().addAll(imageView, button);

                            vBox.getChildren().addAll(text, buttonReply, hBox1);
                            vBox.setAlignment(Pos.CENTER_RIGHT);
                            vBox.setLayoutY(3);

                            hBox.getChildren().addAll(vBox, imageView2);
                            hBox.setAlignment(Pos.CENTER_RIGHT);

                        } catch (ServiceException exception) {
                            System.out.println(exception.getMessage());
                        }
                    } catch (ServiceException e) {
                        e.printStackTrace();
                    }
                }
            }

            else {
                if (friend.getReply() == null) {
                    try {
                        hBox.getChildren().clear();
                        hBox.setLayoutX(0);

                        User user = page.getServiceUser().findOne(friend.getUserSenderId());
                        Image image = new Image(user.getProfilePicture());

                        Button button = new Button(friend.getText());
                        button.setStyle("-fx-background-color: #4e4f50; -fx-background-radius: 30; -fx-border-radius: 30;");
                        button.setTextFill(Color.WHITE);

                        Image image1 = new Image("@../../images/reply.png");
                        ImageView imageView = new ImageView(image1);
                        imageView.setFitHeight(21);
                        imageView.setFitWidth(21);

                        ImageView imageView2 = new ImageView(image);
                        imageView2.setFitHeight(65);
                        imageView2.setFitWidth(65);

                        hBox.getChildren().addAll(imageView2, button, imageView);
                        hBox.setAlignment(Pos.CENTER_LEFT);

                    } catch (ServiceException e) {
                        e.printStackTrace();
                    }
                }
                else {
                    try {
                        hBox.getChildren().clear();
                        vBox = new VBox();
                        hBox.setLayoutX(0);
                        User user = page.getServiceUser().findOne(friend.getUserSenderId());

                        try {
                            Message message = page.getServiceMessage().findOne(friend.getReply());
                            User user1 = page.getServiceUser().findOne(message.getUserSenderId());

                            Button buttonReply = new Button(message.getText());
                            buttonReply.setStyle("-fx-background-color: #8a8d91; -fx-border-radius: 30; -fx-background-radius: 30;");
                            buttonReply.setTextFill(Color.WHITE);

                            Text text = new Text();
                            text.setText("You replied to : " + user1.getFirstName() + " " + user1.getLastName());

                            Image image = new Image(user.getProfilePicture());

                            Button button = new Button(friend.getText());
                            button.setStyle("-fx-background-color: #4e4f50; -fx-background-radius: 30; -fx-border-radius: 30;");
                            button.setTextFill(Color.WHITE);

                            Image image1 = new Image("@../../images/reply.png");
                            ImageView imageView = new ImageView(image1);
                            imageView.setFitHeight(21);
                            imageView.setFitWidth(21);

                            ImageView imageView2 = new ImageView(image);
                            imageView2.setFitHeight(65);
                            imageView2.setFitWidth(65);

                            HBox hBox1 = new HBox();
                            hBox1.setAlignment(Pos.CENTER_LEFT);
                            hBox1.getChildren().addAll(button, imageView);

                            vBox.getChildren().addAll(text, buttonReply, hBox1);
                            vBox.setAlignment(Pos.CENTER_LEFT);
                            vBox.setLayoutY(3);


                            hBox.getChildren().addAll(imageView2, vBox);
                            hBox.setAlignment(Pos.CENTER_LEFT);
                        } catch (ServiceException exception) {
                            System.out.println(exception.getMessage());
                        }
                    } catch (ServiceException e) {
                        e.printStackTrace();
                    }
                }
            }
            setText(null);
            setGraphic(messageCell);
        }
    }
}
