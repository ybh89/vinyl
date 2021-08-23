package com.hansung.vinyl.identification.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.hansung.vinyl.identification.domain.IdentificationToken;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class IdentificationResponse {
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss", timezone = "Asia/Seoul")
    private LocalDateTime expirationData;
    private String email;
    private boolean approved;
    private String message;

    public static IdentificationResponse of(IdentificationToken identificationToken, String responseMessage) {
        return new IdentificationResponse(identificationToken.getExpirationData(),
                identificationToken.getEmail().value(), identificationToken.isApproved(), responseMessage);
    }
}
