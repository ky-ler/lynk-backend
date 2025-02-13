package com.kyler.lynk.chat;

import org.springframework.stereotype.Service;

@Service
public class ChatMapper {

    public ChatResponse toChatResponse(Chat chat, String senderId) {
        return new ChatResponse(
                chat.getId(),
                chat.getChatName(senderId),
                chat.getUnreadMessageCount(senderId),
                chat.getLastMessage(),
                chat.getLastMessageTime(),
                chat.getRecipient().isUserOnline(),
                chat.getSender().getId(),
                chat.getRecipient().getId());
    }
}
