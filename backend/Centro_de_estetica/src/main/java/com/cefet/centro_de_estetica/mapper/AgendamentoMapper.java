package com.cefet.centro_de_estetica.mapper;

import org.springframework.stereotype.Component;

import com.cefet.centro_de_estetica.dto.AgendamentoResponseDTO;
import com.cefet.centro_de_estetica.entity.Agendamento;

@Component
public class AgendamentoMapper {

    public AgendamentoResponseDTO toResponseDTO(Agendamento agendamento) {
        if (agendamento == null) {
            return null;
        }
        return new AgendamentoResponseDTO(agendamento);
    }

}