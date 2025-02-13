package com.kyler.lynk.notification;

import com.kyler.lynk.message.MessageType;

public class Notification {
    private String chatId;
    private String content;
    private String senderId;
    private String recipientId;
    private String chatName;
    private MessageType messageType;
    private NotificationType notificationType;
    private byte[] media;

    public Notification() {
    }

    public Notification(String chatId,
                        String content,
                        String senderId,
                        String recipientId,
                        String chatName,
                        MessageType messageType,
                        NotificationType notificationType,
                        byte[] media) {
        this.chatId = chatId;
        this.content = content;
        this.senderId = senderId;
        this.recipientId = recipientId;
        this.chatName = chatName;
        this.messageType = messageType;
        this.notificationType = notificationType;
        this.media = media;
    }

    public String getChatId() {
        return chatId;
    }

    public void setChatId(String chatId) {
        this.chatId = chatId;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getSenderId() {
        return senderId;
    }

    public void setSenderId(String senderId) {
        this.senderId = senderId;
    }

    public String getRecipientId() {
        return recipientId;
    }

    public void setRecipientId(String recipientId) {
        this.recipientId = recipientId;
    }

    public String getChatName() {
        return chatName;
    }

    public void setChatName(String chatName) {
        this.chatName = chatName;
    }

    public MessageType getMessageType() {
        return messageType;
    }

    public void setMessageType(MessageType messageType) {
        this.messageType = messageType;
    }

    public NotificationType getNotificationType() {
        return notificationType;
    }

    public void setNotificationType(NotificationType notificationType) {
        this.notificationType = notificationType;
    }

    public byte[] getMedia() {
        return media;
    }

    public void setMedia(byte[] media) {
        this.media = media;
    }
}
