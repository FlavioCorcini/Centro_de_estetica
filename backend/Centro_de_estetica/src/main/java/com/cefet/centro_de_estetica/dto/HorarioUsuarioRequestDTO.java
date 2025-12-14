package com.cefet.centro_de_estetica.dto;

import java.time.LocalTime;

import com.cefet.centro_de_estetica.enums.DiaDaSemana;

public record HorarioUsuarioRequestDTO(
	DiaDaSemana diaSemana,
    LocalTime horario,
    Long idFuncionario
) {}