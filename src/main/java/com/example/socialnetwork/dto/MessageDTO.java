package com.example.socialnetwork.dto;


import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class MessageDTO {
    private Long messageId;
    private String sender;
    private LocalDateTime dateTime;
    private String text;
    private String repliedTo;

    public MessageDTO(Long messageId, String sender, LocalDateTime dateTime, String text, String repliedTo) {
        this.messageId = messageId;
        this.sender = sender;
        this.dateTime = dateTime;
        this.text = text;
        this.repliedTo = repliedTo;
    }

    @Override
    public String toString() {
        String reply = "";
        if (repliedTo != null)
            reply = reply + "\nReplied to: " + repliedTo;

        return "___________________________________" +
                "\nMessage ID: " + messageId +
                "\nDate|Time: " + dateTime.format(DateTimeFormatter.ofPattern("dd-MM-yyyy")) + " at: " +
                 dateTime.format(DateTimeFormatter.ofPattern("HH:mm:ss")) +
                 reply +
                "\nSender: " + sender +
                "\nText: " + text;
    }
}
