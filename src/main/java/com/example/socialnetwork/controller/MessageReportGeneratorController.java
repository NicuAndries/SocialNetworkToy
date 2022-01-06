package com.example.socialnetwork.controller;

import com.example.socialnetwork.dto.MessageDTO;
import com.example.socialnetwork.service.Page;
import javafx.event.ActionEvent;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;

import java.util.List;

public class MessageReportGeneratorController {
    public Button generatePDFButton;
    public DatePicker startDatePicker;
    public DatePicker endDatePicker;
    private Page page;
    private Long chatId;

    public void setPage(Page page, Long chatId) {
        this.page = page;
        this.chatId = chatId;
    }

    public void onGeneratePDFButton(ActionEvent actionEvent) {
        List<MessageDTO> messageDTOList = page.getChatMessagesFromAPeriod(startDatePicker.getValue().atStartOfDay(), endDatePicker.getValue().atStartOfDay(), chatId);
        messageDTOList.forEach(System.out::println);
        ((Node) (actionEvent.getSource())).getScene().getWindow().hide();
    }
}
