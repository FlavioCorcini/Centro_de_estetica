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

    /* Nota: Não criamos o método 'toEntity' aqui (DTO -> Entidade) 
       porque o Agendamento depende de buscar Cliente, Funcionário e Serviço no Banco de Dados.
       Fazer isso no Mapper exigiria injetar Repositories aqui, o que deixaria o código acoplado.
       Por isso, a montagem da Entidade Agendamento fica no Service.
    */
}