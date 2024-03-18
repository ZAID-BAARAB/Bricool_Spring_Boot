package com.bricool.security.chat;

import com.bricool.security.user.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ConversationRepository extends JpaRepository<Conversation, Long> {
    Optional<Conversation> findByClient_IdAndServiceProvider_Id(Integer clientId, Long serviceProviderId);
    List<Conversation> findByClientOrServiceProvider(User client, User serviceProvider);

}
