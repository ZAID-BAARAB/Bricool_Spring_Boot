package com.bricool.security.chat;



import com.bricool.security.user.User;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "message")
public class Message {

    @Id
    @GeneratedValue
    private Long id;

    @ManyToOne
    @JsonIgnore
    @JoinColumn(name = "conversation_id")
    private Conversation conversation;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    private String content;

    private Integer client_id;

    private LocalDateTime timestamp;

    @Builder.Default
    private boolean isRead = false;

    public void setIsRead(boolean b) {
        isRead=b;
    }
}
