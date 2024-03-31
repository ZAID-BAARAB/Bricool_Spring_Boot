package com.bricool.security.chat;

import com.bricool.security.user.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ConversationRepository extends JpaRepository<Conversation, Long> {
    Optional<Conversation> findByClient_IdAndServiceProvider_Id(Integer clientId, Integer serviceProviderId);
    List<Conversation> findByClientOrServiceProvider(User client, User serviceProvider);
    Optional<Conversation> findByClient_IdAndServiceProvider_IdAndService_Id(Integer clientId, Integer serviceProviderId, Integer serviceId);


}
