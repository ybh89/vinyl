package com.hansung.vinyl.notification.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Data
public class NotificationRequest {
    @NotBlank
    private String title;
    @NotBlank
    private String body;
    private String imageURL;
    @NotBlank
    private String topic;
}
