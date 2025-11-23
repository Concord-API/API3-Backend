package com.concord.proficio.presentation.viewmodel;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class SquadMemberAddRequestViewModel {
    @NotNull
    private Long colaboradorId;
}





