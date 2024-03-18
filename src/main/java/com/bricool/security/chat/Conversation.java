package com.bricool.security.chat;

import com.bricool.security.service_pack.MyService;
import com.bricool.security.user.User;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "conversation")
public class Conversation {

    @Id
    @GeneratedValue
    private Integer id;

    @ManyToOne
    @JsonIgnore
    @JoinColumn(name = "client_id")
    private User client;

    @ManyToOne
    @JsonIgnore
    @JoinColumn(name = "service_provider_id")
    private User serviceProvider;

    @ManyToOne // Many conversations can be associated with one service
    @JoinColumn(name = "service_id")
    private MyService service;

//    @OneToMany(mappedBy = "conversation", cascade = CascadeType.ALL)
//    private List<Message> messages;
    @OneToMany(mappedBy = "conversation", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Message> messages = new ArrayList<>();
}
