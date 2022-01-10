package com.example.socialnetwork.controller;

import com.example.socialnetwork.domain.Chat;
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
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;
import java.time.LocalDate;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class MessageReportGeneratorController {
    public Button generatePDFButton;
    public DatePicker startDatePicker;
    public DatePicker endDatePicker;
    private Page page;
    private Chat chat;

    public void setPage(Page page, Chat chat) {
        this.page = page;
        this.chat = chat;
    }

    public String getHeader(int raportNumber){
        if(raportNumber == 2){
            return getMessagesHeader();
        }
        if(raportNumber == 1){
            return getUserHeader();
        }
        return null;

    }

    private String getUserHeader(){
        return "Activity raport";
    }

    private String getMessagesHeader() {
        Long friendId = chat.getMembers().stream()
                .filter(member -> !Objects.equals(member, page.getIdUser())).toList().get(0);

        User friend = null;
        try {
            friend = page.getServiceUser().findOne(friendId);
        } catch (ServiceException e) {
            e.printStackTrace();
        }

        String name = friend.getFirstName() + " " + friend.getLastName();
        return "Conversation with " + name;
    }

    public StringBuffer getNeededRaport(int raportNumber, LocalDate firsDate, LocalDate secondDate){
        if(raportNumber == 2)
        {
            return getRaportMessages(firsDate, secondDate);
        }
        if(raportNumber == 1)
        {
            return getRaportUser(firsDate, secondDate);
        }
        return null;
    }

    public StringBuffer getRaportMessages(LocalDate firstDate, LocalDate secondDate){
        List<MessageDTO> raportList = page.getChatMessagesFromAPeriod(firstDate.atStartOfDay(), secondDate.atStartOfDay(), chat.getId());
        StringBuffer raportText = new StringBuffer();
        raportList.forEach(message -> {
            String text =  message.toString();
            raportText.append("\n" + text);
        });
        return raportText;
    }

    public StringBuffer getRaportUser(LocalDate firstDate, LocalDate secondDate) {
        List<MessageDTO> raportList = page.getRaportAllMessages(firstDate.atStartOfDay(), secondDate.atStartOfDay());

        StringBuffer raportText = new StringBuffer();

        raportList.forEach(message -> {
            String text =  message.toString();
            raportText.append("\n" + text);
        });

        List<FriendDTO> raportList2 = null;
        raportList2 = page.getRaportAllNewFriends(firstDate.atStartOfDay().toLocalDate(), secondDate.atStartOfDay().toLocalDate());

        raportList2.forEach(message -> {
            String text =  message.toString();
            raportText.append("\n" + text);
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
        StringBuffer raportText = getNeededRaport(2, startDatePicker.getValue(), endDatePicker.getValue());

        PDFWriter pdfWriter = new PDFWriter(file);
        pdfWriter.createPdfFile();
        String heading = getHeader(2) ;
        pdfWriter.addPage(heading, raportText);
        pdfWriter.saveAndClose();
    }
}
