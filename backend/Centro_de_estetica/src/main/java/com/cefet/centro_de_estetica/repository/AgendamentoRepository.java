package com.cefet.centro_de_estetica.repository;

import java.time.LocalDateTime;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import com.cefet.centro_de_estetica.entity.Agendamento;
import com.cefet.centro_de_estetica.entity.Usuario;

public interface AgendamentoRepository extends JpaRepository<Agendamento, Long> {

    List<Agendamento> findByCliente(Usuario cliente);
    List<Agendamento> findByFuncionario(Usuario funcionario);
    List<Agendamento> findByDataHoraBetween(LocalDateTime inicio, LocalDateTime fim);
    List<Agendamento> findByClienteAndDataHoraBetween(Usuario cliente, LocalDateTime inicio, LocalDateTime fim);
    
   
    List<Agendamento> findByFuncionarioAndDataHoraBetween(Usuario funcionario, LocalDateTime inicio, LocalDateTime fim);

    
    boolean existsByFuncionarioAndDataHora(Usuario funcionario, LocalDateTime dataHora);
}