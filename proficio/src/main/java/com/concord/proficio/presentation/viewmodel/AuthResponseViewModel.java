package com.concord.proficio.presentation.viewmodel;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class AuthResponseViewModel {
    private String token;
    private UserPayloadViewModel user;
}


