package com.example.socialnetwork.controller;

import com.example.socialnetwork.domain.User;
import com.example.socialnetwork.dto.FriendDTO;
import com.example.socialnetwork.dto.MessageDTO;
import com.example.socialnetwork.exceptions.ServiceException;
import com.example.socialnetwork.service.Page;
import com.example.socialnetwork.utils.PDFWriter;
import javafx.event.ActionEvent;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.time.LocalDate;
import java.util.List;
import java.util.Objects;

public class ActivitiesReportGeneratorController {
    public Button generatePDFButton;
    public DatePicker startDatePicker;
    public DatePicker endDatePicker;
    private Page page;

    public void setPage(Page page) {
        this.page = page;
    }
    public String getHeader(int raportNumber){
        if(raportNumber == 1){
            return getUserHeader();
        }
        return null;

    }

    private String getUserHeader(){
        return "Activity raport";
    }

    public StringBuffer getNeededRaport(int raportNumber, LocalDate firsDate, LocalDate secondDate){
        if(raportNumber == 1)
        {
            return getRaportUser(firsDate, secondDate);
        }
        return null;
    }

    public StringBuffer getRaportUser(LocalDate firstDate, LocalDate secondDate) {
        List<MessageDTO> raportList = page.getRaportAllMessages(firstDate.atStartOfDay(), secondDate.atStartOfDay());

        StringBuffer raportText = new StringBuffer();
        raportText.append("\nMessages: ");

        raportList.forEach(message -> {
            String text =  message.toString();
            raportText.append("\n").append(text);
        });

        raportText.append("\n___________________________________\n");

        raportText.append("\nNew friendships: ");
        List<FriendDTO> raportList2 = null;
        raportList2 = page.getRaportAllNewFriends(firstDate.atStartOfDay().toLocalDate(), secondDate.atStartOfDay().toLocalDate());

        raportList2.forEach(message -> {
            String text =  message.toString();
            raportText.append("\n").append(text);
        });

        return raportText;
    }

    public String getFile(){
        Stage stage = (Stage) generatePDFButton.getScene().getWindow();
        FileChooser fileChooser = new FileChooser();
        FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter("PDF files (*.pdf)", "*.pdf");
        fileChooser.getExtensionFilters().add(extFilter);
        File file = fileChooser.showSaveDialog(stage);
        return file.getAbsolutePath();
    }

    public void onGeneratePDFButton(ActionEvent actionEvent) {
        String file = getFile();
        StringBuffer raportText = getNeededRaport(1, startDatePicker.getValue(), endDatePicker.getValue());

        PDFWriter pdfWriter = new PDFWriter(file);
        pdfWriter.createPdfFile();
        String heading = getHeader(1) ;
        pdfWriter.addPage(heading, raportText);
        pdfWriter.saveAndClose();
        ((Node) (actionEvent.getSource())).getScene().getWindow().hide();
    }
}

