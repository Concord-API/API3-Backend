package com.concord.proficio.application.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserPayloadDTO {
    private String id;
    private String name;
    private String email;
    private String role;
}
