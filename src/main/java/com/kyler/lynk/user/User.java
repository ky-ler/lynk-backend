package com.kyler.lynk.user;

import com.kyler.lynk.chat.Chat;
import com.kyler.lynk.common.BaseAuditingEntity;
import jakarta.persistence.*;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "users")
@NamedQuery(name = UserConstants.FIND_USER_BY_EMAIL, query = "SELECT u FROM User u WHERE u.email = :email")
@NamedQuery(name = UserConstants.FIND_USER_BY_PUBLIC_ID, query = "SELECT u FROM User u WHERE u.id = :publicId")
@NamedQuery(name = UserConstants.FIND_ALL_USERS_EXCEPT_SELF, query = "SELECT u FROM User u WHERE u.id != :publicId")
public class User extends BaseAuditingEntity {
    private static final int LAST_ACTIVE_INTERVAL = 5;

    @Id
    private String id;

    private String firstName;

    private String lastName;

    private String email;

    private LocalDateTime lastSeen;

    @OneToMany(mappedBy = "sender")
    private List<Chat> chatsAsSender;

    @OneToMany(mappedBy = "recipient")
    private List<Chat> chatsAsRecipient;

    public User(LocalDateTime lastModifiedDate, LocalDateTime createdDate, String id, String firstName, String lastName, String email, LocalDateTime lastSeen, List<Chat> chatsAsSender, List<Chat> chatsAsRecipient) {
        super(lastModifiedDate, createdDate);
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.lastSeen = lastSeen;
        this.chatsAsSender = chatsAsSender;
        this.chatsAsRecipient = chatsAsRecipient;
    }

    public User() {
    }

    @Transient
    public boolean isUserOnline() {
        return lastSeen != null && lastSeen.isBefore(LocalDateTime.now().minusMinutes(LAST_ACTIVE_INTERVAL));
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public LocalDateTime getLastSeen() {
        return lastSeen;
    }

    public void setLastSeen(LocalDateTime lastSeen) {
        this.lastSeen = lastSeen;
    }
}
