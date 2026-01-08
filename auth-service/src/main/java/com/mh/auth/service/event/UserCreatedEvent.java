package com.mh.auth.service.event;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserCreatedEvent {

    private Long userId;
    private String email;
    private String firstName;
    private String lastName;
    private String role;

    @Builder.Default
    private Instant timestamp = Instant.now();
}