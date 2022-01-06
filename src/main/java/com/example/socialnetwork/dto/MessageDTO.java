package com.example.socialnetwork.dto;


import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class MessageDTO {
    private Long idMessage;
    private String sender;
    private LocalDateTime dateTime;
    private String text;
    private String repliedTo;

    public MessageDTO(Long idMessage, String sender, LocalDateTime dateTime, String text, String repliedTo) {
        this.idMessage = idMessage;
        this.sender = sender;
        this.dateTime = dateTime;
        this.text = text;
        this.repliedTo = repliedTo;
    }

    @Override
    public String toString() {
        String ret = "-------------------------\n" +
                "Id: " + idMessage +
                "\nTime: " + dateTime.format(DateTimeFormatter.ofPattern("dd-MM-yyyy")) +
                "\nFrom: " + sender +
                "\nMessage: " + text;

        if (repliedTo != null)
            ret += "\nReplied to: " + repliedTo + "\n-------------------------";
        else
            ret +=  "\n-------------------------";
        return ret;
    }
}
