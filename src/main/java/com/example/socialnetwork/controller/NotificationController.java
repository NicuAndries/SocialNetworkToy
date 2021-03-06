package com.example.socialnetwork.controller;

import com.example.socialnetwork.domain.Eveniment;
import com.example.socialnetwork.domain.EvenimentNotification;
import com.example.socialnetwork.service.Page;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.layout.Pane;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

public class NotificationController implements Initializable {
    public ListView<Eveniment> notificationListView;
    private ObservableList<Eveniment> evenimentsObservableList = FXCollections.observableArrayList();
    public Button backButton;
    public Pane notificationPane;
    Page page;

    public void setService(Page page) {
        this.page = page;
        populate();
    }

    public void populate() {
        List<EvenimentNotification> evenimentNotificationList = page.populateNotificationList();
        List<Eveniment> eveniments = new ArrayList<>();
        for (EvenimentNotification evenimentNotification : evenimentNotificationList) {
            Eveniment eveniment = page.findOneEveniment(evenimentNotification.getEveniment_id());
            if (eveniment != null)
                eveniments.add(eveniment);
        }
        evenimentsObservableList.setAll(eveniments);
    }

    public void onBackButton(ActionEvent actionEvent) {
        notificationPane.getParent().toBack();
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        notificationListView.setItems(evenimentsObservableList);
        notificationListView.setCellFactory(userListView -> new NotificationCellView(page));
    }
}
