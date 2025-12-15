package com.cefet.centro_de_estetica.dto;

import java.time.LocalDateTime;

public record AgendamentoRequestDTO(
    Long idCliente,
    Long idFuncionario,
    Long idServico,
    LocalDateTime dataHora,  // Formato: "2025-12-20T14:30:00"
    String observacoes
) {}