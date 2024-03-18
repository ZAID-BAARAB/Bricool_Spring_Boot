package com.bricool.security.chat;

import lombok.Data;

@Data
public class MessageRequest {
    private Long serviceProviderId;
    private String content;
    private Integer serviceId;

}
