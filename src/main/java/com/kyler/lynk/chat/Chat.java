package com.kyler.lynk.chat;

import com.kyler.lynk.common.BaseAuditingEntity;
import com.kyler.lynk.message.Message;
import com.kyler.lynk.message.MessageState;
import com.kyler.lynk.message.MessageType;
import com.kyler.lynk.user.User;
import jakarta.persistence.*;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "chat")
@NamedQuery(name = ChatConstants.FIND_CHAT_BY_SENDER_ID,
        query = "SELECT DISTINCT c FROM Chat c WHERE c.sender.id = :senderId " +
                "OR c.recipient.id = :senderId ORDER BY createdDate DESC")
@NamedQuery(name = ChatConstants.FIND_CHAT_BY_SENDER_ID_AND_RECIPIENT_ID,
        query = "SELECT DISTINCT c FROM Chat c WHERE (c.sender.id = :senderId AND c.recipient.id = :recipientId) " +
                "OR (c.sender.id = :recipientId AND c.recipient.id = :senderId)")
public class Chat extends BaseAuditingEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    @ManyToOne
    @JoinColumn(name = "sender_id")
    private User sender;

    @ManyToOne
    @JoinColumn(name = "recipient_id")
    private User recipient;

    @OneToMany(mappedBy = "chat", fetch = FetchType.EAGER)
    @OrderBy("createdDate DESC")
    private List<Message> messages;

    public Chat(LocalDateTime lastModifiedDate, LocalDateTime createdDate, String id, User sender, User recipient, List<Message> messages) {
        super(lastModifiedDate, createdDate);
        this.id = id;
        this.sender = sender;
        this.recipient = recipient;
        this.messages = messages;
    }

    public Chat() {
    }

    @Transient
    public String getChatName(String senderId) {
        if (recipient.getId().equals(senderId)) {
            return sender.getFirstName() + " " + sender.getLastName();
        }

        return recipient.getFirstName() + " " + recipient.getLastName();
    }

    @Transient
    public long getUnreadMessageCount(String senderId) {
        return messages
                .stream()
                .filter(m -> m.getRecipientId().equals(senderId))
                .filter(m -> m.getState() == MessageState.SENT)
                .count();
    }

    @Transient
    public String getLastMessage() {
        if (messages == null || messages.isEmpty()) return null;

        if (messages.getFirst().getType() != MessageType.TEXT) {
            return "Attachment";
        }

        return messages.getFirst().getContent();
    }

    @Transient
    public LocalDateTime getLastMessageTime() {
        if (messages == null || messages.isEmpty()) return null;
        return messages.getFirst().getCreatedDate();
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public User getSender() {
        return sender;
    }

    public void setSender(User sender) {
        this.sender = sender;
    }

    public User getRecipient() {
        return recipient;
    }

    public void setRecipient(User recipient) {
        this.recipient = recipient;
    }

    public List<Message> getMessages() {
        return messages;
    }

    public void setMessages(List<Message> messages) {
        this.messages = messages;
    }
}
