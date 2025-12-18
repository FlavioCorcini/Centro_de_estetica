package com.cefet.centro_de_estetica.dto;

import java.math.BigDecimal;

public record ServicoRequestDTO(
    String nome,
    String descricao,
    BigDecimal valor,
    Integer tempoAtendimento,
    Long idArea
) {
}