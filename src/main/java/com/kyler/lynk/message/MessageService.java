package com.kyler.lynk.message;

import com.kyler.lynk.chat.Chat;
import com.kyler.lynk.chat.ChatRepository;
import com.kyler.lynk.file.FileService;
import com.kyler.lynk.file.FileUtils;
import com.kyler.lynk.notification.Notification;
import com.kyler.lynk.notification.NotificationService;
import com.kyler.lynk.notification.NotificationType;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Service
public class MessageService {
    private final MessageRepository messageRepository;
    private final ChatRepository chatRepository;
    private final MessageMapper messageMapper;
    private final FileService fileService;
    private final NotificationService notificationService;

    public MessageService(MessageRepository messageRepository, ChatRepository chatRepository, MessageMapper messageMapper, FileService fileService, NotificationService notificationService) {
        this.messageRepository = messageRepository;
        this.chatRepository = chatRepository;
        this.messageMapper = messageMapper;
        this.fileService = fileService;
        this.notificationService = notificationService;
    }

    public void saveMessage(MessageRequest messageRequest) {
        Chat chat = chatRepository.findById(messageRequest.getChatId())
                .orElseThrow(() -> new EntityNotFoundException("Chat not found"));

        Message message = new Message();
        message.setContent(messageRequest.getContent());
        message.setChat(chat);
        message.setSenderId(messageRequest.getSenderId());
        message.setRecipientId(messageRequest.getRecipientId());
        message.setType(messageRequest.getType());
        message.setState(MessageState.SENT);

        messageRepository.save(message);

        Notification notification = new Notification();

        notification.setChatId(chat.getId());
        notification.setContent(messageRequest.getContent());
        notification.setSenderId(messageRequest.getSenderId());
        notification.setRecipientId(messageRequest.getRecipientId());
        notification.setMessageType(messageRequest.getType());
        notification.setNotificationType(NotificationType.MESSAGE);
        notification.setChatName(chat.getChatName(message.getSenderId()));

        notificationService.sendNotification(messageRequest.getRecipientId(), notification);
    }

    public List<MessageResponse> findChatMessages(String chatId) {
        return messageRepository.findMessagesByChatId(chatId)
                .stream()
                .map(messageMapper::toMessageResponse)
                .toList();
    }

    @Transactional
    public void setMessagesToSeen(String chatId, Authentication authentication) {
        Chat chat = chatRepository.findById(chatId)
                .orElseThrow(() -> new EntityNotFoundException("Chat not found"));

        final String recipientId = getRecipientId(chat, authentication);

        messageRepository.setMessagesToSeenByChat(chatId, MessageState.SEEN);

        Notification notification = new Notification();
        notification.setChatId(chat.getId());
        notification.setNotificationType(NotificationType.SEEN);
        notification.setRecipientId(recipientId);
        notification.setSenderId(getSenderId(chat, authentication));

        notificationService.sendNotification(recipientId, notification);
    }

    public void uploadMediaMessage(String chatId, MultipartFile file, Authentication authentication) {
        Chat chat = chatRepository.findById(chatId)
                .orElseThrow(() -> new EntityNotFoundException("Chat not found"));

        final String senderId = getSenderId(chat, authentication);
        final String recipientId = getRecipientId(chat, authentication);

        final String filePath = fileService.saveFile(file, senderId);

        Message message = new Message();
        message.setChat(chat);
        message.setSenderId(senderId);
        message.setRecipientId(recipientId);
        message.setType(MessageType.IMAGE);
        message.setState(MessageState.SENT);
        message.setMediaFilePath(filePath);
        messageRepository.save(message);


        Notification notification = new Notification();
        notification.setChatId(chat.getId());
        notification.setSenderId(senderId);
        notification.setRecipientId(recipientId);
        notification.setMessageType(MessageType.IMAGE);
        notification.setNotificationType(NotificationType.MESSAGE);
        notification.setMedia(FileUtils.readFileFromLocation(filePath));

        notificationService.sendNotification(recipientId, notification);
    }

    private String getSenderId(Chat chat, Authentication authentication) {
        if (chat.getSender().getId().equals(authentication.getName())) {
            return chat.getSender().getId();
        }
        return chat.getRecipient().getId();
    }

    private String getRecipientId(Chat chat, Authentication authentication) {
        if (chat.getSender().getId().equals(authentication.getName())) {
            return chat.getRecipient().getId();
        }
        return chat.getSender().getId();
    }
}
