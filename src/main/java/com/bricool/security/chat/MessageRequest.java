package com.bricool.security.chat;

import lombok.Data;

@Data
public class MessageRequest {
    private Integer serviceProviderId;
    private String content;
    private Integer serviceId;
    private Integer clientId;

}
