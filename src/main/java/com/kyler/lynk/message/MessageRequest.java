package com.kyler.lynk.message;

public class MessageRequest {
    private String content;
    private String senderId;
    private String recipientId;
    private MessageType type;
    private String chatId;

    public MessageRequest(String content, String senderId, String recipientId, MessageType type, String chatId) {
        this.content = content;
        this.senderId = senderId;
        this.recipientId = recipientId;
        this.type = type;
        this.chatId = chatId;
    }

    public MessageRequest() {
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

    public MessageType getType() {
        return type;
    }

    public void setType(MessageType type) {
        this.type = type;
    }

    public String getChatId() {
        return chatId;
    }

    public void setChatId(String chatId) {
        this.chatId = chatId;
    }
}
