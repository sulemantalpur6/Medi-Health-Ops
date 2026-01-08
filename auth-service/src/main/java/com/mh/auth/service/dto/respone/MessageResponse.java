package com.mh.auth.service.dto.respone;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MessageResponse {

    private String message;

    @Builder.Default
    private LocalDateTime timestamp = LocalDateTime.now();

    @Builder.Default
    private Boolean success = true;

    public MessageResponse(String message) {
        this.message = message;
        this.timestamp = LocalDateTime.now();
        this.success = true;
    }
}