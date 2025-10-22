package com.concord.proficio.presentation.viewmodel;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class UserPayloadViewModel {
    private String id;
    private String name;
    private String email;
    private String role;
}


