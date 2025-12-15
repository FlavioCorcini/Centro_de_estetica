package com.cefet.centro_de_estetica.dto;

import com.cefet.centro_de_estetica.enums.StatusAgendamento;

public record AtualizacaoStatusDTO(
     StatusAgendamento status
) {}