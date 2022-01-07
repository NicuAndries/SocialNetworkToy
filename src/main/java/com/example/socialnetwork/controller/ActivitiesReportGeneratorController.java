package com.example.socialnetwork.controller;

import com.example.socialnetwork.dto.FriendDTO;
import com.example.socialnetwork.dto.MessageDTO;
import com.example.socialnetwork.service.Page;
import javafx.event.ActionEvent;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;

public class ActivitiesReportGeneratorController {
    public Button generatePDFButton;
    public DatePicker startDatePicker;
    public DatePicker endDatePicker;
    private Page page;

    public void setPage(Page page) {
        this.page = page;
    }


    public void onGeneratePDFButton(ActionEvent actionEvent) {
        List<MessageDTO> messageDTOList = page.getRaportAllMessages(startDatePicker.getValue().atStartOfDay(), endDatePicker.getValue().atStartOfDay());
        messageDTOList.forEach(System.out::println);
        List<FriendDTO> friendDTOList = page.getRaportAllNewFriends(startDatePicker.getValue(), endDatePicker.getValue());
        friendDTOList.forEach(System.out::println);
        ((Node) (actionEvent.getSource())).getScene().getWindow().hide();
    }
}

